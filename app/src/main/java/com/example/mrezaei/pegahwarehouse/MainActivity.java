package com.example.mrezaei.pegahwarehouse;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {

    EditText edt_barcode;
    Button btn_scan_camera;
    Button button;
    ClipData.Item menuIte;
    TextView deliver_count,shrink_count,pallet_count,deliver_id;
    TextView gtin,exp_date,batch_no,serial_no;
    String strBarcode,strDeliverId="";
    private int int_deliver_count = 0,int_shrink_count = 0,int_pallet_count = 0;
    public static final int CAMERA_REQUEST_CODE = 10;
    private ZXingScannerView scannerView;
    public int intLastRadioSelectionId = 2131230881;
    //this variable was private but we changed it to public just to test its operation
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup);
//        radioGroup.check(intLastRadioSelectionId);
        initViews();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            strDeliverId= extras.getString("EXTRA_DELIVER_ID");
            deliver_id.setText(strDeliverId);
        }
        else
        {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.inputDeliverId)
                .content(R.string.input_deliver_message)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT)
                .positiveText("تایید")
                .input(R.string.input_deliverId_hint, R.string.input_prefill, new MaterialDialog.InputCallback()
                {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        if (input.toString().equals("")) {
                            Toast.makeText(getApplicationContext()
                                    , "وارد کردن کد تحویل ضروری است", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), DriverInfo.class);
//                intent.putExtra("EXTRA_MOBILE", strMobile);
                            startActivity(intent);
                        }
                        else{
                            strDeliverId = input.toString();
                            deliver_id.setText(strDeliverId);
                        }
                    }
                }).show();
        }

        edt_barcode = findViewById(R.id.edt_barcode);

        edt_barcode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    split_barcode(edt_barcode);
                }
                return false;
            }

            public boolean onEditorAction(EditText v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    split_barcode(edt_barcode);
                }
                return false;
            }
        });

    }

    private void initViews() {
        init_toolbar();
        deliver_count = findViewById(R.id.txt_delivery_value);
        shrink_count = findViewById(R.id.txt_shrink_value);
        pallet_count = findViewById(R.id.txt_pallet_value);
        deliver_id = findViewById(R.id.txt_deliver_id_value);

        gtin = findViewById(R.id.txt_gtin_value);
        exp_date = findViewById(R.id.txt_exp_date_value);
        batch_no = findViewById(R.id.txt_batch_no_value);
        serial_no = findViewById(R.id.txt_serial_value);

    }

    private void split_barcode(EditText barcode_edit_text) {
        strBarcode = barcode_edit_text.getText().toString();
        if(strBarcode.length()<44)
        {
            Toast.makeText(MainActivity.this, R.string.length_error, Toast.LENGTH_SHORT).show();
        }
        else {
            Barcode barcode = new Barcode(strBarcode,deliver_id.getText().toString(),0);

            String str_gtn = barcode.getGtin();
            String str_exp_date = barcode.getExp();
            String str_batch_no = barcode.getLot();
            String str_serial_no = barcode.getSerial();

            gtin.setText(str_gtn);
            exp_date.setText(str_exp_date);
            batch_no.setText(str_batch_no);
            serial_no.setText(str_serial_no);

//            deliver_count = findViewById(R.id.txt_deliver_count_value);
            int_deliver_count++;
            deliver_count.setText(""+int_deliver_count);

//            RadioGroup radioGroup = findViewById(R.id.radioGroup);

            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.radioButtonSingle:
                    barcode.setLevelId(0);
                    addBarcode(barcode);
                    break;
                case R.id.radioButtonShrink:
                    barcode.setLevelId(1);
                    addBarcode(barcode);
                    break;
                case R.id.radioButtonPallet:
                    barcode.setLevelId(2);
                    addBarcode(barcode);
                    break;
                default:

                    break;
            }
        }
    }

    private void init_toolbar() {

        Toolbar toolbar = findViewById(R.id.main_toolbar_container);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowTitleEnabled(false);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete_barcode:
                delete_barcode(null);
                return true;
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void scanCode(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                readBarcode();

            } else {
                String[] permissionRequested = {Manifest.permission.CAMERA};
                requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);
            }
        }

    }

    public void made_a_call(View view){
        Uri number = Uri.parse("tel:09185785523");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);

    }

    private void readBarcode() {
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());
//        radioGroup=findViewById(R.id.radioGroup);
        intLastRadioSelectionId = radioGroup.getCheckedRadioButtonId();
        setContentView(scannerView);
        scannerView.startCamera();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            readBarcode();
        } else {
            Toast.makeText(this, "مجوز دسترسی به دوربین وجود ندارد", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!(scannerView ==null)) {
            scannerView.stopCamera();
        }
    }

    public void finish_delivery(View view) {
        //onBackPressed();
        radioGroup.check(R.id.radioButtonPallet);
    }

    public void delete_barcode(View view) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.input)
                .content(R.string.input_content)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT)
                .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        Toast.makeText(MainActivity.this,
                                "You have entered this barcode"+input, Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {
        @Override
        public void handleResult(com.google.zxing.Result result) {
            String resultCode = result.getText();
            setContentView(R.layout.activity_main);
            if (!(scannerView == null)) {
                scannerView.stopCamera();
            }
            EditText editText = findViewById(R.id.edt_barcode);
            editText.setText(resultCode, TextView.BufferType.EDITABLE);
            RadioGroup rb = findViewById(R.id.radioGroup);
            rb.check(intLastRadioSelectionId);
//            deliver_count = findViewById(R.id.txt_delivery_value);
            initViews();
            split_barcode(editText);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void addBarcode(Barcode barcode){
        final String strBarcode, strLevelId;

        final Statistics statistics = new Statistics();
        strBarcode = barcode.getComplete_barcode();
        strLevelId = String.valueOf(barcode.getLevelId());
        try {
            JSONObject request = new JSONObject();
            request.put("apiVersion", "1");
            request.put("appVersion", "1");
            request.put("appType", "mobile");
            request.put("deliverId", strBarcode);
            request.put("barcode", strBarcode);
            request.put("levelId", strLevelId);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            queue.getCache().clear();

            Response.Listener<JSONObject> Listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        boolean boolStatus = jsonObject.getBoolean("status");
//                                        JSONArray errorsArray = jsonObject.getJSONArray("errors");
                        if (boolStatus == true) {
                            Toast.makeText(MainActivity.this, "status = true", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                            JSONObject JsonDataObject = jsonObject.getJSONObject("data");
                            String strTotal = JsonDataObject.getString("total");
                            String strShrink = JsonDataObject.getString("shrink");
                            String strPallet = JsonDataObject.getString("pallet");
//                            statistics.totalCount = Integer.parseInt(strTotal);
//                            statistics.shrinkCount = Integer.parseInt(strShrink);
//                            statistics.palletCont = Integer.parseInt(strPallet);
                            deliver_count.setText(strTotal);
                            shrink_count.setText(strShrink);
                            pallet_count.setText(strPallet);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.connectionError, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this,
                            "اتصال به سرور انجام نشد"
                                    +volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            String strDeliverStatUrl = String.format(MainActivity.this.getString(R.string.deliver_stat),
                    MainActivity.this.getString(R.string.serverAddress));
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST,
                    strDeliverStatUrl, request, Listener
                    , errorListener);
            queue.add(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
