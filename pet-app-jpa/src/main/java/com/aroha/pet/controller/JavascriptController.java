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
import com.aroha.pet.payload.JavascriptData;
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
    public ResponseEntity<?> executeJavascript(@CurrentUser UserPrincipal currentUser, @RequestBody JavascriptPayload payload) throws Exception {
        JavascriptResponse javascriptResponse = javascriptservice.executeJavascript(currentUser, payload);
        return ResponseEntity.ok(javascriptResponse);
    }

    @GetMapping("/getJavascriptReport")
    public ResponseEntity<?> generateReportCard() {
        return ResponseEntity.ok(javascriptservice.getReportCard());
    }

    @PostMapping("/getDomainAnalysisForJavascriptProgramming")
    public ResponseEntity<?> getDomainAnalysisForJavascriptPrograming(@RequestParam String createdAt, @RequestParam long created_by) {
        return ResponseEntity.ok(javascriptservice.getDomainResponse(created_by, createdAt));
    }

    @PostMapping("/showAnalysisForJavascript")
    public ResponseEntity<?> showAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
        if (javascriptservice.generateReportAnalysis(createdAt, created_by, domainId).isEmpty()) {
            JavascriptData data = new JavascriptData();
            data.setMessage("No Result Found");
            data.setStatusCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.ok(data);
        }
        JavascriptData data = new JavascriptData();
        data.setData(javascriptservice.generateReportAnalysis(createdAt, created_by, domainId));
        data.setMessage("SUCCESS");
        data.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok(data);
    }

}
