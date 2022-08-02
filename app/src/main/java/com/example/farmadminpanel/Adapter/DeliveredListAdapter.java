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

public class DeliveredListAdapter extends RecyclerView.Adapter<DeliveredListAdapter.ViewHolder>
{
    public Context context;
    private List<ProductOrderList> list;

    public DeliveredListAdapter(Context context, List<ProductOrderList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DeliveredListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customdeliveredlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveredListAdapter.ViewHolder holder, int position)
    {
        holder.productname.setText(list.get(position).getName());
        holder.username.setText(list.get(position).getUsername());
        holder.address.setText(list.get(position).getUseraddress());
        holder.number.setText(list.get(position).getUserphoneno());
        holder.productqty.setText(list.get(position).getQty());
        holder.date.setText(list.get(position).getDate());
        holder.email.setText(list.get(position).getUseremail());

        Picasso.get().load(Constant.AllProductsLINK+list.get(position).getImg_url())
                .placeholder(R.drawable.noimg)
                .into(holder.img);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView productname, username, address, number, productqty,email,date;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productname=itemView.findViewById(R.id.txtDelProName);
            username=itemView.findViewById(R.id.txtDelUserName);
            address=itemView.findViewById(R.id.txtDelUserAddress);
            number=itemView.findViewById(R.id.txtDelPhno);
            productqty=itemView.findViewById(R.id.txtDelProQty);
            email=itemView.findViewById(R.id.txtDelUserEmail);
            date=itemView.findViewById(R.id.txtDelDate);
            img=itemView.findViewById(R.id.ImgDelProImg);
        }
    }
}
