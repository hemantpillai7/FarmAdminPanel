package com.example.farmadminpanel.Adapter;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.SoilServiceRequestList;
import com.example.farmadminpanel.ProcessingListActivity;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.SoilServiceCompletedListActivity;
import com.example.farmadminpanel.SoilServiceProcessingListActivity;
import com.example.farmadminpanel.SoilServiceRequestListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceSoilHistoryAdapter extends RecyclerView.Adapter<ServiceSoilHistoryAdapter.ViewHolder>
{
    public Context context;
    private List<SoilServiceRequestList> list;

    private Dialog mdialog;
    private Button btn_yes, btn_no;

    ProgressDialog dialog;

    public ServiceSoilHistoryAdapter(Context context, List<SoilServiceRequestList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceSoilHistoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customsoilservicehistory,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhoneno());
        holder.status.setText(list.get(position).getStatus());
        holder.date.setText(list.get(position).getDate());
        holder.email.setText(list.get(position).getEmail());
        holder.plotaddress.setText(list.get(position).getPlotaddress());
        holder.address.setText(list.get(position).getAddress());

        if(holder.status.getText().equals("Completed") || holder.status.getText().equals("Cancelled"))
        {
            holder.view.setVisibility(View.INVISIBLE);
            holder.layoutcomplete.setVisibility(GONE);
            holder.layoutprocess.setVisibility(GONE);
        }
        else if(holder.status.getText().equals("Processing"))
        {
            holder.layoutprocess.setVisibility(GONE);
        }
        else if(holder.status.getText().equals("Booked"))
        {
            holder.layoutcomplete.setVisibility(GONE);
        }

        holder.process.setOnClickListener(new View.OnClickListener()
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
                text.setText("Accept Order...!");
                text_msg.setText("");
                text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                btn_yes.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        process(list.get(position).getId());
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

        holder.cancel.setOnClickListener(new View.OnClickListener() {
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
                text.setText("Cancel Order..!");
                text_msg.setText("Are You Sure ");
                text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                btn_yes.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        cancel(list.get(position).getId());
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

        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mdialog = new Dialog(context);
                mdialog.setCancelable(false);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.setContentView(R.layout.alert_dialog);
                btn_yes = mdialog.findViewById(R.id.btn_logout_yes);
                btn_no = mdialog.findViewById(R.id.btn_logout_no);
                btn_yes.setText("Complete");
                btn_no.setText("Cancel");
                TextView text_msg = (TextView) mdialog.findViewById(R.id.text_msg);
                ImageView iv_image = (ImageView) mdialog.findViewById(R.id.iv_image);
                //iv_image.setImageDrawable(getResources().getDrawable(R.drawable.logout));
                TextView text = (TextView) mdialog.findViewById(R.id.text);
                text.setText("Order Status...!");
                text_msg.setText("");
                text.setGravity(View.TEXT_ALIGNMENT_CENTER);
                btn_yes.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        complete(list.get(position).getId());
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

    private void complete(String id)
    {
        dialog= new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String currentdate=  new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.updateSoilservicestatus(id,"Completed",currentdate);
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
                        ((SoilServiceRequestListActivity) context).getSoilServiceRequest();
                        Toast.makeText(context, "Request Completed", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                Toast.makeText(context, "Failed To Complete Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancel(String id)
    {
        dialog= new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String currentdate=  new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.updateSoilservicestatus(id,"Cancelled",currentdate);
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
                        ((SoilServiceRequestListActivity) context).getSoilServiceRequest();
                        Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
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

    private void process(String id)
    {
        dialog= new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String currentdate=  new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.updateSoilservicestatus(id,"Processing",currentdate);
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
                        ((SoilServiceRequestListActivity) context).getSoilServiceRequest();
                        Toast.makeText(context, "request Accepted", Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,phone,email,status,date,plotaddress,address,complete;
        Button process,cancel;
        View view;

        LinearLayout layoutcomplete, layoutprocess;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.reqSoilName);
            phone=itemView.findViewById(R.id.reqSoilPhoneno);
            status=itemView.findViewById(R.id.reqSoilStatus);
            email=itemView.findViewById(R.id.reqSoilEmail);
            plotaddress=itemView.findViewById(R.id.reqSoilPlotAddress);
            address=itemView.findViewById(R.id.reqSoilAddress);
            process=itemView.findViewById(R.id.txtSoilAccept);
            cancel=itemView.findViewById(R.id.txtSoilRej);
            complete=itemView.findViewById(R.id.txtSoilComplete);
            date=itemView.findViewById(R.id.reqSoilDate);
            view=itemView.findViewById(R.id.view);
            layoutcomplete=itemView.findViewById(R.id.LayoutButton1);
            layoutprocess=itemView.findViewById(R.id.LayoutButton2);

        }
    }
}
