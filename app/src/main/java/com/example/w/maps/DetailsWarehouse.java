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
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailsWarehouse extends Activity {
    private ProgressDialog pDialog;
    //int set=0;
    public RadioGroup radioGroup,radioGroupcold;

    public RadioButton cwc,swc,pvt,pcs,rcold,rnormal;

    public int cold=0;

    EditText etwname;
    EditText etwregno;
  //  EditText etwloc;
    public EditText regDate,regMonth,regYear,vregDate,vregMonth,vregYear;
    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";

    EditText etcapacity;
   // public String iscold="cold";
   public Button fbutton;
    EditText etavailabilty;
    EditText etrent;
    //EditText etwtype;
    //EditText etstype;
    Button submit;
    public String action ="warereg",username,phone,password,Area,District,State,PIN,EReg;


    public String wname,wregno,wloc,regdate,regperiod,capacity,availability,rent,wtype,stype;
    public int agri=0,veg=0,elec=0,mach=0,texttile=0,chem=0,food=0,others=0;

    ActionBar actionBar;
    int flag = 0;
    JSONParser jsonParser = new JSONParser();
    private static String url = "http://warehouse.netii.net/index.php";
    private static final String TAG_SUCCESS = "success";

    AlertDialog dialog;
    public  final List<String> seletedItems=new ArrayList();
  //  public LatLng latLng;

    final String[] items = {"Fruits","Vegetables","Electronics","Machinery" ,"Textile","Pulses and Cerels",
            "FoodItems","Others"};
    // arraylist to keep the selected items
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
       /* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_details);

        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        //Go To Login.java
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroupcold = (RadioGroup) findViewById(R.id.radiogroupcold);
       cwc=(RadioButton)findViewById(R.id.radiocwc);
        swc=(RadioButton)findViewById(R.id.radiosws);
        pvt=(RadioButton)findViewById(R.id.radiopvt);
        pcs=(RadioButton)findViewById(R.id.radiopcs);
        rcold=(RadioButton)findViewById(R.id.radiocold);
        rnormal=(RadioButton)findViewById(R.id.radionormal);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radiocwc){
                    wtype="CWC";
                }
                else if(checkedId==R.id.radiosws){
                    wtype="SWC";
                }
                else if(checkedId==R.id.radiopvt){
                    wtype="PVT";
                }
                else if(checkedId==R.id.radiopcs){
                    wtype="PCS";
                }
            }
        });
                radioGroupcold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radiocold){
                    stype="cold";cold=1;}
                else if(checkedId==R.id.radionormal)
                {stype="normal";cold=0;}
            }
        });


        etwname = (EditText) findViewById(R.id.warname);
        etwregno = (EditText) findViewById(R.id.warregno);
        //etwloc = (EditText) findViewById(R.id.warlocation);
        //etregdate = (EditText) findViewById(R.id.warregdate);
        //etregperiod = (EditText) findViewById(R.id.warregperiod);
        etcapacity = (EditText) findViewById(R.id.warcapacity);
        regDate= (EditText) findViewById(R.id.regdate);
        regMonth= (EditText) findViewById(R.id.regmonth);
        regYear= (EditText) findViewById(R.id.regyear);
        vregDate= (EditText) findViewById(R.id.vregdate);
        vregMonth= (EditText) findViewById(R.id.vregmonth);
        vregYear= (EditText) findViewById(R.id.vregyear);
        etavailabilty = (EditText) findViewById(R.id.availability);
        etrent= (EditText) findViewById(R.id.rent);
       // etwtype = (EditText) findViewById(R.id.wartype);
        //etstype= (EditText) findViewById(R.id.storagetype);
      fbutton=(Button)findViewById(R.id.bfacility);
        Bundle bundle = this.getIntent().getExtras();
        username=bundle.getString("pusername");
     //   Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
        password=bundle.getString("ppassword");
        phone=bundle.getString("pphone");
        Area=bundle.getString("pArea");
        District=bundle.getString("pDistrict");
        State=bundle.getString("pState");
        PIN=bundle.getString("pPIN");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        wname= etwname.getText().toString();
        wregno = etwregno.getText().toString();
//        wloc= etwloc.getText().toString();
        //regdate = etregdate.getText().toString();
        //regperiod = etregperiod.getText().toString();
        capacity= etcapacity.getText().toString();
        availability = etavailabilty.getText().toString();
        rent = etrent.getText().toString();

       // stype = etstype.getText().toString();




        submit = (Button) findViewById(R.id.warsubmit);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletedItems.clear();

                builder.setTitle("Select Storages Available");
                builder.setMultiChoiceItems(items, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            // indexSelected contains the index of item (of which checkbox checked)
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected,
                                                boolean isChecked) {

                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    // write your code when user checked the checkbox
                                    seletedItems.add(items[indexSelected]);
                                 //   Toast.makeText(getApplicationContext(),items[indexSelected],Toast.LENGTH_LONG).show();
                                } else if (seletedItems.contains(items[indexSelected])) {
                                    // Else, if the item is already in the array, remove it
                                    // write your code when user Uchecked the checkbox
                                    seletedItems.remove(items[indexSelected]);
                                }
                            }
                        })
                        // Set the action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                fbutton.setEnabled(false);
                              dialog.dismiss();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel
                                dialog.dismiss();

                            }
                        });

                dialog = builder.create();//AlertDialog dialog; create like this outside onClick
                dialog.show();
            }


        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int m=Integer.valueOf(regMonth.getText().toString());
                int d=Integer.valueOf(regDate.getText().toString());
                int y=Integer.valueOf(regYear.getText().toString());
                int vm=Integer.valueOf(vregMonth.getText().toString());
                int vd=Integer.valueOf(vregDate.getText().toString());
                int vy=Integer.valueOf(vregYear.getText().toString());
                if(m <=0 || m>12 || d<=0 ||d >31 || y<=1900 || y>=2500 ||vm <=0 || vm>12 ||vd<=0 ||vd >31 ||vy<=1900 || vy >=2500){
                    Toast.makeText(DetailsWarehouse.this,"Invalid Date",Toast.LENGTH_SHORT).show();
                    return;
                }



                //from login.java
                try{new loginAccess().execute(stype,wtype);
                    submit.setEnabled(false);}
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Error in submitting",Toast.LENGTH_SHORT).show();
                }
            }
       });

            //code to check online details
          /*  private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return false;
            }*/
            //Close code that check online details
        //Close log in

    }


    class loginAccess extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailsWarehouse.this);
            pDialog.setMessage("Signing in....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }




        String wname= etwname.getText().toString();
        String wregno = etwregno.getText().toString();
       String date=regDate.getText().toString();
        String month=regMonth.getText().toString();
        String year=regYear.getText().toString();
        String vdate=vregDate.getText().toString();
        String vmonth=vregMonth.getText().toString();
        String vyear=vregYear.getText().toString();
       // String wloc= etwloc.getText().toString();
        String regdate =month+"/"+date+"/"+year;
        String regperiod =vmonth+"/"+vdate+"/"+vyear;
        //String regperiod = etregperiod.getText().toString();
        String capacity= etcapacity.getText().toString();
        String availability = etavailabilty.getText().toString();
        String rent = etrent.getText().toString();


        Bundle bundle = getIntent().getExtras();
        String username=bundle.getString("pusername");
        String password=bundle.getString("ppassword");
        String Area=bundle.getString("pArea");
        String District=bundle.getString("pDistrict");
        String State=bundle.getString("pState");
        String PIN=bundle.getString("pPIN");
        String phone=bundle.getString("pPhone");

   String address=Area+","+District;

       LatLng latlng= getLocationFromAddress(getApplicationContext(),address);

        double lat = latlng.latitude;
        double lng = latlng.longitude;

String st_type,wt_type;
        @Override
        protected String doInBackground(String... arg0) {
            /*"Fruits","Vegetables","Electronics","Machinery" ,"Textile","Chemicals",
                    "FoodItems","Others"};*/
        st_type=arg0[0];
        wt_type=arg0[1];

            if(seletedItems.contains("Fruits")){agri=cold+1;}
            if(seletedItems.contains("Vegetables")){veg=cold+1;}
            if(seletedItems.contains("Electronics")){elec=cold+1;}
            if(seletedItems.contains("Machinery")){mach=cold+1;}
            if(seletedItems.contains("Textile")){texttile=cold+1;}
            if(seletedItems.contains("Chemicals")){chem=cold+1;}
            if(seletedItems.contains("FoodItems")){food=cold+1;}
            if(seletedItems.contains("Others")){others=cold+1;}

            List<NameValuePair> params = new ArrayList<>();



            params.add(new BasicNameValuePair("action",action));
            params.add(new BasicNameValuePair("name",username));
            params.add(new BasicNameValuePair("phone",phone));
            params.add(new BasicNameValuePair("password",password));
            params.add(new BasicNameValuePair("Area",Area));
            params.add(new BasicNameValuePair("District",District));
            params.add(new BasicNameValuePair("State",State));
            params.add(new BasicNameValuePair("PIN",PIN));
            params.add(new BasicNameValuePair("wname",wname));

            params.add(new BasicNameValuePair("wregno",wregno));

            params.add(new BasicNameValuePair("regdate",regdate));
            params.add(new BasicNameValuePair("regperiod",regperiod));
            params.add(new BasicNameValuePair("wcapacity",capacity));
            params.add(new BasicNameValuePair("wavailability",availability));
            params.add(new BasicNameValuePair("rent",rent));
            params.add(new BasicNameValuePair("type",wt_type));
            params.add(new BasicNameValuePair("storage_type",st_type));
            params.add(new BasicNameValuePair("agriculture",Integer.toString(agri)));
            params.add(new BasicNameValuePair("vegetables",Integer.toString(veg)));
            params.add(new BasicNameValuePair("electronics",Integer.toString(elec)));
            params.add(new BasicNameValuePair("machinery",Integer.toString(mach)));
            params.add(new BasicNameValuePair("texttiles",Integer.toString(texttile)));
            params.add(new BasicNameValuePair("chemicals",Integer.toString(chem)));
            params.add(new BasicNameValuePair("food",Integer.toString(food)));
            params.add(new BasicNameValuePair("others",Integer.toString(others)));
            params.add(new BasicNameValuePair("latitude",Double.toString(lat)));
            params.add(new BasicNameValuePair("longitude",Double.toString(lng)));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //int agri,veg,elec,mach,texttile,chem,food,others;
            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
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
                    if(EReg.equals("Registered"))
                    {
                        Toast.makeText(DetailsWarehouse.this, "Number already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(DetailsWarehouse.this, "Phone number already registered !", Toast.LENGTH_SHORT).show();
                }
                if (flag == 0) {
                    mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putBoolean(welcomeScreenShownPref, true);
                    editor.commit(); // Very important to save the preference

                    Bundle bundle = new Bundle();
                    bundle.putString("ownerphone",phone);
                    Toast.makeText(DetailsWarehouse.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),OwnerRegSms.class);
                    i.putExtras(bundle);

                    startActivity(i);
                }

        }

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),"Error in Area",Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
                p1=new LatLng(0.0,0.0);
                return p1;
        }

        return p1;
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(pDialog!= null)
            pDialog.dismiss();
    }
}








































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































