package com.example.livelocation;

public class Modal {
    private String AreaName,City,State,Country,Landmark,Pincode;

    public Modal() {
    }

    public Modal(String areaName, String city, String state, String country, String landmark, String pincode) {
        AreaName = areaName;
        City = city;
        State = state;
        Country = country;
        Landmark = landmark;
        Pincode = pincode;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }
}
