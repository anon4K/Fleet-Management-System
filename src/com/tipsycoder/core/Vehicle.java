package com.tipsycoder.core;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class Vehicle extends Common {
    private int chassisNum;
    private String condition;
    private int year;
    private String gasAmount;
    private String color;
    private String status;


    public int getChassisNum() {
        return chassisNum;
    }

    public void setChassisNum(int chassisNum) {
        this.chassisNum = chassisNum;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGasAmount() {
        return gasAmount;
    }

    public void setGasAmount(String gasAmount) {
        this.gasAmount = gasAmount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
