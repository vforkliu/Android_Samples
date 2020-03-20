package com.forkliu.demo.retrofitfileupload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        findViewById(R.id.btn_getallimages).setOnClickListener(this);
        findViewById(R.id.btn_getimage).setOnClickListener(this);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, 100);
                String path = "/sdcard/Download/images/test.png";
                // String path = "/sdcard/images/test.png";
                uploadFile(path,"desc");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();

            //calling the upload file method after choosing the file
            uploadFile(selectedImage, "My Image");

        }
    }

    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void uploadFile(String path,String desc){
        File file = new File(path);
        Log.d("Demo","file size:" + file.length());
        Uri uri = Uri.fromFile(file);
        uploadFile(uri,desc);
    }
    private void uploadFile(Uri fileUri, String desc) {

        //creating a file
        // File file = new File(getRealPathFromURI(fileUri));
        File file = new File(fileUri.getPath());
        // File file = new File("/sdcard/Download/Screenshots/Screenshot_20200120-193921.png");

        File dir = Environment.getExternalStorageDirectory();
        Log.e("Demo","external dir:" + dir.getAbsolutePath());
        String mimeType = null;
        try {
            Uri uri = FileProvider.getUriForFile(this, "com.forkliu.demo.retrofitfileupload.fileprovider", file);
            mimeType = getContentResolver().getType(uri);
        }catch (Exception e){
            Log.e("Demo",e.getMessage());
            e.printStackTrace();
        }
        //creating request body for file

//        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUri.getPath());
//        if (extension != null) {
//            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<UploadImageApiResponse> call = api.uploadImage(fileToUpload, descBody);

        //finally performing the call
        call.enqueue(new Callback<UploadImageApiResponse>() {
            @Override
            public void onResponse(Call<UploadImageApiResponse> call, Response<UploadImageApiResponse> response) {
                UploadImageApiResponse body = response.body();
                if (body == null){
                    String message = response.toString();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }else {
                    if (!body.error) {
                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadImageApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_getallimages:
                getAllImages();
                break;
            case R.id.btn_getimage:
                getImage();
                break;
            default:
                break;
        }
    }

    private void getImage(){
        // b193f8eedfa8e747811e2399ab133e22
        String hash = "b193f8eedfa8e747811e2399ab133e22";
        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<UploadImageApiResponse> call = api.getImage(hash);

        //finally performing the call
        call.enqueue(new Callback<UploadImageApiResponse>() {
            @Override
            public void onResponse(Call<UploadImageApiResponse> call, Response<UploadImageApiResponse> response) {
                UploadImageApiResponse body = response.body();
                if (body != null){
                    if (!body.error) {
                        Toast.makeText(getApplicationContext(), body.message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                    }
                }else{
                    String message = response.toString();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadImageApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllImages(){
//The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<UploadImageApiResponse> call = api.getAllImages();

        //finally performing the call
        call.enqueue(new Callback<UploadImageApiResponse>() {
            @Override
            public void onResponse(Call<UploadImageApiResponse> call, Response<UploadImageApiResponse> response) {
                UploadImageApiResponse body = response.body();
                if (body != null){
                    if (!body.error) {
                        Toast.makeText(getApplicationContext(), body.message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), body.message, Toast.LENGTH_LONG).show();
                    }
                }else{
                    String message = response.toString();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadImageApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
