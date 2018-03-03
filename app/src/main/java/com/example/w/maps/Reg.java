package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.w.maps.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Reg extends Activity {
    private ProgressDialog pDialog;
     EditText etusername;
    EditText etpassword;
    EditText etDistrict;
    ActionBar actionBar;
    EditText etphone;
    Button submit;
    String action = "userreg";
    String username;
    String password;
    String Area="default";
    String District;
    String State="default";
    String EReg;
    String name,uid;

    SharedPreferences prefs;
    SharedPreferences mPrefs;


    String phone;
    int flag = 0;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://warehouse.netii.net/index.php";
    private static final String TAG_SUCCESS = "success";


   // public String user= getIntent().getExtras().getString("usertype");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        Bundle bundle = getIntent().getExtras();

//        user = bundle.getString("utype");
        //Go To Login.java
        etusername = (EditText) findViewById(R.id.username);
        etpassword = (EditText) findViewById(R.id.password);

        etDistrict = (EditText) findViewById(R.id.district);
        etphone = (EditText) findViewById(R.id.phonenumber);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        username = etusername.getText().toString();
        password = etpassword.getText().toString();

        District = etDistrict.getText().toString();
        phone = etphone.getText().toString();
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etusername.getText().toString().length()==0){
                    Toast.makeText(Reg.this,"Please enter the username",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etpassword.getText().toString().length()==0){
                    Toast.makeText(Reg.this,"Please enter the password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etDistrict.getText().toString().length()==0){
                    Toast.makeText(Reg.this,"Please enter the District",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etphone.getText().toString().length()==0){
                    Toast.makeText(Reg.this,"Please enter the Phone number",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isOnline(getApplicationContext())){
                    Toast.makeText(Reg.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                //from login.java
                new loginAccess().execute();
            }

            //code to check online details
            private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return false;
            }
            //Close code that check online details
        });
        //Close log in
    }

    class loginAccess extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reg.this);
            pDialog.setMessage("Sign in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        String District= etDistrict.getText().toString();
        String phone = etphone.getText().toString();


        @Override
        protected String doInBackground(String... arg0) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("action", action));
            params.add(new BasicNameValuePair("name", username));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("Area",Area));
            params.add(new BasicNameValuePair("District", District));
            params.add(new BasicNameValuePair("State", State));


            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                uid=json.getString("uid");

                EReg=json.getString("error");
                if (success == 1) {
                    flag = 0;

                } else {
                    // failed to Sign in
                    flag = 1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (flag == 1) {
                if(EReg.equals("Registered")){
                    Toast.makeText(Reg.this, "Phone number already Registered ! try again...", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Reg.this, "Phone number already Registered !", Toast.LENGTH_SHORT).show();
            }
            if (flag == 0) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    SharedPreferences.Editor meditor = mPrefs.edit();
                    meditor.putBoolean("welcomeScreenShown", true);
                    meditor.commit();

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("customer_number", phone);
                    editor.putString("district", District);
                    editor.putString("cust_name",username);
                    editor.putString("user_id", uid);

                    editor.commit();

                    Toast.makeText(Reg.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    Bundle bundle=new Bundle();
                    bundle.putString("cusphone",phone);

                    Intent intent = new Intent(getApplicationContext(), UserRegSms.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                }
            }
    }
}