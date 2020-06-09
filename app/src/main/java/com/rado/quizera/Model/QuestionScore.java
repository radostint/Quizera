package com.rado.quizera.Model;

public class QuestionScore {
    private String question_score, user, score, categoryId, categoryName;

    public QuestionScore() {
    }

    public QuestionScore(String question_score, String user, String score, String categoryId, String categoryName) {
        this.question_score = question_score;
        this.user = user;
        this.score = score;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getQuestion_score() {
        return question_score;
    }

    public void setQuestion_score(String question_score) {
        this.question_score = question_score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
