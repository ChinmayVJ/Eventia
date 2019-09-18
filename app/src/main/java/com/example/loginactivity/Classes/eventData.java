package com.example.loginactivity.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class eventData {
    private String hostName;
    private String groupName;
    private String eventName;
    private String description;
    private Date dateOfEvent;
    private Time startTime;
    private int duration;
    private String address;
    private ArrayList<String> interests;
    private int noOfMembers;

    public eventData(){}

    public eventData(String hostName, String groupName, String eventName, String description, Date dateOfEvent, Time startTime, int duration, String address, ArrayList<String> interests, int noOfMembers) {
        this.hostName = hostName;
        this.groupName = groupName;
        this.eventName = eventName;
        this.description = description;
        this.dateOfEvent = dateOfEvent;
        this.startTime = startTime;
        this.duration = duration;
        this.address = address;
        this.interests = interests;
        this.noOfMembers = noOfMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public int getNoOfMembers() {
        return noOfMembers;
    }

    public void setNoOfMembers(int noOfMembers) {
        this.noOfMembers = noOfMembers;
    }

    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(Date dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
