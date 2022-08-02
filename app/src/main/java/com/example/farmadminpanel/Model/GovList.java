package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GovList implements Serializable
{
    String id;
    String name;
    String description;
    String link;
    String img_link;

    public GovList()
    {

    }

    public GovList(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getString ("id");
            this.name = jsonObject.getString ("name");
            this.description = jsonObject.getString ("description");
            this.link = jsonObject.getString ("link");
            this.img_link = jsonObject.getString ("img_link");
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

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
