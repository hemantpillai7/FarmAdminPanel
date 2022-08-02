package com.example.farmadminpanel.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.farmadminpanel.Adapter.AdvertismnetAdapter;
import com.example.farmadminpanel.AddProducts;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.Constant;
import com.example.farmadminpanel.Api.FileUtils;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.AdvertismentList;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.databinding.FragmentAdvertismentBinding;
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

public class AdvertismentFragment extends Fragment
{
    FragmentAdvertismentBinding binding;
    AdvertismnetAdapter adapter;

    ProgressDialog dialog;

    ArrayList<AdvertismentList> list = new ArrayList<AdvertismentList>();

    int Selectphoto =1;
    String filepath="";
    File imageFile;
    Uri uri;

    String Date = new SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HHmmss",Locale.getDefault()).format(new Date());

    public AdvertismentFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding= FragmentAdvertismentBinding.inflate(inflater, container, false);

        binding.advImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Selectphoto);
            }
        });

        binding.advButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                uploadAdvertisment();
            }
        });

        loadAdvertsiment();

        return binding.getRoot();
    }

    private void uploadAdvertisment()
    {
        if(filepath.equals(""))
        {
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();

        }
        else
        {
            dialog= new ProgressDialog(getContext());
            dialog.show();
            dialog.setContentView(R.layout.progressbar);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            imageFile = new File(filepath);
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
            MultipartBody.Part sliderImage = MultipartBody.Part.createFormData("sliderImage", imageFile.getName(), reqBody);
            ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);

            Call<ResponseBody> result = apiInterface.uploadadevertisment(sliderImage);
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
                            Toast.makeText(getContext(), "Advertisment Added Successfully", Toast.LENGTH_SHORT).show();
                            filepath="";
                            binding.advImg.setImageResource(R.drawable.noimg);

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
    }

    private void loadAdvertsiment()
    {
        dialog= new ProgressDialog(getContext());
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getSlider();
        result.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("ResponseCode").equals("1"))
                    {
                        dialog.dismiss();

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            list.add(new AdvertismentList(object));
                        }
                    binding.adverRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
                    adapter=new AdvertismnetAdapter(getContext(),list);
                    binding.adverRec.setAdapter(adapter);
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

        if(requestCode== Selectphoto && resultCode == RESULT_OK && data !=null && data.getData() != null)
        {
            uri = data.getData();
            Bitmap bitmap = null;
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            filepath= FileUtils.getPath(getContext(),getImageUri(getContext(),bitmap));
            Picasso.get().load(uri).into(binding.advImg);

        }
    }


    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        String profile = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"Advertise"+Time+Date,"");
        return Uri.parse(profile);
    }





}