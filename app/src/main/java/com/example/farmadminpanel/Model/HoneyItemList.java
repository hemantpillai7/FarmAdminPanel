package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class HoneyItemList
{
    String id;
    String honeyname;
    String honeyprice;
    String honeytype;
    String 	honey_img;

    public HoneyItemList()
    {

    }

    public HoneyItemList(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getString ("id");
            this.honeyname = jsonObject.getString ("honeyname");
            this.honeyprice = jsonObject.getString ("honeyprice");
            this.honeytype = jsonObject.getString ("honeytype");
            this.honey_img = jsonObject.getString ("honey_img");
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

    public String getHoneyname() {
        return honeyname;
    }

    public void setHoneyname(String honeyname) {
        this.honeyname = honeyname;
    }

    public String getHoneyprice() {
        return honeyprice;
    }

    public void setHoneyprice(String honeyprice) {
        this.honeyprice = honeyprice;
    }

    public String getHoneytype() {
        return honeytype;
    }

    public void setHoneytype(String honeytype) {
        this.honeytype = honeytype;
    }

    public String getHoney_img() {
        return honey_img;
    }

    public void setHoney_img(String honey_img) {
        this.honey_img = honey_img;
    }
}
