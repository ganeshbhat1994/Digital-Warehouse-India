package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StorageFacility extends Activity {
   public EditText etProductName,etProductQuantity,etProductDays;
    public RadioGroup radioGroupType,radioGroupCold;
    public RadioButton fruits,chemicals,machinery,food,textile,electronics,others;
    Button submit;
    private ProgressDialog pDialog;
    String stype,iscold,cphone;
    int flag=0;
    Float rent;
    Time today = new Time(Time.getCurrentTimezone());
    private static String url="http://warehouse.netii.net/tranUpdate.php";
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    Bundle bundle;
    SharedPreferences mPrefs;
    String str,u_id,w_id,phn,user_name,proname,proquantity,storagedays;
    Spinner spinner1;
    EditText txtProduct;
    List<String> slist;
    List<String> sslist;
    ActionBar actionBar;
    public int cold=0;
    Float cost;
    String scost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_facility);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);

        etProductDays=(EditText)findViewById(R.id.productdays);
        etProductQuantity=(EditText)findViewById(R.id.productquantity);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        txtProduct=(EditText)findViewById(R.id.txtProduct);


        bundle = this.getIntent().getExtras();

        w_id=bundle.getString("wid");
        phn=bundle.getString("ophone");
        rent=Float.valueOf(bundle.getString("rent"));

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        cphone=mPrefs.getString("customer_number", "no_phone");
        user_name=mPrefs.getString("cust_name", "anonymox");
        u_id=mPrefs.getString("user_id", "anonymox");


        submit=(Button)findViewById(R.id.okbutton);

        today.setToNow();

        radioGroupCold=(RadioGroup)findViewById(R.id.radiogroupcold);


        String sss[]=getResources().getStringArray(R.array.product_list);
        slist=new LinkedList<>(Arrays.asList(sss));
        sslist=new LinkedList<>(Arrays.asList(sss));

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,slist);
        spinner1.setAdapter(adapter);
        spinner1.setFocusable(true);

        radioGroupCold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiocold) {
                    iscold = "cold";
                    cold = 1;
                } else if (checkedId == R.id.radionormal) {
                    iscold = "normal";
                    cold = 0;
                }
            }
        });

        txtProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString() != null) {
                    slist.clear();
                    for (String ss : sslist) {
                        if (((ss.toLowerCase())).trim().startsWith(((s.toString()).toLowerCase()).trim())) {
                            slist.add(ss);
                        }

                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // spinner1.clearFocus();
                spinner1.performClick();
                //txtProduct.requestFocus();
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proname = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proname= spinner1.getSelectedItem().toString();
                if (iscold.length()==0){
                    Toast.makeText(StorageFacility.this,"Please select a storage type",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etProductQuantity.getText().toString().length()==0){
                    Toast.makeText(StorageFacility.this, "Please Enter quantity in tons", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(proname.equals("Select the product")){
                    Toast.makeText(StorageFacility.this, "Please Choose the Product to be stored", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etProductDays.getText().toString().length()==0){
                    Toast.makeText(StorageFacility.this, "Please fill the number of days", Toast.LENGTH_SHORT).show();
                    return;
                }
                proname= spinner1.getSelectedItem().toString();
                proquantity=etProductQuantity.getText().toString();
                storagedays=etProductDays.getText().toString();
                cost=(Integer.parseInt(storagedays))*(rent*(Integer.parseInt(proquantity)))/30;
                scost=Float.toString(cost);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(StorageFacility.this);
                builder1.setTitle("Do you want to proceed for transaction");
                builder1.setMessage("Total Estimated Cost of storing  " + proquantity + " tons of " + proname + " for  " + storagedays + " days is :" + scost+" Rs ");
                builder1.setCancelable(true);
                builder1.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    new loginAccess().execute(stype, iscold, cphone, w_id, u_id, phn, user_name);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error in submitting", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder1.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alert = builder1.create();
                alert = builder1.create();
                alert.show();
                //from login.java

            }
        });

 }
    class loginAccess extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StorageFacility.this);
            pDialog.setMessage("Validating.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }



        String userid,warid;
        String cphone;
        String type_cold,type_storage,ophone,uname;
        String transdate=(today.month+"/"+today.monthDay+"/"+today.year).toString();
        @Override
        protected String doInBackground(String... arg0) {
            type_cold=arg0[1];
            type_storage=arg0[0];
            cphone=arg0[2];
            warid=arg0[3];
            userid=arg0[4];
            ophone=arg0[5];
            uname=arg0[6];
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("user_id",userid));
            params.add(new BasicNameValuePair("ware_id",warid));
            params.add(new BasicNameValuePair("cost",scost));
            params.add(new BasicNameValuePair("used_space",proquantity));
            params.add(new BasicNameValuePair("no_of_days_used",storagedays));
            params.add(new BasicNameValuePair("trans_date",transdate));
            params.add(new BasicNameValuePair("storage_type",type_storage));
            params.add(new BasicNameValuePair("item_stored",proname));
            params.add(new BasicNameValuePair("cold",type_cold));
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
                Toast.makeText(StorageFacility.this,"Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(StorageFacility.this,"Validation Successful", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                //** take owner phone number from pageactivity--->Login
                bundle.putString("CPhone",cphone);
                bundle.putString("Pname",proname);
                bundle.putString("Pdays",storagedays);
                bundle.putString("Pquant",proquantity);
                bundle.putString("ownerphone",ophone);
                bundle.putString("uid",userid);
                bundle.putString("wid",warid);
                bundle.putString("username",uname);
                Intent i = new Intent(getApplicationContext(),Sendsms.class);
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


