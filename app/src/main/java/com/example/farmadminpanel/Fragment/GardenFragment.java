package com.example.farmadminpanel.Fragment;

import static android.app.Activity.RESULT_OK;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmadminpanel.Adapter.AdvertismnetAdapter;
import com.example.farmadminpanel.Adapter.GardenAdvAdapter;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.FileUtils;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.Model.AdvertismentList;
import com.example.farmadminpanel.Model.GardenAdverList;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.databinding.FragmentGardenBinding;
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

public class GardenFragment extends Fragment
{
    FragmentGardenBinding binding;

    ProgressDialog dialog;

    GardenAdvAdapter adapter;
    ArrayList<GardenAdverList> list = new ArrayList<GardenAdverList>();

    int Selectphoto =1;
    String filepath="";
    File imageFile;
    Uri uri;

    String Date = new SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HHmmss",Locale.getDefault()).format(new Date());

    public GardenFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=FragmentGardenBinding.inflate(inflater, container, false);


        binding.ImgGardenAdver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Selectphoto);
            }
        });

        binding.BtnGardenAdver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                uploadAdvertisment();
            }
        });

        loadGardenAdvertisment();

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
            MultipartBody.Part gardenimg_url = MultipartBody.Part.createFormData("gardenimg_url", imageFile.getName(), reqBody);
            ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);

            Call<ResponseBody> result = apiInterface.uploadgardenAdvertisment(gardenimg_url);
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
                            binding.ImgGardenAdver.setImageResource(R.drawable.noimg);

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
                    Toast.makeText(getContext(), "Failed to upload advertisment", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadGardenAdvertisment()
    {
        dialog= new ProgressDialog(getContext());
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getgardenAdvertisment();
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
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            list.add(new GardenAdverList(object));
                        }
                        binding.RecGardenAdver.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
                        adapter=new GardenAdvAdapter(getContext(),list);
                        binding.RecGardenAdver.setAdapter(adapter);
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
            Picasso.get().load(uri).into(binding.ImgGardenAdver);

        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        String profile = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"GardenAdv"+Time+Date,"");
        return Uri.parse(profile);
    }
}