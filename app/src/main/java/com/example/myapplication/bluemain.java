package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AutomaticZenRule;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.example.myapplication.contacts.c1;
import static com.example.myapplication.contacts.c2;
import static com.example.myapplication.contacts.c3;
import static com.example.myapplication.contacts.c4;
import static com.example.myapplication.contacts.c5;


public class bluemain extends Activity {

    //    private final String DEVICE_NAME="MyBTBee";
    private final String DEVICE_ADDRESS="A0:60:90:48:42:BF";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private boolean mIsOnHeadsetSco;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Button startButton, sendButton,clearButton,stopButton;
    TextView textView,textview2;
    EditText editText;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    BluetoothHeadset btHeadset;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    LocationManager locationManager;
    boolean stopThread;
    private Set<BluetoothDevice> pairedDevices;
    private AutomaticZenRule btAdapter;
    private boolean stopThread2;
    String string=" ";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluemain);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textview2 = (TextView) findViewById(R.id.textView2);
        setUiEnabled(false);
        checkBTpermissions();
        checkBTpermissions2();
        checkBTpermissions3();
//on going to activity bluemain
        onClickStart();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStart();
            }
        });
    }

    public void setUiEnabled(boolean bool)
    {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);

    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
           /* try {
               // Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {

                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connected;
    }

    public void onClickStart() {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
                textView.append("\nConnection Opened!\n");
            }
        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        stopThread2 = false;
        buffer = new byte[1024];
        final Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {

                /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Hi speak something");
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {

                }*/

                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String str=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {   Log.d("*****before if","****befor");
                                    if(str.equals("A"))
                                    {Log.d("*****in if*****","******in if");
                                        SendMsgToContacts() ;
                                        Log.d("*****inbetween if","inbetwwen*****");
                                        textView.append(str);
                                        stopThread=true;
                                    }//sms with condition
                                     textView.append(str);
                                   // stopThread=true;
                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {Log.d("*****exception if","exception");
                        stopThread = true;
                    }
                }
            }
        });
        Thread thread2;
        thread2 = new Thread(new Runnable()
        {
            public void run() {

              /*  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Hi speak something");*/
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                try {
                   // startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                    Thread.currentThread().sleep(1000);
                } catch (ActivityNotFoundException | InterruptedException a) {
stopThread2=true;
                }
                catch(Exception e){}}
            }});

        thread.start();
        thread2.start();
    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:"+string+"\n");

    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected=false;
        textView.append("\nConnection Closed!\n");
    }

    public void onClickClear(View view) {
        textView.setText("");
    }

    public void SendMsgToContacts() {
        final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

            SmsManager smsManager = SmsManager.getDefault();
location();

            String smsbody="I am in danger please help me., "+textview2.getText().toString()+" ,Use life 360 app to get my live location";

            sharedpreferences = getSharedPreferences(MyPREFERENCES,contacts.MODE_PRIVATE);

            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

            if (sharedpreferences!= null) {
                String contactone = prefs.getString(c1, "");
                String contacttwo = prefs.getString(c2, "");
                String contactthree = prefs.getString(c3, "");
                String contactfour = prefs.getString(c4, "");
                String contactfive = prefs.getString(c5, "");

                smsManager.sendTextMessage(contactone, null, smsbody, null, null);
                smsManager.sendTextMessage(contacttwo, null, smsbody, null, null);
                smsManager.sendTextMessage(contactthree, null, smsbody, null, null);
                smsManager.sendTextMessage(contactfour, null, smsbody, null, null);
                smsManager.sendTextMessage(contactfive, null, smsbody, null, null);

                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
            }
        }


    private void checkBTpermissions()
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
            int permissioncheck = this.checkSelfPermission("Manifest.permission.SEND_SMS");

            if (permissioncheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1001);
            }
        }
    }
    private void checkBTpermissions2()
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
            int permissioncheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");

            if (permissioncheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTpermissions3()
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
            int permissioncheck = this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if (permissioncheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1001);
            }
        }
    }

    void location(){//for fetching user location
        String strin;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }



         if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str="";
                                str = addressList.get(0).getLocality();
                        str+=","+addressList.get(0).getAdminArea();
                        str+=","+addressList.get(0).getSubAdminArea();
                        str+=","+addressList.get(0).getPremises();
                        str+=","+addressList.get(0).getLocale();
                        str+= " , " + addressList.get(0).getCountryName();
                        string+=str;
                        textview2.setText(str);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

     else   if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double latitude=location.getLatitude();
                double longitude=location.getLongitude();

                Geocoder geocoder=new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                    String str=addressList.get(0).getLocality();
                    str+=","+addressList.get(0).getAdminArea();
                    str+=","+addressList.get(0).getSubAdminArea();
                    str+=","+addressList.get(0).getPremises();
                    str+=","+addressList.get(0).getPostalCode();
                    str+=","+addressList.get(0).getLocale();
                    str+=","+addressList.get(0).getAddressLine(0);
                    str+=","+addressList.get(0).getLocality();
                    str+=","+addressList.get(0).getSubLocality();
                    str+=","+addressList.get(0).getLatitude();
                    str+=","+addressList.get(0).getLongitude();
                    str += " , " + addressList.get(0).getCountryName();
                    str+=" ";
                    string+=str;
                    textview2.append(str);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });}
        else{
          string="no location found";
        }
        string=textview2.getText().toString();
    }

    public boolean isOnHeadsetSco()
    {
        return mIsOnHeadsetSco;
    }

    private void SetupBluetooth()
    {BluetoothAdapter btAdapter;

       btAdapter  = BluetoothAdapter.getDefaultAdapter();

        pairedDevices = btAdapter.getBondedDevices();

        BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int profile, BluetoothProfile proxy)
            {
                if (profile == BluetoothProfile.HEADSET)
                {
                    btHeadset = (BluetoothHeadset) proxy;
                }
            }
            public void onServiceDisconnected(int profile)
            {
                if (profile == BluetoothProfile.HEADSET) {
                    btHeadset = null;
                }
            }
        };
        btAdapter.getProfileProxy(bluemain.this, mProfileListener, BluetoothProfile.HEADSET);

    }

    private void startVoice()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(btAdapter.isEnabled())
            {
                for (BluetoothDevice tryDevice : pairedDevices)
                {
                    //This loop tries to start VoiceRecognition mode on every paired device until it finds one that works(which will be the currently in use bluetooth headset)
                    if (btHeadset.startVoiceRecognition(tryDevice))
                    {
                        break;
                    }
                }
            }
        }
        Intent recogIntent;
        recogIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recogIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
SpeechRecognizer recog;
        recog = SpeechRecognizer.createSpeechRecognizer(bluemain.this);
        recog.setRecognitionListener(new RecognitionListener()
        {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
            //further.
        });

        recog.startListening(recogIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(result.get(0).toLowerCase().contains("help"))
                    {
                        Toast.makeText(this,"method trigerred",Toast.LENGTH_LONG).show();
                    }
                   // voiceInput.setText(result.get(0));
                    Toast.makeText(this,result.get(0),Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }
    }

