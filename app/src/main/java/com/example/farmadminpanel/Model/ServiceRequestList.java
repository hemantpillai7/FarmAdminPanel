package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceRequestList
{
    String id;
    String userid;
    String name;
    String email;
    String phoneno;
    String address;
    String date;
    String requesttype;
    String status;
    String message;
    String plotaddress;




    public ServiceRequestList() {
    }

    public ServiceRequestList(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getString("id");
            this.userid = jsonObject.getString("userid");
            this.name = jsonObject.getString("name");
            this.email = jsonObject.getString("email");
            this.phoneno = jsonObject.getString("phoneno");
            this.address = jsonObject.getString("address");
            this.date = jsonObject.getString("date");
            this.requesttype = jsonObject.getString("requesttype");
            this.status = jsonObject.getString("status");
            this.message = jsonObject.getString("message");
            this.plotaddress = jsonObject.getString("plotaddress");

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getPlotaddress() {
        return plotaddress;
    }

    public void setPlotaddress(String plotaddress) {
        this.plotaddress = plotaddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
