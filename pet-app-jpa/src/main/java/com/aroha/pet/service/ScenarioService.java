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
import com.aroha.pet.exception.FileNotFoundException;
import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.model.Question;
import com.aroha.pet.model.Scenario;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.payload.ScenarioDataRequest;
import com.aroha.pet.repository.DomainRepository;
import com.aroha.pet.repository.FunctionRepository;
import com.aroha.pet.repository.QuestionRepository;
import com.aroha.pet.repository.ScenarioRepository;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ScenarioService {

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    DomainRepository domainRepository;

    @Autowired
    FunctionRepository functionRepository;
    
    @Autowired
    QuestionRepository questionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScenarioService.class);

    public GetDomainDataPayload getAllScenario(int domainId, int functionId) {
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
        if (listScenarioData.isEmpty()) {
            return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "No Scenario is added for the function, please add one");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(), listScenarioData, "Scenario Details loaded Successfully");
    }

    public GetDomainDataPayload createScenario(int domainId, int functionId, Scenario scenario) {

        Optional<Domain> byIdDomain = domainRepository.findById(domainId);
        Optional<Function> byIdFunction = functionRepository.findById(functionId);
        if (!byIdDomain.isPresent()) {
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Select domain to add a scenario");
        }
        if (!byIdFunction.isPresent()) {
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Select function to add a scenario");
        }
        Domain d = byIdDomain.get();
        Function f = byIdFunction.get();
        f.setDomain(d);
        scenario.setFunction(f);
        try {
            scenarioRepository.save(scenario);
            logger.info("Scenario saved successfully");
        } catch (Exception ex) {
            logger.error("Scenario failed saved " + ex.getMessage());
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(),"Scenario not saved successfully");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(), "Scenario Saved Successfully");

    }

    public Scenario getFile(int fileId) {
        return scenarioRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("Not found"));
    }
    
    
    
    public GetDomainDataPayload updateScenario(DomainRequest domainRequest) {
		int questionId = domainRequest.getQuestionId(); 
		Scenario scenaObj = domainRequest.getScenario();

		Optional<Question> ques = questionRepository.findById(questionId);
		if(ques.isPresent()) {
			DomainRequest obj = domainRepository.updateDomainData(questionId);

			
			if (scenaObj != null) {
				Optional<Scenario> scenarioData = scenarioRepository.findById(obj.getScenarioId());

				if (!scenarioData.isPresent()) {
					return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "Selected scenario not found");
				}
				ApiResponse res=(ApiResponse)checkDuplicate(domainRequest,obj);
				if(res.getSuccess()) {
					return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(),"Scenario already exists");
				}
				Scenario scenarioObj = scenarioData.get();
				scenarioObj.setScenarioTitle(scenaObj.getScenarioTitle());
				scenarioRepository.save(scenarioObj);
				return new GetDomainDataPayload(HttpStatus.OK.value(), "Scenario updated successfully");
			}
		}		
		return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "Something went wrong");
	}
    
    

    public Object checkDuplicate(DomainRequest domainData) {
        int functionId = domainData.getFunctionId();
        String scenarioTitle = domainData.getScenario().getScenarioTitle().toLowerCase().trim().replaceAll("\\s+", "");
        boolean flag = false;
        List<Scenario> scenario = scenarioRepository.checkDuplicate(functionId);
        Iterator<Scenario> itr = scenario.iterator();
        while (itr.hasNext()) {
            Scenario obj = itr.next();
            if (scenarioTitle.equals(obj.getScenarioTitle().toLowerCase().trim().replaceAll("\\s+", ""))) {
                flag = true;
                break;
            }
        }
        if (flag) {
            return new ApiResponse(Boolean.TRUE, "Scenario already exists");
        } else {
            return new ApiResponse(Boolean.FALSE, "Scenario doesn't exists");
        }
    }
    
    // Method overloading for check duplicate function from update scenario page
    
    public Object checkDuplicate(DomainRequest domainData,DomainRequest functionObj) {
        int functionId = functionObj.getFunctionId();
        String scenarioTitle = domainData.getScenario().getScenarioTitle().toLowerCase().trim().replaceAll("\\s+", "");
        boolean flag = false;
        List<Scenario> scenario = scenarioRepository.checkDuplicate(functionId);
        Iterator<Scenario> itr = scenario.iterator();
        while (itr.hasNext()) {
            Scenario obj = itr.next();
            if (scenarioTitle.equals(obj.getScenarioTitle().toLowerCase().trim().replaceAll("\\s+", ""))) {
                flag = true;
                break;
            }
        }
        if (flag) {
            return new ApiResponse(Boolean.TRUE, "Scenario already exists");
        } else {
            return new ApiResponse(Boolean.FALSE, "Scenario doesn't exists");
        }
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

    public DeleteDomainPayload updateImage(int scenarioId, MultipartFile file) {
        Optional<Scenario> scn = scenarioRepository.findById(scenarioId);
        if (!scn.isPresent()) {
            return new DeleteDomainPayload("Scenario is not found", HttpStatus.BAD_REQUEST.value());
        }
        Scenario scenario = scn.get();
        String err="";
        try {
            if (file != null) {
                scenario.setImage(file.getBytes());
                scenarioRepository.save(scenario);
                return new DeleteDomainPayload("Image updated successfuly", HttpStatus.OK.value());
            }
        } catch (IOException ex) {
            err=ex.getMessage();
            logger.info("unable to update image"+ex.getMessage());
        }
        return new DeleteDomainPayload("Failed to update image", HttpStatus.BAD_REQUEST.value());
    }
}
