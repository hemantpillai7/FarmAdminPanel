package com.example.farmadminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.farmadminpanel.Adapter.ServiceHistoryAdapter;
import com.example.farmadminpanel.Adapter.ServiceSoilHistoryAdapter;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.SoilServiceRequestList;
import com.example.farmadminpanel.databinding.ActivitySoilServiceRequestListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoilServiceRequestListActivity extends AppCompatActivity
{

   ActivitySoilServiceRequestListBinding binding;
    ArrayList<SoilServiceRequestList> list = new ArrayList<>();
    ServiceSoilHistoryAdapter adapter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_service_request_list);

        binding= ActivitySoilServiceRequestListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        getSoilServiceRequest();
    }

    public void getSoilServiceRequest()
    {
        dialog= new ProgressDialog(SoilServiceRequestListActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getSoilserviceRequest();
        result.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                dialog.dismiss();
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("ResponseCode").equals("1"))
                    {
                        list.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("status");
                            if(id.equals("Booked"))
                            {
                                list.add(new SoilServiceRequestList(object));
                            }
                            binding.RecSoilRequest.setLayoutManager(new LinearLayoutManager(SoilServiceRequestListActivity.this, RecyclerView.VERTICAL,false));
                            adapter = new ServiceSoilHistoryAdapter(SoilServiceRequestListActivity.this,list);
                            binding.RecSoilRequest.setAdapter(adapter);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}