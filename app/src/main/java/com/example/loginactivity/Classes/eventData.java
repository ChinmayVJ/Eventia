package com.example.loginactivity.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class eventData {
    String groupName;
    String eventName;
    String description;
    Date dateOfEvent;
    Time startTime;
    int duration;
    LatLng location;
    ArrayList<String> interests;
    int noOfMembers;

    public eventData(){}

    public eventData(String groupName, String eventName, String description, Date dateOfEvent, Time startTime, int duration, LatLng location, ArrayList<String> interests, int noOfMembers) {
        this.groupName = groupName;
        this.eventName = eventName;
        this.description = description;
        this.dateOfEvent = dateOfEvent;
        this.startTime = startTime;
        this.duration = duration;
        this.location = location;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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
