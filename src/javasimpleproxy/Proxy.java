/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasimpleproxy;

import java.net.*;
import java.io.*;

/**
 *
 * @author lzp
 */
public class Proxy {

    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //Socket proxySocket = new Socket("127.0.0.1", 30000);
        if (args.length != 5 && args.length != 6) {
            throw new IllegalArgumentException("IllegalArgument");
        }

        String serverIP = "127.0.0.1";
        int listenPort = 0;
        int connectPort = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-l")) {
                listenPort = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-p")) {
                connectPort = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-s")) {
                serverIP = "127.0.0.1";
            }
            if (args[i].equals("-c")) {
                serverIP = args[i + 1];
            }
        }

        ServerSocket ss = new ServerSocket(listenPort);

        while (true) {
            Socket clientSocket = ss.accept();

            InputStream clientIn = clientSocket.getInputStream();
            BufferedOutputStream outClient = new BufferedOutputStream(clientSocket.getOutputStream());

            //System.out.println("accept client\n");
            // Socket proxySocket = new Socket("127.0.0.1", 7201);
            Socket proxySocket = new Socket(serverIP, connectPort);
            InputStream proxyIn = proxySocket.getInputStream();
            BufferedOutputStream outProxy = new BufferedOutputStream(proxySocket.getOutputStream());

            //System.out.println("connect proxy\n");
            new Thread(new ThreadedHandler(clientIn, outProxy, clientSocket, proxySocket)).start();
            new Thread(new ThreadedHandler(proxyIn, outClient, clientSocket, proxySocket)).start();
        }
    }
}

class ThreadedHandler implements Runnable {

    Socket s1;
    Socket s2;
    InputStream clientIn;
    BufferedOutputStream outProxy;

    public ThreadedHandler(InputStream c, BufferedOutputStream o, Socket s1, Socket s2) {
        this.s1 = s1;
        this.s2 = s2;
        clientIn = c;
        outProxy = o;
    }

    public void run() {
        byte[] arr = new byte[8000];
        int readbytes = 0;
        try {
            while ((readbytes = clientIn.read(arr)) > 0) {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (byte) (arr[i] ^ (byte) 0x66);
                }
                outProxy.write(arr, 0, readbytes);
                outProxy.flush();
                //  System.out.printf("done: %d\n", readbytes);
            }

            s1.close();
            s2.close();

            //System.out.println("socket close\n");
        } catch (Exception e) {
            e.printStackTrace();

            //System.out.println("1\n");
            try {
                s1.close();
                s2.close();
            } catch (Exception e2) {

                e2.printStackTrace();

                //System.out.println("2\n");
            }
        }
    }

}
