package com.example.farmadminpanel.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.farmadminpanel.Adapter.HoneyItemAdapter;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.FileUtils;
import com.example.farmadminpanel.Api.MyValidator;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.HoneyItemList;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.databinding.FragmentHoneyBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoneyFragment extends Fragment
{
    FragmentHoneyBinding binding;
    ArrayList<HoneyItemList> honeyItemArrayList = new ArrayList<HoneyItemList>();
    HoneyItemAdapter adapter;
    private Dialog mdialog;
    private EditText Itemname, Itemrate;
    AutoCompleteTextView Itemtype;
    ImageView Itemimage;

    ProgressDialog dialog;

    ArrayAdapter<String> honeyCategoryAdapter;
    String[] category ={"HoneyBee","Cluster","BeeHive"};

    private CharSequence[] options = {"camera","Gallery","Cancel"};
    int Selectphoto =1;
    String filepath="";
    File imageFile;
    Uri uri;

    String Date = new SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HHmmss",Locale.getDefault()).format(new Date());

    public HoneyFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding=FragmentHoneyBinding.inflate(inflater, container, false);

        honeyCategoryAdapter=new ArrayAdapter<String>(getContext(),R.layout.dropdown_item,category);
        honeyCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                binding.floatingActionButton.animate().rotationBy(180).setListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animator)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator)
                    {
                        add();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        });


        honeyitemListing();
        return binding.getRoot();
    }

    private void add()
    {
        mdialog = new Dialog(getContext(), android.R.style.Theme_Dialog);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.alert_add_honey_item);
        mdialog.setCanceledOnTouchOutside(true);

        Itemimage = mdialog.findViewById(R.id.Img_AddHoneyImg);
        Itemname = mdialog.findViewById(R.id.Edit_AddHoneyName);
        Itemrate = mdialog.findViewById(R.id.Edit_AddHoneyRate);
        Itemtype = mdialog.findViewById(R.id.Edit_AddHoneyType);
        Itemtype.setAdapter(honeyCategoryAdapter);

        mdialog.findViewById(R.id.Img_AddHoneyImg).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Selectphoto);
            }
        });

        mdialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });

        mdialog.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (validateFields())
                {
                    uploadHoneyItem();
                }
                else
                {

                }

            }


        });
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mdialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.dialog_background));
        mdialog.show();

    }

    private void uploadHoneyItem()
    {
        dialog= new ProgressDialog(getContext());
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),Itemname.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),Itemtype.getText().toString());
        RequestBody rate = RequestBody.create(MediaType.parse("text/plain"),Itemrate.getText().toString());

        imageFile = new File(filepath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part honey_img = MultipartBody.Part.createFormData("honey_img", imageFile.getName(), reqBody);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.addHoneyItem(name,rate,type,honey_img);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                dialog.dismiss();
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("ResponseCode").equals("1"))
                    {
                        mdialog.dismiss();
                        Toast.makeText(getContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show();
                        honeyitemListing();
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

    private void honeyitemListing()
    {
        dialog= new ProgressDialog(getContext());
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
            Call<ResponseBody> result =apiInterface.getHoneyproduct();
            result.enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    dialog.dismiss();
                    try {
                        String output = response.body().string();
                        Log.d("Response", "Videos:-" + output);
                        JSONObject jsonObject = new JSONObject(output);

                        if (jsonObject.getString("ResponseCode").equals("1"))
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                honeyItemArrayList.add(new HoneyItemList(object));
                                Log.e("Honey",""+honeyItemArrayList);
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
                            binding.RecHoney.setLayoutManager(gridLayoutManager);
                            adapter = new HoneyItemAdapter(getContext(),honeyItemArrayList);
                            binding.RecHoney.setAdapter(adapter);

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED)
        {
            switch (requestCode)
            {
                case 0:
                    if(resultCode == RESULT_OK && data !=null)
                    {
                        Bitmap image =(Bitmap) data.getExtras().get("data");
                        filepath = FileUtils.getPath(getContext(),getImageUri(getContext(),image));
                        ImageView img =mdialog.findViewById(R.id.Img_AddHoneyImg);
                        img.setImageBitmap(image);
                    }
                    break;

                case 1:
                {
                    if(resultCode == RESULT_OK && data !=null)
                    {
                        uri = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        filepath= FileUtils.getPath(getContext(),getImageUri(getContext(),bitmap));
                        ImageView img =mdialog.findViewById(R.id.Img_AddHoneyImg);
                        Picasso.get().load(uri).into(img);

                    }
                }
            }

        }
    }


    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        String profile = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"HoneyItem"+Time+Date,"");

        return Uri.parse(profile);
    }

    private boolean validateFields()
    {
        boolean result = true;
        if (!MyValidator.isValidField(Itemname))
        {
            result = false;
        }
        if (!MyValidator.isValidField(Itemrate))
        {
            result = false;
        }
        if (!MyValidator.isValidField(Itemtype))
        {
            result = false;
        }
        if(filepath.equals(""))
        {
            Toast.makeText(getContext(), "Upload Image", Toast.LENGTH_SHORT).show();

            result = false;
        }

        return result;
    }
}