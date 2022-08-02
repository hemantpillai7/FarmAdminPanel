package com.example.farmadminpanel.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class AdvertismentList
{
    String sliderImage;
    String id;

    public AdvertismentList()
    {

    }

    public AdvertismentList(JSONObject jsonObject)
    {
        try {
            this.sliderImage = jsonObject.getString ("sliderImage");
            this.id = jsonObject.getString ("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
