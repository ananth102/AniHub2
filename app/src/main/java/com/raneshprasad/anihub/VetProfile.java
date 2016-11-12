package com.raneshprasad.anihub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VetProfile extends AppCompatActivity {
    ImageButton addProf;
    ImageView img;
    Bitmap bitmap_all;
    Intent i;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    String name_str;
    String contact_str;
    String email_str;
    String address_str;
    String bit_str;
    TextView name;
    TextView contact;
    TextView email;
    TextView address;
    int counter = 0;
    ListView lv;
    int counterForList = 0;
    ArrayList list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_profile);
        img = (ImageView) findViewById(R.id.imageView);
        lv = (ListView) findViewById(R.id.listview);
        name = (TextView) findViewById(R.id.textView_nameDis);
        contact = (TextView) findViewById(R.id.textView_ContactDis);
        email = (TextView) findViewById(R.id.textView_EmailDis);
        address = (TextView) findViewById(R.id.textView_AddressDis);

        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        myRef.child("vetAccount").child(android_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    //if(postSnapshot.getValue().toString().equals(android_id)){

                        if(counter == 0){

                            name.setText(postSnapshot.getValue().toString());
                        }else if(counter == 1){
                            contact.setText(postSnapshot.getValue().toString());

                        }else if(counter == 2){
                            email.setText(postSnapshot.getValue().toString());
                        }else if(counter == 3){
                            address.setText(postSnapshot.getValue().toString());
                        }else if(counter == 4){
                            bit_str = postSnapshot.getValue().toString();
                            bitmap_all = StringToBitMap(bit_str);
                            img.setImageBitmap(bitmap_all);
                        }else{
                            break;
                        }

                    //}
                    counter++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addProf = (ImageButton) findViewById(R.id.imageButton);
        addProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VetSetUp.class);
                startActivity(i);
            }
        });
        myRef.child("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    counterForList++;
                    if(postSnapshot.getValue().toString().charAt(0) != '{') {
                        String str = postSnapshot.getValue().toString();
                        list.add(str);
                    }else{
                        break;
                    }
                }
                populateListView(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = ((TextView) view).getText().toString();
                Log.d(str, "DTR");
                i = new Intent(getApplicationContext(), ViewListDetails.class);
                i.putExtra("Details", str);

                startActivity(i);
            }
        });

    }

    public void populateListView(ArrayList<String> list){
        ArrayAdapter<String> myAdap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        Log.d(list.get(1), "Hi");
        lv.setAdapter(myAdap);

    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}

