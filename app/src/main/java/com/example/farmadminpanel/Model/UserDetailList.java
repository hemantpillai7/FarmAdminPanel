package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailList
{
    String userid;
    String name;
    String email;
    String phoneNo;

    public UserDetailList()
    {
    }

    public UserDetailList(JSONObject jsonObject)
    {
        try {
            this.userid = jsonObject.getString ("userid");
            this.name = jsonObject.getString ("name");
            this.email = jsonObject.getString ("email");
            this.phoneNo = jsonObject.getString ("phoneNo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
