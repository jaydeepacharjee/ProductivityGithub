package com.aroha.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.pet.payload.JavaPayload;
import com.aroha.pet.payload.JavaResponse;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.JavaService;

@RestController
@RequestMapping("/api/java")
public class JavaController {

    @Autowired
    private JavaService javaservice;

    @PostMapping("/executeJava")
    public ResponseEntity<?> executeJava(@CurrentUser UserPrincipal currentUser, @RequestBody JavaPayload payload)
            throws Exception {
        JavaResponse javaResponse = javaservice.executeJava(currentUser, payload);
        return ResponseEntity.ok(javaResponse);
    }

}
