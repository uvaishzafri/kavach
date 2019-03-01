package com.example.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.myapplication.contacts.c1;
import static com.example.myapplication.contacts.c2;
import static com.example.myapplication.contacts.c3;
import static com.example.myapplication.contacts.c4;

public class profile extends AppCompatActivity {

    EditText name,address,mobile,mail;
    TextView save;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String n= "nm";
    public static final String a = "adrs";
    public static final String m = "mbl";
    public static final String e = "emal";

    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=(EditText)findViewById(R.id.edtname);
        address=(EditText)findViewById(R.id.edtaddress);
        mobile=(EditText)findViewById(R.id.edtnumber);
        mail=(EditText)findViewById(R.id.edtmail);
        save=(TextView) findViewById(R.id.btnsave);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,contacts.MODE_PRIVATE);
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if (sharedpreferences!= null) {
            String namepref = prefs.getString(n,"" );
            String addresspref = prefs.getString(a,"" );
            String mobilepref = prefs.getString(m,"" );
            String mailpref = prefs.getString(e,"" );
            name.setText(namepref);
            address.setText(addresspref);
            mobile.setText(mobilepref);
            mail.setText(mailpref);

        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //for saving user details

                String adrress1=address.getText().toString();
                String name1=name.getText().toString();
                String mobile1  =mobile .getText().toString();
                String mail1  =mail.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(a, adrress1);
                editor.putString(n, name1);
                editor.putString(m, mobile1);
                editor.putString(e, mail1);

                editor.commit();
                Toast.makeText(profile.this,"Thanks",Toast.LENGTH_LONG).show();
            }
        });


    }
}
