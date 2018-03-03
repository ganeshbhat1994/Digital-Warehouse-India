package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import myapplication.transportwarehouse.Transport_Reg_Activity;

public class InitialActivity extends Activity {
    ActionBar actionBar;
    SharedPreferences mPrefs,mPrefs1;
    Boolean welcomeScreenShown;
    int uuid;
    Button login;
    Bundle bundle;
    private EditText user,password;
    private ProgressDialog pDialog;
    int flag=1;
    JSONParser jsonParser;
    private static String url="http://warehouse.netii.net/userlogin.php";
    private static final String TAG_SUCCESS = "success";
    private static final String user_id="uid";
    private static final String user_name="name";
    String name,phn,pwd;

    TextView txtReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
      /*  welcomeScreenShown=mPrefs.getBoolean("welcomeScreenShown", false);
        if (welcomeScreenShown) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        */
        setContentView(R.layout.activity_initial);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);


        login=(Button)findViewById(R.id.loginbutton);
        user=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        txtReg=(TextView)findViewById(R.id.txtReg);

        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Reg.class);
                startActivity(i);
            }
        });

        // second argument is the default to use if the preference can't be found
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                //Check all fields
                if(user.length()==0)
                {
                    Toast.makeText(InitialActivity.this, "Please Enter correct Userid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()==0)
                {
                    Toast.makeText(InitialActivity.this,"Please Enter correct password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //check connectivity
                if(!isOnline(InitialActivity.this))
                {
                    Toast.makeText(InitialActivity.this,"No network connection", Toast.LENGTH_SHORT).show();
                    return;
                }


                phn=user.getText().toString();
                pwd=password.getText().toString();
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
    }
    class loginAccess extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InitialActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }






        @Override
        protected String doInBackground(String... arg0) {

            List<NameValuePair> params = new ArrayList<>();
            jsonParser=new JSONParser();

            params.add(new BasicNameValuePair("login",phn));
            params.add(new BasicNameValuePair("passwd",pwd));
            JSONObject json = jsonParser.makeHttpRequest(url,"POST",params);
            //   Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                uuid=json.getInt(user_id);
                name=json.getString(user_name);

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
                Toast.makeText(InitialActivity.this,"Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            else if(flag==0){
                Toast.makeText(InitialActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();

                mPrefs1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                SharedPreferences.Editor meditor = mPrefs1.edit();
                meditor.putBoolean("welcomeScreenShown", true);
                meditor.putString("customer_number", phn);
                meditor.putString("user_id", String.valueOf(uuid));
                meditor.putString("cust_name",name);

                Log.e("uid_saving", String.valueOf(uuid));
                meditor.commit();

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
