package com.aroha.pet.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aroha.pet.payload.JavascriptPayload;
import com.aroha.pet.payload.JavascriptResponse;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.JavascriptService;

@RestController
@RequestMapping("api/javascript")
public class JavascriptController {
	
	@Autowired
	private JavascriptService javascriptservice;
	
	
	@PostMapping("/executeJavascript")
	public ResponseEntity<?> executeJavascript(@CurrentUser UserPrincipal currentUser, @RequestBody JavascriptPayload payload) throws Exception{
		JavascriptResponse javascriptResponse=javascriptservice.executeJavascript(currentUser, payload);
		return ResponseEntity.ok(javascriptResponse);		
	}

}
