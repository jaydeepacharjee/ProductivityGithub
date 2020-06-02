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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.aroha.pet.service.LoginLogoutService;
import com.aroha.pet.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private DBService dbService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private LoginLogoutService loginLogoutService;

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
        Long roleId = userService.findUserRole(getUser.getId());
        if (roleId == 1) {
            loginLogoutService.saveLoginTime(getUser.getId());
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutApi(@CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(loginLogoutService.logout(user));
    }

    @GetMapping("/showLearnerLoginDetails")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MENTOR')")
    public ResponseEntity<?> getLoginDetails() {
        return ResponseEntity.ok(loginLogoutService.showLoginDetails());
    }

    @PostMapping("/showlatestlearnerloginDetails")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MENTOR')")
    public ResponseEntity<?> getLatestLoginDetailsOfUser(@RequestParam("userId") Long userId,@RequestParam("historyDay")int days) {
        return ResponseEntity.ok(loginLogoutService.showLoginDetails(userId,days));
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
        Boolean sendEmail = true;

        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(signUpRequest.getEmail());
            helper.setSubject("Successfully Added in Productivity App");
            helper.setText("Hi " + signUpRequest.getName() + ",<br><br>\n"
                    + "You are successfully added in Productivity App <br><br>" + "\n\n"
                    + "<b style=\"color:green\"><u> Following are you credentials</u></b><br>\n"
                    + "<b>Username:</b>&nbsp;&nbsp;<i style=\"color:#6600ff\">" + signUpRequest.getEmail() + "</i><br>\n"
                    + "<b>Password:</b>&nbsp;&nbsp;<i style=\"color:#6600ff\">" + signUpRequest.getPassword() + "</i><br><br>\n\n"
                    + "Please visit the link to SignIn:<b> http://productivity.aroha.co.in/  </b><br>\n"
                    + "<b style=\"color:#ff0066\">You can reset the password using ForgetPassword option from Signin menu</b><br> "
                    + "In case of any queries, kindly contact our customer service desk at the details below\n<br><br>"
                    + "\n\n"
                    + "Warm Regards,<br>\n"
                    + "\n"
                    + "ArohaTechnologies", true);
            javaMailSender.send(msg);
        } catch (Exception ex) {
            sendEmail = false;
            logger.info("--------Email failed to sent to User-------" + ex.getMessage());
        }

        if (sendEmail) {
            return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully\n" + "Successfully sent email"));
        } else {
            return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully\n" + "Failed to sent email"));
        }
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody LoginRequest login) {

        boolean mailExist = userService.existsByEmail(login.getUsernameOrEmail());
        if (!mailExist) {
            return ResponseEntity.ok(new ForgetPasswordPayload(HttpStatus.BAD_REQUEST.value(), Boolean.FALSE, "Email does not exist"));
        } else {
            boolean istrue = userService.forgetPassword(login.getUsernameOrEmail());
            if (istrue) {
                return ResponseEntity.ok(new ForgetPasswordPayload(HttpStatus.OK.value(), Boolean.TRUE, "OTP sent to registered emailId"));
            }
        }
        return ResponseEntity.ok(new ForgetPasswordPayload(HttpStatus.BAD_REQUEST.value(), Boolean.FALSE, "Failed to send mail"));
    }

    @PostMapping("/UpdatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody ForgetPassword password) {
        return ResponseEntity.ok(userService.updatePassword(password));
    }
}
