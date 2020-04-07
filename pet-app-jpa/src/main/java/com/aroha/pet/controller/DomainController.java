package com.aroha.pet.controller;

import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.aroha.pet.model.Domain;
import com.aroha.pet.model.Function;
import com.aroha.pet.model.Question;
import com.aroha.pet.model.Scenario;
import com.aroha.pet.payload.CSV;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.DBService;
import com.aroha.pet.service.DomainService;
import com.aroha.pet.service.FunctionService;
import com.aroha.pet.service.QuestionService;
import com.aroha.pet.service.ScenarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/domain")
public class DomainController {

    @Autowired
    FunctionService functionService;

    @Autowired
    DomainService domainService;

    @Autowired
    ScenarioService scenarioService;

    @Autowired
    QuestionService questionService;

    @Autowired
    DBService dbService;

    private static final Logger logger = LoggerFactory.getLogger(DomainController.class);

    @PostMapping("/savedomain")
    public ResponseEntity<?> saveDomainData(@RequestBody DomainRequest domainRequest) {
        int technologyId = domainRequest.getTechnologyId();
        Domain domain = domainRequest.getDomain();
        return ResponseEntity.ok(domainService.saveDomain(technologyId, domain));
    }

    @PostMapping("/checkDuplicateDomain")
    public ResponseEntity<?> checkDuplicateDoain(@RequestBody DomainRequest domain) {
        return ResponseEntity.ok(domainService.checkDuplicate(domain));
    }

    @PostMapping("/getDomains")
    public ResponseEntity<?> getDomainData(@RequestParam("technologyId")int technologyId) {
    	logger.info("------------------"+technologyId);
        return ResponseEntity.ok(domainService.getAllDomains(technologyId));
    }

    @PostMapping("/saveFunction")
    public ResponseEntity<?> createFunction(@RequestBody DomainRequest domainData) {
        int domainId = domainData.getDomainId();
        Function function = domainData.getFunction();
        return ResponseEntity.ok(functionService.createFunction(domainId, function));
    }

    @PostMapping("/checkDuplicateFunction")
    public ResponseEntity<?> checkDuplicateFunction(@RequestBody DomainRequest domainData) {
        return ResponseEntity.ok(functionService.checkDuplicateFunction(domainData));
    }

    @PostMapping("/getFunctions")
    public ResponseEntity<?> getAllFunctions(@RequestBody DomainRequest domainData) {
        int domainId = domainData.getDomainId();
        return ResponseEntity.ok(functionService.getAllFunctions(domainId));
    }

    @PostMapping("/cehckDuplicateScenario")
    public ResponseEntity<?> checkDuplicateScenario(@RequestBody DomainRequest domainData) {
        return ResponseEntity.ok(scenarioService.checkDuplicate(domainData));
    }

