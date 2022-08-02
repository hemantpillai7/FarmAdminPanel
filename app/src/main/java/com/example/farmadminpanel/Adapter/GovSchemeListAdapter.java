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
import com.example.farmadminpanel.Api.Constant;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.GovList;
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

public class GovSchemeListAdapter extends RecyclerView.Adapter<GovSchemeListAdapter.ViewHolder>
{
    private Context context;
    private List<GovList> list;
    public Dialog dialog;
    private Button btn_yes, btn_no;
    private TextView GovName,GovDescription,Govlink;

    public GovSchemeListAdapter(Context context, List<GovList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GovSchemeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new GovSchemeListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_govschemelist,parent,false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull GovSchemeListAdapter.ViewHolder holder, int position)
    {
        Picasso.get().load(Constant.GovSchemeLink+list.get(position).getImg_link())
                .placeholder(R.drawable.noimg)
                .into(holder.img);

        holder.name.setText(list.get(position).getName());

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
                                edit(list.get(position).getId(),list.get(position).getName(),list.get(position).getDescription(),list.get(position).getLink());
                                return true;
                            case R.id.menu_delete:
                                delete(list.get(position).getId());
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
                Call<ResponseBody> result = apiInterface.deleteGovScheme(id);
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

    private void edit(String id, String name , String description, String link)
    {
        dialog = new Dialog(context, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_edit_govscheme);
        dialog.setCanceledOnTouchOutside(true);
        GovName = dialog.findViewById(R.id.Edit_schemeName);
        GovDescription = dialog.findViewById(R.id.Edit_schemeDescription);
        Govlink = dialog.findViewById(R.id.Edit_schemeLink);

        GovName.setText(name);
        GovDescription.setText(description);
        Govlink.setText(link);

        dialog.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (GovName.getText().toString().trim().equals("") || GovDescription.getText().toString().trim().equals("") || Govlink.getText().toString().trim().equals(""))
                {
                    Toast.makeText(context, "Fill Required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
                    Call<ResponseBody> result = apiInterface.UpdateGovScheme(id, GovName.getText().toString(), GovDescription.getText().toString(), Govlink.getText().toString());
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

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background));
        dialog.show();
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView img , menu;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.schemename);
            img=itemView.findViewById(R.id.schemeimg);
            menu=itemView.findViewById(R.id.menu);
        }
    }
}
