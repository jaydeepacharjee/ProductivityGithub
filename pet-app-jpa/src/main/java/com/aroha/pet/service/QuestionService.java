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
import com.aroha.pet.model.Question;
import com.aroha.pet.model.Scenario;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.payload.QuestionDataRequest;
import com.aroha.pet.repository.DomainRepository;
import com.aroha.pet.repository.FunctionRepository;
import com.aroha.pet.repository.QuestionRepository;
import com.aroha.pet.repository.ScenarioRepository;


@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private FunctionRepository functionRepository;


    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    public GetDomainDataPayload createQuestion(int domainId, int functionId, int scenarioId, Question question) {

        Optional<Domain> byDomainId = domainRepository.findById(domainId);
        Optional<Function> byFunctionId = functionRepository.findById(functionId);
        Optional<Scenario> byScenarioId = scenarioRepository.findById(scenarioId);

        if (!byDomainId.isPresent()) {
//            throw new ResourceNotFoundException("Domain with  id " + domainId + " not Exist");
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Select domain to add a question");
        }
        if (!byFunctionId.isPresent()) {
//            throw new ResourceNotFoundException("Function with  id " + functionId + " not Exist");
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Select function to add a scenario");
        }
        if (!byScenarioId.isPresent()) {
//            throw new ResourceNotFoundException("Scenario with id " + scenarioId + " not Exist");
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(),"Select scenario to add a scenario");
        }
        Domain d = byDomainId.get();
        Function f = byFunctionId.get();
        Scenario s = byScenarioId.get();

        f.setDomain(d);
        d.getFunctions().add(f);
        s.setFunction(f);
        f.getScenario().add(s);
        question.setScenario(s);
        s.getQues().add(question);

        try {
            questionRepository.save(question);
            logger.info("Question saved successfully");
        } catch (Exception ex) {
            logger.error("Question not saved " + ex.getMessage());
            return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(),"Question Saved Successfully");
    }

    public GetDomainDataPayload getQuestionData(int scenarioId) {

        List<Question> listQuestion = questionRepository.findAll();
        List<QuestionDataRequest> listQuestionDataRequest = new ArrayList<>();

        Iterator<Question> itr = listQuestion.iterator();

        while (itr.hasNext()) {
            Question question = itr.next();
            if (question.getScenario().getScenarioId() != scenarioId) {
                continue;
            }
            QuestionDataRequest questionData = new QuestionDataRequest();
            questionData.setQuestionId(question.getQuestionId());
            questionData.setQuestionDescription(question.getQuestionDesc());
            questionData.setAnswer(question.getAnswer());
            listQuestionDataRequest.add(questionData);
        }
        if(listQuestionDataRequest.isEmpty()){
            return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(),"No question is added for the scenario, please add one");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(), listQuestionDataRequest, "SUCCESS");
    }

    public Object checkDuplicateQuestion(DomainRequest domainData) {
        int scenarioId=domainData.getScenarioId();
        String question=domainData.getQuestion().getQuestionDesc().toLowerCase().trim().replaceAll("\\s+","");
        boolean flag=false;
        List<Question>ques=questionRepository.checkDuplicate(scenarioId);
        Iterator<Question>itr=ques.iterator();
        while(itr.hasNext()) {
        	Question obj=itr.next();
        	if(question.equals(obj.getQuestionDesc().toLowerCase().trim().replaceAll("\\s+",""))) {
        		flag=true;
        	}
        }
        if(flag) {
        	return new ApiResponse(Boolean.TRUE,"Question already exists");
        }else {
        	return new ApiResponse(Boolean.FALSE,"Question doesn't exists");
        }
    }

    public DeleteDomainPayload deleteQuestionName(int qestionId) {
        Optional<Question> question = questionRepository.findById(qestionId);
        if (!question.isPresent()) {
            return new DeleteDomainPayload("Question not found", HttpStatus.NOT_FOUND.value());
        }
        Question quesObj = question.get();
        try {
            questionRepository.delete(quesObj);
            return new DeleteDomainPayload("Question deleted successfully", HttpStatus.OK.value());
        } catch (Exception e) {
            return new DeleteDomainPayload(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }
}
