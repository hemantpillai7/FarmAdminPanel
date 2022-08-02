package com.example.farmadminpanel.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.AppConfig;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Fragment.ManageProfileFragment;
import com.example.farmadminpanel.MainActivity;
import com.example.farmadminpanel.Model.UserDetailList;
import com.example.farmadminpanel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.ViewHolder>
{
    public Context context;
    private List<UserDetailList>list;

    ProgressDialog dialog;

    public UserDetailAdapter(Context context, List<UserDetailList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_table,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.id.setText(list.get(position).getUserid());
        holder.name.setText(list.get(position).getName());
        holder.email.setText(list.get(position).getEmail());
        holder.phone.setText(list.get(position).getPhoneNo());

        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle("Remove User");
                ad.setMessage("Are You Sure !!!");
                ad.setIcon(R.drawable.ic_baseline_block_24);
                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        deleteuser();
                        dialog.dismiss();
                    }
                    private void deleteuser()
                    {
                        dialog= new ProgressDialog(context);
                        dialog.show();
                        dialog.setContentView(R.layout.progressbar);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
                        Call<ResponseBody> result = apiInterface.deleteUser(list.get(position).getUserid());
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
                                        Toast.makeText(context, "User Removed Successfully", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                });
                ad.show();

            }
        });

    }




    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView id,name,email,phone;
        ImageView delete;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            id=itemView.findViewById(R.id.tblId);
            name=itemView.findViewById(R.id.tblname);
            email=itemView.findViewById(R.id.tblemail);
            phone=itemView.findViewById(R.id.tblnumber);
            delete=itemView.findViewById(R.id.tbldelete);
        }
    }


}
