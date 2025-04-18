import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
// Main class to launch Server or Client
public class ChatApp {
    public static final int PORT = 12345;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Start as (1) Server or (2) Client?");
        String choice = sc.nextLine();
        if (choice.equals("1")) {
            new ChatServer().startServer();
        } else if (choice.equals("2")) {
            System.out.print("Enter server IP (default: localhost): ");
            String host = sc.nextLine().trim();
            if (host.isEmpty()) host = "localhost";
            new ChatClient().startClient(host);
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
        sc.close();
    }
}
// Server Class
class ChatServer {
    private final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(ChatApp.PORT)) {
            System.out.println("Chat Server started on port " + ChatApp.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
      // Inner class to handle each client
        static class ClientHandler implements Runnable {
        private Socket socket;
        private ChatServer server;
        private PrintWriter out;
        private BufferedReader in;
        private String name;
        public ClientHandler(Socket socket, ChatServer server) {
            this.socket = socket;
            this.server = server;
        }
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Enter your name:");
                name = in.readLine();
                System.out.println(name + " joined the chat.");
                server.broadcast(name + " joined the chat.", this);
                String message;
                while ((message = in.readLine()) != null) {
                    String fullMessage = name + ": " + message;
                    System.out.println(fullMessage);
                    server.broadcast(fullMessage, this);
                }
            } catch (IOException e) {
                System.err.println("Client disconnected: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                server.removeClient(this);
                System.out.println(name + " left the chat.");
                server.broadcast(name + " left the chat.", this);
            }
        }
        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
// Client Class
class ChatClient {
    public void startClient(String host) {
        try (Socket socket = new Socket(host, ChatApp.PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to the chat server.");
            // Thread to listen for server messages
            Thread receiveThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            receiveThread.start();
            // Main thread to send messages
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }
        } catch (IOException e) {
            System.err.println("Unable to connect to server: " + e.getMessage());
        }
    }
}
