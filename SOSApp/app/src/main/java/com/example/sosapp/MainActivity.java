package com.example.sosapp;

import android.Manifest;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private LocationListener locationListener;
    private LocationManager locationManager;
//    private final long MIN_TIME=1000;
//    private final long MIN_DISTANCE=5;
//    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try{
//                    latLng = new LatLng(location.getLatitude(),location.getLongitude());

//                    System.out.println("My Latitude: "+latLng.latitude+" & My Longitude: "+latLng.longitude);

                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){}

            public void onProviderEnabled(String provider){}

            public void onProviderDisabled(String provider){}
        };

        Button button = findViewById(R.id.btn_SOS);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContact, 1);
//                onActivityResult(pickContact,1);
                sendSMS(v);
                while (true){
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          System.out.println("Nxt sms");
                      }
                    }, 60000);
                }

//                String phoneNo= ContactsContract.CommonDataKinds.Phone.NUMBER;
//                Toast.makeText(MainActivity.this, "Number=" + phoneNo, Toast.LENGTH_LONG).show();

            }
            protected void onActivityResult(Intent intent,int requestCode) {
                if (requestCode == 1) {


                    Uri uri = intent.getData();
                    String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                    Cursor cursor = getContentResolver().query(uri, projection,
                            null, null, null);
                    cursor.moveToFirst();

                    String phoneNo= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Toast.makeText(MainActivity.this, "Phone Number is :" + phoneNo, Toast.LENGTH_LONG).show();
//                    System.out.println("Phone Number is :" + phoneNo);
//.
//                    int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                    String number = cursor.getString(numberColumnIndex);
//
//                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//                    String name = cursor.getString(nameColumnIndex);
//
                }
            };

            public  void sendSMS (View v){

                String locationUrl="http://maps.google.com/?q=,";
                String mobileNo = "0773884942";
                String message = "I'm Sachini Ginige IM/2017/023. Please Help Me. I'm in "+locationUrl;

                try{
                    if(!mobileNo.equals("") && !message.equals("")){
                        SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(mobileNo,null, message,null,null);

                        Toast.makeText(getApplicationContext(), "SMS sent to "+mobileNo, Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"SMS sending failed.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}