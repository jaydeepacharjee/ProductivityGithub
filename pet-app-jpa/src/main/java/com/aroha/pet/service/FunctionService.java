package com.aroha.pet.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.payload.FunctionDataRequest;
import com.aroha.pet.repository.DomainRepository;
import com.aroha.pet.repository.FunctionRepository;

@Service
public class FunctionService {

	@Autowired
	FunctionRepository functionRepository;
	@Autowired
	DomainRepository domainRepository;
	
	 private static final Logger logger = LoggerFactory.getLogger(FunctionService.class);
	
	public Function saveFunction(Function function) {
		return functionRepository.save(function);
	}
	
	public String createFunction(int domainId, Function function) {
		
		Optional<Domain> byId = domainRepository.findById(domainId);
		 if (!byId.isPresent()) {
	            throw new ResourceNotFoundException("Domain with a id "+domainId+" Not Exist");
	        }
		 Domain d=byId.get();
		 function.setDomain(d);
		 try {
		 functionRepository.save(function);
		 logger.info("function saved successfully");
		 }catch(Exception ex) {
			 logger.error("Failed saving function "+ex.getMessage());
			 return ex.getMessage();}
		return "Function Saved Successfully";
		 
	}
	
	public List<FunctionDataRequest>  getAllFunctions(int domainId){
		 List<Function> list=functionRepository.findAll();
		 List<FunctionDataRequest> functionDataList=new ArrayList<>();
		 Iterator<Function> itr=list.iterator();
		 while(itr.hasNext()) {
			   Function function=itr.next();
			   if(function.getDomain().getDomainId() != domainId) continue;
			   FunctionDataRequest functionData=new FunctionDataRequest();
			   functionData.setFunctionId(function.getFunctionId());
			   functionData.setFunctionName(function.getFunctionName());
			   functionDataList.add(functionData);
		 }
		 return functionDataList;
	}
}
