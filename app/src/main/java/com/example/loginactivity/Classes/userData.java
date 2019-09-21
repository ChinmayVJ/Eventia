package com.example.loginactivity.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class UserData {

    String Name;
    String userAddress;
    ArrayList<String> memberOfGroup;
    ArrayList<String> hostedEvents;
    ArrayList<String> userInterests;

    public UserData() {
    }

    public UserData(String name, String userAddress, ArrayList<String> memberOfGroup, ArrayList<String> hostedEvents, ArrayList<String> userInterests) {
        Name = name;
        this.userAddress = userAddress;
        this.memberOfGroup = memberOfGroup;
        this.hostedEvents = hostedEvents;
        this.userInterests = userInterests;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
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