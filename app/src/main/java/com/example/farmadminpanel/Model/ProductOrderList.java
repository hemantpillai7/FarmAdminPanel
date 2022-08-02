package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductOrderList
{
    String id;
    String userid;
    String username;
    String name;
    String date;
    String qty;
    String price;
    String totalprice;
    String rating;
    String img_url;
    String userphoneno;
    String useraddress ;
    String status;
    String useremail;

    public ProductOrderList()
    {

    }

    public ProductOrderList(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getString("id");
            this.userid = jsonObject.getString("userid");
            this.username = jsonObject.getString ("username");
            this.name = jsonObject.getString ("name");
            this.date = jsonObject.getString ("date");
            this.qty = jsonObject.getString ("qty");
            this.price = jsonObject.getString ("price");
            this.totalprice = jsonObject.getString ("totalprice");
            this.rating = jsonObject.getString ("rating");
            this.img_url = jsonObject.getString ("img_url");
            this.userphoneno = jsonObject.getString ("userphoneno");
            this.useraddress = jsonObject.getString ("useraddress");
            this.status = jsonObject.getString ("status");
            this.useremail = jsonObject.getString ("useremail");
        } catch (JSONException e) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUserphoneno() {
        return userphoneno;
    }

    public void setUserphoneno(String userphoneno) {
        this.userphoneno = userphoneno;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}
