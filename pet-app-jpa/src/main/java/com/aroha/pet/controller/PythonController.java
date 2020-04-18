package com.aroha.pet.controller;

import java.io.IOException;

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
import com.aroha.pet.payload.PythonData;
import com.aroha.pet.payload.PythonPayload;
import com.aroha.pet.payload.PythonResponse;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.PythonService;

@RestController
@RequestMapping("/api/python")
public class PythonController {
	
	@Autowired
	private PythonService pythonService;
	
	@PostMapping("/executePython")
	public ResponseEntity<?> executePython(@RequestBody PythonPayload pythonPayload, @CurrentUser UserPrincipal currentUser) throws Exception{
		PythonResponse pythonResponse=pythonService.executePython(pythonPayload, currentUser);
		return ResponseEntity.ok(pythonResponse);		
	}
	
	 @GetMapping("/getPythonReport")
	    public ResponseEntity<?> generateReportCard() {
	        return ResponseEntity.ok(pythonService.getReportCard());
	    }

	    @PostMapping("/getDomainAnalysisForPythonProgramming")
	    public ResponseEntity<?> getDomainAnalysisForCPrograming(@RequestParam String createdAt, @RequestParam long created_by) {
	        return ResponseEntity.ok(pythonService.getDomainResponse(created_by, createdAt));
	    }

	    @PostMapping("/showAnalysisForPython")
	    public ResponseEntity<?> showAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
	        if (pythonService.generateReportAnalysis(createdAt, created_by, domainId).isEmpty()) {
	            PythonData data = new PythonData();
	            data.setMessage("No Result Found");
	            data.setStatusCode(HttpStatus.NOT_FOUND.value());
	            return ResponseEntity.ok(data);
	        }
	        PythonData data = new PythonData();
	        data.setData(pythonService.generateReportAnalysis(createdAt, created_by, domainId));
	        data.setStatusCode(HttpStatus.OK.value());
	        data.setMessage("SUCCESS");
	        return ResponseEntity.ok(data);
	    }

}
