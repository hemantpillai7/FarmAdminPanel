package com.example.farmadminpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.farmadminpanel.Adapter.OrderListAdapter;
import com.example.farmadminpanel.Adapter.ProcessingListAdapter;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.ProductOrderList;
import com.example.farmadminpanel.databinding.ActivityDeleteProductBinding;
import com.example.farmadminpanel.databinding.ActivityProcessingListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingListActivity extends AppCompatActivity
{

    ActivityProcessingListBinding binding;
    ArrayList<ProductOrderList> list = new ArrayList<>();
    ProcessingListAdapter adapter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding= ActivityProcessingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        getProcessingList();

    }

    public void getProcessingList()
    {
        dialog= new ProgressDialog(ProcessingListActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getOrderList();
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
                            if(id.equals("Accepted"))
                            {
                                list.add(new ProductOrderList(object));
                                binding.processRec.setLayoutManager(new LinearLayoutManager(ProcessingListActivity.this, RecyclerView.VERTICAL,false));
                                adapter = new ProcessingListAdapter(ProcessingListActivity.this,list);
                                binding.processRec.setAdapter(adapter);
                            }

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