package com.careeroinfo.networking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.careeroinfo.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class File_Upload {

    static String SERVER_PATH = "";

    public void upload(Context context, String cv_filePath, String coverletter_filePath, String certificates_filePath, String email){
        SharedPreference sp = new SharedPreference(context);
        SERVER_PATH = sp.getPreference("ServerHost")+"/";

        // Map is used to multipart the file using okhttp3.RequestBody
        Map<String, RequestBody> map = new HashMap<>();

        String cv = "false";
        String cl = "false";
        String c = "false";

        // Parsing any Media type file
        if(cv_filePath != "") {
            File resume = new File(cv_filePath);
            RequestBody cvBody = RequestBody.create(MediaType.parse("*/*"), resume);
            map.put("cv\"; filename=\"" + resume.getName() + "\"", cvBody);
            cv = "true";
        }
        if(coverletter_filePath != "") {
            File coverletter = new File(coverletter_filePath);
            RequestBody coverletterBody = RequestBody.create(MediaType.parse("*/*"), coverletter);
            map.put("cl\"; filename=\"" + coverletter.getName() + "\"", coverletterBody);
            cl = "true";
        }
        if(certificates_filePath != "") {
            File certificates = new File(certificates_filePath);
            RequestBody certificatesBody = RequestBody.create(MediaType.parse("*/*"), certificates);
            map.put("c\"; filename=\"" + certificates.getName() + "\"", certificatesBody);
            c = "true";
        }
        map.put("function", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "fileUpload"));
        map.put("email", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), email));
        map.put("files", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), cv+","+cl+","+c));
        Log.d("FileUpload", "Before the AppConfig.getRetrofit in testUpload.java");
        ApiConfig getResponse = AppConfig.getRetrofit(SERVER_PATH).create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.upload("token", map);
        Log.d("FileUpload", "Before the call.enqueue in testUpload.java");
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.e("res", response.toString());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        ServerResponse serverResponse = response.body();
                        Log.d("FileUpload", "Line 63 in File_Upload.java");
                        Log.d("FileUpload", "getMessage: "+serverResponse.getMessage());
                        Log.d("FileUpload", "getSuccess: "+serverResponse.getSuccess());
                        Toast.makeText(context, "Files uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Problem uploading the file", Toast.LENGTH_SHORT).show();
                    Log.d("FileUpload", "Line 75 in testUpload.java");
                    Log.d("FileUpload", "Problem uploading the file");
                    Log.d("FileUpload", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.v("Response gotten is", t.getMessage());
                Toast.makeText(context, "Problem uploading the file " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("FileUpload", "Line 89 in File_Upload.java");
                Log.d("FileUpload", "Problem uploading the file " + t.getMessage());
            }
        });
    }
}