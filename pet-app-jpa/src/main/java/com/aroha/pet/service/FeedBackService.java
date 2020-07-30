package com.aroha.pet.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.CPojo;
import com.aroha.pet.model.FeedBack;
import com.aroha.pet.model.JavaPojo;
import com.aroha.pet.model.JavascriptPojo;
import com.aroha.pet.model.PythonPojo;
import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.model.ShowMentorFeebackApi;
import com.aroha.pet.model.Technology;
import com.aroha.pet.model.User;
import com.aroha.pet.payload.DeleteDomainPayload;
import com.aroha.pet.payload.DomainResponsePayload;
import com.aroha.pet.payload.FeedBackStatusPayload;
import com.aroha.pet.payload.FunctionResponsePayload;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.payload.MentorFeedback;
import com.aroha.pet.payload.MentorFeedbackResponse;
import com.aroha.pet.payload.QueryObject;
import com.aroha.pet.payload.QuestionResponsePayload;
import com.aroha.pet.payload.Result;
import com.aroha.pet.payload.ScenarioResponsePayload;
import com.aroha.pet.repository.FeedBackRepository;
import com.aroha.pet.repository.MentorFeedbackRepository;
import com.aroha.pet.repository.QuestionQueryInfoRepository;
import com.aroha.pet.security.UserPrincipal;

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

    @Autowired
    private DBService dbservice;

    @Autowired
    private JavaService javaService;

    @Autowired
    private PythonService pythonService;

    @Autowired
    private JavascriptService javaScriptService;

    @Autowired
    private JavaMailSender javaMailSender;

    private final Logger logger = LoggerFactory.getLogger(FeedBackService.class);

    public GetDomainDataPayload getData() {
        List<Object[]> list = fedRepo.getFeedBackStatus();
        List<FeedBackStatusPayload> listObj = new ArrayList<>();
        list.stream().map((obj) -> {
            FeedBackStatusPayload load = new FeedBackStatusPayload();
            System.out.println("---------------Time before-------"+obj[2]);
            Timestamp i = (Timestamp) obj[2];
            BigInteger id = (BigInteger) obj[0];
            load.setCreated_by(id);
            String name = (String) obj[1];
            load.setName(name);

            //                        load.setCreated_at(i.toString());
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(i.toString());
            } catch (Exception ex) {
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            System.out.println("---------Time after---------"+formatter.format(date));
            load.setCreated_at(formatter.format(date));

            BigInteger j = (BigInteger) obj[3];
            load.setNoOfException(j);
            BigInteger k = (BigInteger) obj[4];
            load.setNoOfScenario(k);
            BigInteger l = (BigInteger) obj[5];
            load.setNoOfSqlStr(l);
            BigDecimal m = (BigDecimal) obj[6];
            load.setProductivity(m);
            return load;
        }).forEachOrdered((load) -> {
            listObj.add(load);
        });
        if (listObj.isEmpty()) {
            return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "No data found");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(), listObj, "SUCCESS");
    }

    public GetDomainDataPayload showAnalysis(long created_by, String createdAt, int domainId) {
        System.out.println("-----------Created Before----------"+createdAt);
        Date date2 = null;
        try {
            date2 = new SimpleDateFormat("dd MMMM yyyy").parse(createdAt);
        } catch (Exception ex) {
        }
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd hh:MM:ss");
        createdAt = formatter.format(date2);
        System.out.println("-----------------createdAt After-------------" + createdAt);
        List<Object[]> list = quesRepo.getReport(created_by, createdAt, domainId);
        List<QueryObject> queryList = new ArrayList<>();
        list.stream().map((object) -> {
            QueryObject query = new QueryObject();
            query.setDoaminName((String) object[0]);
            query.setFunctionName((String) object[1]);
            query.setScenario((String) object[2]);
            query.setSqlStr((String) object[3]);
            if ((String) object[4] == null) {
                query.setExceptionStr("None");
            } else {
                query.setExceptionStr((String) object[4]);
            }
            Timestamp j = (Timestamp) object[5];
            query.setCreatedAt(j.toString());
            query.setQuestionId((int) object[6]);
            query.setFeedback((String) object[7]);
            query.setMentorName((String) object[8]);
            if (object[9] != null) {
                Date date = null;
                try {

                    /*
                        Demo
                     */
                    System.out.println("-------------Feedback Time-------"+object[9]);
                    Timestamp timeObj = (Timestamp) object[9];
                    ZonedDateTime indianTimeZone=ZonedDateTime.ofInstant(timeObj.toInstant(),ZoneId.of("Asia/Kolkata"));
                    String qTime = indianTimeZone.toString().replaceAll("T", " ");
//                    String dateObj=(String)object[9];
//                    Instant timestamp = Instant.parse(dateObj);
//                    ZonedDateTime indianTimeZone = timestamp.atZone(ZoneId.of("Asia/Kolkata"));
//                    String qTime = indianTimeZone.toString().replaceAll("T", " ");
                    /*
                      Demo
                     */
                    //System.out.println("------------------Qtime is:-----------"+qTime);
                   // Timestamp obj = (Timestamp) object[9];
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(qTime.toString());
                } catch (ParseException ex) {
                }
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy");
                query.setFeedBackDate(formatter2.format(date));
            }
            query.setAnswer((String) object[10]);
            if ((String) object[11] != null) {
                String data = ((String) object[11]);
                JSONArray jsona = new JSONArray(data);
                query.setResultStr(dbservice.getJsonArrayAsList(jsona));
            }
            return query;
        }).forEachOrdered((query) -> {
            queryList.add(query);
        });
        if (queryList.isEmpty()) {
            return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "No Data found");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(), queryList, "SUCCESS");
    }

    public Set<DomainResponsePayload> getDomainResponse(long created_by, String createdAt) {
        Set<DomainResponsePayload> domainName = new HashSet<>();
        System.out.println("--------createdAt before---" + createdAt);
        Date date = null;
        try {
            date = new SimpleDateFormat("dd MMMM yyyy").parse(createdAt);
        } catch (Exception ex) {
        }
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd hh:MM:ss");
        createdAt = formatter.format(date);
        System.out.println("----------createdAt---" + createdAt);
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
        System.out.println("-----------Created At Before-----------"+feed.getCreatedAt());
        QueryInfo query = fedRepo.getFeedback(feed.getCreatedAt(), feed.getQuestionId(), feed.getCreatedBy());
        System.out.println("-----------Query Date Before ---------" + query.getCreatedAt());
        String inputValue = query.getCreatedAt().toString();
//        Instant timestamp = Instant.parse(inputValue);
//        ZonedDateTime indianTimeZone = timestamp.atZone(ZoneId.of("Asia/Kolkata"));
//        String qTime = indianTimeZone.toString().replaceAll("T", " ");
//        String fTime = qTime.substring(0, qTime.indexOf("+"));
        String fTime=inputValue.replaceAll("T"," ").replaceAll("Z"," ");
        System.out.println("---------------Final time-----------" + fTime);
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
        feedback.setTechnologyId(technology.getTechId());
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
        //        feedback.setQuery_date(query.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
        feedback.setQuery_date(fTime);
        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }

        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload("Error saving feedback", HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("Feedback Saved successfully", HttpStatus.OK.value());

    }

    // Save C Prgram Feedback
    public DeleteDomainPayload saveCPoramFeedback(MentorFeedback feed, UserPrincipal user) {
        int count = 1;
        CPojo cpojo = cService.findByTechnologyRepo(feed.getCreatedAt(), feed.getQuestionId(), feed.getCreatedBy());

        String inputValue = cpojo.getCreatedAt().toString();
        Instant timestamp = Instant.parse(inputValue);
        ZonedDateTime indianTimeZone = timestamp.atZone(ZoneId.of("Asia/Kolkata"));
        String qTime = indianTimeZone.toString().replaceAll("T", " ");
        String fTime = qTime.substring(0, qTime.indexOf("+"));

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
        feedback.setTechnologyId(technology.getTechId());
        feedback.setFeedback(feed.getFeedback());
        feedback.setLearnerId(userObj.getId());
        feedback.setLearnerName(userObj.getName());
        feedback.setMentorId(user.getId());
        feedback.setMentorName(user.getName());
        feedback.setQuestionId(cpojo.getQuestionId());
        feedback.setQuestion(cpojo.getScenario());
        //		feedback.setResulstr(cpojo.getResultstr());
        feedback.setProgrammingResult(cpojo.getResultstr());
        feedback.setError(cpojo.getError());
        feedback.setProgramingStr(cpojo.getCstr());
        //        feedback.setQuery_date(cpojo.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
        feedback.setQuery_date(fTime);
        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }
        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload("Error saving feedback", HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("Feedback Saved successfully", HttpStatus.OK.value());
    }

    public DeleteDomainPayload saveJavaFeedback(MentorFeedback feed, UserPrincipal user) {
        int count = 1;
        JavaPojo javapojo = javaService.findByTechnologyRepo(feed.getCreatedAt(), feed.getQuestionId(), feed.getCreatedBy());

        String inputValue = javapojo.getCreatedAt().toString();
        Instant timestamp = Instant.parse(inputValue);
        ZonedDateTime indianTimeZone = timestamp.atZone(ZoneId.of("Asia/Kolkata"));
        String qTime = indianTimeZone.toString().replaceAll("T", " ");
        String fTime = qTime.substring(0, qTime.indexOf("+"));

        Long learnerId = javapojo.getCreatedBy();
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
        feedback.setTechnologyId(technology.getTechId());
        feedback.setFeedback(feed.getFeedback());
        feedback.setLearnerId(userObj.getId());
        feedback.setLearnerName(userObj.getName());
        feedback.setMentorId(user.getId());
        feedback.setMentorName(user.getName());
        feedback.setQuestionId(javapojo.getQuestionId());
        feedback.setQuestion(javapojo.getScenario());
        //		feedback.setResulstr(javapojo.getResultstr());
        feedback.setProgrammingResult(javapojo.getResultstr());
        feedback.setError(javapojo.getExceptionstr());
        feedback.setProgramingStr(javapojo.getJavastr());
        //        feedback.setQuery_date(javapojo.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());

        feedback.setQuery_date(fTime);
        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }
        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload("Error saving feedback", HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("Feedback Saved successfully", HttpStatus.OK.value());
    }

    public DeleteDomainPayload saveJavaScriptFeedback(MentorFeedback feed, UserPrincipal user) {
        int count = 1;
        JavascriptPojo javaScriptPojo = javaScriptService.findByTechnologyRepo(feed.getCreatedAt(), feed.getQuestionId(), feed.getCreatedBy());

        String inputValue = javaScriptPojo.getCreatedAt().toString();
        Instant timestamp = Instant.parse(inputValue);
        ZonedDateTime indianTimeZone = timestamp.atZone(ZoneId.of("Asia/Kolkata"));
        String qTime = indianTimeZone.toString().replaceAll("T", " ");
        String fTime = qTime.substring(0, qTime.indexOf("+"));

        logger.info("----------------------Javascript Pojo-------" + javaScriptPojo);
        Long learnerId = javaScriptPojo.getCreatedBy();
        logger.info("------------------ Learner Id---------------" + learnerId);
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
        feedback.setTechnologyId(technology.getTechId());
        feedback.setFeedback(feed.getFeedback());
        feedback.setLearnerId(userObj.getId());
        feedback.setLearnerName(userObj.getName());
        feedback.setMentorId(user.getId());
        feedback.setMentorName(user.getName());
        feedback.setQuestionId(javaScriptPojo.getQuestionId());
        feedback.setQuestion(javaScriptPojo.getScenario());
        //		feedback.setResulstr(javaScriptPojo.getResultstr());
        feedback.setProgrammingResult(javaScriptPojo.getResultstr());
        feedback.setError(javaScriptPojo.getError());
        feedback.setProgramingStr(javaScriptPojo.getJavascriptstr());
        //        feedback.setQuery_date(javaScriptPojo.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
        feedback.setQuery_date(fTime);

        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }
        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload("Error saving feedback", HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("Feedback Saved successfully", HttpStatus.OK.value());
    }

    public DeleteDomainPayload savePythonFeedback(MentorFeedback feed, UserPrincipal user) {
        // TODO Auto-generated method stub
        int count = 1;
        PythonPojo pythonPojo = pythonService.findByTechnologyRepo(feed.getCreatedAt(), feed.getQuestionId(), feed.getCreatedBy());

        String inputValue = pythonPojo.getCreatedAt().toString();
        Instant timestamp = Instant.parse(inputValue);
        ZonedDateTime indianTimeZone = timestamp.atZone(ZoneId.of("Asia/Kolkata"));
        String qTime = indianTimeZone.toString().replaceAll("T", " ");
        String fTime = qTime.substring(0, qTime.indexOf("+"));

        Long learnerId = pythonPojo.getCreatedBy();
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
        feedback.setTechnologyId(technology.getTechId());
        feedback.setFeedback(feed.getFeedback());
        feedback.setLearnerId(userObj.getId());
        feedback.setLearnerName(userObj.getName());
        feedback.setMentorId(user.getId());
        feedback.setMentorName(user.getName());
        feedback.setQuestionId(pythonPojo.getQuestionId());
        feedback.setQuestion(pythonPojo.getScenario());
        //		feedback.setResulstr(pythonPojo.getResultstr());
        feedback.setProgrammingResult(pythonPojo.getResultstr());
        feedback.setError(pythonPojo.getError());
        feedback.setProgramingStr(pythonPojo.getPythonstr());
        //feedback.setQuery_date(pythonPojo.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
        feedback.setQuery_date(fTime);

        Integer getNotify = mentorFeedbackRepo.getLastNotification(learnerId, technology.getTechnologyName());
        if (getNotify == null || getNotify == 0) {
            feedback.setNotification(count++);
        } else {
            feedback.setNotification(++getNotify);
        }
        try {
            mentorFeedbackRepo.save(feedback);
        } catch (Exception ex) {
            return new DeleteDomainPayload("Error saving feedback", HttpStatus.BAD_REQUEST.value());
        }
        return new DeleteDomainPayload("Feedback Saved successfully", HttpStatus.OK.value());
    }

    // Show mentor feedback
    public ShowMentorFeebackApi showFeedback(UserPrincipal user, int techId) {
        List<FeedBack> list = mentorFeedbackRepo.getMentorFeedback(user.getId(), techId);
        ArrayList<MentorFeedbackResponse> listObj = new ArrayList<>();
        Iterator<FeedBack> itr = list.iterator();
        while (itr.hasNext()) {
            FeedBack fobj = itr.next();
            MentorFeedbackResponse mentorFeedback = new MentorFeedbackResponse();
            // mentorFeedback.setFeedbackDate(fobj.getCreatedAt().toString().replaceAll("T", " ").replaceAll("Z", " ").trim());
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(fobj.getCreatedAt().toString());
            } catch (ParseException ex) {
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

            if (fobj.getResulstr() != null) {
                JSONArray jsona = new JSONArray(fobj.getResulstr());
                mentorFeedback.setResultStr(dbservice.getJsonArrayAsList(jsona));
            }

            if (fobj.getProgrammingResult() != null) {
                mentorFeedback.setProgramingResult(fobj.getProgrammingResult());
            }

            if (fobj.getExceptionStr() == null) {
                mentorFeedback.setExceptionStr("None");
            } else {
                mentorFeedback.setExceptionStr(fobj.getExceptionStr());
            }
            if (fobj.getSqlStr() == null) {
                mentorFeedback.setSqlStr("None");
            } else {
                mentorFeedback.setSqlStr(fobj.getSqlStr());
            }
            if (fobj.getProgramingStr() == null) {
                mentorFeedback.setProgramingStr("None");
            } else {
                mentorFeedback.setProgramingStr(fobj.getProgramingStr());
            }
            mentorFeedback.setTechnologyName(fobj.getTechnologyName());
            if (fobj.getError() == null) {
                mentorFeedback.setError("None");
            } else {
                mentorFeedback.setError(fobj.getError());
            }
            mentorFeedback.setNotification(fobj.getNotification());
            //            mentorFeedback.setQuery_date(fobj.getQuery_date());
            Date date2 = null;
            try {
                date2 = new SimpleDateFormat("yyyy-MM-dd").parse(fobj.getQuery_date().toString());
            } catch (ParseException ex) {
            }
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy");
            mentorFeedback.setQuery_date(formatter2.format(date2));
            listObj.add(mentorFeedback);

        }
        if (listObj.isEmpty()) {
            return new ShowMentorFeebackApi(HttpStatus.NO_CONTENT.value(), "Sorry!! No Mentor Feedback Found");
        } else {
            return new ShowMentorFeebackApi(HttpStatus.OK.value(), listObj, "SUCCESS");
        }

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

    public DeleteDomainPayload showReportSQLInEmail(long created_by, String createdAt, int domainId) {
        Optional<User> userObj = userService.findByLearnerId(created_by);
        if (!userObj.isPresent()) {
            return new DeleteDomainPayload("User not found", HttpStatus.BAD_REQUEST.value());
        }
        User user = userObj.get();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(createdAt.toString());
        } catch (ParseException ex) {
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

        //    	Send report from email\
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Report " + formatter.format(date));
            List<Object[]> list = quesRepo.getReport(created_by, createdAt, domainId);
            StringBuffer sb = new StringBuffer("<html><body>");
            for (Object[] object : list) {
                sb.append("<h3 style=\"color:green\">" + "Domain Name: " + "</h3>" + (String) object[0]);
                sb.append("<h3 style=\"color:green\">" + "Function Name: " + "</h3>" + (String) object[1]);
                sb.append("<h3 style=\"color:green\">" + "Question : " + "</h3>" + (String) object[2]);
                sb.append("<h3 style=\"color:green\">" + "Your SQL query: " + "</h3>" + (String) object[3]);
                if ((String) object[4] != null) {
                    sb.append("<h3 style=\"color:red\">" + "Exception: " + "</h3>" + (String) object[4]);
                }
                if ((String) object[7] != null) {
                    sb.append("<h3 style=\"color:green\">" + "Feedback: " + "</h3>" + (String) object[7]);
                }
                if (object[9] != null) {
                    Date date1 = null;
                    try {
                        java.sql.Timestamp obj = (java.sql.Timestamp) object[9];
                        date1 = new SimpleDateFormat("yyyy-MM-dd").parse(obj.toString());
                    } catch (ParseException ex) {
                    }
                    SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM yyyy");
                    sb.append("<h3 style=\"color:green\">" + "Feedback Date: " + "</h3>" + formatter2.format(date1));
                }
                if ((String) object[8] != null) {
                    sb.append("<h3 style=\"color:green\">" + "Mentor Name: " + "</h3>" + (String) object[8]);
                }
                if ((String) object[11] != null) {
                    sb.append("<h3 style=\"color:blue\">" + "Result: " + "</h3>");
                    String data = ((String) object[11]);
                    JSONArray jsona = new JSONArray(data);
                    for (int i = 0; i < jsona.length(); i++) {
                        org.json.JSONObject jsonObj = jsona.getJSONObject(i);
                        Set<String> set = jsonObj.keySet();
                        for (String s : set) {
                            sb.append("<table style=\"width:100%\";\"border:1px solid black\">" + "<tr>" + "<th style=\"color:grey\";\"border:1px solid black\">" + s + "</th>" + "</tr>"
                                    + "<tr>" + "<td style=\"border:1px solid black\">" + jsonObj.get(s) + "</td>" + "</tr>" + "</table>");
                        }
                    }
                }

                sb.append("<hr style=\"color:blue\">");
            }
            sb.append("</body></html>");
            helper.setText("Plain message", sb.toString());
            javaMailSender.send(msg);
            return new DeleteDomainPayload("Mail sent successfully", HttpStatus.OK.value());

        } catch (MessagingException | MailException ex) {
            logger.error("Failed to send mail " + ex.getMessage());
            return new DeleteDomainPayload("Mail failed to sent", HttpStatus.BAD_REQUEST.value());
        }

    }

}
