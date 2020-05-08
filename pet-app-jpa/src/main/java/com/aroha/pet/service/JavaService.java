package com.aroha.pet.service;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(JavaService.class);

	public JavaResponse executeJava(final UserPrincipal currentUser, JavaPayload payload) throws Exception {
		Path currentPath = Paths.get("");
		String projectPath = currentPath.toAbsolutePath().toString();
		int n = 10;
		String random = getAlphaNumericString(n);
		JavaResponse javaresponse = new JavaResponse();
		JavaPojo javapojo = new JavaPojo();	
		sb1 = new StringBuffer();
		String text = payload.getJavapojo().getJavastr();
		sb = new StringBuffer(text);

		int endIdx = sb.indexOf("{");
		int startIdx = sb.indexOf("public");
		String uName = currentUser.getName();
		uName = uName.replaceAll("\\s", "");
		javapojo.setJavastr(text);
		if(endIdx<=0 || startIdx<0) {
			javaresponse.setJavaexception("Java Syntax Error");
			javaresponse.setJavastatus("Error");
			javapojo.setCreatedBy(currentUser.getId());
			javapojo.setExceptionstr("Java Syntax Error");
			javapojo.setQuestionId(payload.getQuestionId());
			javapojo.setScenario(payload.getJavapojo().getScenario());
			javaRepo.save(javapojo);
			return javaresponse;
		}
		String reg=sb.substring(0, endIdx);
		if(!(reg.contains("class") || reg.contains("import"))) {
			javaresponse.setJavaexception("Java Syntax Error");
			javaresponse.setJavastatus("Error");
			javapojo.setCreatedBy(currentUser.getId());
			javapojo.setExceptionstr("Java Syntax Error");
			javapojo.setQuestionId(payload.getQuestionId());
			javapojo.setScenario(payload.getJavapojo().getScenario());
			javaRepo.save(javapojo);
			return javaresponse;
		}

		if(startIdx>endIdx) {
			String allImports="import java.util.*;\nimport java.io.*;\nimport java.text.*;\nimport java.util.regex.*;\nimport java.sql.*;\n";
			sb.replace(0,endIdx,allImports+"public class "+random+"_"+uName);
		}else {
			sb.replace(startIdx,endIdx,"public class "+random+"_"+uName);
		}

		String name = "/" + random + "_" + uName + ".java";
		String name1 = "/  " + random + "_" + uName;

		String dirName = projectPath + "\\" + "JavaPrograms";
		File newFile = new File(projectPath + "\\" + "JavaPrograms");
		newFile.mkdir();
		BufferedWriter writer = new BufferedWriter(new FileWriter(dirName + name));
		writer.write(sb.toString());
		writer.close();
		writer = null;

		String compilationCommand = "javac " + dirName + name;
		String executableCommand = "java -cp " + dirName + name1;

		StringBuffer compile=new StringBuffer();

		StringBuffer executeProgram=new StringBuffer();
		boolean error=false;

		compile=runProcess(compilationCommand);

		if((compile.toString().contains("Exception:"))||(compile.toString().contains("error:"))) {
			error=true;
			int index=compile.toString().indexOf("error:");
			String compileError=compile.substring(index);
			String errorCapture=checkErrorLine(compileError);
			javaresponse.setJavaexception(errorCapture);
			javaresponse.setJavastatus("ERROR");
			javapojo.setCreatedBy(currentUser.getId());
			javapojo.setExceptionstr(compileError);
			javapojo.setQuestionId(payload.getQuestionId());
			javapojo.setScenario(payload.getJavapojo().getScenario());
			javaRepo.save(javapojo);

		}
		if(!error) {
			executeProgram=runProcess(executableCommand);
			if(executeProgram.toString().contains("Exception")) {
				int endIndex=executeProgram.indexOf("\n");
				String compileError=executeProgram.substring(0,endIndex);
				javaresponse.setJavaexception(compileError);
				javaresponse.setJavastatus("EEXCEPTION");
				javapojo.setCreatedBy(currentUser.getId());
				javapojo.setExceptionstr(compileError);
				javapojo.setQuestionId(payload.getQuestionId());
				javapojo.setScenario(payload.getJavapojo().getScenario());
				javaRepo.save(javapojo);
			}else {
				javaresponse.setJavastatus("SUCCESS");
				javaresponse.setJavaresult(executeProgram.toString());
				javapojo.setCreatedBy(currentUser.getId());
				javapojo.setResultstr(executeProgram.toString());
				javapojo.setQuestionId(payload.getQuestionId());
				javapojo.setScenario(payload.getJavapojo().getScenario());
				javaRepo.save(javapojo);
			}
		}

		return javaresponse;

	}


	public String checkErrorLine(String error) {
		String [] splitLines=error.split("\n");
		String res="";
		for(String s:splitLines) {
			if(s.contains("error:") ) {
				if(s.contains("java")) {
					int index=s.indexOf("error:");
					res+=s.substring(index);
					res+="\n";
				}else {
					res+=s;
					res+="\n";
				}
			}else {
				res+=s;
				res+="\n";
			}
		}
		return res;
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

			//            report.setCreated_at(i.toString());
			Date date=null;
			try {
				date=new SimpleDateFormat("yyyy-MM-dd").parse(i.toString());
			}catch(Exception ex) {}
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
			report.setCreated_at(formatter.format(date));


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
		Date date2=null;
		try {
			date2=new SimpleDateFormat("dd MMMM yyyy").parse(createdAt);
		}catch(Exception ex) {}
		SimpleDateFormat formatter2 = new SimpleDateFormat("YYYY-MM-dd hh:MM:ss");
		createdAt=formatter2.format(date2);    	
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
			java.sql.Timestamp j = (java.sql.Timestamp) object[11];
			load.setCreatedAt(j.toString());
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
		Date date=null;
		try {
			date=new SimpleDateFormat("dd MMMM yyyy").parse(createdAt);
		}catch(Exception ex) {}
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd hh:MM:ss");
		createdAt=formatter.format(date);
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
