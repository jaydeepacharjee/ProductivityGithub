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
import com.aroha.pet.model.Technology;
import com.aroha.pet.payload.ApiResponse;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainDataRequest;
import com.aroha.pet.payload.DomainRequest;
import com.aroha.pet.payload.DomainTable;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.repository.DomainRepository;
import com.aroha.pet.repository.DomainTableRepository;
import com.aroha.pet.repository.FunctionRepository;
import com.aroha.pet.repository.QuestionRepository;
import com.aroha.pet.repository.ScenarioRepository;
import java.util.Base64;

@Service
public class DomainService {

	@Autowired
	private DomainRepository domainRepository;

	@Autowired
	private DomainTableRepository domainTableRepo;

	@Autowired
	private FunctionRepository functionRepository;

	@Autowired
	private ScenarioRepository scenarioRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private ScenarioService scenarioService;

	@Autowired
	private TechnologyService techService;

	@Autowired
	private FunctionService functionService;

	private static final Logger logger = LoggerFactory.getLogger(DomainService.class);

	public GetDomainDataPayload getAllDomains(int techId) {

		Optional<Technology> tech = techService.findById(techId);
		if (!tech.isPresent()) {
			return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "Selected technolgy is missing from the database");
		}

		List<Domain> list = domainRepository.getDomainOntechnology(techId);
		Iterator<Domain> itr = list.iterator();
		List<DomainDataRequest> DomainList = new ArrayList<>();
		GetDomainDataPayload data = new GetDomainDataPayload();
		while (itr.hasNext()) {
			Domain d = itr.next();
			DomainDataRequest domainData = new DomainDataRequest();
			domainData.setDomainId(d.getDomainId());
			domainData.setDomainName(d.getDomainName());
			DomainList.add(domainData);
		}
		if (DomainList.isEmpty()) {
			return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "No Domain is added for the technology, please add one");
		} else {
			return new GetDomainDataPayload(HttpStatus.OK.value(), DomainList, "Domain Details loaded Successfully");
		}

	}

	public GetDomainDataPayload saveDomain(int technologyId, Domain domain) {

		Optional<Technology> tech = techService.findById(technologyId);
		if (!tech.isPresent()) {
			return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Select technology to save domain");
		}
		Technology technology = tech.get();
		domain.setTechnology(technology);
		technology.getDomain().add(domain);
		try {
			domainRepository.save(domain);
			logger.info("Domain saved");
		} catch (Exception ex) {
			logger.error("Domain not saved" + ex.getMessage());
			return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(), "Domain not saved successfully");
		}
		return new GetDomainDataPayload(HttpStatus.OK.value(), "Domain Saved Successfully");
	}

	public List<DomainTable> getDomain() {
		List<Object[]> d = domainTableRepo.getDomainData();
		List<DomainTable> list = new ArrayList<>();
		for (Object[] object : d) {
			DomainTable domainTable = new DomainTable();
			domainTable.setTechnologyId((int)object[0]);
			domainTable.setTechnologyName((String)object[1]);
			domainTable.setQuestionId((int) object[2]);
			domainTable.setDomainName((String) object[3]);
			domainTable.setFunctionName((String) object[4]);
			domainTable.setScenarioTitle((String) object[5]);
			domainTable.setQuestionDesc((String) object[6]);
			Scenario scenario = scenarioService.getFile((int) object[7]);
			if (scenario.getImage() != null) {
				byte[] encoded = Base64.getEncoder().encode(scenario.getImage());
				domainTable.setImage(new String(encoded));
			}
			list.add(domainTable);
		}
		return list;
	}

	public GetDomainDataPayload updateDomain(DomainRequest domainRequest) {
		int questionId = domainRequest.getQuestionId(); 
		Domain domainObj = domainRequest.getDomain();

		Optional<Question> ques = questionRepository.findById(questionId);
		if(ques.isPresent()) {
			DomainRequest obj = domainRepository.updateDomainData(questionId);
			logger.info("DomainId " + obj.getDomainId());
			if (domainObj != null) {
				Optional<Domain> objData = domainRepository.findById(obj.getDomainId());

				if (!objData.isPresent()) {
					return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "Selected domain not found");
				}
				ApiResponse res=(ApiResponse)checkDuplicate(domainRequest);
				if(res.getSuccess()) {
					return new GetDomainDataPayload(HttpStatus.BAD_REQUEST.value(),"Domain already exists");
				}
				Domain domain = objData.get();
				domain.setDomainName(domainObj.getDomainName());
				domainRepository.save(domain);
				return new GetDomainDataPayload(HttpStatus.OK.value(), "Domain updated successfully");
			}
		}		
		return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "Something went wrong");
	}


	public Object checkDuplicate(DomainRequest domain) {
		int techId = domain.getTechnologyId();
		String domainName = domain.getDomain().getDomainName().toLowerCase().trim().replaceAll("\\s+", "");
		Boolean flag = false;
		List<Domain> domObj = domainRepository.getDomainOntechnology(techId);
		Iterator<Domain> itr = domObj.iterator();
		while (itr.hasNext()) {
			Domain d = itr.next();
			if (domainName.equals(d.getDomainName().toLowerCase().trim().replaceAll("\\s+", ""))) {
				flag = true;
				break;

			}
		}
		if (flag) {
			return new ApiResponse(Boolean.TRUE, "Domain already exists");
		} else {
			return new ApiResponse(Boolean.FALSE, "Domain doesn't exists");
		}
	}

	public DeleteDomainPayload deleteDomain(int domainId) {
		Optional<Domain> getDomain = domainRepository.findById(domainId);
		if (!getDomain.isPresent()) {
			return new DeleteDomainPayload("Domain Not Present", HttpStatus.NOT_FOUND.value());
		}
		Domain domain = getDomain.get();
		try {
			domainRepository.delete(domain);
			return new DeleteDomainPayload("Domain deleted successfully along with its associated"
					+ " functions,scenarios and questions", HttpStatus.OK.value());

		} catch (Exception ex) {
			return new DeleteDomainPayload(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
	}
}
