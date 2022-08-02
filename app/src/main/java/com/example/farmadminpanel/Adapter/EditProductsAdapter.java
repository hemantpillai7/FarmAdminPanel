package com.example.farmadminpanel.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.example.farmadminpanel.Model.EditProductList;
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

public class EditProductsAdapter extends RecyclerView.Adapter<EditProductsAdapter.ViewHolder>
{

    public Context context;
    private List<EditProductList>list;

    ProgressDialog dialog;

    public Dialog mdialog;
    private Button btn_yes, btn_no;
    private TextView ProName,ProDescription,ProDiscount,ProPrice;


    public EditProductsAdapter(Context context, List<EditProductList> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_productlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Picasso.get().load(Constant.AllProductsLINK+list.get(position).getImg_url())
                .placeholder(R.drawable.noimg)
                .into(holder.itemImg);
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice()+"/-");

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
                                edit(list.get(position).getId(),list.get(position).getName(),list.get(position).getDescription(),list.get(position).getDiscount(),
                                       list.get(position).getPrice(),list.get(position).getImg_url());
                                return true;
                            case R.id.menu_delete:
                                delete(list.get(position).getId(),list.get(position).getName());
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

    private void edit(String id, String name, String description, String discount, String price,String imgurl)
    {
        mdialog = new Dialog(context, android.R.style.Theme_Dialog);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.custom_dialog_edit_totalproducts);
        mdialog.setCanceledOnTouchOutside(true);
        ProName = mdialog.findViewById(R.id.Edit_productName);
        ProDescription = mdialog.findViewById(R.id.Edit_productDescription);
        ProDiscount = mdialog.findViewById(R.id.Edit_productdiscount);
        ProPrice= mdialog.findViewById(R.id.Edit_productprice);

        ProName.setText(name);
        ProDescription.setText(description);
        ProDiscount.setText(discount);
        ProPrice.setText(price);

        mdialog.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (ProName.getText().toString().trim().equals("") || ProDescription.getText().toString().trim().equals("") || ProDiscount.getText().toString().trim().equals("") || ProPrice.getText().toString().trim().equals(""))
                {
                    Toast.makeText(context, "Fill Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mdialog.dismiss();

                    dialog= new ProgressDialog(context);
                    dialog.show();
                    dialog.setContentView(R.layout.progressbar);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    ApiInterface apiInterface= Myconfig.getRetrofit().create(ApiInterface.class);
                    Call<ResponseBody> result = apiInterface.UpdateAllProducts(id,ProName.getText().toString(),ProDescription.getText().toString(),ProDiscount.getText().toString(),ProPrice.getText().toString(),imgurl);
                    result.enqueue(new Callback<ResponseBody>()
                    {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                        {
                            dialog.show();
                            try {
                                String output = response.body().string();
                                JSONObject jsonObject = new JSONObject(output);
                                if (jsonObject.getString("ResponseCode").equals("1"))
                                {
                                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                                mdialog.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }

                    });
                }

            }
        });

        mdialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });

        mdialog.setCanceledOnTouchOutside(false);
        mdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mdialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_background));
        mdialog.show();
    }

    private void delete(String id, String name)
    {
        mdialog = new Dialog(context);
        mdialog.setCancelable(false);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setContentView(R.layout.alert_dialog2);
        btn_yes = mdialog.findViewById(R.id.btn_logout_yes);
        btn_no = mdialog.findViewById(R.id.btn_logout_no);
        btn_yes.setText("Delete");
        btn_no.setText("Cancel");
        TextView text_msg = (TextView) mdialog.findViewById(R.id.text_msg);
        ImageView iv_image = (ImageView) mdialog.findViewById(R.id.iv_image);
        TextView text = (TextView) mdialog.findViewById(R.id.text);
        text_msg.setText("Are you sure you want to Delete");
        text.setText("Delete...!");
        btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog= new ProgressDialog(context);
                dialog.show();
                dialog.setContentView(R.layout.progressbar);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
                Log.e("abc", ""+id +" and " +name);

                Call<ResponseBody> result = apiInterface.deleteTotalproduct(id, name);
                result.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        dialog.show();
//                        Toast.makeText(context, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                        try {
                            String output = response.body().string();
                            JSONObject jsonObject = new JSONObject(output);
                            if (jsonObject.getString("ResponseCode").equals("1"))
                            {
                                //list.remove(list.get(position));
                                Toast.makeText(context, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        Toast.makeText(context, "Please Check Your Network", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
            }
        });
        mdialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, price;
        ImageView itemImg, menu;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.itemName);
            price=itemView.findViewById(R.id.itemPrice);
            itemImg=itemView.findViewById(R.id.itemImg);
            menu=itemView.findViewById(R.id.menu);
        }
    }
}
