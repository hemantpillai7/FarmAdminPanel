package com.example.farmadminpanel.Api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface
{

    @GET("User/getExploreProducts.php")
    Call<ResponseBody>getExploreProducts();

    @Multipart
    @POST("Vendor/PopularProduct.php")
    Call<ResponseBody> popularProduct(@Part("name") RequestBody name,
                                      @Part("description")  RequestBody  description,
                                      @Part("discount") RequestBody  discount,
                                      @Part("type") RequestBody  type,
                                      @Part("price") RequestBody  price,
                                      @Part("rating") RequestBody  rating,
                                      @Part MultipartBody.Part img_url);

    @Multipart
    @POST("Vendor/RecommendedProduct.php")
    Call<ResponseBody> recommendedProduct(@Part("name") RequestBody name,
                                      @Part("description")  RequestBody  description,
                                      @Part("discount") RequestBody  discount,
                                      @Part("type") RequestBody  type,
                                      @Part("price") RequestBody  price,
                                      @Part("rating") RequestBody  rating,
                                      @Part MultipartBody.Part img_url);

    @Multipart
    @POST("Vendor/AllProduct.php")
    Call<ResponseBody> allProduct(@Part("name") RequestBody name,
                                          @Part("description")  RequestBody  description,
                                          @Part("discount") RequestBody  discount,
                                          @Part("type") RequestBody  type,
                                          @Part("price") RequestBody  price,
                                          @Part("rating") RequestBody  rating,
                                          @Part MultipartBody.Part img_url);

    @Multipart
    @POST("Vendor/ExploreProduct.php")
    Call<ResponseBody> exploreProduct(@Part("name") RequestBody name,
                                  @Part("type") RequestBody  type,
                                  @Part MultipartBody.Part img_url);

    @GET("User/getSlider.php")
    Call<ResponseBody>getSlider();

    @FormUrlEncoded
    @POST("Vendor/deleteSlider.php")
    Call<ResponseBody> deleteAdvertisment(@Field("id") String id);

    @Multipart
    @POST("Vendor/SliderImage.php")
    Call<ResponseBody> uploadadevertisment(@Part MultipartBody.Part sliderImage);

    @GET("User/getGardenAdvertisment.php")
    Call<ResponseBody>getgardenAdvertisment();

    @FormUrlEncoded
    @POST("Vendor/DeleteGardenAdvertisment.php")
    Call<ResponseBody>deleteGardenAdvertisment(@Field("id") String id);

    @Multipart
    @POST("Vendor/UploadGardenAdvertisment.php")
    Call<ResponseBody> uploadgardenAdvertisment(@Part MultipartBody.Part gardenimg_url);

    @GET("Vendor/getRegisteredUser.php")
    Call<ResponseBody>getUserDetails();

    @FormUrlEncoded
    @POST("Vendor/deleteUser.php")
    Call<ResponseBody> deleteUser(@Field("userid") String userid);

    @GET("User/getTotalProducts.php")
    Call<ResponseBody>getTotalProducts();

    @Multipart
    @POST("Vendor/InsertVideo.php")
    Call<ResponseBody> insertVideo(@Part("name") RequestBody name,
                                      @Part("link") RequestBody  link,
                                      @Part MultipartBody.Part img_url);

    @FormUrlEncoded
    @POST("Vendor/deleteTotalProducts.php")
    Call<ResponseBody>deleteTotalproduct(@Field("id") String id,
                                   @Field("name") String name);

    @FormUrlEncoded
    @POST("Vendor/UpdateAllProducts.php")
    Call<ResponseBody>UpdateAllProducts(@Field("id") String id,
                                         @Field("name") String name,
                                        @Field("description") String description,
                                        @Field("discount") String discount,
                                        @Field("price") String price,
                                        @Field("img_url") String img_url);


    @GET("Vendor/getOrderList.php")
    Call<ResponseBody>getOrderList();

    @GET("Vendor/getOrderListByDate.php")
    Call<ResponseBody>getOrderListByDate();


    @FormUrlEncoded
    @POST("Vendor/OrderAcceptStatus.php")
    Call<ResponseBody>orderaccept(@Field("id") String id,
                                  @Field("userid") String userid,
                                  @Field("status") String status);


    @FormUrlEncoded
    @POST("Vendor/OrderRejectedStatus.php")
    Call<ResponseBody>orderrejected(@Field("id") String id,
                                  @Field("userid") String userid,
                                  @Field("status") String status);

    @FormUrlEncoded
    @POST("Vendor/OrderDeliveredStatus.php")
    Call<ResponseBody>orderdelivered(@Field("id") String id,
                                  @Field("userid") String userid,
                                  @Field("status") String status,
                                     @Field("date") String date);




    @GET("Vendor/getTotalServiceRequest.php")
    Call<ResponseBody>getTotalserviceRequest();

    @FormUrlEncoded
    @POST("Vendor/UpdateServiceRequestStatus.php")
    Call<ResponseBody>updateservicestatus(@Field("id") String id,
                                     @Field("status") String status,
                                     @Field("requesttype") String requesttype);

    @GET("Vendor/getSoilServiceRequest.php")
    Call<ResponseBody>getSoilserviceRequest();

    @FormUrlEncoded
    @POST("Vendor/UpdateSoilServiceRequestStatus.php")
    Call<ResponseBody>updateSoilservicestatus(@Field("id") String id,
                                          @Field("status") String status,
                                          @Field("date") String date);

    @GET("User/getHoneyproductlist.php")
    Call<ResponseBody>getHoneyproduct();

    @FormUrlEncoded
    @POST("Vendor/UpdateHoneyItem.php")
    Call<ResponseBody>updateHoneyItem(@Field("id") String id,
                                      @Field("honeyname") String honeyname,
                                      @Field("honeyprice") String honeyprice);

    @FormUrlEncoded
    @POST("Vendor/deleteHoneyItem.php")
    Call<ResponseBody>deleteHoneyItem(@Field("id") String id);

    @Multipart
    @POST("Vendor/AddHoneyItem.php")
    Call<ResponseBody> addHoneyItem(@Part("honeyname") RequestBody  honeyname,
                                     @Part("honeyprice")  RequestBody  honeyprice,
                                     @Part("honeytype") RequestBody  honeytype,
                                     @Part MultipartBody.Part honey_img);

    @GET("User/getGovScheme.php")
    Call<ResponseBody>getGovSchemes();

    @FormUrlEncoded
    @POST("Vendor/deleteGovScheme.php")
    Call<ResponseBody>deleteGovScheme(@Field("id") String id);

    @FormUrlEncoded
    @POST("Vendor/UpdateGovScheme.php")
    Call<ResponseBody>UpdateGovScheme(@Field("id") String id,
                                      @Field("name") String name,
                                      @Field("description") String description,
                                      @Field("link") String link);

    @Multipart
    @POST("Vendor/AddGovScheme.php")
    Call<ResponseBody> AddGovScheme(@Part("name") RequestBody  name,
                                    @Part("link")  RequestBody  link,
                                    @Part("description") RequestBody  description,
                                    @Part MultipartBody.Part img_link);
}
