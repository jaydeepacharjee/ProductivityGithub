package com.aroha.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.pet.payload.CPayload;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.CService;

@RestController
@RequestMapping("/api/C")
public class CController {

	@Autowired
	private CService cService;

	@PostMapping("/execute")
	public ResponseEntity<?> executeCProgram(@CurrentUser UserPrincipal user,@RequestBody CPayload load){
		try {
			return ResponseEntity.ok(cService.cService(user, load));
		}catch (Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}
	}
}
