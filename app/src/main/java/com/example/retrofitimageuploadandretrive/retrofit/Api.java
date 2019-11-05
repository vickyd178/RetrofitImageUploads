package com.example.retrofitimageuploadandretrive.retrofit;

import com.example.retrofitimageuploadandretrive.model.DefaultResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    @Multipart
    @POST("upload.php")
    Call<DefaultResponse> uploadImage(
            @Part("name") RequestBody name,
            @Part("image\"; filename=\"photo.jpg\" ")RequestBody image
    );

}
