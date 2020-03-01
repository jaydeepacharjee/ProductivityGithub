package com.aroha.pet;

import com.aroha.pet.model.Role;
import com.aroha.pet.model.RoleName;
import com.aroha.pet.model.User;
import com.aroha.pet.repository.RoleRepository;
import com.aroha.pet.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PetApplicationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() {
        User adminUser = new User();
        adminUser.setName("AdminTEST");
        adminUser.setEmail("adminTEST@aroha.co.in");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setPhoneNo("9249876052");
        Set<Role> a = new HashSet<>();
        Role r = roleRepository.findByName(RoleName.ROLE_ADMIN).get();
        a.add(r);
        adminUser.setRoles(a);
        adminUser.setAddress("Aroha Technologies");
        userRepository.save(adminUser);
    }

}
