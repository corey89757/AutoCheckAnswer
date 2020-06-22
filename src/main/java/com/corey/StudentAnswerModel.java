package com.corey;

/**
 * @author wujian
 * @date 2020/06/22
 */
public class StudentAnswerModel {
    private String name;
    private Integer totalScore;

    public StudentAnswerModel(String name, Integer totalScore) {
        this.name = name;
        this.totalScore = totalScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
