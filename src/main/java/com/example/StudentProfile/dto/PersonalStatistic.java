package com.example.StudentProfile.dto;

import com.example.StudentProfile.models.User;

public class PersonalStatistic implements Comparable<PersonalStatistic>{
    private User user;
    private int iron = 0;
    private int bronze = 0;
    private int silver = 0;
    private int golden = 0;

    private Double rate;

    public PersonalStatistic(User user, int iron, int bronze, int silver, int golden) {
        this.user = user;
        this.iron = iron;
        this.bronze = bronze;
        this.silver = silver;
        this.golden = golden;
        this.rate = setRate();
    }

    public Double getRate() {
        return rate;
    }

    public Double setRate() {
        double k = this.iron * 0.1 + this.bronze * 0.2 + this.silver * 0.3 + this.golden * 0.4;
        this.rate = k;
        return k;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIron() {
        return iron;
    }

    public void setIron(int iron) {
        this.iron = iron;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getGolden() {
        return golden;
    }

    public void setGolden(int golden) {
        this.golden = golden;
    }


    @Override
    public int compareTo(PersonalStatistic o) {
        int result = this.rate.compareTo(o.rate);
        return result;
    }
}
