package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class GardenAdverList
{
    String id;
    String gardenimg_url;

    public GardenAdverList()
    {
    }

    public GardenAdverList(JSONObject jsonObject)
    {
        try {
        this.id = jsonObject.getString ("id");
        this.gardenimg_url =jsonObject.getString ("gardenimg_url");
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

    public String getGardenimg_url() {
        return gardenimg_url;
    }

    public void setGardenimg_url(String gardenimg_url) {
        this.gardenimg_url = gardenimg_url;
    }
}
