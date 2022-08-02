package com.example.farmadminpanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.farmadminpanel.Api.ApiInterface;
import com.example.farmadminpanel.Api.FileUtils;
import com.example.farmadminpanel.Api.MyValidator;
import com.example.farmadminpanel.Api.Myconfig;
import com.example.farmadminpanel.databinding.ActivityAddProductsBinding;
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

public class AddProducts extends AppCompatActivity
{
    ActivityAddProductsBinding binding;

    ArrayAdapter<String>adapterItems;
    ArrayList<String> typelist = new ArrayList<String>();

    ArrayAdapter<String>adaptercategory;
    String[] category ={"Popular Products","Recommended Products","Explore Products"};

    int Selectphoto =1;
    String filepath="";
    File imageFile;
    Uri uri;

    String Date = new SimpleDateFormat("yyyymmdd", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HHmmss",Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding=ActivityAddProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        adapterItems=new ArrayAdapter<String>(this,R.layout.dropdown_item,typelist);
        binding.proType.setAdapter(adapterItems);

        adaptercategory=new ArrayAdapter<String>(this,R.layout.dropdown_item,category);
        binding.proCategory.setAdapter(adaptercategory);


        binding.proType.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        binding.proCategory.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String category = parent.getItemAtPosition(position).toString();
            }
        });


        binding.proImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Selectphoto);
            }
        });

        binding.proButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (validateFields())
                {
                    uploadproduct();
                }
                else
                {

                }
            }
        });




        getproductType();


    }

    private void uploadproduct()
    {
        String filed= binding.proCategory.getText().toString();

        if(filed.equals("Popular Products"))
        {
            uploadPopularproduct();
        }
        else if(filed.equals("Recommended Products"))
        {

            uploadRecommendedProduct();

        }
        else if(filed.equals("Explore Products"))
        {

            uploadExploreProduct();

        }

    }

    private void uploadExploreProduct()
    {
        float s = binding.starRating.getRating();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),binding.proName.getText().toString());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"),binding.proDescription.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),binding.proType.getText().toString());
        RequestBody discount = RequestBody.create(MediaType.parse("text/plain"),binding.prodiscount.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"),binding.proPrice.getText().toString());
        RequestBody rating = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(s));

        imageFile = new File(filepath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part img_url = MultipartBody.Part.createFormData("img_url", imageFile.getName(), reqBody);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.allProduct(name,
                desc,discount,type,price,rating,img_url);
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
                        Toast.makeText(AddProducts.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        binding.proName.setText("");
                        binding.proCategory.setText("");
                        binding.proType.setText("");
                        binding.proDescription.setText("");
                        binding.prodiscount.setText("");
                        binding.proPrice.setText("");
                        filepath="";
                        binding.starRating.setRating(0);
                        binding.proImage.setImageResource(R.drawable.noimg);
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
                Toast.makeText(AddProducts.this, "Failed To Upload Product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadRecommendedProduct()
    {
        float s = binding.starRating.getRating();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),binding.proName.getText().toString());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"),binding.proDescription.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),binding.proType.getText().toString());
        RequestBody discount = RequestBody.create(MediaType.parse("text/plain"),binding.prodiscount.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"),binding.proPrice.getText().toString());
        RequestBody rating = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(s));

        imageFile = new File(filepath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part img_url = MultipartBody.Part.createFormData("img_url", imageFile.getName(), reqBody);

        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.recommendedProduct(name,
                desc,discount,type,price,rating,img_url);
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
                        Toast.makeText(AddProducts.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        binding.proName.setText("");
                        binding.proCategory.setText("");
                        binding.proType.setText("");
                        binding.proDescription.setText("");
                        binding.prodiscount.setText("");
                        binding.proPrice.setText("");
                        filepath="";
                        binding.starRating.setRating(0);
                        binding.proImage.setImageResource(R.drawable.noimg);
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
                Toast.makeText(AddProducts.this, "Failed To Upload Product", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadPopularproduct()
    {
        float s = binding.starRating.getRating();
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"),binding.proName.getText().toString());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"),binding.proDescription.getText().toString());
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),binding.proType.getText().toString());
        RequestBody discount = RequestBody.create(MediaType.parse("text/plain"),binding.prodiscount.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"),binding.proPrice.getText().toString());
        RequestBody rating = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(s));

        imageFile = new File(filepath);

        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);

        MultipartBody.Part img_url = MultipartBody.Part.createFormData("img_url", imageFile.getName(), reqBody);


        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.popularProduct(name,
                desc,discount,type,price,rating,img_url);
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
                        Toast.makeText(AddProducts.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        binding.proName.setText("");
                        binding.proCategory.setText("");
                        binding.proType.setText("");
                        binding.proDescription.setText("");
                        binding.prodiscount.setText("");
                        binding.proPrice.setText("");
                        filepath="";
                        binding.starRating.setRating(0);
                        binding.proImage.setImageResource(R.drawable.noimg);
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
                Toast.makeText(AddProducts.this, "Failed To Upload Product", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getproductType()
    {
        ApiInterface apiInterface = Myconfig.getRetrofit().create(ApiInterface.class);
        Call<ResponseBody> result = apiInterface.getExploreProducts();
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
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            typelist.add(object.getString("type"));

                        }
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
                Toast.makeText(AddProducts.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== Selectphoto && resultCode == RESULT_OK && data !=null && data.getData() != null)
        {
            uri = data.getData();
            Bitmap bitmap = null;
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            filepath= FileUtils.getPath(AddProducts.this,getImageUri(this,bitmap));
            Picasso.get().load(uri).into(binding.proImage);

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
        if (!MyValidator.isValidField(binding.proName))
        {
            result = false;
        }
        if (!MyValidator.isValidAutoCompletetext(binding.proCategory))
        {
            result = false;
        }
        if (!MyValidator.isValidField(binding.proDescription))
        {
            result = false;
        }
        if (!MyValidator.isValidAutoCompletetext(binding.proType))
        {
            result = false;
        }
        if (!MyValidator.isValidateDiscount(binding.prodiscount))
        {
            result = false;
        }
        if (!MyValidator.isValidField(binding.proPrice))
        {
            result = false;
        }
        if(filepath.equals(""))
        {
            Toast.makeText(this, "Upload Product Image", Toast.LENGTH_SHORT).show();

            result = false;
        }

        return result;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}