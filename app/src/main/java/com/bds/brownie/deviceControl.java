package com.bds.brownie;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.StreamHandler;

public class deviceControl extends AppCompatActivity {

    Button btnOn, btnRead, btnDis, btnTestMotor, btnTimeSet, btnAlarm1Set, btnAlarm2Set, btnAlarm3Set, btnAlarm4Set, btnMotorSet;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView timeText, alarm1Text, alarm2Text, alarm3Text, alarm4Text, motorText;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    private BluetoothFeederService mFeederService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device


        setContentView(R.layout.activity_device_control);


        btnRead = (Button)findViewById(R.id.buttonRead);
        btnTimeSet = (Button)findViewById(R.id.buttonTimeSet);
        btnTestMotor = (Button)findViewById(R.id.buttonTestMotor);

        btnAlarm1Set = (Button)findViewById(R.id.buttonAlarm1Set);
        btnAlarm2Set = (Button)findViewById(R.id.buttonAlarm2Set);
        btnAlarm3Set = (Button)findViewById(R.id.buttonAlarm3Set);
        btnAlarm4Set = (Button)findViewById(R.id.buttonAlarm4Set);

        btnMotorSet = (Button)findViewById(R.id.buttonMotorSet);

        //btnDis = (Button)findViewById(R.id.buttonDisconnect);
        timeText = (TextView)findViewById(R.id.timeText);
        alarm1Text = (TextView)findViewById(R.id.alarm1Text);
        alarm2Text = (TextView)findViewById(R.id.alarm2Text);
        alarm3Text = (TextView)findViewById(R.id.alarm3Text);
        alarm4Text = (TextView)findViewById(R.id.alarm4Text);
        motorText = (TextView)findViewById(R.id.motorText);

        //new ConnectBT().execute(); //Call the class to connect
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (myBluetooth == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }

        //commands to be sent to bluetooth
        btnRead.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String answ = runCommandOnDevice("gettime\n");
                if (answ.contains("OK")){
                    timeText.setText(answ.replace("OK:time:", "").replace("255","00"));
                }else{
                    msg("Ошибка получения времени");
                }

                answ = runCommandOnDevice("getalarm1\n");
                if (answ.contains("OK")){
                    alarm1Text.setText(answ.replace("OK:alarm1:", "").replace("255","00"));
                }else{
                    msg("Ошибка получения времени");
                }

                answ = runCommandOnDevice("getalarm2\n");
                if (answ.contains("OK")){
                    alarm2Text.setText(answ.replace("OK:alarm2:", "").replace("255","00"));
                }else{
                    msg("Ошибка получения времени");
                }

                answ = runCommandOnDevice("getalarm3\n");
                if (answ.contains("OK")){
                    alarm3Text.setText(answ.replace("OK:alarm3:", "").replace("255","00"));
                }else{
                    msg("Ошибка получения времени");
                }

                answ = runCommandOnDevice("getalarm4\n");
                if (answ.contains("OK")){
                    alarm4Text.setText(answ.replace("OK:alarm4:", "").replace("255","00"));
                }else{
                    msg("Ошибка получения времени");
                }

                answ = runCommandOnDevice("getmotorturn\n");
                if (answ.contains("OK")){
                    motorText.setText(answ.replace("OK:motorturn:", ""));
                }else{
                    msg("Ошибка оборотов");
                }
            }
        });

        btnTestMotor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("motortest\n");
                if (answ.contains("OK")){
                    msg("Мотор прошел проверку");
                }else{
                    msg("Ошибка проверки мотора " + answ);
                }
            }
        });

        btnTimeSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("settime:" + timeText.getText() + "\n");
                if (answ.contains("OK")){
                    msg("Время установлено");
                }else{
                    msg("Ошибка установки времени " + answ);
                }
            }
        });

        btnAlarm1Set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("setalarm1:" + alarm1Text.getText() + "\n");
                if (answ.contains("OK")){
                    msg("Время установлено");
                }else{
                    msg("Ошибка установки времени " + answ);
                }
            }
        });

        btnAlarm2Set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("setalarm2:" + alarm2Text.getText() + "\n");
                if (answ.contains("OK")){
                    msg("Время установлено");
                }else{
                    msg("Ошибка установки времени " + answ);
                }
            }
        });

        btnAlarm3Set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("setalarm3:" + alarm3Text.getText() + "\n");
                if (answ.contains("OK")){
                    msg("Время установлено");
                }else{
                    msg("Ошибка установки времени " + answ);
                }
            }
        });

        btnAlarm4Set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("setalarm4:" + alarm4Text.getText() + "\n");
                if (answ.contains("OK")){
                    msg("Время установлено");
                }else{
                    msg("Ошибка установки времени " + answ);
                }
            }
        });

        btnMotorSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String answ = runCommandOnDevice("setmotorturn:" + motorText.getText() + "\n");
                if (answ.contains("OK")){
                    msg("Обороты мотора установлены");
                }else{
                    msg("Ошибка установки оборотов мотора " + answ);
                }
            }
        });
        /*btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });*/


    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!myBluetooth.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mFeederService == null) {
            setupFeederService();
        }
    }

    private void setupFeederService() {
        //Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        //mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        //mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        /*mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });*/

        // Initialize the BluetoothChatService to perform bluetooth connections
        mFeederService = new BluetoothFeederService(this, mHandler);

        // Initialize the buffer for outgoing messages
        //mOutStringBuffer = new StringBuffer("");
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //activity = this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothFeederService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothFeederService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothFeederService.STATE_LISTEN:
                        case BluetoothFeederService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    /*mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

                        Toast.makeText(this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
*/
                    break;
                case Constants.MESSAGE_TOAST:
                    /*if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }*/
                    break;
            }
        }
    };

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("gettime\n".toString().getBytes());

                byte[] buffer = new byte[256];  // buffer store for the stream
                InputStream tmpIn = btSocket.getInputStream();
                DataInputStream mmInStream = new DataInputStream(tmpIn);
                // Read from the InputStream
                int bytes = mmInStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                //textView.setText(readMessage);
                //readMessage += "";
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private String runCommandOnDevice(String commantText)
    {
        String res = "";

        if (btSocket!=null) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = btSocket.getInputStream();
                tmpOut = btSocket.getOutputStream();
            } catch (IOException e) {
                //Log.e(TAG, "temp sockets not created", e);
            }

            byte[] buffer = new byte[1024];
            int bytes;

            try {
                tmpOut.write(commantText.getBytes());
            } catch (IOException e) {
                //Log.e(TAG, "temp sockets not created", e);
            }

            StringBuilder readMessage = new StringBuilder();

            while (true) {
                try {
                    bytes = tmpIn.read(buffer);
                    String readed = new String(buffer, 0, bytes);
                    readMessage.append(readed);

                    // маркер конца команды - вернуть ответ в главный поток
                    if (readed.contains("\n")) {
                        //mHandler.obtainMessage(DeviceControlActivity.MESSAGE_READ, bytes, -1, readMessage.toString()).sendToTarget();
                        //readMessage.setLength(0);
                        break;
                    }
                }catch (IOException e){
                    break;
                }
            }
            return readMessage.toString();
        }

        return res;
    }



    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(deviceControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
