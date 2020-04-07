package com.aroha.pet.service;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.JavaPojo;
import com.aroha.pet.payload.DomainResponsePayload;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.payload.JavaPayload;
import com.aroha.pet.payload.JavaReport;
import com.aroha.pet.payload.JavaReportAnalysisPayload;
import com.aroha.pet.payload.JavaResponse;
import com.aroha.pet.repository.JavaRepo;
import com.aroha.pet.security.UserPrincipal;
import java.io.BufferedReader;

@Service
public class JavaService {

    @Autowired
    JavaRepo javaRepo;
    StringBuffer sb = null;
    StringBuffer sb1 = null;
//
//	@Autowired
//	ServletContext context;

    private static final Logger logger = LoggerFactory.getLogger(JavaService.class);

    public JavaResponse executeJava(final UserPrincipal currentUser, JavaPayload payload) throws Exception {
        Path currentPath = Paths.get("");
        String projectPath = currentPath.toAbsolutePath().toString();

        //String absolutePathToIndexJSP=context.getRealPath("/");
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        String currTimeAndDate = df.format(dateobj);
        //String my_path= System.getenv("JAVA_PROGRAMS_PET");	
        //logger.info("JAVA_PROGRAMS_PET:  "+absolutePathToIndexJSP);
        int n = 10;
        String random = getAlphaNumericString(n);

        int qId = payload.getQuestionId();
        sb1 = new StringBuffer();
        String text = payload.getJavapojo().getJavastr();

        sb = new StringBuffer(text);
        int endIdx = sb.indexOf("{");
        int startIdx = sb.indexOf("public");
        //sb.replace(startIdx,endIdx, "class "+random);
        String uName = currentUser.getName();
        uName = uName.replaceAll("\\s", "");
        sb.replace(startIdx, endIdx, "class " + random + "_" + uName);
        String name = "/" + random + "_" + uName + ".java";
        String name1 = "/  " + random + "_" + uName;

        System.out.println("Name is: " + name);
        System.out.println("Name1 is: " + name1);
        //String dirName = "d:/Java";
        //String dirName = my_path;
        //String dirName=absolutePathToIndexJSP;
        String dirName = projectPath + "\\" + "JavaPrograms";
        File newFile = new File(projectPath + "\\" + "JavaPrograms");
        newFile.mkdir();

        BufferedWriter writer = new BufferedWriter(new FileWriter(dirName + name));
        writer.write(sb.toString());
        writer.close();
        writer = null;

        String compilationCommand = "javac " + dirName + name;
        String executableCommand = "java -cp " + dirName + name1;

        System.out.println("File name is: " + dirName + name1);
        JavaResponse javaresponse = new JavaResponse();
        JavaPojo javapojo = new JavaPojo();
        runProcess(compilationCommand);
        runProcess(executableCommand);
        //JSONArray jsona = null;
        javapojo.setJavastr(text);
        //javaresponse.setJava(text);
        //jsona=getResultForJava(sb1);
        //System.out.println("Jsona is: "+jsona);
        System.out.println("Sb1 is: " + sb1);
        if (sb1.toString().contains("Exception:")) {
            //javaresponse.setJavaresult(getJsonArrayAsList(jsona));
            String exception = sb1.toString().substring(sb1.toString().indexOf("Exception"));
            System.out.println("Error is: " + exception);
            int r = exception.toString().indexOf("\n");
            int m = exception.toString().indexOf("Exception");
            System.out.println("M is: " + m);
            System.out.println("R is: " + r);
            //cpojo.setResultstr(sb.toString().substring(sb.toString().indexOf("error")));
            //javapojo.setResultstr(exception);
            //javaresponse.setJavaexception(exception);

            javapojo.setResultstr(exception.toString().substring(m, r));
            javapojo.setExceptionstr(exception.toString().substring(m, r));
            javaresponse.setJavaexception(exception.toString().substring(m, r));
            javaresponse.setJavastatus("EXCEPTION");
            javapojo.setQuestionId(qId);
            javapojo.setScenario(payload.getJavapojo().getScenario());
            //javapojo.setCreatedAt(currTimeAndDate);
            javapojo.setCreatedBy(currentUser.getId());
            javaRepo.save(javapojo);
            return javaresponse;
        } else if (sb1.toString().contains("error:") || sb1.toString().contains("Error:")) {
            javaresponse.setJavastatus("ERROR");
            String error = sb1.toString().substring(sb1.toString().indexOf("error"));
            System.out.println("Error is: " + error);
            int r = error.toString().indexOf("\n");
            int m = error.toString().indexOf("error");
            //cpojo.setResultstr(sb.toString().substring(sb.toString().indexOf("error")));
            javapojo.setResultstr(error.toString().substring(m, r));
            javapojo.setExceptionstr(error.toString().substring(m, r));
            javaresponse.setJavaexception(error.toString().substring(m, r));

            javapojo.setQuestionId(qId);
            javapojo.setScenario(payload.getJavapojo().getScenario());
            //javapojo.setCreatedAt(currTimeAndDate);
            javapojo.setCreatedBy(currentUser.getId());
            javaRepo.save(javapojo);
            return javaresponse;
        } else {
            //javaresponse.setJavaresult(getJsonArrayAsList(jsona));
            javaresponse.setJavaresult(sb1.toString());
            javapojo.setResultstr(sb1.toString());
            javaresponse.setJavastatus("SUCCESS");
            javapojo.setQuestionId(qId);
            javapojo.setScenario(payload.getJavapojo().getScenario());
            //javapojo.setCreatedAt(currTimeAndDate);
            javapojo.setCreatedBy(currentUser.getId());
            javaRepo.save(javapojo);
            return javaresponse;
        }
    }

