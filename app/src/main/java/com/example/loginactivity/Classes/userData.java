package com.example.loginactivity.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class UserData {
    String name;
    LatLng userLocation;
    ArrayList<String> memberOfGroup;
    ArrayList<String> userInterests;

    public UserData() {
    }

    public UserData(String name, LatLng userLocation, ArrayList<String> memberOfGroup, ArrayList<String> userInterests) {
        this.name = name;
        this.userLocation = userLocation;
        this.memberOfGroup = memberOfGroup;
        this.userInterests = userInterests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public ArrayList<String> getMemberOfGroup() {
        return memberOfGroup;
    }

    public void setMemberOfGroup(ArrayList<String> memberOfGroup) {
        this.memberOfGroup = memberOfGroup;
    }

    public ArrayList<String> getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(ArrayList<String> userInterests) {
        this.userInterests = userInterests;
    }
}
