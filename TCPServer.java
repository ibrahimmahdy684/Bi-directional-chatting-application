package project;

import java.io.*; 
import java.net.*; 
import java.util.concurrent.*;

public class TCPServer {

    private static final int PORT = 6807;

    public static void main(String[] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("Server started and listening on port " + PORT);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            executor.submit(new ClientHandler(connectionSocket));
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            BufferedReader outtoClient= new BufferedReader(new InputStreamReader(System.in));
            String clientSentence;
            while ((clientSentence = inFromClient.readLine()) != null) {
            	
                System.out.println("Received from client: " + clientSentence);
                String capitalizedSentence = clientSentence.toUpperCase() + '\n';
                outToClient.writeBytes(capitalizedSentence);
            }
            
           
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}