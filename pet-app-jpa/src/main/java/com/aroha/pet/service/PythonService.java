package com.aroha.pet.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aroha.pet.model.CPojo;
import com.aroha.pet.model.JavascriptPojo;
import com.aroha.pet.model.PythonPojo;
import com.aroha.pet.payload.CReport;
import com.aroha.pet.payload.CReportAnalysisPayload;
import com.aroha.pet.payload.DomainResponsePayload;
import com.aroha.pet.payload.GetDomainDataPayload;
import com.aroha.pet.payload.JavascriptResponse;
import com.aroha.pet.payload.PythonPayload;
import com.aroha.pet.payload.PythonReport;
import com.aroha.pet.payload.PythonReportAnalysisPayload;
import com.aroha.pet.payload.PythonResponse;
import com.aroha.pet.repository.PythonRepo;
import com.aroha.pet.security.UserPrincipal;

@Service
public class PythonService {

	@Autowired
	PythonRepo pythonRepo;

	StringBuffer sbuffer=null;
	StringBuffer sb=null;
	int ctr=0;

	public PythonResponse executePython(PythonPayload pythonPayload, UserPrincipal currentUser) throws Exception {
		// TODO Auto-generated method stub
		Path currentPath=Paths.get("");
		String projectPath=currentPath.toAbsolutePath().toString();
		String dirName=projectPath+"\\"+"PythonPrograms";
		String fileString=generateRandomWord(8);
		File newFile=new File(dirName);
		newFile.mkdir();
		String uName = currentUser.getName();
		uName = uName.replaceAll("\\s", "");
		String fileName=fileString+"_"+uName+".py";
		String pythonCode=pythonPayload.getPythonpojo().getPythonstr();
		sbuffer=new StringBuffer(pythonCode);
		sb=new StringBuffer();
		BufferedWriter writer=new BufferedWriter(new FileWriter(dirName+"/"+fileName));
		writer.write(sbuffer.toString());
		writer.close();
		writer=null;

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String currTimeAndDate=df.format(dateobj);

		String executableCommand="python "+dirName+"/"+fileName;
		sb=runProcess(executableCommand);
		System.out.println("I am here: "+sb);

		PythonResponse pythonresponse=new PythonResponse();
		PythonPojo pythonpojo=new PythonPojo();
		JSONArray jsona = null;

		if(sb.toString().contains("AssertionError") || sb.toString().contains("AttributeError") || 
				sb.toString().contains("EOFError") || sb.toString().contains("FloatingPointError") || 
				sb.toString().contains("GeneratorExit") || sb.toString().contains("ImportError")||
				sb.toString().contains("IndexError") || sb.toString().contains("KeyError") || 
				sb.toString().contains("KeyboardInterrupt") || sb.toString().contains("MemoryError") || 
				sb.toString().contains("NameError") || sb.toString().contains("NotImplementedError") ||
				sb.toString().contains("OSError") || sb.toString().contains("OverflowError") || 
				sb.toString().contains("ReferenceError") || sb.toString().contains("RuntimeError") || 
				sb.toString().contains("StopIteration") || sb.toString().contains("SyntaxError") ||
				sb.toString().contains("IndentationError") || sb.toString().contains("TabError") || 
				sb.toString().contains("SystemError") || sb.toString().contains("SystemExit") || 
				sb.toString().contains("TypeError") || sb.toString().contains("UnboundLocalError") ||
				sb.toString().contains("UnicodeError") || sb.toString().contains("UnicodeEncodeError") || 
				sb.toString().contains("UnicodeDecodeError") || sb.toString().contains("UnicodeTranslateError") || 
				sb.toString().contains("ValueError") || sb.toString().contains("ZeroDivisionError")) {
			//jsona=getResultForJava(sb);
			System.out.println("Sb is: "+sb);
			//pythonpojo.setCreatedAt(currTimeAndDate);
			pythonpojo.setCreatedBy(currentUser.getId());
			pythonpojo.setPythonstr(pythonCode);
			pythonpojo.setQuestionId(pythonPayload.getQuestionId());
			pythonpojo.setScenario(pythonPayload.getPythonpojo().getScenario());
			String error=sb.toString().substring(sb.toString().indexOf(".py"));
			System.out.println("Error is: "+error);
			//int r=error.toString().indexOf("Error:");
			//int m=error.toString().indexOf("<module>");
			pythonpojo.setResultstr(error);		
			pythonpojo.setError(error);
			//pythonresponse.setPythonprogram(pythonCode);
			pythonresponse.setPythonstatus("ERROR");
			pythonresponse.setPythonerror(error);
			pythonRepo.save(pythonpojo);
			return pythonresponse;
		}
		else {
			//jsona=getResultForJava(sb);
			//pythonpojo.setCreatedAt(currTimeAndDate);
			pythonpojo.setCreatedBy(currentUser.getId());
			pythonpojo.setPythonstr(pythonCode);
			pythonpojo.setQuestionId(pythonPayload.getQuestionId());
			pythonpojo.setScenario(pythonPayload.getPythonpojo().getScenario());
			pythonpojo.setResultstr(sb.toString());	
			//pythonresponse.setPythonprogram(pythonCode);
			pythonresponse.setPythonstatus("SUCCESS");
			pythonresponse.setPythonresult(sb.toString());
			System.out.println("I am here: "+sb.toString());
			pythonRepo.save(pythonpojo);
			return pythonresponse;
		}
	}
	private String generateRandomWord(int length) {
		Random r = new Random(); // Intialize a Random Number Generator with SysTime as the seed
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++) { // For each letter in the word
			char tmp = (char) ('a' + r.nextInt('z' - 'a')); // Generate a letter between a and z
			sb.append(tmp); // Add it to the String
		}
		return sb.toString();
	}

	private StringBuffer printLines(String cmd, InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(
				new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
			ctr++;
		}
		return sb;
	}

	private StringBuffer runProcess(String command) throws Exception {
		StringBuffer sb2=new StringBuffer();
		Process pro = Runtime.getRuntime().exec(command);
		InputStream temp=pro.getInputStream();
		//sb=new StringBuffer();
		sb2=printLines(command + " stdout:", temp);
		temp.close();
		temp=pro.getErrorStream();
		//System.out.println("I am here: "+sb);
		printLines(command + " stderr:",temp);
		temp.close();
		temp=null;
		pro.waitFor();
		System.out.println(command + " exitValue() " + pro.exitValue());
		pro.destroy();
		return sb2;
	}

	private JSONArray getResultForJava(StringBuffer sb) throws Exception {
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("output", sb);
		json.put(obj);
		return json;
	}

	private List getJsonArrayAsList(JSONArray jsona) {
		return jsona.toList();
	}

	public GetDomainDataPayload getReportCard() {
		List<Object[]> listObj = pythonRepo.generateReport();
		List<PythonReport> list = new ArrayList<>();
		listObj.stream().map((obj) -> {
			PythonReport report = new PythonReport();
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

	public List<PythonReportAnalysisPayload> generateReportAnalysis(String createdAt, Long createdBy, int domainId) {
		List<Object[]> listObj = pythonRepo.generateReportAnalysis(createdAt, createdBy, domainId);
		//logger.info("------------- I am here----------------" + listObj.size());
		List<PythonReportAnalysisPayload> list = new ArrayList<>();
		listObj.stream().map((object) -> {
			PythonReportAnalysisPayload load = new PythonReportAnalysisPayload();
			load.setDomainName((String) object[0]);
			load.setFunctionName((String) object[1]);
			load.setScenarioTitle((String) object[2]);
			load.setPythonStr((String) object[3]);
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
				} catch (ParseException ex) {
				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
				load.setFeedbackDate(formatter.format(date));
			}
			java.sql.Timestamp j = (java.sql.Timestamp) object[11];
			load.setCreatedAt(j.toString());
			return load;
		}).forEachOrdered((load) -> {
			list.add(load);
		});
		return list;
	}

	public PythonPojo findByTechnologyRepo(String createdAt, int questionId,Long createdBy) {
		return pythonRepo.searchPythonRepo(createdAt, questionId,createdBy);
	}

	public Set<DomainResponsePayload> getDomainResponse(long created_by, String createdAt) {
		Set<DomainResponsePayload> domainName = new HashSet<>();
		List<Object[]> getDomain = pythonRepo.getDomainAnalsisRepo(created_by, createdAt);
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
