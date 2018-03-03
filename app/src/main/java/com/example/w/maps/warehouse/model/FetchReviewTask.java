package com.example.w.maps.warehouse.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.w.maps.warehouse.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ganesh on 24/12/15.
 */
public class FetchReviewTask extends AsyncTask<String, Void, String> {

    private final FetchReviewListener listener;

    int uid;
    String msg;
    JSONObject json;
    String sJson;

    public ReviewData [] data=new ReviewData[8];

    public FetchReviewTask(FetchReviewListener listener, int uid) {
        this.uid=uid;
        this.listener=listener;
    }
    @Override
    protected String doInBackground(String... params) {
        if(params == null) return null;

        // get url from params
        String url = params[0];

        try {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
            String str = String.valueOf(uid);
            nameValuePairs.add(new BasicNameValuePair("uid", str));
            Log.e("uid passed in back is", Integer.toString(uid));

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
            msg="no sJson";
            if(listener != null) listener.onFetchFailure(msg);
            return;
        }

        try {
            // convert json string to json array
            // create apps list
             Log.e("ReviewJSON", sJson);

            int jsonStart = sJson.indexOf("[");
            int jsonEnd = sJson.lastIndexOf("]");
            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                sJson = sJson.substring(jsonStart, jsonEnd + 1);
            }

            JSONArray aJson = new JSONArray(sJson);
            int i;
            for( i=0; i<aJson.length(); i++) {
                json = aJson.getJSONObject(i);
                data[i] = new ReviewData();
                data[i].comment = json.getString("txt_comment");
                data[i].name = json.getString("name");
            }
            while(i<=5)
            {
                data[i] = new ReviewData();
                data[i].comment=" ";
                data[i].name=" ";
                i++;
            }
            if(listener != null)
            {
                listener.onFetchComplete(data);
            }

        }
        catch (JSONException e) {
            msg = "Invalid response   :";
            if(listener != null) listener.onFetchFailure(msg+e.getMessage());
            Log.e("msg review", e.getMessage());
        }

    }
}
