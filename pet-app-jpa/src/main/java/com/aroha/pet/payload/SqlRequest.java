package com.aroha.pet.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.Question;

/**
 *
 * @author Sony George | Date : 3 Apr, 2019 6:31:19 PM
 */
public class SqlRequest {

    @NotNull
    private DbInfo dbInfo;
    
    @NotNull
    @NotEmpty
    private String sql;
    
    private Question question;
    private String scenario;

    public DbInfo getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
    

}
