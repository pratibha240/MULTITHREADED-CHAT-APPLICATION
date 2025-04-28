import java.io.*;
import java.net.*;

public class SimpleChatClient {
    private static String serverAddress = "127.0.0.1";  // Use your server's IP address (127.0.0.1 for localhost)
    private static int serverPort = 12346;  // Use the same port as the server

    public static void main(String[] args) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Create a thread to listen for messages from the server
            Thread listenerThread = new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);  // Display messages from other clients
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            });
            listenerThread.start();

            // Allow user to input messages to send to the server
            String userMessage;
            while ((userMessage = userInput.readLine()) != null) {
                out.println(userMessage);  // Send user message to server
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}
