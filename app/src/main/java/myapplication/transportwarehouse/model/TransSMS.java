package myapplication.transportwarehouse.model;

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

import com.example.w.maps.JSONParser;
import com.example.w.maps.MainActivity;
import com.example.w.maps.R;
import com.example.w.maps.warehouse.PageActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransSMS extends Activity {

    //    public Button sbutton,cbutton;
    TextView textView;
    private ProgressDialog pDialog;
    int flag = 0;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://manjeshs.in/warehouse/way.php";
    private static final String TAG_SUCCESS = "success";
    ActionBar actionBar;
    String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsms);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        textView=(TextView)findViewById(R.id.tv);
        Intent intent=getIntent();
        phone=intent.getExtras().getString("ophone");

        if (!isOnline(getApplicationContext())) {
            Toast.makeText(TransSMS.this, "No network connection", Toast.LENGTH_SHORT).show();
            return;
        } else {
            new loginAccess().execute(phone);
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

        }

      String phone="8722907722",message;

        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            message=("you transport have been booked :)");
            // phone=arg0[1];
            params.add(new BasicNameValuePair("message",message));
            params.add(new BasicNameValuePair("phone",phone));
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
            if(flag==1){
                textView.setText("SMS sending Failed :)");
                Toast.makeText(TransSMS.this, "Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            }

            else{
                textView.setText("SMS Sent :)");
                Toast.makeText(TransSMS.this,"SMS sent", Toast.LENGTH_SHORT).show();
                finish();
              // Intent i=new Intent(getApplicationContext(), PageActivity.class);
                //startActivity(i);


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






