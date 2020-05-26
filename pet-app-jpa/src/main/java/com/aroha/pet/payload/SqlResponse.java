package com.aroha.pet.payload;

import java.io.Serializable;
import java.util.List;
import com.aroha.pet.model.DbInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Sony George | Date : 20 Mar, 2019 5:11:14 PM
 */
public class SqlResponse implements Serializable {

    private DbInfo dbInfo;
    private String sql;
    private List result;
    private String exception;
    private String status;
    private String message;

    @JsonIgnore
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

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SqlResponse [result=" + result + "]";
	}
	
	

}
