package com.example.wechat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by 80561 on 2016/12/20.
 */

public class SendMessage {
    private String yourName;
    private String content;
    private String sendTo;

    private Socket socket;
    private PrintWriter out = null;

    public SendMessage(String yourName, String content, String sendTo){
        this.yourName = yourName;
        this.content = content;
        this.sendTo = sendTo;
        socket = MainActivity.socket;
    }

    public void sendMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (!socket.isClosed()){
                    if (socket.isConnected()) {
                        if (!socket.isOutputShutdown()) {
                            out.println(yourName);
                            out.println(content);
                            out.println(sendTo);
                        }
                    }
                }
            }
        }).start();
    }
}
