package com.example.farmadminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.farmadminpanel.Adapter.ServiceHistoryAdapter;
import com.example.farmadminpanel.Adapter.ServiceRequestAdapter;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.ServiceRequestList;
import com.example.farmadminpanel.databinding.ActivityDeliveredListBinding;
import com.example.farmadminpanel.databinding.ActivityGardenServiceStatusBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GardenServiceStatusActivity extends AppCompatActivity {

    ActivityGardenServiceStatusBinding binding;
    ArrayList<ServiceRequestList> list = new ArrayList<>();
    ServiceHistoryAdapter adapter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityGardenServiceStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        getGardenHistoryList();
    }

    private void getGardenHistoryList()
    {
        dialog= new ProgressDialog(GardenServiceStatusActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getTotalserviceRequest();
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

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("requesttype");
                            String id2 = object.getString("status");
                            if(id.equals("GardenService") && !id2.equals("Booked"))

                            {
                                list.add(new ServiceRequestList(object));
                            }
                            binding.RecGardenHistory.setLayoutManager(new LinearLayoutManager(GardenServiceStatusActivity.this, RecyclerView.VERTICAL,false));
                            adapter = new ServiceHistoryAdapter(GardenServiceStatusActivity.this,list);
                            binding.RecGardenHistory.setAdapter(adapter);
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