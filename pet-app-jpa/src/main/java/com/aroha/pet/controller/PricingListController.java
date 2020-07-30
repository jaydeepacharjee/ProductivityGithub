package com.aroha.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aroha.pet.service.CoursePriceService;

@RestController
@RequestMapping("/api/pricing")
public class PricingListController {
	
	@Autowired
	private CoursePriceService courseService;
	
	@GetMapping("/list")
	public ResponseEntity<?> getPricing(){
		return ResponseEntity.ok(courseService.getAllPrice());
	}
	
	@PostMapping("/getListBasedOnId")
	public ResponseEntity<?> getPriceBasedOnId(@RequestParam("priceId") int priceId){
		return ResponseEntity.ok(courseService.getPriceOnId(priceId));
	}

}
