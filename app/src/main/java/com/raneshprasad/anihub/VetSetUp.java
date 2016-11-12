package com.raneshprasad.anihub;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import static java.security.AccessController.getContext;

public class VetSetUp extends AppCompatActivity {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    EditText name;
    EditText contect_info;
    EditText email;
    EditText address;
    Button setContact;
    String name_str;
    String contact_str;
    String email_str;
    String address_str;
    Button picture;
    String str_bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_set_up);
        picture = (Button) findViewById(R.id.button_pic_vetere);
        name = (EditText) findViewById(R.id.editText_name);
        contect_info = (EditText) findViewById(R.id.editText_contact);
        email = (EditText) findViewById(R.id.editText_email);
        address = (EditText)findViewById(R.id.editTextaddress);
        setContact = (Button) findViewById(R.id.button_info_vet);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(VetSetUp.this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(VetSetUp.this,
                            android.Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(VetSetUp.this,
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

        setContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str = name.getText().toString();
                contact_str = contect_info.getText().toString();
                email_str = email.getText().toString();
                address_str = address.getText().toString();
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                myRef.child("vetAccount").push().setValue(android_id);
                myRef.child("vetAccount").child(android_id).push().setValue(name_str);
                myRef.child("vetAccount").child(android_id).push().setValue(contact_str);
                myRef.child("vetAccount").child(android_id).push().setValue(email_str);
                myRef.child("vetAccount").child(android_id).push().setValue(address_str);
                myRef.child("vetAccount").child(android_id).push().setValue(str_bitmap);
                Toast.makeText(getApplicationContext(), "Thanks for creating your account!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), VetProfile.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView) findViewById(R.id.imageView_vete_pic);
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
}
