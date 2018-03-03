package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends Activity {
    private Button commentButton;
    private EditText etComment;
    private RatingBar rating;

    private ProgressDialog pDialog;
    int flag=0;
    JSONParser jsonParser = new JSONParser();
    private static String url="http://warehouse.netii.net/userreview.php";
    private static final String TAG_SUCCESS = "success";
  ActionBar actionBar;
String uid,wid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        Bundle bundle=this.getIntent().getExtras();
      uid=bundle.getString("userid");
        wid=bundle.getString("warid");
        commentButton=(Button)findViewById(R.id.commentbutton);
        etComment=(EditText)findViewById(R.id.comment);
        rating=(RatingBar)findViewById(R.id.ratebar);
        Drawable progress=rating.getProgressDrawable();
        DrawableCompat.setTint(progress,Color.BLUE);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isOnline(ReviewsActivity.this))
                {
                    Toast.makeText(ReviewsActivity.this, "No network connection", Toast.LENGTH_LONG).show();
                    return;
                }
                if(rating.getRating()==0){
                    Toast.makeText(ReviewsActivity.this,"Please Rate the Warehouse",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etComment.getText().toString().length()==0){
                    Toast.makeText(ReviewsActivity.this,"Please comment",Toast.LENGTH_SHORT).show();
                    return;
                }

                new loginAccess().execute(uid,wid);          }

            private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting())
                {
                    return true;
                }
                return false;
            }
        });
        
    }
    class loginAccess extends AsyncTask<String, String, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReviewsActivity.this);
            pDialog.setMessage("Posting reviews...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        double rate=(double)rating.getRating();
        String cmnt=etComment.getText().toString();

        String userid,warid;
        @Override
        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            userid=arg0[0];
            warid=arg0[1];

            params.add(new BasicNameValuePair("user_id", userid));
            params.add(new BasicNameValuePair("ware_id", warid));
            params.add(new BasicNameValuePair("txt_comment",cmnt));
            params.add(new BasicNameValuePair("rating",rate+""));
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
                Toast.makeText(ReviewsActivity.this, "Please Enter Correct informations", Toast.LENGTH_LONG).show();
            else{
                Toast.makeText(ReviewsActivity.this,"Comments posted", Toast.LENGTH_LONG).show();
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
