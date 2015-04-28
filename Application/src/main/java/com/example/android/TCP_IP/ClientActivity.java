package com.example.android.TCP_IP;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bluetoothlegatt.R;
import com.example.android.database.*;

import java.io.PrintWriter;
import java.net.Socket;


public class ClientActivity extends Activity {

    private EditText textFieldPort, textFieldAddress;
    private Button button;
    private String portNumber;
    private static String count;
    private String ipAddress;
    private static int koliko;

    private static String count1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);
        final MyDB   myDB = new MyDB(this);
        textFieldPort = (EditText) findViewById(R.id.Port); // reference to the text field

        textFieldAddress = (EditText) findViewById(R.id.IPaddress);
        button = (Button) findViewById(R.id.buttonConnect); // reference to the send button

        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
         portNumber = textFieldPort.getText().toString();
                 // get the text message on the text field
                //portNumber = Integer.parseInt(port);
                ipAddress = textFieldAddress.getText().toString();
                //myDB.deleteTable();
             myDB.createRecords(ipAddress,portNumber);

                Cursor c = myDB.selectRecords();
                koliko= c.getCount();
                count = c.getString(c.getCount()-1);
                count1 = c.getString(c.getCount());
                //SendMessage sendMessageTask = new SendMessage(ipAddress, port);
              //  sendMessageTask.setIP(ipAddress);
               // sendMessageTask.setPORT(Integer.parseInt(portNumber));
           Toast.makeText(getBaseContext(),"Connected", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static String getPortNumber() {

        return count;
    }

    public static int getKoliko() {

        return koliko;
    }

    public static String getPortNumber2() {

        return count1;
    }

    public String getIpAddress() {
        return ipAddress;
    }
/**
    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("proveraaaaaaaaaaaaaaaaaaaa");
            try {

                client = new Socket(ipAddress, PORT); // connect to the server
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.write(messsage); // write the message to output stream

                printwriter.flush();
                printwriter.close();
                client.close(); // closing the connection

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
**/


}