package myapplication.transportwarehouse;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.w.maps.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import myapplication.Transport_Full_details;
import myapplication.transportwarehouse.adapters.TransportAdapter;
import myapplication.transportwarehouse.model.TransportDetails;
import nearbyproject.JSONParser1;

/**
 * Created by anurag on 11/15/2015.
 */
public class Transportdetails extends ActionBarActivity implements View.OnClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;

    List<TransportDetails> list = null;
    ListView listView;
    TransportAdapter transportAdapter;
int inputSelection;
    Button filter,sort;
    int gen;
    int types;
    Float rent,cost,rate,ratingbar;
    String variety,source,destination;
    AlertDialog.Builder[] alert,alert1,filtering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transport_details);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);

        cost=0.0f;
        Intent intent=getIntent();
        source = intent.getExtras().getString("Clocation");
        destination=intent.getExtras().getString("Olocation");
        rent=Float.valueOf(intent.getExtras().getString("rent"));

     //   Toast.makeText(getApplicationContext(),String.valueOf("source= "+source+"  dest ="+destination+" "),Toast.LENGTH_LONG).show();
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
      //  mContentView = findViewById(R.id.fullscreen_content);

        filter = (Button) findViewById(R.id.dummy_button);
        sort = (Button) findViewById(R.id.dummy_button1);


        final String[] photo = {"Cost","Rating"};
        final String[] type = {"T1", "T2", "T3"};
        alert = new AlertDialog.Builder[]{new AlertDialog.Builder(this)};
        filtering = new AlertDialog.Builder[] {new AlertDialog.Builder(this)};
        filtering[0].setTitle("Filter type:");
        filtering[0].setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                switch(types){
                    case 1:
                        variety="T1";
                        break;
                    case 2:
                        variety="T2";
                        break;
                    case 3:
                        variety="T2";
                        break;
                }
                new task1().execute();
            }
        });
        alert[0].setTitle("Sort by:");
        alert[0].setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                switch(gen){
                    case 1:
                        new costtask().execute();
                        break;
                    case 2:
                        new ratingtask().execute();
                        break;
                }
            }
        });

        alert[0].setSingleChoiceItems(photo, -1, new
                DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (photo[which].equals("cost"))
                        {
                            gen = 1;
                        } else if (photo[which].equals("rating"))
                        {
                            gen = 2;
                        }
                    }
                });

        filtering[0].setSingleChoiceItems(type, -1, new
                DialogInterface.OnClickListener()

                {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0 : types = 1;
                                break;
                            case 1 : types = 2;
                                break;
                            case 2 : types =3;
                        }
                    }

                });



        // Set up the user interaction to manually show or hide the system UI.
       /* mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        new task().execute();

        filter.setOnClickListener(this);
        sort.setOnClickListener(this);
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dummy_button:
                alert[0].show();
                if(gen==1)
                    ;//sortby cost
                else if(gen==2)
                    ;//sortby rating
                break;
            case R.id.dummy_button1:
                filtering[0].show();
                break;
        }
    }
    //Class to sort through rating
    class ratingtask extends AsyncTask<String,String,Void>
    {
        private ProgressDialog progressDialog=new ProgressDialog(Transportdetails.this);
        InputStream is=null;
        String result="";
        @Override
        protected Void doInBackground(String... strings) {
            String url_select = "http://warehouse.netii.net/trans_cost.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


           // progressDialog.dismiss();
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(3);
            param.add(new BasicNameValuePair("variety", "t1"));
            param.add(new BasicNameValuePair("source", source));
            param.add(new BasicNameValuePair("destination", destination));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();
             //   Toast.makeText(getApplicationContext(), "here:" + is, Toast.LENGTH_SHORT).show();
               // Log.e("filter result", "is is here" + is);

            } catch (Exception e) {
                //Log.e("log_tag", "Error in http connection" + e.toString());
                //    Toast.makeText(Tabs.this, "Please try again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
               // Toast.makeText(getApplicationContext(), "RESULT FILTER1:" + result, Toast.LENGTH_SHORT).show();
                //Log.d("string", "string1 is: " + result);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Sorting by cost...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String id,name,licence ,phone,email,total_vehicle;
            int id1;
            list = new ArrayList<TransportDetails>();
            try{

                JSONArray Jarray=new JSONArray(result);
          //  Toast.makeText(getBaseContext(), "ratingtask" + result, Toast.LENGTH_SHORT).show();
                for(int i=0;i<Jarray.length();i++)
                {
                    try{
                        JSONObject Jasonobject=null;
                        Jasonobject=Jarray.getJSONObject(i);
                        id=Jasonobject.getString("id");
                        id1= Integer.parseInt(id);

                        name=Jasonobject.getString("name");
                        licence=Jasonobject.getString("licence");
                        phone=Jasonobject.getString("phone");
                        email=Jasonobject.getString("email");
                        total_vehicle=Jasonobject.getString("total_vehicle");
                        list.add(new TransportDetails(id1,phone, licence, email, name, total_vehicle, 5,cost));
                    }catch(JSONException j){
                        j.printStackTrace();
                    }
                }
              //  transportAdapter.notifyDataSetChanged();
                this.progressDialog.dismiss();
            }catch (Exception e){
                Log.e("log_tag", "Error parsing data" + e.toString());
            }

            transportAdapter = new TransportAdapter(list,getApplicationContext());

            listView = (ListView) findViewById(R.id.fullscreen_contentlist);
            listView.setAdapter(transportAdapter);
            listView.setItemsCanFocus(true);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(getApplicationContext(), Transport_Full_details.class);

                    String name1  = (list.get(position)).getName();
                    String addr = list.get(position).getAddress();
                    String pno = list.get(position).getPhone_no();
                    String service = list.get(position).getService();
                    String type = list.get(position).getTypes();
                    Float rating = list.get(position).getRating();

                    Bundle extras = new Bundle();

                    String[] extra = new String[] {name1,addr,pno,service,type,rating.toString()};
                    extras.putStringArray("EXTRAS",extra);
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 1) {
                        toggle();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }
    //Class to sort through cost
    class costtask extends AsyncTask<String,String,Void>
    {
        private ProgressDialog progressDialog=new ProgressDialog(Transportdetails.this);
        InputStream is=null;
        String result="";
        @Override
        protected Void doInBackground(String... strings) {
            String url_select = "http://warehouse.netii.net/trans_cost.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


            //progressDialog.dismiss();
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(3);
            param.add(new BasicNameValuePair("variety", "t1"));
            param.add(new BasicNameValuePair("source", source));
            param.add(new BasicNameValuePair("destination", destination));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();
            //    Toast.makeText(getApplicationContext(), "here:" + is, Toast.LENGTH_SHORT).show();
              //  Log.e("filter result", "is is here" + is);

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection" + e.toString());
                //    Toast.makeText(Tabs.this, "Please try again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                //Toast.makeText(getApplicationContext(), "RESULT FILTER1:" + result, Toast.LENGTH_SHORT).show();
                //Log.d("string", "string1 is: " + result);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Sorting by cost...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String id,name,licence ,phone,email,total_vehicle;
            int id1;
            list = new ArrayList<TransportDetails>();
            try{

                JSONArray Jarray=new JSONArray(result);
               // Toast.makeText(getBaseContext(), "costTask" + result, Toast.LENGTH_SHORT).show();
                for(int i=0;i<Jarray.length();i++)
                {
                    try{
                        JSONObject Jasonobject=null;
                        Jasonobject=Jarray.getJSONObject(i);
                        id=Jasonobject.getString("id");
                        id1= Integer.parseInt(id);

                        name=Jasonobject.getString("name");
                        licence=Jasonobject.getString("licence");
                        phone=Jasonobject.getString("phone");
                        email=Jasonobject.getString("email");
                        total_vehicle=Jasonobject.getString("total_vehicle");
                        list.add(new TransportDetails(id1,phone, licence, email, name, total_vehicle, 5,cost));
                    }catch(JSONException j){
                        j.printStackTrace();
                    }
                }
                this.progressDialog.dismiss();
            }catch (Exception e){
                Log.e("log_tag", "Error parsing data" + e.toString());
            }

            transportAdapter = new TransportAdapter(list,getApplicationContext());

            listView = (ListView) findViewById(R.id.fullscreen_contentlist);
            listView.setAdapter(transportAdapter);
            listView.setItemsCanFocus(true);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(getApplicationContext(), Transport_Full_details.class);

                    String name1  = (list.get(position)).getName();
                    String addr = list.get(position).getAddress();
                    String pno = list.get(position).getPhone_no();
                    String service = list.get(position).getService();
                    String type = list.get(position).getTypes();
                    Float rating = list.get(position).getRating();
                    Float cost1=list.get(position).getCost();

                    Bundle extras = new Bundle();

                    String[] extra = new String[] {name1,addr,pno,service,type,rating.toString(),cost1.toString()};
                    extras.putStringArray("EXTRAS",extra);
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 1) {
                        toggle();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }
    //Class to parse varity based transport
    class task1 extends AsyncTask<String,String,Void>
    {
        private ProgressDialog progressDialog=new ProgressDialog(Transportdetails.this);
        InputStream is=null;
        String result="";
        @Override
        protected Void doInBackground(String... strings) {

            String url_select = "http://warehouse.netii.net/trans_variety.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);

            for (int i = 0; i < 20; i++)
                publishProgress();
            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(3);
            param.add(new BasicNameValuePair("variety", variety));
            param.add(new BasicNameValuePair("source", source));
            param.add(new BasicNameValuePair("destination", destination));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();
                //Toast.makeText(getApplicationContext(), "here:" + is, Toast.LENGTH_SHORT).show();
                //Log.e("filter result", "is is here" + is);

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection" + e.toString());
                //    Toast.makeText(Tabs.this, "Please try again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                //Toast.makeText(getApplicationContext(), "RESULT FILTER1:" + result, Toast.LENGTH_SHORT).show();
                //Log.d("string", "string1 is: " + result);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Filtering...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String id,name,licence ,phone,email,total_vehicle;
            int id1;
            list = new ArrayList<TransportDetails>();
            try{

                JSONArray Jarray=new JSONArray(result);
                //Toast.makeText(getBaseContext(), "task1" + result, Toast.LENGTH_SHORT).show();
                for(int i=0;i<Jarray.length();i++)
                {
                    try{
                        JSONObject Jasonobject=null;
                        Jasonobject=Jarray.getJSONObject(i);
                        id=Jasonobject.getString("id");
                        id1= Integer.parseInt(id);

                        name=Jasonobject.getString("name");
                        licence=Jasonobject.getString("licence");
                        phone=Jasonobject.getString("phone");
                        email=Jasonobject.getString("email");
                        total_vehicle=Jasonobject.getString("total_vehicle");
                        list.add(new TransportDetails(id1,phone, licence, email, name, total_vehicle, ratingbar,cost));
                    }catch(JSONException j){
                        j.printStackTrace();
                    }
                }
                this.progressDialog.dismiss();
            }catch (Exception e){
                Log.e("log_tag", "Error parsing data" + e.toString());
            }

            transportAdapter = new TransportAdapter(list,getApplicationContext());

            listView = (ListView) findViewById(R.id.fullscreen_contentlist);
            listView.setAdapter(transportAdapter);
            listView.setItemsCanFocus(true);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(getApplicationContext(), Transport_Full_details.class);

                    String name1  = (list.get(position)).getName();
                    String addr = list.get(position).getAddress();
                    String pno = list.get(position).getPhone_no();
                    String service = list.get(position).getService();
                    String type = list.get(position).getTypes();
                    Float rating = list.get(position).getRating();

                    Bundle extras = new Bundle();

                    String[] extra = new String[] {name1,addr,pno,service,type,rating.toString()};
                    extras.putStringArray("EXTRAS",extra);
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 1) {
                        toggle();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }
    //Class to parse the transport database
    class task extends AsyncTask<String,String,Void>
    {
        private ProgressDialog progressDialog=new ProgressDialog(Transportdetails.this);
        InputStream is=null;
        String result="";
        String sJson;
        @Override
        protected Void doInBackground(String... params) {
            String url_select = "http://warehouse.netii.net/trans_location.php";
            JSONParser1 jsonParser = new JSONParser1();
            /*JSONParser jsonParser=new JSONParser();
            try {
                result=jsonParser.getJSONFromUrl(url_select);
                Log.d("here i am", "yeah !!!!!!!!!" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(2);
            //param.add(new BasicNameValuePair("variety","T1"));
            param.add(new BasicNameValuePair("source", source));
            param.add(new BasicNameValuePair("destination",destination));
            sJson = jsonParser.makeHttpRequest(url_select,"POST",param);

            //Log.e("sJson", " " + sJson);
            return null;
        }
        protected void onPostExecute(Void v){
            String id,name,licence ,phone,email,total_vehicle;
            int id1;
            list = new ArrayList<TransportDetails>();
            try{

                JSONArray Jarray=new JSONArray(sJson);
             //  Toast.makeText(getBaseContext(), "task" + sJson, Toast.LENGTH_SHORT).show();
                for(int i=0;i<Jarray.length();i++)
                {
                    try{
                        JSONObject Jasonobject=null;
                        Jasonobject=Jarray.getJSONObject(i);
                        id=Jasonobject.getString("id");
                        id1= Integer.parseInt(id);

                        name=Jasonobject.getString("name");
                        licence=Jasonobject.getString("licence");
                        phone=Jasonobject.getString("phone");
                        email=Jasonobject.getString("email");
                        total_vehicle=Jasonobject.getString("total_vehicle");
                        rate=Float.valueOf(Jasonobject.getString("total_vehicle"));
                        ratingbar=Float.valueOf(Jasonobject.getString("rating"));
                        cost=rate+rent;
                        list.add(new TransportDetails(id1,phone, licence, email, name, total_vehicle,ratingbar,cost));
                    }catch(JSONException j){
                        j.printStackTrace();
                    }
                }
                this.progressDialog.dismiss();
            }catch (Exception e){
                Log.e("log_tag", "Error parsing data" + e.toString());
            }

            transportAdapter = new TransportAdapter(list,getApplicationContext());

            listView = (ListView) findViewById(R.id.fullscreen_contentlist);
            listView.setAdapter(transportAdapter);
            listView.setItemsCanFocus(true);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(getApplicationContext(), Transport_Full_details.class);

                    String name1  = (list.get(position)).getName();
                    String addr = list.get(position).getAddress();
                    String pno = list.get(position).getPhone_no();
                    String service = list.get(position).getService();
                    String type = list.get(position).getTypes();
                    Float rating = list.get(position).getRating();

                    Bundle extras = new Bundle();

                    String[] extra = new String[] {name1,addr,pno,service,type,rating.toString()};
                    extras.putStringArray("EXTRAS",extra);
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 1) {
                        toggle();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
        protected void onPreExecute(){
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    task.this.cancel(true);
                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
          //  actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            /*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
    /*    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*/
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}