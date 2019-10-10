package com.example.loginactivity.Classes;

import java.util.ArrayList;

public class UserData {

    private String name;
    private String imageUrl;
    private String address;
    private String gender;
    private ArrayList<String> memberOfGroup;
    private ArrayList<String> hostedEvents;
    private ArrayList<String> userInterests;

    public UserData() {
    }

    public UserData(String name) {
        this.name = name;
        this.address = "";
        this.memberOfGroup = new ArrayList<>();
        this.hostedEvents = new ArrayList<>();
        this.userInterests = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getMemberOfGroup() {
        return memberOfGroup;
    }

    public void setMemberOfGroup(ArrayList<String> memberOfGroup) {
        this.memberOfGroup = memberOfGroup;
    }

    public ArrayList<String> getHostedEvents() {
        return hostedEvents;
    }

    public void setHostedEvents(ArrayList<String> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public ArrayList<String> getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(ArrayList<String> userInterests) {
        this.userInterests = userInterests;
    }
}