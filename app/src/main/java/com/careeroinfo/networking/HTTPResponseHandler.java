package com.careeroinfo.networking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.careeroinfo.Home;
import com.careeroinfo.SharedPreference;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPResponseHandler {

    public void analyzeResponse(Context context, Activity activity, String endpoint, String jsonString) throws JSONException {
        if(endpoint == "login"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String status = jsonObject.getString("status");
                if (status.equals("success")){
                    Log.v("http_response_login", "success");
                    SharedPreference sp = new SharedPreference(context);
                    sp.setPreference("info_nic", jsonObject.getString("info_nic"));
                    sp.setPreference("info_name", jsonObject.getString("info_name"));
                    sp.setPreference("info_email", jsonObject.getString("info_email"));
                    sp.setPreference("info_birthday", jsonObject.getString("info_birthday"));
                    sp.setPreference("info_address", jsonObject.getString("info_address"));
                    sp.setPreference("info_location_lat", jsonObject.getString("info_location_lat"));
                    sp.setPreference("info_location_lng", jsonObject.getString("info_location_lng"));
                    sp.setPreference("info_profession", jsonObject.getString("info_profession"));
                    sp.setPreference("info_education", jsonObject.getString("info_education"));
                    sp.setPreference("info_username", jsonObject.getString("info_username"));
                    sp.setPreference("info_password", jsonObject.getString("info_password"));
                    sp.setPreference("isLoggedIn", "true");
                    showResponse(context, activity, new Intent(context, Home.class), "redirect", "Login Success!", jsonObject.getString("msg"));
                } else {
                    Log.v("http_response_login", "failed");
                    showResponse(context, activity, null, "info", "Login Failed!", jsonObject.getString("msg"));
                }
            } else {
                Log.v("http_response_login", "empty");
                showResponse(context, activity, null, "error", "Login Failed!", "Error Occurred!");
            }
        } else if(endpoint == "register"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String msg = jsonObject.getString("status");
                if (msg.equals("success")){
                    Log.v("http_response_register", "success");
                    SharedPreference sp = new SharedPreference(context);
                    sp.setPreference("isLoggedIn", "true");
                    showResponse(context, activity, new Intent(context, Home.class), "redirect", "Registration Success!", jsonObject.getString("msg"));
                } else {
                    Log.v("http_response_register", "failed");
                    showResponse(context, activity, null, "info", "Registration Failed!", jsonObject.getString("msg"));
                }
            } else {
                Log.v("http_response_register", "empty");
                showResponse(context, activity, null, "error", "Registration Failed!", "Error Occurred!");
            }
        } else if(endpoint == "complaint"){
            if(jsonString != ""){
                JSONObject jsonObject = new JSONObject(jsonString);
                String msg = jsonObject.getString("status");
                if (msg.equals("success")){
                    Log.v("http_response_register", "success");
                    showResponse(context, activity, new Intent(context, Home.class), "redirect", "Complaint Submitted!", jsonObject.getString("msg"));
                } else {
                    Log.v("http_response_register", "failed");
                    showResponse(context, activity, null, "info", "Complaint Failed!", jsonObject.getString("msg"));
                }
            } else {
                Log.v("http_response_register", "empty");
                showResponse(context, activity, null, "error", "Complaint Failed!", "Error Occurred!");
            }
        }
    }

    private void showResponse(Context context, Activity activity, Intent intent, String type, String title, String subtitle){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new iOSDialogBuilder(context)
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener("Got it",new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                if(type.equals("redirect")) {
                                    context.startActivity(intent);
                                }
                            }
                        })
                        .build().show();
            }
        });
    }
}