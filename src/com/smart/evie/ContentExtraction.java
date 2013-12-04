package com.smart.evie;

import java.io.IOException;

import java.util.ArrayList;

import android.util.Log;

import java.io.*;
import java.net.*;

public class ContentExtraction {
	
	public static ArrayList<String> findHashtags(String description) {
		ArrayList<String> hashtags = new ArrayList<String>();

		String hostName = "128.237.236.98";

        try {
        	InetAddress host = InetAddress.getByName(hostName);
	        int portNumber = 3000;
        	Socket socket = new Socket(host, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            out.println(description);
            while ((fromServer = in.readLine()) != null) {
                Log.i("evie_debug", "Server: " + fromServer);
                if (fromServer.equals("Bye42."))
                    break;
                hashtags.add(fromServer);
            }
            
            fromUser = "Bye42.";
            Log.i("evie_debug", "Client: " + fromUser);
            out.println(fromUser);
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.err.println(e.getMessage());
        }

		return hashtags;
	}
}
