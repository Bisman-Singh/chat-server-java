import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Multi-client chat server. Broadcasts messages to all connected clients.
 * Protocol: NAME:username, MSG:message
 * Author: Bisman Singh <bismanmadaan1@gmail.com>
 */
public class ChatServer {
    private static final int PORT = 9999;
    private static final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Chat server on port " + PORT);
            while (true) {
                Socket client = server.accept();
                ClientHandler handler = new ClientHandler(client);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    static void broadcast(String message, ClientHandler exclude) {
        for (ClientHandler c : clients) {
            if (c != exclude && c.username != null) {
                c.send(message);
            }
        }
    }

    static void remove(ClientHandler c) {
        clients.remove(c);
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("NAME:")) {
                        username = line.substring(5).trim();
                        broadcast(username + " joined", this);
                    } else if (line.startsWith("MSG:") && username != null) {
                        broadcast(username + ": " + line.substring(4), this);
                    }
                }
            } catch (IOException e) {
                // client disconnected
            } finally {
                if (username != null) {
                    broadcast(username + " left", this);
                }
                remove(this);
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        void send(String msg) {
            if (out != null) {
                out.println(msg);
            }
        }
    }
}
