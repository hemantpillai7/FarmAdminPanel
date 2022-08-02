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
import com.example.farmadminpanel.Api.Constant;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.ProductOrderList;
import com.example.farmadminpanel.OrderListActivity;
import com.example.farmadminpanel.ProcessingListActivity;
import com.example.farmadminpanel.R;
import com.squareup.picasso.Picasso;


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

public class ProcessingListAdapter extends  RecyclerView.Adapter<ProcessingListAdapter.ViewHolder>
{
    public Context context;
    private List<ProductOrderList> list;
    private Dialog mdialog;
    private Button btn_yes, btn_no;
    String currentdate;

    ProgressDialog dialog;

    public ProcessingListAdapter(Context context, List<ProductOrderList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProcessingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customprocessinglist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessingListAdapter.ViewHolder holder, int position)
    {
        holder.productname.setText(list.get(position).getName());
        holder.username.setText(list.get(position).getUsername());
        holder.address.setText(list.get(position).getUseraddress());
        holder.number.setText(list.get(position).getUserphoneno());
        holder.productqty.setText(list.get(position).getQty());

        Picasso.get().load(Constant.AllProductsLINK+list.get(position).getImg_url())
                .placeholder(R.drawable.noimg)
                .into(holder.img);

        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mdialog = new Dialog(context);
                mdialog.setCancelable(false);
                mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialog.setContentView(R.layout.alert_dialog);
                btn_yes = mdialog.findViewById(R.id.btn_logout_yes);
                btn_no = mdialog.findViewById(R.id.btn_logout_no);
                btn_yes.setText("Delivered");
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
                        deliveredOrder(list.get(position).getId(),list.get(position).getUserid());
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

    private void deliveredOrder(String id, String userid)
    {
        dialog= new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        currentdate =  new SimpleDateFormat("yyyy MM dd", Locale.getDefault()).format(new Date());
        ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.orderdelivered(id,userid,"Delivered",currentdate);
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
                        ((ProcessingListActivity) context).getProcessingList();
                        Toast.makeText(context, "Order Delivered Successfully", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Status Failed", Toast.LENGTH_SHORT).show();
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
        TextView productname, username, address, number, productqty;
        Button delivered;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productname=itemView.findViewById(R.id.orderProName);
            username=itemView.findViewById(R.id.orderUserName);
            address=itemView.findViewById(R.id.orderUserAddress);
            number=itemView.findViewById(R.id.orderUserNumber);
            productqty=itemView.findViewById(R.id.orderProQty);
            img=itemView.findViewById(R.id.orderImg);
            delivered=itemView.findViewById(R.id.orderDelivered);

        }
    }
}
