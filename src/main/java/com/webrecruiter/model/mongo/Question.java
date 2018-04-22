/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.model.mongo;

/**
 *
 * @author irina
 */
public class Question {

    private String questionText;
    private String questionAnswer1;
    private String questionAnswer2;
    private String questionAnswer3;
    private String correctAnswer;

    public Question(String questionText, String questionAnswer1, String questionAnswer2, String questionAnswer3, String correctAnswer) {
        this.questionText = questionText;
        this.questionAnswer1 = questionAnswer1;
        this.questionAnswer2 = questionAnswer2;
        this.questionAnswer3 = questionAnswer3;
        this.correctAnswer = correctAnswer;
    }

    public Question() {
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionAnswer1() {
        return questionAnswer1;
    }

    public void setQuestionAnswer1(String questionAnswer1) {
        this.questionAnswer1 = questionAnswer1;
    }

    public String getQuestionAnswer2() {
        return questionAnswer2;
    }

    public void setQuestionAnswer2(String questionAnswer2) {
        this.questionAnswer2 = questionAnswer2;
    }

    public String getQuestionAnswer3() {
        return questionAnswer3;
    }

    public void setQuestionAnswer3(String questionAnswer3) {
        this.questionAnswer3 = questionAnswer3;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}
