package myapplication.transportwarehouse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.w.maps.InitialActivity;
import com.example.w.maps.JSONParser;
import com.example.w.maps.MainActivity;
import com.example.w.maps.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 1/9/2016.
 */
public class Transp_add_details extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    EditText smallnumber, smallcost, mediumnumber, mediumcost, largenumber, largecost, xlargenumber, xlargecost, rent;
    Button submit;
    CheckBox c1, c2, c3, c4;
    Bundle bundle;
    SharedPreferences mPrefs;
    String tCompany, pass, licence, pno, area, district, state, pin, smallnumbers="0", smallcosts="0", mediumnumbers="0", mediumcosts="0",
            largenumbers="0", largecosts="0", xlargenumbers="0", xlargecosts="0";
    final String welcomeScreenShownPref = "welcomeScreenShown";
    Boolean welcomeScreenShown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transp_add_details);

        smallcost = (EditText) findViewById(R.id.smallcost);
        smallnumber = (EditText) findViewById(R.id.smallnumber);
        largecost = (EditText) findViewById(R.id.largecost);
        largenumber = (EditText) findViewById(R.id.largenumber);
        mediumcost = (EditText) findViewById(R.id.medcost);
        mediumnumber = (EditText) findViewById(R.id.mednumber);
        xlargecost = (EditText) findViewById(R.id.xlargecost);
        xlargenumber = (EditText) findViewById(R.id.xlargenumber);
        //rent = (EditText) findViewById(R.id.rent);
        submit = (Button) findViewById(R.id.trans_submit);
        c1 = (CheckBox) findViewById(R.id.checkBox1);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
        c4 = (CheckBox) findViewById(R.id.checkBox4);

        submit.setOnClickListener(this);
        c1.setOnCheckedChangeListener(this);
        c2.setOnCheckedChangeListener(this);
        c3.setOnCheckedChangeListener(this);
        c4.setOnCheckedChangeListener(this);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        bundle = this.getIntent().getExtras();
        tCompany = bundle.getString("tCompany");
        pass = bundle.getString("pass");
        //tCompany,pass,licence,pno,area,district,state,pin
        licence = bundle.getString("licence");
        pno = bundle.getString("pno");
        area = bundle.getString("area");
        district = bundle.getString("district");
        state = bundle.getString("state");
        pin = bundle.getString("pin");

    }

    @Override
    public void onClick(View v) {
        //smallnumbers,smallcosts,mediumnumbers,mediumcosts,largenumbers,largecosts,xlargenumbers,xlargecosts
        smallcosts = (smallcost.getText().toString());
        smallnumbers = (smallnumber.getText().toString());
        mediumcosts = (mediumcost.getText().toString());
        mediumnumbers = (mediumnumber.getText().toString());
        largecosts = (largecost.getText().toString());
        largenumbers = (largenumber.getText().toString());
        xlargecosts = (xlargecost.getText().toString());
        xlargenumbers = (xlargenumber.getText().toString());
        new trans_reg_task().execute();
       // if();
        if (welcomeScreenShown) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();}
        else {
            Intent intent = new Intent(getApplicationContext(), InitialActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.checkBox1:
                if(!(smallcost.getVisibility()==View.VISIBLE)) {
                    smallcost.setVisibility(View.VISIBLE);
                    smallnumber.setVisibility(View.VISIBLE);
                }
                else
                {
                    smallcost.setVisibility(View.GONE);
                    smallnumber.setVisibility(View.GONE);
                }
                smallcost.setText("0");
                smallcost.setText("0");
                break;
            case R.id.checkBox2:
                if(!(mediumcost.getVisibility()==View.VISIBLE)) {
                    mediumcost.setVisibility(View.VISIBLE);
                    mediumnumber.setVisibility(View.VISIBLE);
                }
                else
                {
                    mediumcost.setVisibility(View.GONE);
                    mediumnumber.setVisibility(View.GONE);
                }
                mediumcost.setText("0");
                mediumnumber.setText("0");
                break;
            case R.id.checkBox3:
                if(!(largecost.getVisibility()==View.VISIBLE)) {
                    largecost.setVisibility(View.VISIBLE);
                    largenumber.setVisibility(View.VISIBLE);
                }
                else
                {
                    largecost.setVisibility(View.GONE);
                    largenumber.setVisibility(View.GONE);
                }
                largecost.setText("0");largenumber.setText("0");
                break;
            case R.id.checkBox4:

                if(!(xlargecost.getVisibility()==View.VISIBLE)) {
                    xlargecost.setVisibility(View.VISIBLE);
                    xlargenumber.setVisibility(View.VISIBLE);
                }
                else
                {
                    xlargecost.setVisibility(View.GONE);
                    xlargenumber.setVisibility(View.GONE);
                }
                xlargecost.setText("0");xlargenumber.setText("0");
                break;
        }
    }

    class trans_reg_task extends AsyncTask<String, String, Void> {
        int flag = 0;
        private ProgressDialog progressDialog=new ProgressDialog(Transp_add_details.this);
        private final String TAG_SUCCESS = "success";
        JSONParser jsonParser = new JSONParser();
        private String url = "http://warehouse.netii.net/trans_register.php";

        @Override
        protected Void doInBackground(String... params) {


            List<NameValuePair> xparams = new ArrayList<>();
//Passing the transport details to the database
//tCompany,pass,licence,pno,area,district,state,pin,   smallnumber,smallcost,mediumnumber,mediumcost,largenumber,largecost,xlargenumber,xlargecost
            xparams.add(new BasicNameValuePair("name", tCompany));
            xparams.add(new BasicNameValuePair("phone", pno));
            xparams.add(new BasicNameValuePair("pass", pass));
            xparams.add(new BasicNameValuePair("licence", licence));
            xparams.add(new BasicNameValuePair("area", area));
            xparams.add(new BasicNameValuePair("district", district));
            xparams.add(new BasicNameValuePair("state", state));
            xparams.add(new BasicNameValuePair("pin", pin));
            xparams.add(new BasicNameValuePair("smallcost", smallcosts));
            xparams.add(new BasicNameValuePair("smallnumber", smallnumbers));
            xparams.add(new BasicNameValuePair("mediumnumber", mediumnumbers));
            xparams.add(new BasicNameValuePair("mediumcost", mediumcosts));
            xparams.add(new BasicNameValuePair("largenumber", largenumbers));
            xparams.add(new BasicNameValuePair("largecost", largecosts));
            xparams.add(new BasicNameValuePair("xlargenumber", xlargenumbers));
            xparams.add(new BasicNameValuePair("xlargecost", xlargecosts));
//Validation
          //  if(smallcosts.trim()=="" || smallnumbers.trim()=="" || mediumcosts.trim()==""||mediumnumbers.trim()==""||largecosts.trim()==""||largenumbers.trim()==""
            //        ||xlargenumbers.trim()==""||xlargecosts.trim()=="")
            //{
              //  Toast.makeText(getApplicationContext(),"Please fill the ")}
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", xparams);

            try {
                int success = json.getInt(TAG_SUCCESS);
                Log.e("sucess msg: ", String.valueOf(success));

                if (success == 1) {
                    flag = 0;
                } else {
                    // failed to login
                    flag = 1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

         /*   progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    trans_reg_task.this.cancel(true);
                }
            });*/
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            //this.progressDialog.dismiss();
           // this.progressDialog.cancel();
            if (flag == 1)
                Toast.makeText(getApplicationContext(), "Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            else if (flag == 0) {
                Toast.makeText(getApplicationContext(), "Submit...OK", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}