package nearbyproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.w.maps.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class PlaceDetailsActivity extends ActionBarActivity {

	WebView mWvPlaceDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_details);

		// Getting reference to WebView ( wv_place_details ) of the layout
		// activity_place_details
		mWvPlaceDetails = (WebView) findViewById(R.id.wv_place_details);

		mWvPlaceDetails.getSettings().setUseWideViewPort(false);

		// Getting place reference from the map
		String reference = getIntent().getStringExtra("reference");
		Toast.makeText(getApplicationContext(), reference + " End ", Toast.LENGTH_SHORT).show();

		StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
		sb.append("reference=" + reference);
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyCfdXATlz7jtM6MEvy9Xh_3_g_Ivc5ysXE");

		// Creating a new non-ui thread task to download Google place details
		PlacesTask placesTask = new PlacesTask();

		// Invokes the "doInBackground()" method of the class PlaceTask
		placesTask.execute(sb.toString());

	};

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();
			br.close();

		} catch (Exception e) {
			Log.d("Exception", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	/** A class, to download Google Place Details */
	private class PlacesTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {
			ParserTask parserTask = new ParserTask();

			// Start parsing the Google place details in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Place Details in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, HashMap<String, String>> {

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected HashMap<String, String> doInBackground(String... jsonData) {

			HashMap<String, String> hPlaceDetails = null;
			PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Start parsing Google place details in JSON format
				hPlaceDetails = placeDetailsJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return hPlaceDetails;
		}

		@Override
		protected void onPostExecute(HashMap<String, String> hPlaceDetails) {

			String name = hPlaceDetails.get("name");
			String icon = hPlaceDetails.get("icon");
			String vicinity = hPlaceDetails.get("vicinity");
			String lat = hPlaceDetails.get("lat");
			String lng = hPlaceDetails.get("lng");
			String formatted_address = hPlaceDetails.get("formatted_address");
			String formatted_phone = hPlaceDetails.get("formatted_phone");
			String website = hPlaceDetails.get("website");
			String rating = hPlaceDetails.get("rating");
			String international_phone_number = hPlaceDetails.get("international_phone_number");
			String url = hPlaceDetails.get("url");

			String mimeType = "text/html";
			String encoding = "utf-8";

			String data = "<img style='float: left;' src=" + icon + " alt='' /><center>" + name + "</center>"
					+ "<br style='clear: both;' />" + "<hr />" + "Vicinity : " + vicinity + "" + "Location : " + lat
					+ "," + lng + "" + "Address : " + formatted_address + "" + "Phone : " + formatted_phone + ""
					+ "Website : " + website + "" + "Rating : " + rating + "" + "International Phone : "
					+ international_phone_number + "" + "URL : <a href=" + url + ">" + url + "</a>" + "";

			// Setting the data in WebView
			mWvPlaceDetails.loadDataWithBaseURL("", data, mimeType, encoding, "");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
