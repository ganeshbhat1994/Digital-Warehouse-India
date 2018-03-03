package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

public class UpdateOwner extends Activity {

    EditText etAvailability,etRent;
    Button btnUpdate;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url="http://warehouse.netii.net/upPost.php";
    private static final String TAG_SUCCESS = "success";
    int flag=0;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_owner);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        etAvailability=(EditText)findViewById(R.id.availability);
        etRent=(EditText) findViewById(R.id.rent);
        btnUpdate=(Button)findViewById(R.id.Update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avail=etAvailability.getText().toString();
                String rent= etRent.getText().toString();
               // Toast.makeText(getApplicationContext(),"hey"+avail+rent,Toast.LENGTH_SHORT).show();
                if(avail.length()==0 || rent.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isOnline(UpdateOwner.this))
                {
                    Toast.makeText(UpdateOwner.this,"Please connect to internet !...try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                        try {
                            new MyTask().execute(avail,rent);
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Error while Updating", Toast.LENGTH_SHORT).show();
                        }
                }
            }

        });
    }
    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    class MyTask extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateOwner.this);
            pDialog.setMessage("Updating...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        //Bundle Code ???????????????

        //String passwd="7";
        Bundle bundle = getIntent().getExtras();
        String userPhone=bundle.getString("phone");
        String passwd=bundle.getString("password");


              @Override
        protected String doInBackground(String... arg0) {


            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("ph",userPhone));
            params.add(new BasicNameValuePair("hale",passwd));
            params.add(new BasicNameValuePair("av",arg0[0]));
            params.add(new BasicNameValuePair("re",arg0[1]));

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
                Toast.makeText(UpdateOwner.this,"Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(UpdateOwner.this,"Updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
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
