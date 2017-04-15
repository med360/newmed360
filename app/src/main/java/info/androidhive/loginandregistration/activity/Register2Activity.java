package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

import static info.androidhive.loginandregistration.R.id.bgp;
import static info.androidhive.loginandregistration.R.id.email;

public class Register2Activity extends Activity {


    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputnationality;
    private Spinner inputgender;
    private EditText inputdob;
    private Spinner inputbgp;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;



    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private TextView nationalityview;
    private int year, month, day;
    public String countrysel;
    private Context con;

    Spinner spnr,spinbgp;

    String[] gender = {
            "Male",
            "Female",

    };

    String[] bloodgroup = {
            "O-positive",
            "O-negative",
            "A-positive",
            "A-negative",
            "B-positive",
            "B-negative",
            "AB-positive",
            "AB-negative",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        inputFullName = (EditText) findViewById(R.id.name);
        inputnationality = (EditText) findViewById(R.id.national);
        inputgender = (Spinner) findViewById(R.id.gender);
        inputdob = (EditText) findViewById(R.id.dob);
        inputbgp = (Spinner) findViewById(R.id.bgp);
        btnRegister = (Button) findViewById(R.id.btnRegister);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register2Activity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                 String name = inputFullName.getText().toString().trim();
                String nationality = inputnationality.getText().toString().trim();
                String gender = inputgender.getSelectedItem().toString().trim();
                String dob = inputdob.getText().toString().trim();
                String bgp = inputbgp.getSelectedItem().toString().trim();


                if (!name.isEmpty() && !nationality.isEmpty() && !gender.isEmpty() && !dob.isEmpty()&& !bgp.isEmpty()) {
                    createProfile(name, nationality, gender, dob, bgp);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        spnr = (Spinner)findViewById(R.id.gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, gender);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spnr.getSelectedItemPosition();

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                }
        );


        spinbgp = (Spinner)findViewById(R.id.bgp);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, bloodgroup);

        spinbgp.setAdapter(adapter2);
        spinbgp.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinbgp.getSelectedItemPosition();

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                }
        );


        dateView = (EditText) findViewById(R.id.dob);
        nationalityview = (TextView) findViewById(R.id.national);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }










    private void createProfile(final String name, final String nationality, final String gender, final String dob, final String bgp) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();
        Log.e("medlogin", "in register2activity before post");
        StringRequest strReq2 = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("medlogin", "code to clear cache is below");
                    AppController.getInstance().getRequestQueue().getCache().remove(AppConfig.URL_REGISTER2);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        // Inserting row in users table


                        Toast.makeText(getApplicationContext(), "Please Provide Your Profile Details", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                Register2Activity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("nationality", nationality);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("bgp", bgp);
                params.put("userid", RegisterActivity.userid);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq2, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }





















    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    public void test(View view) {
        inputgender = (Spinner) findViewById(R.id.gender);
        inputbgp = (Spinner) findViewById(R.id.bgp);
        String gender = inputgender.getSelectedItem().toString().trim();
        String bgp= inputbgp.getSelectedItem().toString().trim();
        inputFullName = (EditText) findViewById(R.id.name);
        inputFullName.setText(bgp +" " +gender);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    }



