package com.example.retrofitimageuploadandretrive.views.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.retrofitimageuploadandretrive.model.DefaultResponse;
import com.example.retrofitimageuploadandretrive.retrofit.RetrofitClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void uploadImage(RequestBody name,RequestBody imageUrl){
        view.showLoading();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().uploadImage(name,imageUrl);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NonNull Call<DefaultResponse> call,@NonNull Response<DefaultResponse> response) {
                view.hideLoading();
                if (response.isSuccessful()){
                    view.onErrorMessage(response.body().getMessage());
                    if (!response.body().isError()){
                        view.onSuccessfulUpload();
                    }
                }else{
                    view.onErrorMessage(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onErrorMessage(t.getLocalizedMessage());

            }
        });
    }

}
