package com.essentia.support;

/**
 * Created by kyawzinlatt94 on 2/1/15.
 */
public class UserObject {

    private  String userId;
    private  String name;
    private  String gender;
    private  String weight;
    private  String height;
    private  String age;
    private  String maxHR;
    private  String avgHR;
    private String restingHR;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String dateOfBirth) {
        this.age = dateOfBirth;
    }

    public String getMaxHR() {
        return maxHR;
    }

    public void setMaxHR(String maxHR) {
        this.maxHR = maxHR;
    }

    public String getAvgHR() {
        return avgHR;
    }

    public void setAvgHR(String avgHR) {
        this.avgHR = avgHR;
    }

    public String getRestingHR() {
        return restingHR;
    }

    public void setRestingHR(String restingHR) {
        this.restingHR = restingHR;
    }
}
