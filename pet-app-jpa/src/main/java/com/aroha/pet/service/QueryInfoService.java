package com.aroha.pet.service;

import java.text.SimpleDateFormat;
import com.aroha.pet.exception.ResourceNotFoundException;
import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.model.Question;
import com.aroha.pet.model.User;
import com.aroha.pet.repository.QueryInfoRepository;
import com.aroha.pet.repository.QuestionRepository;
import com.aroha.pet.repository.UserRepository;
import com.aroha.pet.security.UserPrincipal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        System.out.println("------------------- I am here ----------------");
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
