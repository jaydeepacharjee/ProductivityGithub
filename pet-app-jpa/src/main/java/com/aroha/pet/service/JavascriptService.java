package com.aroha.pet.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aroha.pet.model.JavaPojo;
import com.aroha.pet.model.JavascriptPojo;
import com.aroha.pet.payload.JavaResponse;
import com.aroha.pet.payload.JavascriptPayload;
import com.aroha.pet.payload.JavascriptResponse;
import com.aroha.pet.repository.JavascriptRepo;
import com.aroha.pet.security.UserPrincipal;


@Service
public class JavascriptService {


	StringBuffer sb=null;
	StringBuffer sb2=null;

	@Autowired
	JavascriptRepo javascriptRepo;

	@Autowired
	ServletContext context;

	private static final Logger logger = LoggerFactory.getLogger(JavascriptService.class);

	public JavascriptService() {
		super();
	}

	public JavascriptResponse executeJavascript(final UserPrincipal currentUser, JavascriptPayload payload) throws Exception{
		//Get the Tomcat Root Directory to store all programs
		Path currentPath=Paths.get("");
		String projectPath=currentPath.toAbsolutePath().toString();
		String dirName=projectPath+"\\"+"JavascriptPrograms";
		String fileString=generateRandomWord(8);
		File newFile=new File(dirName);
		newFile.mkdir();

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String currTimeAndDate=df.format(dateobj);	
		//logger.info("JAVA_PROGRAMS_PET:  "+absolutePath);

		String uName=currentUser.getName();
		sb2=new StringBuffer();

		//Generate Random Name for each Javascript Program
		//int n=15;
		String random=fileString+"_"+uName;

		int qId=payload.getQuestionId();
		String text=payload.getJavascriptpojo().getJavascriptstr();
		sb=new StringBuffer(text);

		String filename="/"+random+".js";
		//System.out.println("Name is : "+filename);
		//String dirName=absolutePath;

		//Use buffered writer to store the written code in specified file path
		BufferedWriter writer = new BufferedWriter(new FileWriter(dirName+filename));
		writer.write(sb.toString());
		writer.close();
		writer = null;

		//Command to execute javascript Code
		String command="node "+dirName+filename;

		JavascriptResponse javascriptresponse=new JavascriptResponse();
		JavascriptPojo javascriptpojo=new JavascriptPojo();
		runProcess(command);
		JSONArray jsona = null;

		if(sb2.toString().contains("SyntaxError") || sb2.toString().contains("EvalError") || sb2.toString().contains("RangeError") || 
				sb2.toString().contains("ReferenceError") || sb2.toString().contains("TypeError") || sb2.toString().contains("URIError")) {
			jsona=getResultForJava(sb2);
			System.out.println("Sb2 is: "+sb2);
			javascriptpojo.setQuestionId(qId);
			javascriptpojo.setJavascriptstr(text);
			javascriptpojo.setScenario(payload.getJavascriptpojo().getScenario());
			
			String error=sb2.toString().substring(sb2.toString().indexOf(".js:"));
			System.out.println("Error is: "+error);
			int r=error.toString().indexOf("at");
			int m=error.toString().indexOf("\n");
			//cpojo.setResultstr(sb.toString().substring(sb.toString().indexOf("error")));
			javascriptpojo.setResultstr(error.toString().substring(m+1, r));
			
			//javascriptpojo.setResultstr(sb2.toString());
			javascriptpojo.setCreatedAt(currTimeAndDate);
			javascriptpojo.setCreatedBy(currentUser.getId());
			javascriptresponse.setJavascript(text);
			//javascriptresponse.setJavascriptresult(getJsonArrayAsList(jsona));
			javascriptresponse.setJavascripterror(error.toString().substring(m+1, r));
			javascriptresponse.setJavascriptstatus("ERROR");
			javascriptRepo.save(javascriptpojo);
			return javascriptresponse;
		}
		else {
			jsona=getResultForJava(sb2);
			javascriptpojo.setJavascriptstr(text);		
			javascriptpojo.setResultstr(jsona.toString());
			javascriptpojo.setQuestionId(qId);
			javascriptpojo.setScenario(payload.getJavascriptpojo().getScenario());
			javascriptpojo.setCreatedAt(currTimeAndDate);
			javascriptpojo.setCreatedBy(currentUser.getId());
			javascriptresponse.setJavascript(text);
			javascriptresponse.setJavascriptresult(getJsonArrayAsList(jsona));
			javascriptresponse.setJavascriptstatus("SUCCESS");
			javascriptRepo.save(javascriptpojo);
			return javascriptresponse;
		}
	}	


	private StringBuffer printLines(String cmd, InputStream ins) throws Exception {
		int ctr=0;
		String line = null;
		BufferedReader in = new BufferedReader(
				new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			sb2.append(line);
			sb2.append("\n");
			ctr++;
		}
		in.close();
		in=null;
		return sb2;
	}

	//To connect to the commmand prompt to execute the code
	private StringBuffer runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		InputStream temp = pro.getInputStream();
		sb2=printLines(command + " stdout:", temp);
		temp.close();
		temp = pro.getErrorStream();
		StringBuffer sb2=printLines(command + " stderr:", temp);
		temp.close();
		temp = null;
		pro.waitFor();
		pro.destroy();		
		return sb2;
	}

	private JSONArray getResultForJava(StringBuffer sb2) throws Exception {
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("output", sb2);
		json.put(obj);
		return json;
	}

	private List getJsonArrayAsList(JSONArray jsona) {
		return jsona.toList();
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
}
