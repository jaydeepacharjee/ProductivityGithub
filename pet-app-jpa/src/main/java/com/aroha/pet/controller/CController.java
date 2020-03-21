package com.aroha.pet.controller;

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
    public ResponseEntity<?> generateReportCard(){
        if(cService.getReportCard().isEmpty()){
            return ResponseEntity.ok(new DeleteDomainPayload("No Record", HttpStatus.OK.value()));
        }
        return ResponseEntity.ok(cService.getReportCard());
    }
    
    @PostMapping("/showAnalysis")
    public ResponseEntity<?> showAnalysis(){
        return ResponseEntity.ok("OK");
    }
}
