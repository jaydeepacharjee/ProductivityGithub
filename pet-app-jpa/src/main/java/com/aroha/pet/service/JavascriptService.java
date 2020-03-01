package com.aroha.pet.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.aroha.pet.repository.JavascriptDataRepo;
import com.aroha.pet.security.UserPrincipal;


@Service
public class JavascriptService {


	StringBuffer sb=null;
	StringBuffer sb2=null;

	@Autowired
	JavascriptDataRepo javascriptdatarepo;

	@Autowired
	ServletContext context;

	private static final Logger logger = LoggerFactory.getLogger(JavascriptService.class);

	public JavascriptService() {
		super();
	}

	public JavascriptResponse executeJavascript(final UserPrincipal currentUser, JavascriptPayload payload) throws IOException{
		//Get the Tomcat Root Directory to store all programs
		String absolutePath=context.getRealPath("/");
		System.out.println("Absolute Path is: "+absolutePath);

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String currTimeAndDate=df.format(dateobj);	
		//logger.info("JAVA_PROGRAMS_PET:  "+absolutePath);

		String uName=currentUser.getName();
		sb2=new StringBuffer();

		//Generate Random Name for each Javascript Program
		int n=15;
		String random=uName+"_"+"JS"+"_"+getAlphaNumericString(n);

		int qId=payload.getQuestionId();
		String text=payload.getJavascriptpojo().getJavascriptstr();
		sb=new StringBuffer(text);

		String filename="/"+random+".js";
		System.out.println("Name is : "+filename);
		String dirName=absolutePath;

		//Use buffered writer to store the written code in specified file path
		BufferedWriter writer = new BufferedWriter(new FileWriter(dirName+filename));
		writer.write(sb.toString());
		writer.close();
		writer = null;

		//Command to execute javascript Code
		String command="node "+dirName+filename;

		JavascriptResponse javascriptresponse=new JavascriptResponse();
		JavascriptPojo javascriptpojo=new JavascriptPojo();

		try {
			runProcess(command);
			JSONArray jsona = null;

			//If the written code doesnot contains any of the error or exception then execute this block
			if(!(sb2.toString().contains("SyntaxError") || sb2.toString().contains("EvalError") || sb2.toString().contains("RangeError") || 
					sb2.toString().contains("ReferenceError") || sb2.toString().contains("TypeError") || sb2.toString().contains("URIError")||
					(sb2.toString().contains("Exception")))) {
				jsona=getResultForJava(sb2);
				javascriptpojo.setJavascriptstr(text);		
				javascriptpojo.setResultstr(jsona.toString());
				javascriptpojo.setQuestionId(qId);
				javascriptpojo.setScenario(payload.getJavascriptpojo().getScenario());
				javascriptpojo.setCreatedAt(currTimeAndDate);

				javascriptresponse.setJavascript(text);
				javascriptresponse.setJavascriptresult(getJsonArrayAsList(jsona));
				javascriptresponse.setJavascriptstatus("SUCCESS");

				try {
					javascriptdatarepo.save(javascriptpojo);
				}catch(Exception ex) {
					logger.error("Error saving javascript pojo "+ex.getMessage());
				}
			}

			//If the written code contains any errors then execute this block
			else if(sb2.toString().contains("SyntaxError") || sb2.toString().contains("EvalError") || sb2.toString().contains("RangeError") || 
					sb2.toString().contains("ReferenceError") || sb2.toString().contains("TypeError") || sb2.toString().contains("URIError")) {
				jsona=getResultForJava(sb2);
				javascriptpojo.setQuestionId(qId);
				javascriptpojo.setJavascriptstr(text);
				javascriptpojo.setScenario(payload.getJavascriptpojo().getScenario());
				javascriptpojo.setResultstr(jsona.toString());
				javascriptpojo.setCreatedAt(currTimeAndDate);

				javascriptresponse.setJavascript(text);
				javascriptresponse.setJavascriptresult(getJsonArrayAsList(jsona));
				javascriptresponse.setJavascriptstatus("ERROR");

				try {
					javascriptdatarepo.save(javascriptpojo);
				}catch(Exception ex) {
					logger.error("Error saving javascript pojo "+ex.getMessage());
				}
			}
		}
		catch(Exception e) {
			logger.error("Error in javascript "+e.getMessage());
			javascriptresponse.setJavascriptstatus("ERROR");;
		}

		//javascriptpojo.setExceptionstr(javascriptresponse.getJavascriptexception());
		return javascriptresponse;		
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

	//To generate random string for the each file
	public static String getAlphaNumericString(int n) 
	{ 

		// chose a Character random from this String 
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789"
				+ "abcdefghijklmnopqrstuvxyz"; 

		// create StringBuffer size of AlphaNumericString 
		StringBuffer sb1 = new StringBuffer(n); 

		for (int i = 0; i < n; i++) { 
			int index = (int)(AlphaNumericString.length() * Math.random()); 
			// add Character one by one in end of sb 
			sb1.append(AlphaNumericString .charAt(index)); 
		}    
		return sb1.toString(); 
	}
}
