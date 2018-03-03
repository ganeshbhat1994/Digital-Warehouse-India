package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class OwnerLogin extends Activity {
    ActionBar actionBar;

    Button login;
    private EditText user,password;
    private ProgressDialog pDialog;
    int flag=0,ware_id=1,uid=1;
    JSONParser jsonParser = new JSONParser();
    private static String url="http://warehouse.netii.net/ownerlogin.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);

        //Get all data and log in
        login=(Button)findViewById(R.id.loginbutton);
        user=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                //Check all fields
                if(user.length()==0)
                {
                    Toast.makeText(OwnerLogin.this,"Please Enter correct Userid", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.length()==0)
                {
                    Toast.makeText(OwnerLogin.this,"Please Enter correct password", Toast.LENGTH_LONG).show();
                    return;
                }
                //check connectivity
                if(!isOnline(OwnerLogin.this))
                {
                    Toast.makeText(OwnerLogin.this,"No network connection", Toast.LENGTH_LONG).show();
                    return;
                }

                //from login.java
                new loginAccess().execute();
            }

            //code to check online details
            private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting())
                {
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
            pDialog = new ProgressDialog(OwnerLogin.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }




        String phn=user.getText().toString();
        String pwd=password.getText().toString();
        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("login",phn));
            params.add(new BasicNameValuePair("passwd",pwd));
            JSONObject json = jsonParser.makeHttpRequest(url,"POST",params);
            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {
                    flag=0;
                }
                else
                {
                    // failed to login
                    flag=1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if(flag==1)
                Toast.makeText(OwnerLogin.this,"Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(OwnerLogin.this,"Login Successful", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("phone",phn);
                bundle.putString("password",pwd);
                Toast.makeText(getApplicationContext(),phn,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),UpdateOwner.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }

        }

    }
    @Override
    protected void onStop() {
        super.onStop();

        if(pDialog!= null)
            pDialog.dismiss();
    }




}
