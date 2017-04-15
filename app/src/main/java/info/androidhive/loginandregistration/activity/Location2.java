package info.androidhive.loginandregistration.activity;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.androidhive.loginandregistration.R;

public class Location2 extends AppCompatActivity {
    private TextView lblLocation;
    private Button btnShowLocation, btnStartLocationUpdates;

    GPSTracker gpst =new GPSTracker();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location2);
        setContentView(R.layout.activity_locationact);
        lblLocation = (TextView) findViewById(R.id.lblLocation);
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnStartLocationUpdates = (Button) findViewById(R.id.btnLocationUpdates);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                double latit=gpst.getlat();
              //  logger.e("gps");
                Log.e("tag","GPS: "+latit);
               // Log.d(TAG, "onClick: ");
               lblLocation.setText("The latitude is "+latit);
            }
        });
    }
}

