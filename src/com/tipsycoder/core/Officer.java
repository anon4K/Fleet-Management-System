package com.tipsycoder.core;

import java.sql.Date;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class Officer extends Common {
    private Integer badgeNum;
    private String firstName;
    private String lastName;
    private String rank;
    private java.sql.Date dob;
    private String mStatus;
    private String address;
    private String noteWorthy;
    private String pNumber;
    private String parish;
    private String nationality;

    public Officer() {}

    public Officer(Integer badgeNum, String firstName, String lastName, String rank, String station, String division, String area, java.sql.Date dob, String mStatus, String address, String noteWorthy, String pNumber) {
        this.setBadgeNum(badgeNum);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setRank(rank);
        this.setArea(area);
        this.setStation(station);
        this.setDivision(division);
        this.setDob(dob);
        this.setmStatus(mStatus);
        this.setAddress(address);
        this.setNoteWorthy(noteWorthy);
        this.setpNumber(pNumber);
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNoteWorthy() {
        return noteWorthy;
    }

    public void setNoteWorthy(String noteWorthy) {
        this.noteWorthy = noteWorthy;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public Integer getBadgeNum() {
        return badgeNum;
    }

    public void setBadgeNum(Integer badgeNum) {
        this.badgeNum = badgeNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }


    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
