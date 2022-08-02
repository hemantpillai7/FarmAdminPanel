package com.example.farmadminpanel.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmadminpanel.AddProducts;
import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.FileUtils;
import com.example.farmadminpanel.Api.MyValidator;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.R;
import com.example.farmadminpanel.databinding.FragmentCategoryBinding;
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

public class CategoryFragment extends Fragment
{
    FragmentCategoryBinding binding;

    private CharSequence[] options = {"camera","Gallery","Cancel"};
    int Selectphoto =1;
    String filepath="";
    File imageFile;
    Uri uri;

    String Date = new SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HHmmss",Locale.getDefault()).format(new Date());

    public CategoryFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

       binding= FragmentCategoryBinding.inflate(inflater, container, false);

       binding.ExpImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
               builder.setTitle("Select Image");
               builder.setItems(options, new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int i)
                   {
                       if(options[i].equals("camera"))
                       {
                           Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                           startActivityForResult(takepic,0);
                       }
                       else if(options[i].equals("Gallery"))
                       {
                           Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                           startActivityForResult(gallery,1);
                       }
                       else
                       {
                           dialog.dismiss();
                       }
                   }
               });
               builder.show();
           }
       });

       binding.ExpButton.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               if (validateFields())
               {
                   uploadCategory();
               }
               else
               {

               }
           }
       });

        return binding.getRoot();
    }

    private void uploadCategory()
    {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),binding.ExpName.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),binding.ExpType.getText().toString());

        imageFile = new File(filepath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part img_url = MultipartBody.Part.createFormData("img_url", imageFile.getName(), reqBody);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.exploreProduct(name,type,img_url);
        result.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try {
                    String output = response.body().string();
                    Log.e("Response", "GetResponse:-" + output);
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getString("ResponseCode").equals("1"))
                    {
                        Toast.makeText(getContext(), "Category Added Successfully", Toast.LENGTH_SHORT).show();
                        filepath="";
                        binding.ExpName.setText("");
                        binding.ExpType.setText("");
                        binding.ExpImage.setImageResource(R.drawable.noimg);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            Picasso.get().load(uri).into(binding.ExpImage);

        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        String profile = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,"Product"+Time+Date,"");

        return Uri.parse(profile);
    }

    private boolean validateFields()
    {
        boolean result = true;
        if (!MyValidator.isValidField(binding.ExpName))
        {
            result = false;
        }
        if (!MyValidator.isValidField(binding.ExpType))
        {
            result = false;
        }
        if(filepath.equals(""))
        {
            Toast.makeText(getContext(), "Upload Category Image", Toast.LENGTH_SHORT).show();

            result = false;
        }

        return result;
    }
}