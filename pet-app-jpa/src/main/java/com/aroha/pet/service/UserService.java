package com.aroha.pet.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.aroha.pet.exception.ResourceNotFoundException;
import com.aroha.pet.model.Role;
import com.aroha.pet.model.RoleName;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.ForgetPassword;
import com.aroha.pet.payload.UsersListPayload;
import com.aroha.pet.repository.RoleRepository;
import com.aroha.pet.repository.UserRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author Sony George | Date : 6 Mar, 2019 6:05:12 PM
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    static String unique_password = "";

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<Role> findByRoleName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UsersListPayload> findByRoles(String roleName) {
//        return userRepository.findByRoles(roleName);
        List<User> userList = userRepository.findByRoles(roleName);
        List<UsersListPayload> list = new ArrayList<>();
        Iterator<User> itr = userList.iterator();
        while (itr.hasNext()) {
            User userObj = itr.next();
            UsersListPayload userPayload = new UsersListPayload();
            userPayload.setId(userObj.getId());
            userPayload.setName(userObj.getName());
            userPayload.setEmail(userObj.getEmail());
            userPayload.setPhoneNo(userObj.getPhoneNo());
            userPayload.setAltPhoneNo(userObj.getAltPhoneNo());
            userPayload.setPrimarySkills(userObj.getPrimarySkills());
            userPayload.setSecondarySkills(userObj.getSecondarySkills());
            userPayload.setAddress(userObj.getAddress());

            if (userObj.getDateOfJoin() != null) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(userObj.getDateOfJoin());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // System.out.println("Date is: "+date);
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                userPayload.setDateOfJoin(formatter.format(date));
            }
            userPayload.setSoe(userObj.getSoe());
            userPayload.setSoeRef(userObj.getSoeRef());
            list.add(userPayload);
        }
        return list;
    }

    public Optional<User> findByLearnerId(long id) {
        return userRepository.findById(id);
    }

    public User updateData(User user) {

        User getUserEmail = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", user.getEmail()));
        if (user.getName() != null) {
            getUserEmail.setName(user.getName());
        }
        if (user.getAddress() != null) {
            getUserEmail.setAddress(user.getAddress());
        }
        if (user.getPhoneNo() != null) {
            getUserEmail.setPhoneNo(user.getPhoneNo());
        }
        if (user.getAltPhoneNo() != null) {
            getUserEmail.setAltPhoneNo(user.getAltPhoneNo());
        }
        if (user.getDateOfJoin() != null) {
            getUserEmail.setDateOfJoin(user.getDateOfJoin());
        }
        if (user.getEmail() != null) {
            getUserEmail.setEmail(user.getEmail());
        }
        if (user.getPrimarySkills() != null) {
            getUserEmail.setPrimarySkills(user.getPrimarySkills());
        }
        if (user.getSecondarySkills() != null) {
            getUserEmail.setSecondarySkills(user.getSecondarySkills());
        }

        return userRepository.save(getUserEmail);

    }

    public boolean forgetPassword(String userOrEmail) {
        long code = Code();
        Optional<User> user = userRepository.findByEmail(userOrEmail);
        User getUser = user.get();
        logger.info("Forget Password OTP generated for the user : " + getUser.getName());
        boolean istrue = sendEmail(code, userOrEmail);
        return istrue;
    }

    //	Send Email With unique OTP
    public boolean sendEmail(Long unique_password, String userOrEmail) {
        //logger.info("OTP Password is "+unique_password);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userOrEmail);
        msg.setSubject("Forget Password");
        msg.setText("Dear User ,\n"
                + "\n"
                + "The OTP generated for your Account with ID " + userOrEmail + "  is: " + unique_password
                + "\n\n"
                + "\nUse this OTP to change the password\n"
                + "In case of any queries, kindly contact our customer service desk at the details below\n"
                + "\n"
                + "\n"
                + "Warm Regards,\n"
                + "\n"
                + "ArohaTechnologies");
        try {
            javaMailSender.send(msg);
            logger.info("Email sent to registered email");
            return true;
        } catch (Exception ex) {
            logger.error("Failed to send mail " + ex.getMessage());

        }
        return false;

    }

    public static long Code() //this code returns the  unique 16 digit code  
    {  //creating a 16 digit code using Math.random function 
        long code = (long) ((Math.random() * Math.pow(9, 5)));
        return code; //returning the code 
    }

    public Object updatePassword(ForgetPassword object) {
        String getOtpFromUser = object.getOneTimePass();
        if (getOtpFromUser.equals(unique_password)) {
            String email = object.getUsernameOrEmail();
            Optional<User> obj = userRepository.findByEmail(email);
            User user = obj.get();
            if (passwordEncoder.matches(object.getPassword(), user.getPassword())) {
                logger.info("Error You can not give previous password, please enter a new password");
                return "You can not give previous password, please enter a new password";
            } else {
                user.setPassword(passwordEncoder.encode(object.getPassword()));
                userRepository.save(user);
                logger.info("password changed for :" + user.getName());
                return "Password updated successfully, please login with your new password";
            }
        } else {
            logger.error("OTP didn't matched");
            return "OTP did not matched";
        }
    }
}
