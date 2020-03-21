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
import com.aroha.pet.payload.DomainDataRequest;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.DomainTable;
import com.aroha.pet.repository.DomainRepository;
import com.aroha.pet.repository.DomainTableRepository;
import com.aroha.pet.repository.FunctionRepository;
import com.aroha.pet.repository.QuestionRepository;
import com.aroha.pet.repository.ScenarioRepository;
import java.util.Base64;

@Service
public class DomainService {

	@Autowired
	DomainRepository domainRepository;

	@Autowired
	DomainTableRepository domainTableRepo;

	@Autowired
	FunctionRepository functionRepository;

	@Autowired
	ScenarioRepository scenarioRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	ScenarioService scenarioService;

	private static final Logger logger = LoggerFactory.getLogger(DomainService.class);

	public List<DomainDataRequest> getAllDomains() {
		List<Domain> list = domainRepository.findAll();
		Iterator<Domain> itr = list.iterator();
		List<DomainDataRequest> DomainList = new ArrayList<DomainDataRequest>();
		while (itr.hasNext()) {
			Domain d = itr.next();
			DomainDataRequest domainData = new DomainDataRequest();
			domainData.setDomainId(d.getDomainId());
			domainData.setDomainName(d.getDomainName());
			DomainList.add(domainData);
		}
		return DomainList;

	}

	public String saveDomain(Domain domain) {
		try {
			domainRepository.save(domain);
			logger.info("Domain saved successfully");
		} catch (Exception ex) {
			logger.error("Domain failed to saved" + ex.getMessage());
			return ex.getMessage();
		}
		return "Domain Saved Successfully";
	}

	public List<DomainTable> getDomain() {
		List<Object[]> d = domainTableRepo.getDomainData();
		List<DomainTable> list = new ArrayList<>();
		for (Object[] object : d) {
			DomainTable domainTable = new DomainTable();
			domainTable.setQuestionId((int) object[0]);
			domainTable.setDomainName((String) object[1]);
			domainTable.setFunctionName((String) object[2]);
			domainTable.setScenarioTitle((String) object[3]);
			domainTable.setQuestionDesc((String) object[4]);
			Scenario scenario = scenarioService.getFile((int) object[5]);
			byte[] encoded = Base64.getEncoder().encode(scenario.getImage());
			domainTable.setImage(new String(encoded));
			list.add(domainTable);
		}
		return list;
	}

	public String updateData(int quetionId, Domain domain, Function function, Scenario scenario, Question question) {

		Optional<Question> ques = questionRepository.findById(quetionId);
		if (ques.isPresent()) {

			DomainRequest obj = domainRepository.updateDomainData(quetionId);
			if (domain != null) {
				Optional<Domain> objData = domainRepository.findById(obj.getDomainId());

				if (!objData.isPresent()) {
					throw new RuntimeException("Domai with id " + obj.getDomainId() + " not found");
				}
				Domain domainObj = objData.get();
				domainObj.setDomainName(domain.getDomainName());
				domainRepository.save(domainObj);
			}

			if (function != null) {
				Optional<Function> functionData = functionRepository.findById(obj.getFunctionId());

				if (!functionData.isPresent()) {
					throw new RuntimeException("Function with id " + obj.getFunctionId() + " not found");
				}
				Function functionObj = functionData.get();
				functionObj.setFunctionName(function.getFunctionName());
				functionRepository.save(functionObj);
			}

			if (scenario != null) {
				Optional<Scenario> scenarioData = scenarioRepository.findById(obj.getScenarioId());

				if (!scenarioData.isPresent()) {
					throw new RuntimeException("Scenario with id " + obj.getScenarioId() + " not dound");
				}
				Scenario scenarioObj = scenarioData.get();
				scenarioObj.setScenarioTitle(scenario.getScenarioTitle());
				scenarioRepository.save(scenarioObj);
			}

			if (question != null) {
				Optional<Question> questionData = questionRepository.findById(obj.getQuestionId());
				if (!questionData.isPresent()) {
					throw new RuntimeException("Question with id " + obj.getQuestionId() + " not found");
				}
				Question questionObj = questionData.get();
				questionObj.setQuestionDesc(question.getQuestionDesc());
				questionRepository.save(questionObj);
			}
		} else {
			throw new RuntimeException("Question with id " + quetionId + " not present");
		}
		if (domain != null || function != null || scenario != null || question != null) {
			return "Domain Table Updated";
		}
		return "";
	}

	public Object checkDuplicate(Domain domain) {
		if (domainRepository.existsBydomainName(domain.getDomainName())) {
			return new ApiResponse(Boolean.TRUE, "Domain name already Exists");
		}
		return new ApiResponse(Boolean.FALSE, "Domain not exists");
	}

	public DeleteDomainPayload deleteDomain(int domainId) {
		Optional<Domain> getDomain=domainRepository.findById(domainId);
		if(!getDomain.isPresent()) {
			return new DeleteDomainPayload("Domain Not Present" , HttpStatus.NOT_FOUND.value());
		}
		Domain domain =getDomain.get();
		try {
			domainRepository.delete(domain);
			return new  DeleteDomainPayload("SuccessFully Deleted", HttpStatus.OK.value());

		}catch(Exception ex) {
			return new DeleteDomainPayload(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
	}
}
