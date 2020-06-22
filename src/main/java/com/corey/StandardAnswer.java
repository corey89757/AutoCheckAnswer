package com.corey;

/**
 * @author wujian
 * @date 2020/06/22
 */
public class StandardAnswer {

    private String answer;
    private Integer score;

    public StandardAnswer(String answer, Integer score) {
        this.answer = answer;
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
