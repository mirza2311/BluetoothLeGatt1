package com.example.android.bluetoothlegatt;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Mirza on 2015-03-22.
 */
public class SendMessage extends AsyncTask<Void, Void, Void> {

    private Socket client;
    private PrintWriter printwriter;
    private String messsage;

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }



    @Override
    protected Void doInBackground(Void... params) {

        try {

            client = new Socket("192.168.1.95", 4444); // connect to the server
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
