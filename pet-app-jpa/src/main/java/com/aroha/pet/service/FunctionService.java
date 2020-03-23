package com.aroha.pet.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainRequest;
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
            throw new ResourceNotFoundException("Domain with a id " + domainId + " Not Exist");
        }
        Domain d = byId.get();
        function.setDomain(d);
        try {
            functionRepository.save(function);
            logger.info("function saved successfully");
        } catch (Exception ex) {
            logger.error("Failed saving function " + ex.getMessage());
            return ex.getMessage();
        }
        return "Function Saved Successfully";

    }

    public List<FunctionDataRequest> getAllFunctions(int domainId) {
        List<Function> list = functionRepository.findAll();
        List<FunctionDataRequest> functionDataList = new ArrayList<>();
        Iterator<Function> itr = list.iterator();
        while (itr.hasNext()) {
            Function function = itr.next();
            if (function.getDomain().getDomainId() != domainId) {
                continue;
            }
            FunctionDataRequest functionData = new FunctionDataRequest();
            functionData.setFunctionId(function.getFunctionId());
            functionData.setFunctionName(function.getFunctionName());
            functionDataList.add(functionData);
        }
        return functionDataList;
    }

    public Object checkDuplicateFunction(DomainRequest domainData) {
        if (functionRepository.checkDuplicate(domainData.getDomainId(), domainData.getFunction().getFunctionName()) > 0) {
            return new ApiResponse(Boolean.TRUE, "Function name already present for the domain ");
        }
        return new ApiResponse(Boolean.FALSE, "Function not present for domain");
    }
    
    public DeleteDomainPayload deleteFunction(int functionId) {
    	Optional<Function> function=functionRepository.findById(functionId);
    	if(!function.isPresent()) {
    		return new DeleteDomainPayload("Function not present",HttpStatus.NOT_FOUND.value());
    	}
    	Function funObj=function.get();
    	try {
    		functionRepository.delete(funObj);
    		return new DeleteDomainPayload("Function deleted successfully along with its associated"
                        + "scenarios and questions", HttpStatus.OK.value());
    	}catch (Exception e) {
    		return new DeleteDomainPayload(e.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
    }

}
