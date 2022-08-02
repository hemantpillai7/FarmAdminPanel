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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.FileUtils;
import com.example.farmadminpanel.Api.MyValidator;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.databinding.FragmentVideoBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment
{

    FragmentVideoBinding binding;
    int Selectphoto =1;
    String filepath="";
    File imageFile;
    Uri uri;

    ProgressDialog dialog;

    String Date = new SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HHmmss",Locale.getDefault()).format(new Date());
    public VideoFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding=FragmentVideoBinding.inflate(inflater, container, false);

        binding.vdoImgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Selectphoto);
            }
        });

        binding.vdoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (validateFields())
                {
                    uploadVideo();
                }
                else
                {

                }
            }
        });

        return binding.getRoot();
    }

    private void uploadVideo()
    {
        dialog= new ProgressDialog(getContext());
        dialog.show();
        dialog.setContentView(R.layout.progressbar);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),binding.vdoName.getText().toString());
        RequestBody link = RequestBody.create(MediaType.parse("text/plain"),binding.vdoLnk.getText().toString());

        imageFile = new File(filepath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part img_url = MultipartBody.Part.createFormData("img_url", imageFile.getName(), reqBody);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.insertVideo(name,link,img_url);
        result.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                dialog.dismiss();
                try {
                    String output = response.body().string();
                    Log.e("Response", "GetResponse:-" + output);
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("ResponseCode").equals("1"))
                    {
                        Toast.makeText(getContext(), "Video Added Successfully", Toast.LENGTH_SHORT).show();
                        filepath="";
                        binding.vdoName.setText("");
                        binding.vdoLnk.setText("");
                        binding.vdoImg.setImageResource(R.drawable.noimg);
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
            Picasso.get().load(uri).into(binding.vdoImg);

        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        String profile = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"Video"+Time+Date,"");

        return Uri.parse(profile);
    }

    private boolean validateFields()
    {
        boolean result = true;
        if (!MyValidator.isValidField(binding.vdoName))
        {
            result = false;
        }
        if (!MyValidator.isValidField(binding.vdoLnk))
        {
            result = false;
        }
        if(filepath.equals(""))
        {
            Toast.makeText(getContext(), "Please Upload Thumbnail", Toast.LENGTH_SHORT).show();

            result = false;
        }

        return result;
    }
}