package project;

import java.io.*;  

import java.net.*;

public class TCPClient {

    private static final String SERVER_ADDRESS = "172.20.10.10";
    private static final int SERVER_PORT = 6807;

    public static void main(String[] args) throws Exception {
        Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Thread to read messages from the server
        Thread readThread = new Thread(() -> {
            try {
                String modifiedSentence;
                while ((modifiedSentence = inFromServer.readLine()) != null) {
                    System.out.println("FROM SERVER: " + modifiedSentence);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
       
        Thread writeThread = new Thread(() -> {
            try {
                String sentence;
                while ((sentence = inFromUser.readLine()) != null) {
                    outToServer.writeBytes(sentence + '\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        readThread.start();
        writeThread.start();

        
        readThread.join();
        writeThread.join();

        clientSocket.close();
    }
}
