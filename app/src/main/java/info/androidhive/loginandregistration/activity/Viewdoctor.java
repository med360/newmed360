package info.androidhive.loginandregistration.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.adapter.CustomListAdapter;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.app.PlaceJSONParser;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Movie;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


//from location act

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;





import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;


import com.android.volley.toolbox.JsonArrayRequest;
public class Viewdoctor extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    //from location act



    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://azmediame.net/med360/new/get_all_doctors.php";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    //private EditText loc;
    private CustomListAdapter adapter;
    private SessionManager session;
    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    private Button detbtn;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent = new Intent(Viewdoctor.this,
                            Viewdoctor.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.messg:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent2 = new Intent(Viewdoctor.this,
                            Viewdoctor.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.profilenav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent3 = new Intent(Viewdoctor.this,
                            MainActivity.class);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.logoutnav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    session = new SessionManager(getApplicationContext());
                    session.setLogin(false);
                    session.logoutUser();
                    return true;

            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {




        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdoctor);

detbtn= (Button) findViewById(R.id.detbtn);

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                             public void onItemClick(AdapterView adapterView, View view, int position, long id) {

 HashMap<String, String> hm = (HashMap<String, String>) adapterView.getItemAtPosition(position);
    String str = hm.get("description");
    String selplaceid=  hm.get("placeid");
    Log.e("gplaces", "the id of the selected place is: "+selplaceid);
   atvPlaces.setText(str);
String api_key="AIzaSyB23yTJ6rI0HPYLw1LBXRI4duiQscXhEx0";
String placedetailurl="https://maps.googleapis.com/maps/api/place/details/json?placeid="+selplaceid+"&key="+api_key+"";

  Log.e("placedetail", "code to clear cache is below");
 AppController.getInstance().getRequestQueue().getCache().remove(placedetailurl);
   String tag_string_req = "req_login";                                               StringRequest strReq = new StringRequest(Request.Method.POST, placedetailurl, new Response.Listener<String>() {

    @Override
      public void onResponse(String response) {
   Log.d(TAG, "place detail Response: " + response.toString());

  Log.e("placedetail", "response received");
        try {
            JSONObject jObj = new JSONObject(response);
            JSONObject result = jObj.getJSONObject("result");
            JSONObject geometry = result.getJSONObject("geometry");
            JSONObject sellocation = geometry.getJSONObject("location");
            String lat = sellocation.getString("lat");
            String lng = sellocation.getString("lng");
            Log.e("placedetail", "Latitude is: "+lat+"Longtiude is: "+lng+"");
            get_all_doctors(lat,lng);
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
                                                 }, new Response.ErrorListener() {

                                                     @Override
                                                     public void onErrorResponse(VolleyError error) {
                                                         Log.e(TAG, "Login Error: " + error.getMessage());
                                                         Toast.makeText(getApplicationContext(),
                                                                 error.getMessage(), Toast.LENGTH_LONG).show();

                                                     }
                                                 }) {

                                                     @Override
                                                     protected Map<String, String> getParams() {
                                                         // Posting parameters to login url
                      Log.e("medlogin", "inside getparams method and the email storing in hashmap ");
                                                         Map<String, String> params = new HashMap<String, String>();
                                               return params;
                                                     }

                                                 };

                                                 // Adding request to request queue
                                                 AppController.getInstance().addToRequestQueue(strReq, tag_string_req);








            Toast.makeText(getApplicationContext(), selplaceid, Toast.LENGTH_SHORT).show();

                                             }
                                         });

        listView = (ListView) findViewById(R.id.list);
        //loc=(EditText) findViewById(R.id.location);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        displayLocation();

        // Get listview
       // ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String did = ((TextView) view.findViewById(R.id.did)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        DoctorProfile.class);
                // sending pid to next activity
                in.putExtra("did", did);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
       // loc.setOnClickListener(new View.OnClickListener() {

      //      @Override
      //      public void onClick(View v) {
      //          togglePeriodicLocationUpdates();
       //     }
       // });


        detbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                displayLocation();
            }
        });

        atvPlaces.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               atvPlaces.setText("");
            }
        });






        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

    }





    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
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
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception dloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }







