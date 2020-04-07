package com.aroha.pet.controller;

import com.aroha.pet.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jaydeep
 */
@RestController
@RequestMapping("/api/tech")
public class TechnologyController {
    
    @Autowired
    private TechnologyService techService;
    
    @GetMapping("/allTechnology")
    public ResponseEntity<?> getAllTechnology(){
        return ResponseEntity.ok(techService.findAllTechnology());
    }
}
