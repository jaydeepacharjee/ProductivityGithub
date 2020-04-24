package com.aroha.pet.payload;

import java.util.List;

public class MentorFeedbackResponse {

    private String feedbackDate;
    private int questionId;
    private String mentorName;
    private Long mentorId;
    private long learnerId;
    private String learnerName;
    private String feedback;
    private String question;
    private List resultStr;
    private String programingResult;
    private String exceptionStr;
    private String sqlStr;
    private String programingStr;
    private String technologyName;
    private String error;
    private int notification;
    private String query_date;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public long getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(long learnerId) {
        this.learnerId = learnerId;
    }


  
    public String getLearnerName() {
        return learnerName;
    }

    public void setLearnerName(String learnerName) {
        this.learnerName = learnerName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }



    public List getResultStr() {
		return resultStr;
	}

	public void setResultStr(List resultStr) {
		this.resultStr = resultStr;
	}

	public String getExceptionStr() {
        return exceptionStr;
    }

    public void setExceptionStr(String exceptionStr) {
        this.exceptionStr = exceptionStr;
    }

    public String getSqlStr() {

        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getQuery_date() {
        return query_date;
    }

    public void setQuery_date(String query_date) {
        this.query_date = query_date;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }


    public String getProgramingStr() {
		return programingStr;
	}

	public void setProgramingStr(String programingStr) {
		this.programingStr = programingStr;
	}

	public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

	public String getProgramingResult() {
		return programingResult;
	}

	public void setProgramingResult(String programingResult) {
		this.programingResult = programingResult;
	}
    

}