public void get_all_doctors(final String latit,final String longit){
    Log.e("dsp", "before volley request");
    // changing action bar color


    // Creating volley request obj
adapter.clearData();
    adapter.notifyDataSetChanged();
    String tag_string_req = "req_login";
    Log.e("medlogin", "code to clear cache is below");
    AppController.getInstance().getRequestQueue().getCache().remove(url);
    StringRequest strReq = new StringRequest(Request.Method.POST,
            url, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            hidePDialog();
            try {

                Log.e("dsp", "inside onresponse");





                JSONObject jObj = new JSONObject(response);
                boolean success = jObj.getBoolean("success");


                if (success) {

                    JSONArray doctorarray = jObj.getJSONArray("doctor");


                    Log.e("dsp", "inside onresponse: " + doctorarray);
// Parsing json
                    for (int i = 0; i < doctorarray.length(); i++) {
                        try {
                            Log.e("dsp", "inside response loop");
                            JSONObject obj = doctorarray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setTitle(obj.getString("name"));
                            movie.setThumbnailUrl(obj.getString("image"));
                            movie.setRating(((Number) obj.getDouble("rating")).doubleValue());
                            movie.setYear(obj.getString("add"));
                            movie.setGenre(obj.getString("specialit"));
                            movie.setDid(obj.getString("did"));

                            // adding movie to movies array
                            movieList.add(movie);
                            Log.e("dsp", "added to list");
                            Log.e("medlogin", "code to clear cache is below");
                            AppController.getInstance().getRequestQueue().getCache().remove(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    // Error in login. Get the error message
                    Log.e("medlogin", "error in listing doctor");
                    String errorMsg = jObj.getString("message");

                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();

        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Doctor receive Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hidePDialog();
        }

    }) {

        @Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Log.e("medlogin", "inside getparams method ");
            Map<String, String> params = new HashMap<String, String>();
            params.put("latit", latit);
            params.put("longit", longit);

            return params;
        }

    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
}





    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }}
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            stopLocationUpdates();}
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {
        Log.e("dsp", "before permission check in displaylocation()");
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        //  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        //ActivityCompat.requestPermissions(this,Manifest.permission.ACCESS_COARSE_LOCATION,1)

        Log.e("dsp", "Periodic location updates started! in displaylocation()");


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            String latit=""+latitude;
            String longit=""+longitude;
            get_all_doctors(latit,longit);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.e("latitude", "inside latitude--" + latitude);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    //loc.setText(address + " " + city + " " + country);
                    atvPlaces.setText(address + " " + city + " " + country);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else {

           // loc.setText("(Couldn't get the location. Make sure location is enabled on the device)");
            atvPlaces.setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    private void displayLocation2() {
        Log.e("dsp", "before permission check in displaylocation()");
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  }, MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        //  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        //ActivityCompat.requestPermissions(this,Manifest.permission.ACCESS_COARSE_LOCATION,1)

        Log.e("dsp", "Periodic location updates started! in displaylocation()");


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            String latit=""+latitude;
            String longit=""+longitude;
            //get_all_doctors(latit,longit);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.e("latitude", "inside latitude--" + latitude);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    //loc.setText(address + " " + city + " " + country);
                    //atvPlaces.setText(address + " " + city + " " + country);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else {

            // loc.setText("(Couldn't get the location. Make sure location is enabled on the device)");
            atvPlaces.setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text


            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text


            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.e(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        Log.e(TAG, "Periodic location updates started! - startLocationUpdates()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details .
            return;
        }
        Log.e("dsp", "Periodic location updates started! - after permission check-  startLocationUpdates()");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.e("dsp", "Periodic location updates started! - after full code-  startLocationUpdates()");
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyB23yTJ6rI0HPYLw1LBXRI4duiQscXhEx0";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
                Log.e("gplaces", "the data is: "+data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { R.id.autocompleteText };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, R.layout.autocomplete_list_item, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
            synchronized (adapter){
                adapter.notifyDataSetChanged();
            }

        }
    }

    //@Override
  //  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.activity_main, menu);
     //   return true;
   // }
}




