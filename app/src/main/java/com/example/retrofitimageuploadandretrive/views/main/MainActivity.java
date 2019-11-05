package com.example.retrofitimageuploadandretrive.views.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofitimageuploadandretrive.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CODE = 111;

    private Button btnUpload;
    private EditText inputChoose;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri mImageUri;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpload = findViewById(R.id.btnUpload);
        inputChoose = findViewById(R.id.inputChooseImage);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        presenter = new MainPresenter(this);

        inputChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mImageUri!=null)
                presenter.uploadImage(getStringBody("ImageName"),getFileBody(mImageUri));

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imageView);

        }
    }

    private void openFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_IMAGE_CODE);

    }

    private RequestBody getFileBody(Uri uri){
        File file = new File(getRealPathFromURI(uri));
        return RequestBody.create(MediaType.parse(Objects.requireNonNull(getContentResolver().getType(uri))), file);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(MainActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private RequestBody getStringBody(String str){
        return RequestBody.create(MediaType.parse("text/plain"), str);
    }


    @Override
    public void onSuccessfulUpload() {
        Log.e(TAG, "onSuccessfulUpload: ");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
