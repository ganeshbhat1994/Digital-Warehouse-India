package com.example.w.maps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w.maps.warehouse.model.FetchReviewListener;
import com.example.w.maps.warehouse.model.FetchReviewTask;
import com.example.w.maps.warehouse.model.ReviewData;

public class Review extends AppCompatActivity implements FetchReviewListener{

    private ProgressDialog dialog;
    int uid;
    float rating;
    RatingBar ratingBar;

    TextView txtReview1,txtReview2,txtReview3,txtReview4,txtReview5,txtReview6,txtUser1,txtUser2,txtUser3,txtUser4,txtUser5,txtUser6,txtRat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtReview1=(TextView)findViewById(R.id.txtReview1);
        txtReview2=(TextView)findViewById(R.id.txtReview2);
        txtReview3=(TextView)findViewById(R.id.txtReview3);
        txtReview4=(TextView)findViewById(R.id.txtReview4);
        txtReview5=(TextView)findViewById(R.id.txtReview5);
        txtReview6=(TextView)findViewById(R.id.txtReview6);

        txtUser1=(TextView)findViewById(R.id.txtUser1);
        txtUser2=(TextView)findViewById(R.id.txtUser2);
        txtUser3=(TextView)findViewById(R.id.txtUser3);
        txtUser4=(TextView)findViewById(R.id.txtUser4);
        txtUser5=(TextView)findViewById(R.id.txtUser5);
        txtUser6=(TextView)findViewById(R.id.txtUser6);
        txtRat=(TextView)findViewById(R.id.txtRat);


        ratingBar=(RatingBar)findViewById(R.id.rateBar2);

        Intent intent=getIntent();
        if(intent.hasExtra("uid")==true) {
            uid = intent.getExtras().getInt("uid");
            rating=intent.getExtras().getFloat("rating");
            ratingBar.setRating(rating);
            txtRat.setText(String.valueOf(rating));
           // Log.e("uid passed is",Integer.toString(uid));

            dialog = ProgressDialog.show(this, "", "Loading please wait...");

            String url1 = "http://warehouse.netii.net/reviews.php";
            FetchReviewTask task = new FetchReviewTask(this,uid);
            task.execute(url1);

        }
    }

    @Override
    public void onFetchComplete(ReviewData [] data) {
        if(dialog != null)  dialog.dismiss();

        //Log.e("comment", data[0].comment);
        //Log.e("name", data[0].name);

        txtReview1.setText(data[0].comment);
        txtReview2.setText(data[1].comment);
        txtReview3.setText(data[2].comment);
        txtReview4.setText(data[3].comment);
        txtReview5.setText(data[4].comment);
        txtReview6.setText(data[5].comment);

        txtUser1.setText(data[0].name);
        txtUser2.setText(data[1].name);
        txtUser3.setText(data[2].name);
        txtUser4.setText(data[3].name);
        txtUser5.setText(data[4].name);
        txtUser6.setText(data[5].name);

    }

    @Override
    public void onFetchFailure(String msg) {
        if(dialog != null)  dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }


}
