package com.aroha.pet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.FeedBack;
import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.model.QuestionQueryInfo;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.DomainResponsePayload;
import com.aroha.pet.payload.FeedBackStatusPayload;
import com.aroha.pet.payload.FunctionResponsePayload;
import com.aroha.pet.payload.MentorFeedback;
import com.aroha.pet.payload.MentorFeedbackResponse;
import com.aroha.pet.payload.Message;
import com.aroha.pet.payload.Query;
import com.aroha.pet.payload.QuestionResponsePayload;
import com.aroha.pet.payload.Result;
import com.aroha.pet.payload.ScenarioResponsePayload;
import com.aroha.pet.repository.FeedBackRepository;
import com.aroha.pet.repository.MentorFeedbackRepository;
import com.aroha.pet.repository.QuestionQueryInfoRepository;
import com.aroha.pet.security.UserPrincipal;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository fedRepo;

    @Autowired
    private QuestionQueryInfoRepository quesRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MentorFeedbackRepository mentorFeedbackRepo;

    private final Logger logger = LoggerFactory.getLogger(FeedBackService.class);

    public List<FeedBackStatusPayload> getData() {
        List<Object[]> list = fedRepo.getFeedBackStatus();
        ArrayList<FeedBackStatusPayload> listObj = new ArrayList<>();
        list.stream().map((obj) -> {
            FeedBackStatusPayload load = new FeedBackStatusPayload();
            java.sql.Timestamp i = (java.sql.Timestamp) obj[2];
            java.math.BigInteger id = (java.math.BigInteger) obj[0];
            load.setCreated_by(id);
            String name = (String) obj[1];
            load.setName(name);
            load.setCreated_at(i.toString());
            java.math.BigInteger j = (java.math.BigInteger) obj[3];
            load.setNoOfException(j);
            java.math.BigInteger k = (java.math.BigInteger) obj[4];
            load.setNoOfScenario(k);
            java.math.BigInteger l = (java.math.BigInteger) obj[5];
            load.setNoOfSqlStr(l);
            java.math.BigDecimal m = (java.math.BigDecimal) obj[6];
            load.setProductivity(m);
            return load;
        }).forEachOrdered((load) -> {
            listObj.add(load);
        });
        return listObj;
    }

    public Message showAnalysis(long created_by, String createdAt, int questionId) {
        ArrayList<QuestionQueryInfo> list = (ArrayList<QuestionQueryInfo>) quesRepo.getReport(created_by, createdAt, questionId);
        Iterator<QuestionQueryInfo> itr = list.iterator();
        Message message = new Message();
        List<Query> queryList = new ArrayList<>();
        while (itr.hasNext()) {
            QuestionQueryInfo query = itr.next();
            Query queryObj = new Query();
            queryObj.setScenario(query.getScenario());
            queryObj.setSqlStr(query.getSqlStr());

            if (query.getExceptionStr() == null) {

                queryObj.setExceptionStr("No results to display");

            } else {

                queryObj.setExceptionStr(query.getExceptionStr());
            }
            if (query.getResultStr() != null) {
                JSONArray jsona = new JSONArray(query.getResultStr());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Data", jsona);

                queryObj.setResultStr(jsonObject.toString());

            } else {
                JSONArray jsona = new JSONArray("[{NoData:No result to display}]");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Data", jsona);

                queryObj.setResultStr(jsonObject.toString());
            }

            queryObj.setQuestionId(query.getQuestionId());
            String created = query.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim();
            queryObj.setCreatedAt(created);
            queryObj.setMentorName(query.getMentorName());
            queryObj.setFeedback(query.getFeedback());
            queryObj.setFeedBackDate(query.getFeedbackDate());
            queryList.add(queryObj);
            message.setQueryResponse(queryList);
            message.setStatus(HttpStatus.OK);
        }
        return message;
    }

    public Set<DomainResponsePayload> getDomainResponse(long created_by, String createdAt) {
        Set<DomainResponsePayload> domainName = new HashSet<>();
        List<Object[]> getDomain = fedRepo.getDomainRepo(created_by, createdAt);
        getDomain.stream().map((object) -> {
            DomainResponsePayload dLoad = new DomainResponsePayload();
            dLoad.setDomain_id((int) object[0]);
            dLoad.setDomainName((String) object[1]);
            return dLoad;
        }).forEachOrdered((dLoad) -> {
            domainName.add(dLoad);
        });
        return domainName;
    }

    public Set<FunctionResponsePayload> getFunctionResponse(long created_by, String createdAt, int domainId) {
        Set<FunctionResponsePayload> functionName = new HashSet<>();
        List<Object[]> getFunction = fedRepo.getFunctionRepo(created_by, createdAt, domainId);
        getFunction.stream().map((object) -> {
            FunctionResponsePayload fLoad = new FunctionResponsePayload();
            fLoad.setFunction_id((int) object[0]);
            fLoad.setFunctionName((String) object[1]);
            return fLoad;
        }).forEachOrdered((fLoad) -> {
            functionName.add(fLoad);
        });
        return functionName;
    }

    public Set<ScenarioResponsePayload> getScenarioResponse(long created_by, String createdAt, int domainId, int functionId) {
        Set<ScenarioResponsePayload> secenarioName = new HashSet<>();
        List<Object[]> getScenario = fedRepo.getScenarioRepo(created_by, createdAt, domainId, functionId);
        getScenario.stream().map((object) -> {
            ScenarioResponsePayload sLoad = new ScenarioResponsePayload();
            sLoad.setScenario_id((int) object[0]);
            sLoad.setScenarioTitle((String) object[1]);
            return sLoad;
        }).forEachOrdered((sLoad) -> {
            secenarioName.add(sLoad);
        });
        return secenarioName;
    }

    public Set<QuestionResponsePayload> getQuestionResponse(long created_by, String createdAt, int domainId, int functionId, int scenarioId) {
        Set<QuestionResponsePayload> questionName = new HashSet<>();
        List<Object[]> getQuestion = fedRepo.getQuestionRepo(created_by, createdAt, domainId, functionId, scenarioId);
        getQuestion.stream().map((object) -> {
            QuestionResponsePayload qLoad = new QuestionResponsePayload();
            qLoad.setQuestionId((int) object[0]);
            qLoad.setQuestion((String) (object[1]));
            return qLoad;
        }).forEachOrdered((qLoad) -> {
            questionName.add(qLoad);
        });
        return questionName;
    }

    public HashMap<String, Result> checkException(long created_by, String createdAt) {
        HashMap<String, Result> map = new HashMap<>();
        List<QuestionQueryInfo> list = quesRepo.getException(created_by, createdAt);
        Iterator<QuestionQueryInfo> itr = list.iterator();
        while (itr.hasNext()) {
            QuestionQueryInfo query = itr.next();
            if (query.getExceptionStr() != null) {
                String temp1 = query.getExceptionStr();
                String temp3 = "";
                if (temp1.indexOf("(") == 0) {
                    int pos1 = temp1.lastIndexOf(")");
                    String temp2 = temp1.substring(pos1 + 1).trim();
                    temp3 = temp2.replaceAll(" ", "_").replaceAll("'", "");
                    if (!map.containsKey(temp3)) {
                        Result res = new Result();
                        res.setCount(1);
                        map.put(temp3, res);
                    } else {
                        Result res = map.get(temp3);
                        int c = res.getCount();
                        res.setCount(++c);
                        map.put(temp3, res);
                    }
                } else {
                    String temp4 = temp1.replaceAll(" ", "_").replaceAll("'", "");
                    if (!map.containsKey(temp4)) {
                        Result res = new Result();
                        res.setCount(1);
                        map.put(temp4, res);
                    } else {
                        Result res = map.get(temp4);
                        int c = res.getCount();
                        res.setCount(++c);
                        map.put(temp4, res);
                    }
                }

            }
        }
        return map;
    }

    //    Mentor FeedbackService
    public String saveFeedback(MentorFeedback feed, UserPrincipal user) {
        int count = 1;
        QueryInfo query = fedRepo.getFeedback(feed.getCreatedAt(), feed.getQuestionId());
        long learnerId = query.getCreatedBy();
        Optional<User> userData = userService.findByLearnerId(learnerId);

        if (!userData.isPresent()) {
            throw new RuntimeException("User with id " + learnerId + " not found");
        }
        User userObj = userData.get();
        FeedBack feedback = new FeedBack();
        feedback.setFeedback(feed.getFeedback());
        feedback.setLearnerId(userObj.getId());
        feedback.setLearnerName(userObj.getName());
        feedback.setMentorId(user.getId());
        feedback.setMentorName(user.getName());
        feedback.setQuestionId(query.getQuestionId());
        feedback.setQuestion(query.getScenario());
        feedback.setResulstr(query.getResultStr());
        feedback.setExceptionStr(query.getExceptionStr());
        feedback.setSqlStr(query.getSqlStr());
        feedback.setQuery_date(query.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());

        System.out.println("------------- Feedback status:-  " + feedback);
        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId);
        System.out.println("---------- GetNotify: " + getNotify);
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }

        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Feedback saved";

    }

    // Show mentor feedback
    public List<MentorFeedbackResponse> showFeedback(UserPrincipal user) {
        List<FeedBack> list = mentorFeedbackRepo.getMentorFeedback(user.getId());
        ArrayList<MentorFeedbackResponse> listObj = new ArrayList<>();
        Iterator<FeedBack> itr = list.iterator();
        while (itr.hasNext()) {
            FeedBack fobj = itr.next();
            MentorFeedbackResponse mentorFeedback = new MentorFeedbackResponse();
            mentorFeedback.setFeedbackDate(fobj.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
            mentorFeedback.setQuestionId(fobj.getQuestionId());
            mentorFeedback.setMentorName(fobj.getMentorName());
            mentorFeedback.setMentorId(fobj.getMentorId());
            mentorFeedback.setLearnerId(fobj.getLearnerId());
            mentorFeedback.setLearnerName(fobj.getLearnerName());
            mentorFeedback.setFeedback(fobj.getFeedback());
            mentorFeedback.setQuestion(fobj.getQuestion());
            if (fobj.getResulstr() == null) {
                mentorFeedback.setResulstr("No results to display");
            } else {
                mentorFeedback.setResulstr(fobj.getResulstr());
            }
            if (fobj.getExceptionStr() == null) {
                mentorFeedback.setExceptionStr("No results to display");
            } else {
                mentorFeedback.setExceptionStr(fobj.getExceptionStr());
            }
            mentorFeedback.setSqlStr(fobj.getSqlStr());
            mentorFeedback.setNotification(fobj.getNotification());
            mentorFeedback.setQuery_date(fobj.getQuery_date());
            listObj.add(mentorFeedback);
        }
        return listObj;
    }

    // Clear notification
    public void clearNotification(UserPrincipal user) {
        List<FeedBack> list = mentorFeedbackRepo.getMentorFeedback(user.getId());
        for (FeedBack f : list) {
            f.setNotification(0);
            mentorFeedbackRepo.save(f);
        }
    }
}
