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

import com.aroha.pet.exception.FileNotFoundException;
import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.model.Scenario;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.ScenarioDataRequest;
import com.aroha.pet.repository.DomainRepository;
import com.aroha.pet.repository.FunctionRepository;
import com.aroha.pet.repository.ScenarioRepository;

@Service
public class ScenarioService {

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    DomainRepository domainRepository;

    @Autowired
    FunctionRepository functionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScenarioService.class);

    public List<ScenarioDataRequest> getAllScenario(int domainId, int functionId) {
        List<Scenario> scenarioList = scenarioRepository.findAll();
        List<ScenarioDataRequest> listScenarioData = new ArrayList<>();

        Iterator<Scenario> itr = scenarioList.iterator();
        while (itr.hasNext()) {
            Scenario sc = itr.next();
            if (sc.getFunction().getFunctionId() != functionId || sc.getFunction().getDomain().getDomainId() != domainId) {
                continue;
            }
            ScenarioDataRequest scData = new ScenarioDataRequest();
            scData.setScenarioId(sc.getScenarioId());
            scData.setScenarioTitle(sc.getScenarioTitle());
            listScenarioData.add(scData);
        }

        return listScenarioData;
    }

    public String createScenario(int domainId, int functionId, Scenario scenario) {

        Optional<Domain> byIdDomain = domainRepository.findById(domainId);
        Optional<Function> byIdFunction = functionRepository.findById(functionId);
        if (!byIdDomain.isPresent()) {
            throw new ResourceNotFoundException("Domain with a id " + domainId + " Not Exist");
        }
        if (!byIdFunction.isPresent()) {
            throw new ResourceNotFoundException("Function with a id " + functionId + " Not Exist");
        }
        Domain d = byIdDomain.get();
        System.out.println("------------- Domain id: " + d.getDomainId());
        Function f = byIdFunction.get();
        System.out.println("------------- Function id is: " + f.getFunctionId());
        f.setDomain(d);
        scenario.setFunction(f);
        try {
            scenarioRepository.save(scenario);
            logger.info("Scenario saved successfully");
        } catch (Exception ex) {
            logger.error("Scenario failed saved " + ex.getMessage());
            return ex.getMessage();
        }
        return "Scenario Saved Successfully";

    }

    public Scenario getFile(int fileId) {

        //System.out.println("FieldId: "+fileId);
        return scenarioRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("Not found"));
    }

    public Object checkDuplicate(DomainRequest domainData) {
        if (scenarioRepository.checkDuplicate(domainData.getFunctionId(), domainData.getScenario().getScenarioTitle()) > 0) {
            return new ApiResponse(Boolean.TRUE, "Scenario name already present for the function ");
        }
        return new ApiResponse(Boolean.FALSE, "Scenario Name not Present for the function");
    }

    public DeleteDomainPayload deleteScenarioName(int scenarioId) {
        Optional<Scenario> scenario = scenarioRepository.findById(scenarioId);
        if (!scenario.isPresent()) {
            return new DeleteDomainPayload("Scenario not found", HttpStatus.NOT_FOUND.value());
        }
        Scenario scenariObj = scenario.get();
        try {
            scenarioRepository.delete(scenariObj);
            return new DeleteDomainPayload("Scenario deleted successfully along with its associated "
                    + "questions", HttpStatus.OK.value());
        } catch (Exception e) {
            return new DeleteDomainPayload(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
