package com.example.farmadminpanel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmadminpanel.Model.ServiceRequestList;
import com.example.farmadminpanel.R;

import java.util.List;

public class ServiceHistoryAdapter extends RecyclerView.Adapter<ServiceHistoryAdapter.ViewHolder>
{
    public Context context;
    private List<ServiceRequestList> list;

    public ServiceHistoryAdapter(Context context, List<ServiceRequestList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceHistoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customservicehistorylist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhoneno());
        holder.status.setText(list.get(position).getStatus());
        holder.date.setText(list.get(position).getDate());
        holder.message.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,phone,status,date,message;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.reqName);
            phone=itemView.findViewById(R.id.reqPhoneno);
            status=itemView.findViewById(R.id.reqStatus);
            date=itemView.findViewById(R.id.reqDate);
            message=itemView.findViewById(R.id.reqMsg);
        }
    }
}
