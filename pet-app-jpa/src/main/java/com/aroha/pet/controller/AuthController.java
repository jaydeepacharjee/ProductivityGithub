package com.aroha.pet.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.aroha.pet.exception.AppException;
import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.Role;
import com.aroha.pet.model.RoleName;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.ForgetPassword;
import com.aroha.pet.payload.ForgetPasswordPayload;
import com.aroha.pet.payload.JwtAuthenticationResponse;
import com.aroha.pet.payload.LoginRequest;
import com.aroha.pet.payload.SignUpRequest;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.JwtTokenProvider;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.DBService;
import com.aroha.pet.service.UserService;

/**
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserService userService;

	@Autowired
	DBService dbService;

	@Autowired
	private JavaMailSender javaMailSender;

	/*
	 * @Autowired private JwtAuthenticationResponse jwtResponse;
	 */
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(),
						loginRequest.getPassword()
						)
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		Optional<User> user = userService.findByEmail(loginRequest.getUsernameOrEmail());
		User getUser = user.get();
		logger.info(getUser.getName() + " logged in successfully");
		//		jwtResponse.setTokenType("Bearer");
		//		jwtResponse.setAccessToken(jwt);
		//		jwtResponse.setId(getUser.getId());
		//		jwtResponse.setName(getUser.getName());
		//		jwtResponse.setUsername(getUser.getEmail());
		//		jwtResponse.setRoles(authentication.getAuthorities());
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
		//return ResponseEntity.ok(jwtResponse);
	}
	

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, @CurrentUser UserPrincipal currentUser) {

		if (userService.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
					HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(signUpRequest.getName(),
				signUpRequest.getEmail(), signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role userRole = null;
		if (signUpRequest.getUserType().equals("learner")) {

			userRole = userService.findByRoleName(RoleName.ROLE_USER)
					.orElseThrow(() -> new AppException("User Role not set."));

		}
		if (signUpRequest.getUserType().equals("mentor")) {
			if (currentUser.isAdminRole()) {
				userRole = userService.findByRoleName(RoleName.ROLE_MENTOR)
						.orElseThrow(() -> new AppException("User Role not set."));
			} else {
				logger.error("Only admin can create mentor");
				throw new RuntimeException("Only admin can create Mentor");
			}
		}
		if (signUpRequest.getUserType().equals("admin")) {
			if (currentUser.isAdminRole()) {
				userRole = userService.findByRoleName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new AppException("User Role not set."));
			} else {
				logger.error("Only admin can create another admin");
				throw new RuntimeException("Only admin can create another admin");
			}
		}
		logger.info("-----------------------------Iam here--------------");
		user.setPhoneNo(signUpRequest.getPhoneNo());
		user.setAltPhoneNo(signUpRequest.getAltPhoneNo());
		user.setPrimarySkills(signUpRequest.getPrimarySkills());
		user.setSecondarySkills(signUpRequest.getSecondarySkills());
		user.setAddress(signUpRequest.getAddress());
		user.setDateOfJoin(signUpRequest.getDateOfJoin());
		user.setSoe(signUpRequest.getSoe());
		user.setSoeRef(signUpRequest.getSoeRef());
		user.setRoles(Collections.singleton(userRole));
		Optional<DbInfo> db = dbService.getDbInfoById(signUpRequest.getDbId());

		if (db.isPresent()) {
			DbInfo dd = db.get();
			user.getDbs().add(dd);
		}
		User result = userService.save(user);
		logger.info("user saved");
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getName().replaceAll(" ", "")).toUri();



		//      Send email to user once they added successfully
		Boolean sendEmail=true;

		MimeMessage msg = javaMailSender.createMimeMessage();
		try {
			logger.info("-----------Email is---------"+signUpRequest.getEmail());
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(signUpRequest.getEmail());
			helper.setSubject("Successfully Added in Productivity App");
			helper.setText("Hi "+signUpRequest.getName()+",<br><br>\n"
				

        		   +"You are successfully added in Productivity App <br><br>"+"\n\n"
        		   +"<b style=\"color:green\"><u> Following are you credentials</u></b><br>\n"
        		   +"<b>Username:</b>&nbsp;&nbsp;<i style=\"color:#6600ff\">"+signUpRequest.getEmail()+"</i><br>\n"
        		   +"<b>Password:</b>&nbsp;&nbsp;<i style=\"color:#6600ff\">"+signUpRequest.getPassword()+"</i><br><br>\n\n"
        		   +"Please visit the link to SignIn:<b> http://productivity.aroha.co.in/  </b><br>\n"
        		   +"<b style=\"color:#ff0066\">You can reset the password using ForgetPassword option from Signin menu</b><br> "
        		   +"In case of any queries, kindly contact our customer service desk at the details below\n<br><br>"
        		   +"\n\n"
        		   +"Warm Regards,<br>\n"
        		   +"\n"
        		   +"ArohaTechnologies",true);
			 javaMailSender.send(msg);
			  logger.info("--------Email sent to User-------");
			}catch(Exception ex) {
			sendEmail=false;
			logger.info("--------Email failed to sent to User-------"+ex.getMessage());
		}

		if(sendEmail) {
			return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully\n"+"Successfully sent email"));
		}else {
			return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully\n"+"Failed to sent email"));
		}
	}

	@PostMapping("/forgetPassword")
	public ResponseEntity<?> forgetPassword(@RequestBody LoginRequest login) {

		boolean mailExist = userService.existsByEmail(login.getUsernameOrEmail());
		if (!mailExist) {
			return ResponseEntity.ok(new ForgetPasswordPayload(HttpStatus.BAD_REQUEST.value(),Boolean.FALSE, "Email does not exist"));
		} else {
			boolean istrue = userService.forgetPassword(login.getUsernameOrEmail());
			if (istrue) {
				return ResponseEntity.ok(new ForgetPasswordPayload(HttpStatus.OK.value(),Boolean.TRUE, "OTP sent to registered emailId"));
			}
		}
		return ResponseEntity.ok(new ForgetPasswordPayload(HttpStatus.BAD_REQUEST.value(),Boolean.FALSE, "Failed to send mail"));

	}

	@PostMapping("/UpdatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody ForgetPassword password) {
		return ResponseEntity.ok(userService.updatePassword(password));
	}
}
