package com.essentia.support;

/**
 * Created by kyawzinlatt94 on 2/1/15.
 */
public class UserObject {

    public static String userId;
    public static String name;
    public static String password;
    public static String gender;
    public static String weight;
    public static String height;
    public static String dateOfBirth;
    public static String maxHR;
    public static String avgHR;

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
