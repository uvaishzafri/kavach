package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageEdit;
    TextView tvsettings;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageEdit = (ImageView) findViewById(R.id.imgedit);
        tvsettings = (TextView) findViewById(R.id.tvsettings);

        checkBTpermissions();

        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,contacts.class);
                startActivity(intent);
            }
        });

        tvsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Setting.class);
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
                    this.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1001);
                }
            }
        }


    }
