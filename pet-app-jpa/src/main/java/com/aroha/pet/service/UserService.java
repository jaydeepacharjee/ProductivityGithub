package com.aroha.pet.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.aroha.pet.exception.ResourceNotFoundException;
import com.aroha.pet.model.Role;
import com.aroha.pet.model.RoleName;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.ForgetPassword;
import com.aroha.pet.payload.ForgetPasswordCheck;
import com.aroha.pet.payload.ForgetPasswordPayload;
import com.aroha.pet.payload.UsersListPayload;
import com.aroha.pet.repository.RoleRepository;
import com.aroha.pet.repository.UserRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author Sony George | Date : 6 Mar, 2019 6:05:12 PM
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    Map<String, ForgetPasswordCheck> map = null;

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
    
    public long findUserRole(Long userId) {
    	return userRepository.getRole(userId);
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

    public ForgetPasswordPayload updateData(User user) {

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

//        return userRepository.save(getUserEmail);
        try {
            userRepository.save(getUserEmail);
//            return new ApiResponse(Boolean.TRUE, "Successfully updated user data");
            return new ForgetPasswordPayload(HttpStatus.OK.value(), Boolean.TRUE, "Successfully updated user data");
        } catch (Exception ex) {
//            return new ApiResponse(Boolean.FALSE, ex.getMessage());
            return new ForgetPasswordPayload(HttpStatus.BAD_REQUEST.value(), Boolean.FALSE, ex.getMessage());
        }

    }

    public boolean forgetPassword(String userOrEmail) {
        long code = Code();
        map = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        ForgetPasswordCheck f = new ForgetPasswordCheck(code, formatter.format(date));
        map.put(userOrEmail, f);
        Optional<User> user = userRepository.findByEmail(userOrEmail);
        User getUser = user.get();
        logger.info("Forget Password OTP generated for the user : " + getUser.getName());
//        Long i = new Long(code);
//        unique_password = i.toString();
        boolean istrue = sendEmail(code, userOrEmail,getUser.getName());
        return istrue;
    }

    //	Send Email With unique OTP
    public boolean sendEmail(Long unique_password, String userOrEmail,String name) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(userOrEmail);
            helper.setSubject("Forget Password");
            helper.setText("Hi "+ name+",<br><br>\n"
                    + "\n"
                    + "The OTP generated for your Account with ID <i style=\"color:green\"><b>" + userOrEmail + "</b></i>  is: <br>" +"<b style=\"color:blue\">" + unique_password+"</b>"
                    + "<br><br>" 
                    + "\nUse this OTP to change the password, <b style=\"color:red\">OTP will expire in 3 minutes.</b>\n <br><br>"
                    + "In case of any queries, kindly contact our customer service desk at the details below\n<br><br>"
                    + "\n"
                    + "\n"
                    + "Warm Regards,<br>\n"
                    + "\n"
                    + "ArohaTechnologies",true);
            javaMailSender.send(msg);
            logger.info("Email sent to registered email");
            return true;

        } catch (MessagingException | MailException ex) {
             logger.error("Failed to send mail " + ex.getMessage());
        }
        return false;
    }

    public static long Code() //this code returns the  unique 16 digit code  
    {  //creating a 16 digit code using Math.random function 
        long code = (long) ((Math.random() * Math.pow(9, 7)));
        return code; //returning the code 
    }

    public Object updatePassword(ForgetPassword object) {
        if(map==null){
            return new ForgetPasswordPayload(HttpStatus.CONFLICT.value(),Boolean.FALSE,"Something went wrong,try again later");
        }
        ForgetPasswordCheck code = map.get(object.getUsernameOrEmail());
        String getOtpFromUser = object.getOneTimePass();
        logger.info("--------Generated OTP with object-----" + code.getCode());
        logger.info("eNTERED PASSWORD IS: " + getOtpFromUser);
         
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        
        String enteredDate = formatter.format(date);
        Date date1 = null;
        Date date2 = null;
        
        
        Long diffMinutes = null;
        try {
            date1 = formatter.parse(code.getGeneratedDate());
            date2 = formatter.parse(enteredDate);
            long diff = date2.getTime() - date1.getTime();
            diffMinutes = diff / (60 * 1000) % 60;
            logger.info("------ Entered Time is--------" + diffMinutes);
        } catch (ParseException ex) {
            return new ForgetPasswordPayload(HttpStatus.BAD_REQUEST.value(), Boolean.FALSE, ex.getMessage());
        }

        if (diffMinutes > 3) {
            return new ForgetPasswordPayload(HttpStatus.CONFLICT.value(), Boolean.FALSE, "OTP time expired,generate again");
        } else {

            if (getOtpFromUser.equals(code.getCode().toString())) {
                String email = object.getUsernameOrEmail();
                Optional<User> obj = userRepository.findByEmail(email);
                User user = obj.get();
                if (passwordEncoder.matches(object.getPassword(), user.getPassword())) {
                    logger.info("Error You can not give previous password, please enter a new password");
                    return new ForgetPasswordPayload(HttpStatus.CONFLICT.value(), Boolean.FALSE, "Password is same as the old one,please enter new password");
                } else {
                    user.setPassword(passwordEncoder.encode(object.getPassword()));
                    userRepository.save(user);
                    logger.info("password changed for :" + user.getName());
                    return new ForgetPasswordPayload(HttpStatus.OK.value(), Boolean.TRUE, "Password updated successfully, please login with your new password");
                }
            } else {
                logger.error("OTP didn't matched");
                return new ForgetPasswordPayload(HttpStatus.CONFLICT.value(), Boolean.FALSE, "OTP didn't match");
            }
        }
    }
}
