package com.raneshprasad.anihub;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.raneshprasad.anihub.R.id.imageView;

public class Helper extends AppCompatActivity {
    EditText nameAni;
    LocationManager locationManager;
    LocationListener locationListener;
    EditText phno;
    EditText descAddress;
    CheckBox severe;
    CheckBox moderate;
    ImageView mImageView;
    CheckBox mild;
    String str_bitmap;
    Button post;
    Button picture;
    String severity = "";
    String locations = "";
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        picture = (Button) findViewById(R.id.button_pic);
        severe = (CheckBox) findViewById(R.id.checkBox_severe);
        moderate = (CheckBox) findViewById(R.id.checkBox_moderate);
        mild = (CheckBox) findViewById(R.id.checkBox_mild);
        nameAni = (EditText) findViewById(R.id.editText_aniDesc);
        phno = (EditText) findViewById(R.id.editText_phno);
        descAddress = (EditText) findViewById(R.id.editText_adddesc);
        post = (Button) findViewById(R.id.button_postHelp);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locations = location.getLatitude() + " " + location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Helper.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;

            }
        }else{
            configureButton();
        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        locations = location.getLatitude() + " " + location.getLongitude();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                };
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Helper.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET
                        }, 10);
                        return;

                    }
                }else{
                    configureButton();
                }*/
                //locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);


                myRef.child("Post").push().setValue(nameAni.getText().toString());
                myRef.child("Post").child(nameAni.getText().toString()).push().setValue(phno.getText().toString());
                myRef.child("Post").child(nameAni.getText().toString()).push().setValue(descAddress.getText().toString());
                myRef.child("Post").child(nameAni.getText().toString()).push().setValue(severity);
                myRef.child("Post").child(nameAni.getText().toString()).push().setValue(str_bitmap);
                myRef.child("Post").child(nameAni.getText().toString()).push().setValue(locations);
                Log.d(locations, "Location");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Helper.this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Helper.this,
                            android.Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(Helper.this,
                                new String[]{android.Manifest.permission.CAMERA},
                                0);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkBox_severe:
                if (checked) {
                    severity = "3";

                }
                moderate.setChecked(false);
                mild.setChecked(false);
                break;
            case R.id.checkBox_moderate:
                if (checked) {
                    severity = "2";

                }
                severe.setChecked(false);
                mild.setChecked(false);
                break;
            case R.id.checkBox_mild:
                if (checked) {
                    severity = "1";

                }
                severe.setChecked(false);
                moderate.setChecked(false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView) findViewById(R.id.imageView_pic);
            image.setImageBitmap(theImage);
            str_bitmap = BitMapToString(theImage);

        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }

    }

    private void configureButton(){
        locationManager.requestLocationUpdates("gps", 10, 0,locationListener);
    }


}
