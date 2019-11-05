package com.example.retrofitimageuploadandretrive.views.main;

public interface MainView {
    void onSuccessfulUpload();
    void showLoading();
    void hideLoading();
    void onErrorMessage(String message);


}
