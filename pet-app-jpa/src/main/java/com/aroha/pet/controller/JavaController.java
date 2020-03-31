package com.aroha.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aroha.pet.payload.CData;
import com.aroha.pet.payload.JavaData;
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
    
    @GetMapping("/getJavaReport")
    public ResponseEntity<?> generateReportCard(){
    	if(javaservice.getReportCard().isEmpty()) {
    		return ResponseEntity.ok("No Record");
    	}
		return ResponseEntity.ok(javaservice.getReportCard());   	
    }
    
    @PostMapping("/getDomainAnalysisForJavaProgramming")
    public ResponseEntity<?> getDomainAnalysisForJavaPrograming(@RequestParam String createdAt, @RequestParam long created_by) {
        return ResponseEntity.ok(javaservice.getDomainResponse(created_by, createdAt));
    }
    
    @PostMapping("/showAnalysisForJava")
    public ResponseEntity<?> showAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
        if (javaservice.generateReportAnalysis(createdAt, created_by, domainId).isEmpty()) {
            JavaData data = new JavaData();
            data.setMessage("No Result Found");
            data.setStatusCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.ok(data);
        }
        JavaData data = new JavaData();
        data.setData(javaservice.generateReportAnalysis(createdAt, created_by, domainId));
        data.setMessage("SUCCESS");
        data.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok(data);
    }
}