    private StringBuffer printLines(String cmd, InputStream ins) throws Exception {
        int ctr = 0;
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            sb1.append(line);
            sb1.append("\n");
            ctr++;
        }
        in.close();
        in = null;
        return sb1;
    }

    private StringBuffer runProcess(String command) throws Exception {
        StringBuffer sb2 = null;
        StringBuffer sb3 = null;
        try {
            Process pro = Runtime.getRuntime().exec(command);
            InputStream temp = pro.getInputStream();
            //sb=new StringBuffer();
            sb2 = printLines(command + " stdout:", temp);
            temp.close();
            temp = pro.getErrorStream();
            //System.out.println("I am here: "+sb);
            sb3 = printLines(command + " stderr:", temp);
            temp.close();
            temp = null;
            pro.waitFor();
            //System.out.println(command + " exitValue() " + pro.exitValue());
            pro.destroy();
            return sb2;
        } catch (Exception ex) {
            return sb3;
        }
    }

    private JSONArray getResultForJava(StringBuffer sb1) throws Exception {
        JSONArray json = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("output", sb1);
        json.put(obj);
        return json;
    }

    private List getJsonArrayAsList(JSONArray jsona) {
        return jsona.toList();
    }

    public static String getAlphaNumericString(int n) {

        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString 
        StringBuffer sb1 = new StringBuffer(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb 
            sb1.append(AlphaNumericString.charAt(index));
        }
        return sb1.toString();
    }

    public GetDomainDataPayload getReportCard() {
        List<Object[]> listObj = javaRepo.generateReport();
        List<JavaReport> list = new ArrayList<>();
        listObj.stream().map((obj) -> {
            JavaReport report = new JavaReport();
            report.setUserId((java.math.BigInteger) obj[0]);
            report.setName((String) obj[1]);
            java.sql.Timestamp i = (java.sql.Timestamp) obj[2];
            report.setCreated_at(i.toString());
            report.setNoOfError((java.math.BigInteger) obj[3]);
            report.setNoOfQuestion((java.math.BigInteger) obj[4]);
            report.setNoOfAttempt((java.math.BigInteger) obj[5]);
            report.setProductivity((java.math.BigDecimal) obj[6]);
            return report;
        }).forEachOrdered((report) -> {
            list.add(report);
        });
        if (list.isEmpty()) {
            return new GetDomainDataPayload(HttpStatus.NO_CONTENT.value(), "No Data Found");
        }
        return new GetDomainDataPayload(HttpStatus.OK.value(), list, "SUCCESS");
    }

    public List<JavaReportAnalysisPayload> generateReportAnalysis(String createdAt, Long createdBy, int domainId) {
        List<Object[]> listObj = javaRepo.generateReportAnalysis(createdAt, createdBy, domainId);
        List<JavaReportAnalysisPayload> list = new ArrayList<>();
        listObj.stream().map((object) -> {
            JavaReportAnalysisPayload load = new JavaReportAnalysisPayload();
            load.setDomainName((String) object[0]);
            load.setFunctionName((String) object[1]);
            load.setScenarioTitle((String) object[2]);
            load.setJavaStr((String) object[3]);
            load.setError((String) object[4]);
            load.setQuestionId((int) object[5]);
            load.setResultStr((String) object[6]);
            load.setScenario((String) object[7]);
            load.setFeedback((String) object[8]);
            load.setMentorName((String) object[9]);
            java.sql.Timestamp i = (java.sql.Timestamp) object[10];
            Date date = null;
            if (i != null) {
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(i.toString());
                } catch (Exception ex) {
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                load.setFeedbackDate(formatter.format(date));
            }
            return load;
        }).forEachOrdered((load) -> {
            list.add(load);
        });
        return list;
    }

    public JavaPojo findByTechnologyRepo(String createdAt, int questionId,Long createdBy) {
        return javaRepo.searchJavaRepo(createdAt, questionId,createdBy);
    }

    public Set<DomainResponsePayload> getDomainResponse(long created_by, String createdAt) {
        Set<DomainResponsePayload> domainName = new HashSet<>();
        List<Object[]> getDomain = javaRepo.getDomainAnalsisRepo(created_by, createdAt);
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
    

}
