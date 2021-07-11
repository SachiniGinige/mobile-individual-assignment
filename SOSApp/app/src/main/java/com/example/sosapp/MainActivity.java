package com.example.sosapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private String phoneNo;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 6000;
    private final long MIN_DISTANCE = 5;
    //    private LatLng latLng;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        button = findViewById(R.id.btn_SOS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContact, 1);
            };
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Uri uri = intent.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    phoneNo = cursor.getString(phoneIndex);

                    Toast.makeText(MainActivity.this, "Phone Number is :" + phoneNo, Toast.LENGTH_LONG).show();
                    System.out.println("Phone Number is :" + phoneNo);

                    sendSMS(phoneNo);
                    System.out.println("Alert "+1);

//                    while (true){
//                        final Handler handler = new Handler(Looper.getMainLooper());
//                        handler.postDelayed(new Runnable() {
//                          @Override
//                          public void run() {
//                              System.out.println("Next Alert");
//                          }
//                        }, 60000);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to get Phone Number", Toast.LENGTH_LONG).show();
                    System.out.println("Failed to get Phone Number");
                }
            }
        }
    };

    public  void sendSMS (String phoneNo){
        getLocationUpdates();
        String locationUrl="http://maps.google.com/?q=,";
//        String mobileNo = phoneNo; //actual code line
        String mobileNo=""; //code line inserted to avoid sending text messages each time app is tested
        String message = "I'm Sachini Ginige IM/2017/023. Please Help Me. I'm in "+locationUrl;

        if(!mobileNo.equals("") && !message.equals("")){
            try{
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(mobileNo,null, message,null,null);

                Toast.makeText(getApplicationContext(), "SMS sent to "+mobileNo, Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"SMS sending failed.",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void getLocationUpdates(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        latitude=lastKnownLocation.getLatitude();
        longitude=lastKnownLocation.getLongitude();
        System.out.println("Latitude: "+latitude+"\nLongitude"+longitude);
    }
}