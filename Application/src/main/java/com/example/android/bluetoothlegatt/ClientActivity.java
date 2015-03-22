package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientActivity extends Activity {
    private Socket client;
    private PrintWriter printwriter;
    private EditText textFieldMessage, textFieldAddress;
    private Button button;
    private String messsage;
    private String ipAddress;
    private int PORT = 4444;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);

        textFieldMessage = (EditText) findViewById(R.id.message); // reference to the text field

        textFieldAddress = (EditText) findViewById(R.id.address);
        button = (Button) findViewById(R.id.button1); // reference to the send button

        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                messsage = textFieldMessage.getText().toString(); // get the text message on the text field
                ipAddress = textFieldAddress.getText().toString();
                textFieldMessage.setText(""); // Reset the text field to blank
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
        });
    }

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



}