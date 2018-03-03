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
import android.net.Uri;
import android.os.AsyncTask;
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

public class Sendsms extends Activity {

//    public Button sbutton,cbutton;
 TextView textView;
    private ProgressDialog pDialog;
    int flag = 0;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://manjeshs.in/warehouse/way.php";
    private static final String TAG_SUCCESS = "success";
    ActionBar actionBar;
    String phone, productname, quantity, numberdays, ownphone, userid, warid, user_name;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsms);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        textView=(TextView)findViewById(R.id.tv);
        bundle = this.getIntent().getExtras();

        /**** take owner phone number from Storage facility bundle*/
        phone = bundle.getString("CPhone");
        productname = bundle.getString("Pname");
        quantity = bundle.getString("Pquant");
        numberdays = bundle.getString("Pdays");
        ownphone = bundle.getString("ownerphone");
        userid = bundle.getString("uid");
        warid = bundle.getString("wid");
        user_name = bundle.getString("username");
        if (!isOnline(getApplicationContext())) {
            Toast.makeText(Sendsms.this, "No network connection", Toast.LENGTH_LONG).show();
            return;
        } else {
            new loginAccess().execute(phone, productname, quantity, numberdays, ownphone, userid, warid, user_name);
        }
    }

    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }




    class loginAccess extends AsyncTask<String, String, String> {



        protected void onPreExecute() {

            super.onPreExecute();
            textView.setText("Sending SMS...");
            //pDialog = new ProgressDialog(Sendsms.this);
            //pDialog.setMessage("Sending SMS...");
            //pDialog.setIndeterminate(false);
           // pDialog.setCancelable(true);
            //pDialog.show();
        }

        String cphone,message,proname,proquantity,prodays,uid,wid,name;

        String ophone;/**** take it from Storage facility bundle*/


        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
             cphone=arg0[0];
            proname=arg0[1];
            proquantity=arg0[2];
            prodays=arg0[3];
            ophone=arg0[4];
            uid=arg0[5];
            wid=arg0[6];
            name=arg0[7];
            message=("Mr./Mrs. "+name+" needs to store "+proquantity+" tons of "+proname+" for "+prodays+" days in your warehouse.\ncustomer phone number->"+cphone);

            params.add(new BasicNameValuePair("message",message));
            params.add(new BasicNameValuePair("phone",ophone));
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

            //pDialog.dismiss();

            if(flag==1) {
                textView.setText("SMS sending Failed :)");
                Toast.makeText(Sendsms.this, "Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            }
                else{
                textView.setText("SMS Sent :)");
                Toast.makeText(Sendsms.this,"SMS sent", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Sendsms.this);
                builder1.setTitle("COMMENT");
                builder1.setMessage("Do you wish to add comments?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Bundle bundle=new Bundle();
                                bundle.putString("userid",uid);
                                bundle.putString("warid",wid);
                                Intent i=new Intent(getApplicationContext(),ReviewsActivity.class);
                                i.putExtras(bundle);
                                startActivity(i);
                                dialog.cancel();
                                finish();
                            }
                        });
                builder1.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);
                                dialog.cancel();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



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






