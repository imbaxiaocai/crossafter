package com.example.crossafter.pub.bean;

public class User{
    private int uid;
    private String uname;
    private String upsw;
    private String rname;//真名
    private String ID;
    private String phonenumber;
    private String location;
    private double wallet;
    private int usertype; //0-零售商 1-供应商
    private String Avatar;
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpsw() {
        return upsw;
    }

    public void setUpsw(String upsw) {
        this.upsw = upsw;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public int getUserType() {
        return usertype;
    }

    public void setUserType(int userType) {
        this.usertype = userType;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof User){
            if(((User) obj).uname.equals(this.uname)&&((User) obj).upsw.equals(this.upsw)){
                return true;
            }
        }
        return false;
    }


}
