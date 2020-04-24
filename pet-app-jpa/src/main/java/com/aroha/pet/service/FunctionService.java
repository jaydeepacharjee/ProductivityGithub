package com.aroha.pet.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.FunctionDataRequest;
import com.aroha.pet.payload.GetDomainDataPayload;
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

    public GetDomainDataPayload createFunction(int domainId, Function function) {

        Optional<Domain> byId = domainRepository.findById(domainId);
        if (!byId.isPresent()) {
              return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Select domain to save a function");
        }
        Domain d = byId.get();
        function.setDomain(d);
        try {
            functionRepository.save(function);
            logger.info("function saved successfully");
        } catch (Exception ex) {
            logger.error("Failed saving function " + ex.getMessage());
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(),"Function not saved successfully");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(),"Function Saved Successfully");

    }

    public GetDomainDataPayload getAllFunctions(int domainId) {
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
        if(functionDataList.isEmpty()){
            return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(),"No Function is added for the domain, please add one");
        }else{
            return new GetDomainDataPayload(HttpStatus.OK.value(),functionDataList ,"SUCCESS");
        }    
    }

    public Object checkDuplicateFunction(DomainRequest domainData) {
       int domainId=domainData.getDomainId();
       String functionName=domainData.getFunction().getFunctionName().toLowerCase().trim().replaceAll("\\s+","");
       boolean flag=false;
       List<Function>function=functionRepository.checkDuplicate(domainId);
       Iterator<Function>itr=function.iterator();
       while(itr.hasNext()) {
    	   Function funObject=itr.next();
    	   if(functionName.equals(funObject.getFunctionName().toLowerCase().trim().replaceAll("\\s+",""))) {
    		   flag=true;
    	   }
       }
       if(flag) {
			return new ApiResponse(Boolean.TRUE, "Function already exists");
		}else {
			return new ApiResponse(Boolean.FALSE,"Function doesn't exists");
		}
    }

    public DeleteDomainPayload deleteFunction(int functionId) {
        Optional<Function> function = functionRepository.findById(functionId);
        if (!function.isPresent()) {
            return new DeleteDomainPayload("Function not present", HttpStatus.NOT_FOUND.value());
        }
        Function funObj = function.get();
        try {
            functionRepository.delete(funObj);
            return new DeleteDomainPayload("Function deleted successfully along with its associated"
                    + "scenarios and questions", HttpStatus.OK.value());
        } catch (Exception e) {
            return new DeleteDomainPayload(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

}
