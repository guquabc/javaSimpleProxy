/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasimpleproxy;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lzp
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //Socket serverSocket = new Socket("106.187.95.66", 30001);

        //ServerSocket ss = new ServerSocket(30000);
        ServerSocket ss = new ServerSocket(7202);

        while (true) {
            Socket browserSocket = ss.accept();

            
            
            InputStream browserIn = browserSocket.getInputStream();
            BufferedOutputStream outBrowser = new BufferedOutputStream(browserSocket.getOutputStream());

            System.out.println("accept browser\n");
            //Socket serverSocket = new Socket("127.0.0.1", 30001);
            Socket serverSocket = new Socket("106.187.95.66", 30001);
            InputStream serverIn = serverSocket.getInputStream();
            BufferedOutputStream outServer = new BufferedOutputStream(serverSocket.getOutputStream());

            
            System.out.println("connect server\n");
            new Thread(new ThreadedHandler(browserIn, outServer,browserSocket,serverSocket)).start();
            new Thread(new ThreadedHandler(serverIn, outBrowser,browserSocket,serverSocket)).start();
//
//            byte[] arr = new byte[2048];
//            int readbytes = 0;
//            while ((readbytes = serverIn.read(arr)) > 0) {
//                for (int i = 0; i < arr.length; i++) {
//                    arr[i] = (byte) (arr[i] ^ (byte) 0x66);
//                }
//                outBrowser.write(arr, 0, readbytes);
//
//                System.out.printf("done: %d", readbytes);
//            }
        }
    }
}
