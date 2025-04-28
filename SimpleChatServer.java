
import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleChatServer {
    private static List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(12346)) {  // Server listens on port 12345
            while (true) {
                new ClientHandler(serverSocket.accept()).start();  // Accept and handle each client
            }
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message) {
        for (PrintWriter out : clients) {
            out.println(message);
        }
    }

    // Inner class to handle client communication
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clients) {
                    clients.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Message from client: " + message);
                    broadcast(message);  // Broadcast message to all clients
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                    synchronized (clients) {
                        clients.remove(out);
                    }
                } catch (IOException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
