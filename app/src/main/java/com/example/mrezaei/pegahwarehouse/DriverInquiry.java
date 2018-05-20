package com.example.mrezaei.pegahwarehouse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DriverInquiry extends AppCompatActivity {

    ImageView img_back;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    EditText driver_code,truck_code;
    Button btnInquiry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inquiry);
        init_toolbar();
        init_views();
    }

    private void init_views() {
        driver_code = findViewById(R.id.edt_driver_code);
        truck_code = findViewById(R.id.edt_truck_code);
        btnInquiry = findViewById(R.id.btn_start);
    }

    private void init_toolbar() {
        Toolbar toolbar = findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
    }

    public void driver_inquiry_click(View view) {
       // show_progress();

        String strTruck_code = truck_code.getText().toString().trim();
        boolean boolCheckDriverAndTruckCodes;
        boolCheckDriverAndTruckCodes = true;
        if(boolCheckDriverAndTruckCodes) {

            sendDriverAndTruckInfoToServer();
        }
        else{
            Intent intent = new Intent(getBaseContext(), DriverInfo.class);
//                intent.putExtra("EXTRA_MOBILE", strMobile);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_page_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_ = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void show_progress() {
        ProgressBar progressBar = findViewById(R.id.loading_driver_progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void sendDriverAndTruckInfoToServer(){
        String strDriverCode = driver_code.getText().toString();
        String strTruckCode = truck_code.getText().toString();

        try {
            JSONObject request = new JSONObject();
            request.put("apiVersion", "1");
            request.put("appVersion", "1");
            request.put("appType", "mobile");
            request.put("driverCode", strDriverCode);
            request.put("truckCode", strTruckCode);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            queue.getCache().clear();

            Response.Listener<JSONArray> Listener = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    try {
//                        [
//                        {
//                            "status":"true/false",
//                                "data":{
//                                        Truck json object
//                                        }
//                                 "errors":[
//                                        {
//                                            "message":""
//                                        }
//                                        .
//                                        .
//                                        .
//                                 ]
//                        }
//
//                        },
//                        {
//                            "status":"true/false",
//                                "data":{
//                                        Truck json object
//                                        }
//                                 "errors":[
//                                        {
//                                            "message":""
//                                        }
//                                        .
//                                        .
//                                        .
//                                 ]
//                        }]
                        final Intent intent = new Intent(getApplicationContext(), DriverInfo.class);
                        JSONObject jDriverResponce  = jsonArray.getJSONObject(0);
                        boolean boolStatus = jDriverResponce.getBoolean("status");
                        //driver object is in index 0
                        if (boolStatus == true) {
                            Toast.makeText(getApplicationContext(), R.string.ValidDriver, Toast.LENGTH_SHORT).show();
                            JSONObject JsonDataObject = jDriverResponce.getJSONObject("data");
                            String strId = JsonDataObject.getString("id");
                            String prStrDriverCode = JsonDataObject.getString("driverCode");
                            String strFirstName = JsonDataObject.getString("firstName");
                            String strLastName = JsonDataObject.getString("lastName");
                            String srtPhoto = JsonDataObject.getString("photo");
                            saveDriverInDb(strId,prStrDriverCode,strFirstName,strLastName,srtPhoto);
                            intent.putExtra("EXTRA_DRIVER_CODE", prStrDriverCode);
                            intent.putExtra("EXTRA_FIRST_NAME", strFirstName);
                            intent.putExtra("EXTRA_LAST_NAME", strLastName);
                            intent.putExtra("EXTRA_PHOTO", srtPhoto);

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.InvalidDriver, Toast.LENGTH_SHORT).show();
                        }
                        JSONObject jTruckResponce  = jsonArray.getJSONObject(1);
                        boolean boolTruckStatus = jTruckResponce.getBoolean("status");
                        //truck object is in index 1
                        if (boolTruckStatus == true) {
                            Toast.makeText(getApplicationContext(), R.string.ValidTruck, Toast.LENGTH_SHORT).show();
                            JSONObject JsonDataObject = jTruckResponce.getJSONObject("data");
                            String strId = JsonDataObject.getString("id");
                            String prStrPlaque = JsonDataObject.getString("plaque");
                            String strTruckCode = JsonDataObject.getString("truckCode");
                            String strTruckModel = JsonDataObject.getString("truckModel");
                            saveTruckInDb(strId,prStrPlaque,strTruckCode,strTruckModel);
                            intent.putExtra("EXTRA_TRUCK_CODE", strTruckCode);
                            intent.putExtra("EXTRA_TRUCK_PLAQUE", prStrPlaque);
                            intent.putExtra("EXTRA_TRUCK_MODEL", strTruckModel);

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.InvalidTruck, Toast.LENGTH_SHORT).show();
                        }
//                        if(boolStatus == false){
//                            new AlertDialog.Builder(DriverInquiry.this)
//                                    .setTitle("راننده نامعتبر")
//                                    .setMessage("کد راننده در سرور پیدا نشد آیا با کد فعلی ادامه میدهید؟")
//                                    .setNegativeButton(android.R.string.no, null)
//                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface arg0, int arg1) {
//
//                                        }
//                                }).create().show();
                            startActivity(intent);
//                        }
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
                                    +volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            String strSignupUrl = String.format(getString(R.string.driverAndTruckURL),getString(R.string.serverAddress));
            MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(Request.Method.POST,strSignupUrl,request, Listener
                    , errorListener);

            queue.add(jsonArrayRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void saveTruckInDb(String strId, String strPlaque,
                              String strTruckCode, String strTruckModel) {
        try{
            TruckDatabaseHelper truckDatabaseHelper = new TruckDatabaseHelper(this);

            truckDatabaseHelper.addTruck(new Truck(strId,strPlaque,strTruckCode,strTruckModel));
            Toast.makeText(this, "Truck saved succefully", Toast.LENGTH_SHORT).show();
            truckDatabaseHelper.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDriverInDb(String strId, String strDriverCode,
                               String strFirstName, String strLastName, String strPhoto) {
        try{
            DriverDatabaseHelper driverDatabaseHelper = new DriverDatabaseHelper(this);

            driverDatabaseHelper.addDriver(
                    new DriverPerson(strId,strDriverCode,strFirstName,strLastName,strPhoto));
            Toast.makeText(this, "Driver saved succefully", Toast.LENGTH_SHORT).show();
            driverDatabaseHelper.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
