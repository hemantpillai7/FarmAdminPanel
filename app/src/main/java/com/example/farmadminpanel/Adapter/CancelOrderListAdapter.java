package com.example.farmadminpanel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.farmadminpanel.Api.Constant;
import com.example.farmadminpanel.Model.ProductOrderList;
import com.example.farmadminpanel.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CancelOrderListAdapter extends RecyclerView.Adapter<CancelOrderListAdapter.ViewHolder>
{
    public Context context;
    private List<ProductOrderList> list;

    public CancelOrderListAdapter(Context context, List<ProductOrderList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customcanceledlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.productname.setText(list.get(position).getName());
        holder.username.setText(list.get(position).getUsername());
        holder.address.setText(list.get(position).getUseraddress());
        holder.number.setText(list.get(position).getUserphoneno());
        holder.productqty.setText(list.get(position).getQty());
        holder.email.setText(list.get(position).getUseremail());
        holder.date.setText(list.get(position).getDate());

        Picasso.get().load(Constant.AllProductsLINK+list.get(position).getImg_url())
                .placeholder(R.drawable.noimg)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView productname, username, address, number, productqty,email,date;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            productname=itemView.findViewById(R.id.txtCanProName);
            username=itemView.findViewById(R.id.txtCanUserName);
            address=itemView.findViewById(R.id.txtCanUserAddress);
            number=itemView.findViewById(R.id.txtCanPhno);
            productqty=itemView.findViewById(R.id.txtCanProQty);
            email=itemView.findViewById(R.id.txtCanUserEmail);
            date=itemView.findViewById(R.id.txtCanDate);
            img=itemView.findViewById(R.id.ImgCanProImg);
        }
    }
}
