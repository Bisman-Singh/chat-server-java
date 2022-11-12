import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Chat client. Connects to server, sends NAME and MSG, receives broadcasts.
 * Author: Bisman Singh <bismanmadaan1@gmail.com>
 */
public class ChatClient {
    private static final String HOST = "localhost";
    private static final int PORT = 9999;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : HOST;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : PORT;

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter username: ");
            String username = sc.nextLine().trim();
            out.println("NAME:" + username);

            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected.");
                }
            }).start();

            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                if (msg.equalsIgnoreCase("/quit")) break;
                out.println("MSG:" + msg);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
