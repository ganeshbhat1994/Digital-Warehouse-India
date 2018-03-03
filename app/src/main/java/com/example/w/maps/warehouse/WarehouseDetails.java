package com.example.w.maps.warehouse;

import android.util.Log;

/**
 * Created by ganesh on 29/9/15.
 */
public class WarehouseDetails{

    private int wcapacity,wavailability;
    private String wname;
    private String phone,ownName,waddress1,waddress2,waddress3,type,storage_type,regdate,regperiod,wregno;
    private int uId;
    private float rent,rating;
    private int noRaters;
    public WarehouseDetails()
    {

    }

    public WarehouseDetails(float rating, int wcapacity, int wavailability, String wname, String waddress1,
                            String waddress2, String waddress3,String ownName,String phone, String type, String storage_type, String regdate,
                            String regperiod, int uId, float rent, int noRaters,String wregno)
    {
        this.rating = rating;
        this.wcapacity = wcapacity;
        this.wavailability = wavailability;
        this.wname = wname;
        this.ownName=ownName;
        this.phone=phone;
        this.waddress1 = waddress1;
        this.waddress2 = waddress2;
        this.waddress3 = waddress3;
        this.type = type;
        this.storage_type = storage_type;
        this.regdate = regdate;
        this.regperiod = regperiod;
        this.uId = uId;
        this.rent = rent;
        this.noRaters = noRaters;
        this.wregno=wregno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOwnName() {
        return ownName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

    public String getWregno() {
        return wregno;
    }

    public void setWregno(String wregno) {
        this.wregno = wregno;
    }

    public float getRating() {return rating;}

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getWcapacity() {
        return wcapacity;
    }

    public void setWcapacity(int wcapacity) {
        this.wcapacity = wcapacity;
    }

    public int getWavailability() {
        return wavailability;
    }

    public void setWavailability(int wavailability) {
        this.wavailability = wavailability;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getWaddress1() {
        return waddress1;
    }

    public void setWaddress1(String waddress1) {
        this.waddress1 = waddress1;
    }

    public String getWaddress2() {
        return waddress2;
    }

    public void setWaddress2(String waddress2) {
        this.waddress2 = waddress2;
    }

    public String getWaddress3() {
        return waddress3;
    }

    public void setWaddress3(String waddress3) {
        this.waddress3 = waddress3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStorage_type() {
        return storage_type;
    }

    public void setStorage_type(String storage_type) {
        this.storage_type = storage_type;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getRegperiod() {
        return regperiod;
    }

    public void setRegperiod(String regperiod) {
        this.regperiod = regperiod;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public int getNoRaters() {
        return noRaters;
    }

    public void setNoRaters(int noRaters) {
        this.noRaters = noRaters;
    }

}
