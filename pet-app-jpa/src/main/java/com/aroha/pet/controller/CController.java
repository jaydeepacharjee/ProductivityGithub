package com.aroha.pet.controller;

import com.aroha.pet.payload.CData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aroha.pet.payload.CPayload;
import com.aroha.pet.payload.CResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
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

    @PostMapping("/getReport")
    public ResponseEntity<?> generateReportCard() {
        if (cService.getReportCard().isEmpty()) {
            return ResponseEntity.ok("No Record");
        }
        return ResponseEntity.ok(cService.getReportCard());
    }

    @PostMapping("/showAnalysis")
    public ResponseEntity<?> showAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
        if (cService.generateReportAnalysis(createdAt, created_by, domainId).isEmpty()) {
            CData data=new CData();
            data.setMessage("No Resule Found");
            data.setStatusCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.ok(data);
        }
        CData data=new CData();
        data.setData(cService.generateReportAnalysis(createdAt, created_by, domainId));
        data.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok(data);
    }
}
