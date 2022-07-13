package com.example.bloodapp;

public class Data {
   private String IdCard;
    private String LineAddress;
    private String Province;
    private String City;
    private String BloodType;
    private double lat;
    private double lon;
    private String Name;
    private String uid;

    public Data() {

    }
    public Data ( String Name,String Uid,String id, String addr, String province,String city,String btype,double _lat,double _lon){
        this.IdCard = id;
        this.LineAddress = addr;
        this.Province= province;
        this.City = city;
        this.BloodType = btype;
        this.lat = _lat;
        this.lon = _lon;
        this.Name=Name;
        this.uid=Uid;
    }
    public String getIdCard() {
        return IdCard;
    }

    public String getLineAddress() {
        return LineAddress;
    }

    public String getProvince() {
        return Province;
    }

    public String getCity() {
        return City;
    }

    public String getBloodType() {
        return BloodType;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return Name;
    }

    public String getUid() {
        return uid;
    }


}
