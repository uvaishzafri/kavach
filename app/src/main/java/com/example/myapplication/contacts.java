package com.example.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class
contacts extends AppCompatActivity {
    EditText contact1,contact2,contact3,contact4,contact5;
    TextView save;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String c1 = "one";
    public static final String c2 = "two";
    public static final String c3 = "three";
    public static final String c4 = "four";
    public static final String c5 = "five";

    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contact1=(EditText)findViewById(R.id.etContactOne);
        contact2=(EditText)findViewById(R.id.etContactTwo);
        contact3=(EditText)findViewById(R.id.etContactThree);
        contact4=(EditText)findViewById(R.id.etContactFour);
        contact5=(EditText)findViewById(R.id.etContactFive);

        save=(TextView)findViewById(R.id.btnsave);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,contacts.MODE_PRIVATE);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if (sharedpreferences!= null) {
            String contactone = prefs.getString(c1,"" );
            String contacttwo = prefs.getString(c2,"" );
            String contactthree = prefs.getString(c3,"" );
            String contactfour = prefs.getString(c4,"" );
            String contactfive = prefs.getString(c5,"" );
            contact1.setText(contactone);
            contact2.setText(contacttwo);
            contact3.setText(contactthree);
            contact4.setText(contactfour);
            contact5.setText(contactfive);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String one  = contact1.getText().toString();
                String two  = contact2.getText().toString();
                String three  = contact3.getText().toString();
                String four  = contact4.getText().toString();
                String five  = contact5.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(c1, one);
                editor.putString(c2, two);
                editor.putString(c3, three);
                editor.putString(c4, four);
                editor.putString(c5, five);
                editor.commit();
                Toast.makeText(contacts.this,"Thanks",Toast.LENGTH_LONG).show();
            }
        });
    }

}
