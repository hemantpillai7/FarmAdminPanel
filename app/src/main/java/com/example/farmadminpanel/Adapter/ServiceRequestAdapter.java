package com.example.farmadminpanel.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.ServiceRequestList;

import com.example.farmadminpanel.ProcessingListActivity;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.ServiceRequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder>
{
    public Context context;
    private List<ServiceRequestList> list;

    private Dialog mdialog;
    private Button btn_yes, btn_no;

    ProgressDialog dialog;

    public ServiceRequestAdapter(Context context, List<ServiceRequestList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ServiceRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ServiceRequestAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customservicerequestlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRequestAdapter.ViewHolder holder, int position)
    {
        //holder.id.setText(list.get(position).getUserid());
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhoneno());
        holder.status.setText(list.get(position).getStatus());
        holder.type.setText(list.get(position).getRequesttype());
        holder.date.setText(list.get(position).getDate());
        holder.message.setText(list.get(position).getMessage());

        holder.complete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mdialog = new Dialog(context);
                mdialog.setCancelable(false);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.setContentView(R.layout.alert_dialog);
                btn_yes = mdialog.findViewById(R.id.btn_logout_yes);
                btn_no = mdialog.findViewById(R.id.btn_logout_no);
                btn_yes.setText("Yes");
                btn_no.setText("No");
                TextView text_msg = (TextView) mdialog.findViewById(R.id.text_msg);
                ImageView iv_image = (ImageView) mdialog.findViewById(R.id.iv_image);
                //iv_image.setImageDrawable(getResources().getDrawable(R.drawable.logout));
                TextView text = (TextView) mdialog.findViewById(R.id.text);
                text.setText("Complete Order...!");
                text_msg.setText("");
                text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                btn_yes.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        completerequest(list.get(position).getId(),list.get(position).getRequesttype());
                        mdialog.dismiss();
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mdialog.dismiss();
                    }
                });
                mdialog.show();

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mdialog = new Dialog(context);
                mdialog.setCancelable(false);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.setContentView(R.layout.alert_dialog);
                btn_yes = mdialog.findViewById(R.id.btn_logout_yes);
                btn_no = mdialog.findViewById(R.id.btn_logout_no);
                btn_yes.setText("Yes");
                btn_no.setText("No");
                TextView text_msg = (TextView) mdialog.findViewById(R.id.text_msg);
                ImageView iv_image = (ImageView) mdialog.findViewById(R.id.iv_image);
                //iv_image.setImageDrawable(getResources().getDrawable(R.drawable.logout));
                TextView text = (TextView) mdialog.findViewById(R.id.text);
                text.setText("Cancel Request...!");
                text_msg.setText("");
                text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                btn_yes.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        cancelrequest(list.get(position).getId(),list.get(position).getRequesttype());
                        mdialog.dismiss();
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mdialog.dismiss();
                    }
                });
                mdialog.show();
            }
        });
    }

    private void cancelrequest(String id, String requesttype)
    {
        dialog= new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.updateservicestatus(id,"Cancelled",requesttype);
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
                        ((ServiceRequestActivity) context).getservicerequest();
                        Toast.makeText(context, "Request Cancelled", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Please Try Later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completerequest(String id, String requesttype)
    {
        dialog= new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.updateservicestatus(id,"Completed",requesttype);
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
                        ((ServiceRequestActivity) context).getservicerequest();
                        Toast.makeText(context, "Request Completed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Please Try Later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,phone,status,type,id,date,message,complete,cancel;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            //id=itemView.findViewById(R.id.reqId);
            name=itemView.findViewById(R.id.reqName);
            phone=itemView.findViewById(R.id.reqPhoneno);
            status=itemView.findViewById(R.id.reqStatus);
            type=itemView.findViewById(R.id.reqType);
            date=itemView.findViewById(R.id.reqDate);
            message=itemView.findViewById(R.id.reqMsg);
            complete=itemView.findViewById(R.id.txtSerAccept);
            cancel=itemView.findViewById(R.id.txtSerRej);
        }
    }
}
