package com.example.android.TCP_IP;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Mirza on 2015-03-22.
 */
public class SendMessage extends AsyncTask<Void, Void, Void> {

    private Socket client;
    private  DataOutputStream outputToClient;
    private PrintWriter printwriter;
    private  byte[] messsage;
    private   String IP = "192.168.1.95";
    private  int PORT = 8000;



    public SendMessage(){

    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public  void setMesssage(byte[] messsage) {
        this.messsage = messsage;
    }


    public int getPORT() {
        return PORT;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            sendBytes(messsage);

            client.close(); // closing the connection

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }

    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        client = new Socket(IP, PORT);

        outputToClient  = new DataOutputStream(client.getOutputStream());
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        // Other checks if needed.

        // May be better to save the streams in the support class;
        // just like the socket variable.
        OutputStream out = client.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, start, len);
        }
    }
}
