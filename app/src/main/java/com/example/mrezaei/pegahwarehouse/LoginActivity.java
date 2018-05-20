package com.example.mrezaei.pegahwarehouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    CheckBox check_show_Password;
    EditText edt_user_name, edt_Password;
    Button btn_log_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init_widgets();

        check_show_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_show_Password.isChecked()) {
                    edt_Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt_Password.setSelection(edt_Password.length());
                } else {
                    edt_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_Password.setSelection(edt_Password.length());
                }
            }
        });
    }

    private void init_widgets() {
        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        edt_Password = (EditText) findViewById(R.id.edt_password);
        check_show_Password = (CheckBox) findViewById(R.id.check_show_password);
        btn_log_in = (Button) findViewById(R.id.btn_log_in);
    }

    public void log_in_clicked(View view) {
        sendUserInfoToServer();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("خروج از برنامه")
                .setMessage("آیا مایل به خروج از برنامه هستید؟")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    private void sendUserInfoToServer(){
        final String strUserName, strPassword;

        strUserName = edt_user_name.getText().toString().trim();
        strPassword = edt_Password.getText().toString().trim();

//        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, strSignupUrl,null, Listener,  errorListener);
//        requestQueue.add(jsObjRequest);

        try {
            JSONObject request = new JSONObject();
            request.put("apiVersion", "1");
            request.put("appVersion", "1");
            request.put("appType", "mobile");
            request.put("userName", strUserName);
            request.put("password", strPassword);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            queue.getCache().clear();

            Response.Listener<JSONObject> Listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        boolean boolStatus = jsonObject.getBoolean("status");
//                                        JSONArray errorsArray = jsonObject.getJSONArray("errors");
                        if (boolStatus == true) {
                            Toast.makeText(getApplicationContext(), R.string.Successfullylogin, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                            JSONObject JsonDataObject = jsonObject.getJSONObject("data");
                            String strId = JsonDataObject.getString("id");
                            String prStrUsername = JsonDataObject.getString("username");
                            String strFname = JsonDataObject.getString("fname");
                            String strLname = JsonDataObject.getString("lname");
                            String srtPhoto = JsonDataObject.getString("photo");
                            saveUserInDb(strId,prStrUsername,strFname,strLname,strFname);
                            Intent intent = new Intent(getBaseContext(), DriverInquiry.class);
                            intent.putExtra("EXTRA_MOBILE", strUserName);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.connectionError, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.serverConnectionError)
                                    +volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            String strSignupUrl = String.format(getString(R.string.loginURL),getString(R.string.serverAddress));
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, strSignupUrl, request, Listener
                    , errorListener);
            queue.add(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void saveUserInDb(String strId, String strUserName,
                              String strFname, String strLname, String srtPhoto) {
        try{
            UserDatabaseHelper user = new UserDatabaseHelper(this);

            user.addUser(new User(strId,strUserName,strFname,strLname,srtPhoto));
            Toast.makeText(this, "Data saved succefully", Toast.LENGTH_SHORT).show();
            user.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