    @PostMapping("/saveScenario")
    public ResponseEntity<?> createScenario(@RequestParam("model") String model, @RequestPart(name = "file", required = false) MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();
        Scenario scenario = null;
        try {
            DomainRequest domainData = mapper.readValue(model, DomainRequest.class);
            int domainId = domainData.getDomainId();
            int functionId = domainData.getFunctionId();
            scenario = domainData.getScenario();
            if (file != null) {
                scenario.setImage(file.getBytes());
                scenario.setFileName(file.getOriginalFilename());
            }
            return ResponseEntity.ok(scenarioService.createScenario(domainId, functionId, scenario));
        } catch (Exception ex) {
            logger.error("Failed saving scenario" + ex.getMessage());
            return ResponseEntity.ok(new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @PostMapping(value = "/getScenarioImage")
    public ResponseEntity<?> getScenario(@RequestBody DomainRequest domain) {

        int scenarioId = domain.getScenarioId();
        Scenario scenario = scenarioService.getFile(scenarioId);
        byte[] encoded = Base64.getEncoder().encode(scenario.getImage());
        return ResponseEntity.ok(new String(encoded));

    }

    @PostMapping("/getScenarios")
    public ResponseEntity<?> getAllScenarios(@RequestBody DomainRequest domainData) {
        int domainId = domainData.getDomainId();
        int functionId = domainData.getFunctionId();
        return ResponseEntity.ok(scenarioService.getAllScenario(domainId, functionId));
    }

    @PostMapping("/checDuplicateQuestion")
    public ResponseEntity<?> checkDuplicateQuestion(@RequestBody DomainRequest domainData) {
        return ResponseEntity.ok(questionService.checkDuplicateQuestion(domainData));
    }

    @PostMapping("/saveQuestion")
    public ResponseEntity<?> createQuestion(@RequestParam("model") String model, @RequestPart(name = "file", required = false) MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        Question question = null;

        try {
            DomainRequest domainData = mapper.readValue(model, DomainRequest.class);
            int domainId = domainData.getDomainId();
            int functionId = domainData.getFunctionId();
            int scenarioId = domainData.getScenarioId();

            question = domainData.getQuestion();

            if (file != null) {
                try (Reader in = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                    CSV csv = new CSV(true, ',', in);
                    List< String> fieldNames = null;
                    if (csv.hasNext()) {
                        fieldNames = new ArrayList<>(csv.next());
                    }
                    List< Map< String, String>> list = new ArrayList<>();
                    while (csv.hasNext()) {
                        List< String> x = csv.next();
                        Map< String, String> obj = new LinkedHashMap<>();
                        for (int i = 0; i < fieldNames.size(); i++) {
                            obj.put(fieldNames.get(i), x.get(i));
                        }
                        list.add(obj);
                    }
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    question.setAnswer(mapper.writeValueAsString(list));

                }
            }
            return ResponseEntity.ok(questionService.createQuestion(domainId, functionId, scenarioId, question));
        } catch (Exception ex) {
            logger.error("question not saved" + ex.getMessage());
            return ResponseEntity.ok(ex.getMessage());
        }
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<?> getSQLQuestion(@RequestBody DomainRequest domainData) {
        int scenarioId = domainData.getScenarioId();
        return ResponseEntity.ok(questionService.getQuestionData(scenarioId));
    }
    

    @GetMapping("/getDomain")
    public ResponseEntity<?> getDomain() {
        if(domainService.getDomain().isEmpty()){
            return ResponseEntity.ok(new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(),"No data is found"));
        }
        return ResponseEntity.ok(new GetDomainDataPayload(HttpStatus.OK.value(),domainService.getDomain(),"SUCCESS"));
    }

    @RequestMapping(value = "/updateDomain", method = RequestMethod.POST)
    public ResponseEntity<?> updateDomain(@RequestBody DomainRequest domainRequest, @CurrentUser UserPrincipal user) {

        if (user.isLearnerRole()) {
            throw new RuntimeException("Only Admin and mentor can update");
        }
        int questionId = domainRequest.getQuestionId();
        Question questObj = domainRequest.getQuestion();
        Domain domainObj = domainRequest.getDomain();
        Function funObj = domainRequest.getFunction();
        Scenario scenaObj = domainRequest.getScenario();

        return ResponseEntity.ok(domainService.updateData(questionId, domainObj, funObj, scenaObj, questObj));
    }

    @PostMapping("/deleteDomainName")
    public ResponseEntity<?> deleteDomain(@RequestBody DomainRequest domainRequest) {
        int domainId = domainRequest.getDomainId();
        return ResponseEntity.ok(domainService.deleteDomain(domainId));
    }

    @PostMapping("/deleteFunctionName")
    public ResponseEntity<?> deleteFunction(@RequestBody DomainRequest domainRequest) {
        return ResponseEntity.ok(functionService.deleteFunction(domainRequest.getFunctionId()));
    }

    @PostMapping("/deleteScenarioName")
    public ResponseEntity<?> deleteScenarioName(@RequestBody DomainRequest domainRequest) {
        return ResponseEntity.ok(scenarioService.deleteScenarioName(domainRequest.getScenarioId()));
    }

    @PostMapping("/deleteQuestionName")
    public ResponseEntity<?> deleteQuestionName(@RequestBody DomainRequest domainRequest) {
        return ResponseEntity.ok(questionService.deleteQuestionName(domainRequest.getQuestionId()));
    }

}
