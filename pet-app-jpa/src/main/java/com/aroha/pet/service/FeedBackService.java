package com.aroha.pet.service;

import com.aroha.pet.model.CPojo;
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
import com.aroha.pet.model.Technology;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainResponsePayload;
import com.aroha.pet.payload.FeedBackStatusPayload;
import com.aroha.pet.payload.FunctionResponsePayload;
import com.aroha.pet.payload.MentorFeedback;
import com.aroha.pet.payload.MentorFeedbackResponse;
import com.aroha.pet.payload.Message;
import com.aroha.pet.payload.QueryObject;
import com.aroha.pet.payload.QuestionResponsePayload;
import com.aroha.pet.payload.Result;
import com.aroha.pet.payload.ScenarioResponsePayload;
import com.aroha.pet.repository.FeedBackRepository;
import com.aroha.pet.repository.MentorFeedbackRepository;
import com.aroha.pet.repository.QuestionQueryInfoRepository;
import com.aroha.pet.security.UserPrincipal;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private TechnologyService techService;

    @Autowired
    private CService cService;

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

    public Message showAnalysis(long created_by, String createdAt, int domainId) {
        List<Object[]> list = quesRepo.getReport(created_by, createdAt, domainId);
        Message message = new Message();
        List<QueryObject> queryList = new ArrayList<>();
        for (Object[] object : list) {
            QueryObject queryObj = new QueryObject();
            queryObj.setScenario((String) object[0]);
            queryObj.setSqlStr((String) object[1]);

            if ((String) object[2] == null) {

                queryObj.setExceptionStr("No results to display");

            } else {

                queryObj.setExceptionStr((String) object[2]);
            }
            if ((String) object[9] != null) {
                JSONArray jsona = new JSONArray((String) object[9]);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Data", jsona);

                queryObj.setResultStr(jsonObject.toString());

            } else {
                JSONArray jsona = new JSONArray("[{Data:No result to display}]");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Data", jsona);

                queryObj.setResultStr(jsonObject.toString());
            }

            queryObj.setQuestionId((int) object[4]);
            java.sql.Timestamp j = (java.sql.Timestamp) object[3];
            String created = j.toString().replaceAll("T", " ").replaceAll("Z", " ").trim();
            queryObj.setCreatedAt(created);
            queryObj.setMentorName((String) object[6]);
            queryObj.setFeedback((String) object[5]);
            java.sql.Timestamp k = (java.sql.Timestamp) object[7];
            if (k == null) {
                queryObj.setFeedBackDate(null);
            } else {
                queryObj.setFeedBackDate(k.toString());
            }
            queryList.add(queryObj);
            message.setQueryResponse(queryList);
            message.setStatus(HttpStatus.OK.value());
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
        List<QueryObject> list = quesRepo.getException(created_by, createdAt);
        Iterator<QueryObject> itr = list.iterator();
        while (itr.hasNext()) {
            QueryObject query = itr.next();
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
    public DeleteDomainPayload saveFeedback(MentorFeedback feed, UserPrincipal user) {
        int count = 1;
        QueryInfo query = fedRepo.getFeedback(feed.getCreatedAt(), feed.getQuestionId());
        long learnerId = query.getCreatedBy();
        Optional<User> userData = userService.findByLearnerId(learnerId);
        Optional<Technology> tech = techService.findById(feed.getTechnologyId());
        if (!userData.isPresent()) {
            throw new RuntimeException("User with id " + learnerId + " not found");
        }
        if (!tech.isPresent()) {
            throw new RuntimeException("Technology with Id " + feed.getTechnologyId() + " not found");
        }
        User userObj = userData.get();
        Technology technology = tech.get();
        FeedBack feedback = new FeedBack();
        feedback.setTechnologyName(technology.getTechnologyName());
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
        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }

        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("FeedBack Saved successfully", HttpStatus.OK.value());

    }

    // Save C Prgram Feedback
    public DeleteDomainPayload saveCPoramFeedback(MentorFeedback feed, UserPrincipal user) {
        int count = 1;
        CPojo cpojo = cService.findByTechnologyRepo(feed.getCreatedAt(), feed.getQuestionId());
        Long learnerId = cpojo.getCreatedBy();
        Optional<User> userData = userService.findByLearnerId(learnerId);
        Optional<Technology> tech = techService.findById(feed.getTechnologyId());
        if (!userData.isPresent()) {
            throw new RuntimeException("User with id " + learnerId + " not found");
        }
        if (!tech.isPresent()) {
            throw new RuntimeException("Technology with Id " + feed.getTechnologyId() + " not found");
        }
        User userObj = userData.get();
        Technology technology = tech.get();
        FeedBack feedback = new FeedBack();
        feedback.setTechnologyName(technology.getTechnologyName());
        feedback.setFeedback(feed.getFeedback());
        feedback.setLearnerId(userObj.getId());
        feedback.setLearnerName(userObj.getName());
        feedback.setMentorId(user.getId());
        feedback.setMentorName(user.getName());
        feedback.setQuestionId(cpojo.getQuestionId());
        feedback.setQuestion(cpojo.getScenario());
        feedback.setResulstr(cpojo.getResultstr());
        feedback.setError(cpojo.getError());
        feedback.setcStr(cpojo.getCstr());
        feedback.setQuery_date(cpojo.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }
        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("FeedBack Saved successfully", HttpStatus.OK.value());
    }

    // Show mentor feedback
    public List<MentorFeedbackResponse> showFeedback(UserPrincipal user) {
        List<FeedBack> list = mentorFeedbackRepo.getMentorFeedback(user.getId());
        ArrayList<MentorFeedbackResponse> listObj = new ArrayList<>();
        Iterator<FeedBack> itr = list.iterator();
        while (itr.hasNext()) {
            FeedBack fobj = itr.next();
            MentorFeedbackResponse mentorFeedback = new MentorFeedbackResponse();
            // mentorFeedback.setFeedbackDate(fobj.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(fobj.getCreatedAt().toString());
            } catch (Exception ex) {
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            mentorFeedback.setFeedbackDate(formatter.format(date));
            mentorFeedback.setQuestionId(fobj.getQuestionId());
            mentorFeedback.setMentorName(fobj.getMentorName());
            mentorFeedback.setMentorId(fobj.getMentorId());
            mentorFeedback.setLearnerName(fobj.getLearnerName());
            mentorFeedback.setLearnerId(fobj.getLearnerId());
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
            if (fobj.getSqlStr() == null) {
                mentorFeedback.setSqlStr("No results to display");
            } else {
                mentorFeedback.setSqlStr(fobj.getSqlStr());
            }
            if (fobj.getcStr() == null) {
                mentorFeedback.setcStr("No results to display");
            } else {
                mentorFeedback.setcStr(fobj.getcStr());
            }
            mentorFeedback.setTechnologyName(fobj.getTechnologyName());
            if (fobj.getError() == null) {
                mentorFeedback.setError("No results to display");
            } else {
                mentorFeedback.setError(fobj.getError());
            }
            mentorFeedback.setNotification(fobj.getNotification());
//            mentorFeedback.setQuery_date(fobj.getQuery_date());
            Date date2 = null;
            try {
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(fobj.getQuery_date().toString());
            } catch (Exception ex) {
            }
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy");
            mentorFeedback.setQuery_date(formatter2.format(date2));
            listObj.add(mentorFeedback);
        }
        return listObj;
    }

    // Clear notification
    public void clearNotification(UserPrincipal user) {
        List<FeedBack> list = mentorFeedbackRepo.getMentorFeedback(user.getId());
        list.stream().map((f) -> {
            f.setNotification(0);
            return f;
        }).forEachOrdered((f) -> {
            mentorFeedbackRepo.save(f);
        });
    }
}
