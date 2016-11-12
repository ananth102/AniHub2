package com.raneshprasad.anihub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ViewListDetails extends AppCompatActivity {
    Intent intent;
    String name = "";
    String severity;
    String adddesp;
    String phno;
    String bit_str;
    TextView name_text;
    TextView severity_text;
    TextView adddesp_text;
    TextView phno_text;
    ImageView image;
    Button SMS;
    Bitmap bitmap_all;
    String locations = "";
    Button showlocation;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_details);
        showlocation = (Button) findViewById(R.id.button_golocation);
        image = (ImageView) findViewById(R.id.imageView_View);
        SMS = (Button) findViewById(R.id.button_SMS);
        intent = getIntent();
        name = intent.getExtras().getString("Details");
        Log.d(name, "name");
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        name_text = (TextView) findViewById(R.id.textView_Viewname);
        severity_text = (TextView) findViewById(R.id.textView_severeView);
        adddesp_text = (TextView) findViewById(R.id.textView_addressView);
        phno_text = (TextView) findViewById(R.id.textView_phView);
        myRef.child("Post").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if(counter == 0){
                        phno = postSnapshot.getValue().toString();
                    }else if(counter == 1){
                        adddesp = postSnapshot.getValue().toString();
                    }else if(counter == 2){
                        severity = postSnapshot.getValue().toString();
                        if(severity.equals("1")){
                            severity = "mild";
                        }else if(severity.equals("2")){
                            severity = "moderate";
                        }else if(severity.equals("3")){
                            severity = "severe";
                        }

                    }else if (counter == 3){
                        bit_str = postSnapshot.getValue().toString();
                        bitmap_all = StringToBitMap(bit_str);
                        image.setImageBitmap(bitmap_all);
                    }else if (counter == 4){
                        locations = postSnapshot.getValue().toString();
                    }
                    counter++;
                }
                name_text.setText("Animal Type: " + name);
                severity_text.setText("Severity of Injury: " + severity);
                adddesp_text.setText("The Description of Address: " + adddesp);
                phno_text.setText("Contact Information: " + phno);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri sendSMSTo = Uri.parse("smsto:" + phno);
                Intent intent = new Intent(Intent.ACTION_SENDTO, sendSMSTo);
                intent.putExtra("sms_body", "Dear caller, the help is on the way.");

                startActivity(intent);
            }
        });

        showlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String lat = "";
                    String longi = "";
                    boolean isSpace = false;
                    for (int i = 0; i < locations.length(); i++) {
                        if (isSpace) {
                            longi += locations.charAt(i) + "";
                        } else if (!isSpace && locations.charAt(i) != ' ') {
                            lat += locations.charAt(i) + "";
                        } else if (!isSpace && locations.charAt(i) == ' ') {
                            isSpace = true;
                        }
                    }

                    String uri = String.format(Locale.ENGLISH, "geo:%s%s", lat, longi);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), "There is no direct location for this person.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
