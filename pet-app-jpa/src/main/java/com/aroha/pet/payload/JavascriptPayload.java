package com.aroha.pet.payload;

import com.aroha.pet.model.JavascriptPojo;

public class JavascriptPayload {

    private int questionId;
    private JavascriptPojo javascriptpojo;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public JavascriptPojo getJavascriptpojo() {
        return javascriptpojo;
    }

    public void setJavascriptpojo(JavascriptPojo javascriptpojo) {
        this.javascriptpojo = javascriptpojo;
    }

}
