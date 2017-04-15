package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SessionManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class DoctorProfile extends Activity {
    private ProgressDialog pDialog;

    private ImageView imgview;
    private TextView txtName;
    private TextView txtspcl;
    private TextView txtdoctornationality;
    private TextView txtgender;
    private NetworkImageView thumbNail;



    private TextView txtnational;
    private SessionManager session;
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent = new Intent(DoctorProfile.this,
                            Viewdoctor.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.messg:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent2 = new Intent(DoctorProfile.this,
                            Viewdoctor.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.profilenav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent3 = new Intent(DoctorProfile.this,
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
        // Progress dialog



        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Log.e("singledoctor", "after setcontentview");

  txtName=(TextView)findViewById(R.id.doctorname);
        txtdoctornationality= (TextView) findViewById(R.id.doctornationality);
        txtgender = (TextView) findViewById(R.id.doctorgender);
        txtspcl = (TextView) findViewById(R.id.doctorspcl);


       thumbNail = (NetworkImageView) findViewById(R.id.doctoreimage);
        // getting product details from intent
        Intent i = getIntent();
        // getting product id (pid) from intent
        final String did = i.getStringExtra("did");

        Log.e("singledoctor", "before function call");

        getDoctor(did);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }





    private void getDoctor(final String did) {
        pDialog.setMessage("Fetching Doctor info ...");
        showDialog();
    String tag_string_req = "req_login";
        Log.e("singledoctor", "before string request");
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_DOCTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Doctor Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("medlogin", "code to clear cache is below");
                    AppController.getInstance().getRequestQueue().getCache().remove(AppConfig.URL_DOCTOR);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        JSONObject doctor = jObj.getJSONObject("doctor");
                        String name = doctor.getString("name");
                        String address = doctor.getString("add");
                        String nationality = doctor.getString("nationality");
                        String speciality = doctor.getString("specialit");
                        String did = doctor.getString("did");
                        String gender = doctor.getString("gender");
                        String image = doctor.getString("image");

                    txtName.setText(name);
                        txtdoctornationality.setText(nationality);
                        txtgender.setText(gender);
                        txtspcl.setText(speciality);






                        ImageLoader imgLoader;
                        imgLoader = AppController.getInstance().getImageLoader();
                        thumbNail.setImageUrl(image, imgLoader);






                        // Loader image - will be shown before loading image
                       // int loader = R.drawable.medlogo;

                        // Imageview to show


                        // Image url
                        //String image_url = "http://api.androidhive.info/images/sample.jpg";

                        // ImageLoader class instance
                        //imgLoader = new ImageLoader(getApplicationContext());

                        // whenever you want to load an image from url
                        // call DisplayImage function
                        // url - image url to load
                        // loader - loader image, will be displayed before getting image
                        // image - ImageView
                        //imgLoader.DisplayImage(image_url, loader, imgview);











                } else {
                        // Error in login. Get the error message
                        Log.e("medlogin", "error in login");
                        String errorMsg = jObj.getString("error_msg");

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GETDOCTOR", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to get doctor api
                Log.e("medlogin", "inside getparams method and the email storing in hashmap for post are: "  + "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("did", did);


                return params;
            }

        };
    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
