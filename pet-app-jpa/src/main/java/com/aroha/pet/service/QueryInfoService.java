package com.aroha.pet.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.pet.exception.ResourceNotFoundException;
import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.model.Question;
import com.aroha.pet.model.User;
import com.aroha.pet.repository.QueryInfoRepository;
import com.aroha.pet.repository.QuestionRepository;
import com.aroha.pet.repository.UserRepository;
import com.aroha.pet.security.UserPrincipal;

/**
 *
 * @author Sony George | Date : 13 Mar, 2019 4:32:17 PM
 */
@Service
public class QueryInfoService {

    @Autowired
    QueryInfoRepository queryInfoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository quesRepo;
    
    private Logger logger=LoggerFactory.getLogger(QueryInfoService.class);

    public List<QueryInfo> getAllQueryInfoCreatedBy(UserPrincipal currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getEmail()));

        return queryInfoRepository.findAllByCreatedBy(user.getId());
    }

    public QueryInfo save(QueryInfo queryInfo) {
        return queryInfoRepository.save(queryInfo);
    }
    
    public QueryInfo update(QueryInfo queryInfo) {
        return queryInfoRepository.save(queryInfo);
    }
    
    public boolean checkDuplicateQuery(int questionId,Date date,Long userId,String sqlstr) {
//    	logger.info("-----questionid---"+questionId);
//    	logger.info("-------UerId-----"+userId);
//    	logger.info("---------SqlStr-------"+sqlstr);
    	boolean flag=false;
    	try {
    		 SimpleDateFormat formatter2 = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
//    		 logger.info("--------------------Date is-----------"+formatter2.format(date));
    		if(queryInfoRepository.checkDuplicateQuery(questionId, date, userId, sqlstr)>0){
    			flag=true;
    		}
    		
    	}catch (Exception ex) {}
    	logger.info("-------------Flag-------"+flag);
    	return flag;
    }

    public List<QueryInfo> getAllQueryInfoByParams(Long userId, String fromDate, String toDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (userId == null || userId < 1) {
            throw new RuntimeException("User Id Can not be Null or less than 1");
        }
        if (fromDate == null && toDate == null) {
            return queryInfoRepository.findAllByCreatedBy(userId);
        }
        if (fromDate != null && toDate == null) {
            return queryInfoRepository.findAllByCreatedByAndCreatedAtGreaterThanEqual(userId, sdf.parse(fromDate).toInstant());
        }
        if (fromDate != null && toDate != null) {
            return queryInfoRepository.findAllByCreatedByAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(userId, sdf.parse(fromDate).toInstant(), sdf.parse(toDate).toInstant());
        }
        if (fromDate == null && toDate != null) {
            return queryInfoRepository.findAllByCreatedByAndCreatedAtLessThanEqual(userId, sdf.parse(toDate).toInstant());
        }
        return null;
    }

    //         Testing purpose
    public void getAllQuestions() {
        List<QueryInfo> query = queryInfoRepository.findAll();
        List<Question> question = quesRepo.findAll();
        for (int i = 0; i < query.size(); i++) {
            QueryInfo list = query.get(i);
            for (int j = 0; j < question.size(); j++) {
                Question obj = question.get(j);
                if (list.getScenario() != null) {
                    if (list.getScenario().trim().equalsIgnoreCase(obj.getQuestionDesc().trim())) {
                        list.setQuestionId(obj.getQuestionId());
                        queryInfoRepository.save(list);
                    }
                }
            }
        }

    }
}
