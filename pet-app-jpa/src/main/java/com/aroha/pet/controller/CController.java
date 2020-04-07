package com.aroha.pet.controller;

import com.aroha.pet.payload.CData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.pet.payload.CPayload;
import com.aroha.pet.payload.CResponse;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.CService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/c")
public class CController {

    @Autowired
    private CService cService;

    @PostMapping("/executeC")
    public ResponseEntity<?> executeC(@RequestBody CPayload cpayload, @CurrentUser UserPrincipal currentUser) throws Exception {
        System.out.println("I am here");
        CResponse cResponse = cService.executeC(cpayload, currentUser);
        return ResponseEntity.ok(cResponse);
    }

    @GetMapping("/getReport")
    public ResponseEntity<?> generateReportCard() {
        return ResponseEntity.ok(cService.getReportCard());
    }

    @PostMapping("/getDomainAnalysisForCProgramming")
    public ResponseEntity<?> getDomainAnalysisForCPrograming(@RequestParam String createdAt, @RequestParam long created_by) {
        return ResponseEntity.ok(cService.getDomainResponse(created_by, createdAt));
    }

    @PostMapping("/showAnalysis")
    public ResponseEntity<?> showAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
        if (cService.generateReportAnalysis(createdAt, created_by, domainId).isEmpty()) {
            CData data = new CData();
            data.setMessage("No Result Found");
            data.setStatusCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.ok(data);
        }
        CData data = new CData();
        data.setData(cService.generateReportAnalysis(createdAt, created_by, domainId));
        data.setStatusCode(HttpStatus.OK.value());
        data.setMessage("SUCCESS");
        return ResponseEntity.ok(data);
    }
}
