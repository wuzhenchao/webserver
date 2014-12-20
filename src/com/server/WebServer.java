package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by wzc on 14/12/16.
 */
public class WebServer {

    public void serverStart(int port) {
        try {
            ServerSocket serversocket = new ServerSocket(port);
            while (true){
                Socket socket = serversocket.accept();
                new Processor(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8090;
        if(args.length==1){
            port = Integer.parseInt(args[0]);
        }
        System.out.println("port is "+port);
        new WebServer().serverStart(port);

    }
}

