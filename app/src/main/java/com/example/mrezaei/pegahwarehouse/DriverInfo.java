package com.example.mrezaei.pegahwarehouse;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverInfo extends AppCompatActivity {

    CircleImageView img_driverPhoto;
    TextView txt_driverName, txt_driverCode,
            txt_truckCode, txt_truckPlaque, txt_truckModel;
    Button btn_startDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);
        init_toolbar();
        initViews();
        showDriverAndTruckInfo();
    }

    private void showDriverAndTruckInfo() {
        Bundle extras = getIntent().getExtras();
//        img_driverPhoto.setImageResource(extras.getString("EXTRA_PHOTO"));
        if (extras != null) {
            txt_driverCode.setText(extras.getString("EXTRA_DRIVER_CODE"));
            txt_driverName.setText(extras.getString("EXTRA_FIRST_NAME") +
                    " " + extras.getString("EXTRA_LAST_NAME"));
            txt_truckCode.setText(extras.getString("EXTRA_TRUCK_CODE"));
            txt_truckPlaque.setText(extras.getString("EXTRA_TRUCK_PLAQUE"));
            txt_truckModel.setText(extras.getString("EXTRA_TRUCK_MODEL"));
        }

    }

    private void init_toolbar() {
        Toolbar toolbar = findViewById(R.id.driverInfo_toolbar_container);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void startDelivery(View view) {
        sendUserInfoToServer();
    }

    private void initViews() {
        img_driverPhoto = findViewById(R.id.img_driverPhoto);
        txt_driverCode = findViewById(R.id.txt_driverCode);
        txt_driverName = findViewById(R.id.txt_driverName);
        txt_truckCode = findViewById(R.id.txt_truckCode);
        txt_truckPlaque = findViewById(R.id.txt_truckPlaque);
        txt_truckModel = findViewById(R.id.txt_truckModel);
        btn_startDelivery = findViewById(R.id.btn_startDelivery);
    }

    private void sendUserInfoToServer() {
        try {
            JSONObject request = new JSONObject();
            request.put("apiVersion", "1");
            request.put("appVersion", "1");
            request.put("appType", "mobile");

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            queue.getCache().clear();

            Response.Listener<JSONObject> Listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        boolean boolStatus = jsonObject.getBoolean("status");
//                                        JSONArray errorsArray = jsonObject.getJSONArray("errors");
                        if (boolStatus == true)
                        {
                            Toast.makeText(getApplicationContext(), R.string.Successfullylogin, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                            JSONObject JsonDataObject = jsonObject.getJSONObject("data");
                            String strDeliverId = JsonDataObject.getString("deliver_id");
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.putExtra("EXTRA_DELIVER_ID", strDeliverId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.noDeliverId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
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
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.serverConnectionError)
                                    + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            String strSignupUrl = String.format(getString(R.string.deliver_id_URL), getString(R.string.serverAddress));
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, strSignupUrl, request, Listener
                    , errorListener);
            queue.add(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}