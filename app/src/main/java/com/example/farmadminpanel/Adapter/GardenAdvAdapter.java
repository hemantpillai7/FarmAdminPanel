package com.example.farmadminpanel.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Constant;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.GardenAdverList;
import com.example.farmadminpanel.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GardenAdvAdapter extends RecyclerView.Adapter<GardenAdvAdapter.ViewHolder>
{
    private Context context;
    private List<GardenAdverList> list;

    ProgressDialog dialog;

    public GardenAdvAdapter(Context context, List<GardenAdverList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GardenAdvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GardenAdvAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adverstiment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GardenAdvAdapter.ViewHolder holder, int position)
    {
        Picasso.get().load(Constant.GardenprojectLink+list.get(position).getGardenimg_url())
                .placeholder(R.drawable.noimg)
                .into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog= new ProgressDialog(context);
                dialog.show();
                dialog.setContentView(R.layout.progressbar);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String id= list.get(position).getId();
                ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
                Call<ResponseBody> result = apiInterface.deleteGardenAdvertisment(id);
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
                                list.remove(list.get(position));
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
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
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView delete;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img=itemView.findViewById(R.id.adverRAC);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
