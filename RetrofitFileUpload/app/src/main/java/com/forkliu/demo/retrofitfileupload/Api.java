package com.forkliu.demo.retrofitfileupload;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    //the base URL for our API
    //make sure you are not using localhost
    //find the ip usinc ipconfig command
    // 10.90.176.204
    // 10.6.23.13
    String BASE_URL = "http://10.90.176.204:10010/ImageUploadApi/";

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("api.php?apicall=upload")
    Call<UploadImageApiResponse> uploadImage(@Part MultipartBody.Part file, @Part("desc") RequestBody desc);

    @GET("api.php?apicall=getallimages")
    Call<UploadImageApiResponse> getAllImages();

    @GET("api.php?apicall=getimage")
    Call<UploadImageApiResponse> getImage(@Query("hash") String hash);

}
