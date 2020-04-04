package com.aroha.pet.controller;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.aroha.pet.exception.AppException;
import com.aroha.pet.exception.ResourceNotFoundException;
import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.model.Role;
import com.aroha.pet.model.RoleName;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.PagedResponse;
import com.aroha.pet.payload.SignUpRequest;
import com.aroha.pet.payload.UserControllerPayload;
import com.aroha.pet.payload.UserIdentityAvailability;
import com.aroha.pet.payload.UserProfile;
import com.aroha.pet.payload.UserSummary;
import com.aroha.pet.payload.UsersListPayload;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.DBService;
import com.aroha.pet.service.QueryInfoService;
import com.aroha.pet.service.UserService;
import com.aroha.pet.util.AppConstants;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private DBService dbService;
    @Autowired
    private QueryInfoService queryService;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MENTOR')") // just for example
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.getAuthorities());
        return ResponseEntity.ok(userSummary);
//        if (userSummary != null) {
//            return ResponseEntity.ok(new UserControllerPayload(HttpStatus.OK.value(), "SUCCESS", userSummary));
//        } else {
//            return ResponseEntity.ok(new UserControllerPayload(HttpStatus.NO_CONTENT.value(), "FAIL"));
//        }
    }

    @GetMapping("/user/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = userService.existsByEmail(email);
        return ResponseEntity.ok(new UserIdentityAvailability(isAvailable));
    }

    @GetMapping("/users/{email}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')") // just for example
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable(value = "email") String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        UserProfile userProfile = new UserProfile(user.getId(), user.getName(), user.getCreatedAt(), user.getRoles());

        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/users/list/{userType}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MENTOR')") // just for example
    public ResponseEntity<List<UsersListPayload>> getUsersByType(@PathVariable(value = "userType") String userType) {
        String roleName = "";
        if (userType.contains("learner")) {
            roleName = "ROLE_USER";
        }
        if (userType.contains("mentor")) {
            roleName = "ROLE_MENTOR";
        }
        if (userType.contains("admin")) {
            roleName = "ROLE_ADMIN";
        }
        return ResponseEntity.ok(userService.findByRoles(roleName));
    }

    @GetMapping("/user/me/dbs")
    public PagedResponse<?> getMyDataBases(@CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        logger.info("--------------size is----------" + currentUser);
        return dbService.getDataBasesCreatedBy(currentUser, page, size);
    }

    @GetMapping("/user/me/dbs/all")
    public List<DbInfo> getAllMyDataBases(@CurrentUser UserPrincipal currentUser) {
        return dbService.getAllDataBasesCreatedBy(currentUser);
    }

    @GetMapping("/user/me/queries/all")
    public List<QueryInfo> getAllMyQueries(@CurrentUser UserPrincipal currentUser) {
        return queryService.getAllQueryInfoCreatedBy(currentUser);
    }

    @PostMapping("user/me/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MENTOR')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userService.findByEmail(signUpRequest.getEmail())
                .orElseThrow(() -> new AppException("User Not Found."));

        if (userService.existsByEmail(signUpRequest.getEmail())) {
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = null;
        if (signUpRequest.getUserType().equals("learner")) {
            userRole = userService.findByRoleName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }
        if (signUpRequest.getUserType().equals("mentor")) {
            userRole = userService.findByRoleName(RoleName.ROLE_MENTOR)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }
        if (signUpRequest.getUserType().equals("admin")) {
            userRole = userService.findByRoleName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }

        System.out.println("ROle is: " + userRole);

        user.setRoles(Collections.singleton(userRole));
        user.setName(signUpRequest.getName());
        user.setPhoneNo(signUpRequest.getPhoneNo());
        user.setAltPhoneNo(signUpRequest.getAltPhoneNo());
        user.setPrimarySkills(signUpRequest.getPrimarySkills());
        user.setSecondarySkills(signUpRequest.getSecondarySkills());
        user.setAddress(signUpRequest.getAddress());
        user.setDateOfJoin(signUpRequest.getDateOfJoin());
        user.setSoe(signUpRequest.getSoe());
        user.setSoeRef(signUpRequest.getSoeRef());

        User result = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getName().replaceAll(" ", "")).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User Updated successfully"));
    }

    @GetMapping("/user/admin")
    public ResponseEntity<UserProfile> createAdmin() {
        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setEmail("admin@aroha.co.in");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setPhoneNo("9249876052");
        Set<Role> a = new HashSet<>();
        Role r = userService.findByRoleName(RoleName.ROLE_ADMIN).get();
        a.add(r);
        adminUser.setRoles(a);
        adminUser.setAddress("Aroha Technologies");
        userService.save(adminUser);
        logger.info("admin saved successfully");
        UserProfile userProfile = new UserProfile(adminUser.getId(), adminUser.getName(), adminUser.getCreatedAt(), adminUser.getRoles());

        return ResponseEntity.ok(userProfile);
    }

    //	 Updating user details
    @PostMapping("/user/updDetails")
    public ResponseEntity<?> updateUsers(@RequestBody User user) {

        return ResponseEntity.ok(userService.updateData(user));

    }
}
