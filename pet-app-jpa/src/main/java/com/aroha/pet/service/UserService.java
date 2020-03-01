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
import com.aroha.pet.repository.RoleRepository;
import com.aroha.pet.repository.UserRepository;

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
    //	public Optional<User> findById(long id) {
    //		return userRepository.findById(id);
    //	}

    public List<User> findByRoles(String roleName) {
        return userRepository.findByRoles(roleName);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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
        for (long i = code; i != 0; i /= 100)//a loop extracting 2 digits from the code  
        {
            long digit = i % 100;//extracting two digits 
            if (digit <= 90) {
                digit = digit + 32;
            }
            //converting those two digits(ascii value) to its character value 
            char ch = (char) digit;
            // adding 32 so that our least value be a valid character  
            unique_password = ch + unique_password;//adding the character to the string 
        }
        Optional<User> user = userRepository.findByEmail(userOrEmail);
        User getUser = user.get();
        logger.info("Forget Password OTP generated for the user : " + getUser.getName());
        boolean istrue = sendEmail(unique_password, userOrEmail);
        return istrue;
    }

    //	Send Email With unique OTP
    public boolean sendEmail(String unique_password, String userOrEmail) {
        //logger.info("OTP Password is "+unique_password);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userOrEmail);
        msg.setSubject("Forget Password");
        msg.setText("Dear User ,\n"
                + "\n"
                + "The OTP generated for your Account with ID " + userOrEmail + "  is : " + unique_password
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
        long code = (long) ((Math.random() * 9 * Math.pow(10, 15)) + Math.pow(10, 15));
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