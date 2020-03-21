package com.aroha.pet.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aroha.pet.model.CProgram;
import com.aroha.pet.payload.CPayload;
import com.aroha.pet.payload.CResponse;
import com.aroha.pet.repository.CRepository;
import com.aroha.pet.security.UserPrincipal;


@Service
public class CService {
	StringBuffer sb=null;
	int ctr=0;
	
	@Autowired
	private CRepository cRepo;
	
	public CResponse cService(UserPrincipal user,CPayload load) throws IOException {
		String fileString=generateRandomWord(5);
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		File file=new File(s+"\\CProgramFolder");
		file.mkdir();
		FileWriter writer=new FileWriter(s+"\\CProgramFolder\\"+fileString+".cpp",false);
		writer.write("#include<stdio.h>"+"\n"+"#include<conio.h>"+"\n"
				+"#include<assert.h>"+"\n"+"#include<math.h>"+"\n"+
				"#include<stdlib.h>"+"\n"+"#include<string.h>"+"\n"+"#include<ctype.h>"+						
				"\n"+"using namespace std;"+"\n"+load.getcProgram().getcStr());
		writer.flush();
		writer.close();

		runProcess("C:\\TDM-GCC-64\\bin\\gcc.exe "+s+"\\CProgramFolder\\"+fileString+".cpp -o "+s+"\\CProgramFolder\\"+fileString);		
		if(sb.toString().contains("warning:")) {
			CProgram q1=new CProgram();
			q1.setcStr(load.getcProgram().getcStr());
			q1.setQuestionId(load.getQuestionId());
			q1.setResultstr(sb.toString());
			q1.setScenario(load.getcProgram().getScenario());
			q1.setCreatedBy(user.getId());
			cRepo.save(q1);
			
			CResponse response=new CResponse();
			response.setcScenario(load.getcProgram().getScenario());
			//response.setcException(error.toString().substring(m,r));
			response.setcStatus("Exception");
			return response;
		}
		
		if(runProcess(s+"\\CProgramFolder\\"+fileString)!=null) {
			sb=runProcess(s+"\\CProgramFolder\\"+fileString);
			CProgram q1=new CProgram();
			q1.setcStr(load.getcProgram().getcStr());
			q1.setQuestionId(load.getQuestionId());
			q1.setResultstr(sb.toString());
			q1.setScenario(load.getcProgram().getScenario());
			q1.setCreatedBy(user.getId());
			cRepo.save(q1);
			CResponse response=new CResponse();
			response.setcScenario(load.getcProgram().getScenario());
			JSONArray jsona=new JSONArray();
			JSONObject obj=new JSONObject();
			obj.put("output", sb);
			jsona.put(obj);
			response.setcResult(jsona.toList());
			response.setcStatus("SUCCESS");
			return response;
		}else {
			runProcess(s+"\\CProgramFolder\\"+fileString);
			String error=sb.toString().substring(sb.toString().indexOf("error"));
			int r=error.toString().indexOf("\n");
			int m=error.toString().indexOf("error");
			CProgram q1=new CProgram();
			q1.setcStr(load.getcProgram().getcStr());
			q1.setQuestionId(load.getQuestionId());
			q1.setExceptionstr(error.toString().substring(m,r));
			q1.setScenario(load.getcProgram().getScenario());
			q1.setCreatedBy(user.getId());
			cRepo.save(q1);
			CResponse response=new CResponse();
			response.setcScenario(load.getcProgram().getScenario());
			response.setcException(error.toString().substring(m,r));
			response.setcStatus("Error");
			return response;

		}
	}
	private  String generateRandomWord(int length) {
		Random r = new Random(); 
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++) { 
			char tmp = (char) ('a' + r.nextInt('z' - 'a')); 
			sb.append(tmp);
		}
		return sb.toString();
	}
	private StringBuffer printLines(String cmd, InputStream ins) throws Exception {
		String line = null;
		sb=new StringBuffer();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
			ctr++;
		}
		in.close();
		in=null;
		return sb;
	}
	private StringBuffer runProcess(String command) {
		StringBuffer s1=null;
		StringBuffer s2=null;
		try {
			Process pro = Runtime.getRuntime().exec(command);
			InputStream temp=pro.getInputStream();
			s1=printLines(command + " stdout:", temp);
			temp.close();
			temp=pro.getErrorStream();
			s2=printLines(command + " stderr:",temp);
			temp.close();
			temp=null;
			pro.waitFor();
			pro.destroy();
			return s1;
		}catch(Exception ex) {
			return s2;
		}

	}
}


