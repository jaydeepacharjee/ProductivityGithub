package com.aroha.pet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.aroha.pet.model.audit.UserDateAudit;

/**
 *
 * @author Sony George | Date : 13 Mar, 2019 1:37:47 PM
 */
@Entity
@Table(name = "query_info")
public class QueryInfo extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(insertable = true, updatable = false)
    private String sqlStr;
    @Lob
    @Column(insertable = false, updatable = true)
    private String resultStr;
    @Lob
    @Column(insertable = false, updatable = true)
    private String exceptionStr;
//	@ManyToOne
//	@JoinColumn
//	private DBInfo dBInfo;
    @Column(insertable = true, updatable = false)
    private String dbType;
    @Column(insertable = true, updatable = false)
    private String jdbcUrl;
    @Column(insertable = true, updatable = false)
    private String userName;
    @Column(insertable = true, updatable = false)
    private String password;
    
    @Column(insertable = true, updatable = false)
    private String scenario;
    
    private int questionId;
    
	public QueryInfo( String scenario,String sqlStr, String exceptionStr) {
		super();
		this.sqlStr = sqlStr;
		this.exceptionStr = exceptionStr;
		this.scenario = scenario;
	}

	public QueryInfo() {
		super();
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public String getExceptionStr() {
        return exceptionStr;
    }

    public void setExceptionStr(String exceptionStr) {
        this.exceptionStr = exceptionStr;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

}
