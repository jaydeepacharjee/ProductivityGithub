package com.aroha.pet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.PriceModel;
import com.aroha.pet.payload.PriceResponsePayload;
import com.aroha.pet.repository.CoursePriceRepository;

@Service
public class CoursePriceService {
	
	@Autowired
	private CoursePriceRepository courseRepo;
	
	public PriceResponsePayload getAllPrice() {
		List<PriceModel> list=courseRepo.findAll();
		if(list.isEmpty()) {
			return new PriceResponsePayload(HttpStatus.BAD_REQUEST.value(),"Empty Record");
		}
		return new PriceResponsePayload(HttpStatus.OK.value(),"Sucessfully record found", list);
		
	}
	
	public PriceResponsePayload getPriceOnId(int priceId) {

		Optional<PriceModel> obj=courseRepo.findById(priceId);
		if(!obj.isPresent()) {
			return new PriceResponsePayload(HttpStatus.BAD_REQUEST.value(), "Course not found");
		}
		PriceModel price =obj.get();
		return new PriceResponsePayload(HttpStatus.OK.value(),"Successfully record found",price);
	}
}
