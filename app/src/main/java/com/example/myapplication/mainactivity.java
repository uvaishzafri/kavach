package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.*;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.myapplication.contacts.c1;
import static com.example.myapplication.contacts.c2;
import static com.example.myapplication.contacts.c3;
import static com.example.myapplication.contacts.c4;
import static com.example.myapplication.contacts.c5;

public class mainactivity extends AppCompatActivity {

    TextView imageEdit,imageprofile;
    TextView tvsettings;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageEdit = (TextView) findViewById(R.id.imgedit);
        tvsettings = (TextView) findViewById(R.id.tvsettings);
        imageprofile=(TextView) findViewById(R.id.imgprofile);

        checkBTpermissions();
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
      //  ActivityCompat.requestPermissions(new String[]{permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, 1001); //Any number
        ActivityCompat.requestPermissions(this, new String[]{permission.SEND_SMS}, 1);

        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainactivity.this,contacts.class);
                startActivity(intent);
            }
        });

        tvsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainactivity.this, bluemain.class);
                startActivity(intent);
            }
        });

        imageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainactivity.this,profile.class);
                 startActivity(intent);
            }
        });
    }


        @RequiresApi(api = Build.VERSION_CODES.M)
                private void checkBTpermissions()
        {
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
                int permissioncheck = this.checkSelfPermission("Manifest.permission.SEND_SMS");

                if (permissioncheck != 0) {
                    this.requestPermissions(new String[]{permission.SEND_SMS}, 1001);
                }
            }
        }


    }
