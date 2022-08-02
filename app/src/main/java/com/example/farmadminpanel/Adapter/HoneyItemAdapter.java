package com.example.farmadminpanel.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.AppConfig;
import com.example.farmadminpanel.Api.Constant;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Fragment.HoneyFragment;
import com.example.farmadminpanel.MainActivity;
import com.example.farmadminpanel.Model.HoneyItemList;
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

public class HoneyItemAdapter extends RecyclerView.Adapter<HoneyItemAdapter.ViewHolder>
{
    private Context context;
    private List<HoneyItemList> honeyItemList;
    public Dialog dialog;
    private Button btn_yes, btn_no;
    private TextView Itemname,Itemrate;

    public HoneyItemAdapter(Context context, List<HoneyItemList> honeyItemList)
    {
        this.context = context;
        this.honeyItemList = honeyItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customhoneyitem, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        holder.name.setText(honeyItemList.get(position).getHoneyname());
        holder.rate.setText("Rs "+honeyItemList.get(position).getHoneyprice()+"/-");
        Picasso.get().load(Constant.HoneyProductLink+honeyItemList.get(position).getHoney_img())
                .placeholder(R.drawable.noimg)
                .into(holder.imageView);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view)
            {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(context);
                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.options_edit_menu, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, holder.menu);
                optionsMenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                edit(honeyItemList.get(position).getId(),honeyItemList.get(position).getHoneyname(),honeyItemList.get(position).getHoneyprice());

                                return true;
                            case R.id.menu_delete:
                                delete(honeyItemList.get(position).getId());
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {
                    }
                });


                // Display the menu
                optionsMenu.show();
            }


        });
    }

    private void delete(String id)
    {
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog2);
        btn_yes = dialog.findViewById(R.id.btn_logout_yes);
        btn_no = dialog.findViewById(R.id.btn_logout_no);
        btn_yes.setText("Delete");
        btn_no.setText("Cancel");
        TextView text_msg = (TextView) dialog.findViewById(R.id.text_msg);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_image);
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text_msg.setText("Are you sure you want to Delete");
        text.setText("Delete...!");
        btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
                Call<ResponseBody> result = apiInterface.deleteHoneyItem(id);
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

                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                            dialog.dismiss();
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
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void edit(String id, String name , String rate)
    {
        dialog = new Dialog(context, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_edit_honey);
        dialog.setCanceledOnTouchOutside(true);
        Itemname = dialog.findViewById(R.id.Edit_honeyName);
        Itemrate = dialog.findViewById(R.id.Edit_honeyRate);
        Itemname.setText(name);
        Itemrate.setText(rate);

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Itemname.getText().toString().trim().equals("") || Itemrate.getText().toString().trim().equals(""))
                {
                    Toast.makeText(context, "Fill Required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
                    Call<ResponseBody> result = apiInterface.updateHoneyItem(id, Itemname.getText().toString(), Itemrate.getText().toString());
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
                                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                }
                                dialog.dismiss();
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

            }


        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background));
        dialog.show();

    }


    @Override
    public int getItemCount()
    {
        return honeyItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView,menu;
        TextView name, rate;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView=itemView.findViewById(R.id.honeyimage);
            menu=itemView.findViewById(R.id.menu);
            name=itemView.findViewById(R.id.honeyname);
            rate=itemView.findViewById(R.id.honeyRate);
        }
    }
}
