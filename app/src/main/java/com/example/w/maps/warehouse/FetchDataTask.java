package com.example.w.maps.warehouse;

/**
 * Created by ganesh on 21/11/15.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.w.maps.warehouse.model.FetchDataListener;
import com.example.w.maps.warehouse.model.MyList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchDataTask extends AsyncTask<String, Void, String> {
    private final FetchDataListener listener;
    private String msg;
    MyList list=new MyList();
    WarehouseDetails app;
    int keyMost,keyRate,keyCold,keyCost,keyAcc;
    String keySearch;
    JSONObject json;
    String sJson,productVal;

    public FetchDataTask(FetchDataListener listener,int keyCost,int keyMost,int keyRate,int keyCold,int keyAcc,String  productVal,String keySearch) {
        this.listener = listener;
        this.keyCost=keyCost;
        this.keyMost=keyMost;
        this.keyRate=keyRate;
        this.keyCold=keyCold;
        this.keyAcc=keyAcc;
        this.productVal=productVal;
        this.keySearch=keySearch;
    }

    @Override
    protected String doInBackground(String... params) {
        if(params == null) return null;

        // get url from params
        String url = params[0];

        try {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(20);

            String str= String.valueOf(keyCost);
            nameValuePairs.add(new BasicNameValuePair("keyCost", str));
            String str1= String.valueOf(keyMost);
            nameValuePairs.add(new BasicNameValuePair("keyMost", str1));
            String str2= String.valueOf(keyRate);
            nameValuePairs.add(new BasicNameValuePair("keyRate", str2));
            String str3= String.valueOf(keyCold);
            nameValuePairs.add(new BasicNameValuePair("keyCold", str3));
            String str4= String.valueOf(keyAcc);
            nameValuePairs.add(new BasicNameValuePair("keyAcc", str4));
            String str9= String.valueOf(productVal);
            nameValuePairs.add(new BasicNameValuePair("productVal",str9));
            String str10= String.valueOf(keySearch);
            nameValuePairs.add(new BasicNameValuePair("keySearch", str10));


            sJson = jsonParser.makeHttpRequest(url,"POST",nameValuePairs);

            return sJson;
        }
        catch(Exception e){
            msg = "No Network Connection";
        }
        return null;
    }

   @Override
    protected void onPostExecute(String sJson) {
        if(sJson == null) {
            msg="please connect to Internet!!...nd try again";
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }

        try {
            // convert json string to json array
                        // create apps list
            //Log.e("List",sJson);

            int jsonStart = sJson.indexOf("[");
            int jsonEnd = sJson.lastIndexOf("]");
            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                sJson = sJson.substring(jsonStart, jsonEnd + 1);
            }

            JSONArray aJson= new JSONArray(sJson);
            json=new JSONObject();
               // Log.e("TestEntry","its happening");

            for(int i=0; i<aJson.length(); i++) {
                 json= aJson.getJSONObject(i);
//                Log.e("json",json.toString());
                 app= new WarehouseDetails();

                app.setuId(json.getInt("uid"));
                app.setWname(json.getString("wname"));
                app.setWregno(json.getString("wregno"));
                app.setWaddress1(json.getString("Area"));
                app.setWaddress2(json.getString("District"));
                app.setWaddress3(json.getString("State"));
                app.setRegdate(json.getString("regdate"));
                app.setRegperiod(json.getString("regperiod"));
                app.setWcapacity(json.getInt("wcapacity"));
                app.setWavailability(json.getInt("wavailability"));
                app.setRent(json.getInt("rent"));
                app.setType(json.getString("type"));
                app.setStorage_type(json.getString("storage_type"));
                app.setRating(json.getInt("rating"));
                app.setNoRaters(json.getInt("noRaters"));
                app.setOwnName(json.getString("owner_name"));
                app.setPhone(json.getString("contact"));

                // add the app to apps list
                list.add(app);

            }


            //notify the activity that fetch data has been complete
            if(listener != null)
            {
                listener.onFetchComplete(list);
            }
        }
        catch (JSONException e) {
            msg = "Invalid response   :";
            if(listener != null) listener.onFetchFailure(msg+e.getMessage());
            Log.e("msg", e.getMessage());
        }
    }


}

