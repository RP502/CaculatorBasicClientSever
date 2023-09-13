package ServerTest;
import java.io.*;
import java.net.*;

public class ParallelTCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client request in a new thread
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read input from client
            float a = Float.parseFloat(in.readLine());
            float b = Float.parseFloat(in.readLine());
            char operator = in.readLine().charAt(0);

            // Calculate the result
            float result;
            switch (operator) {
                case '+':
                    result = a + b;
                    break;
                case '-':
                    result = a - b;
                    break;
                case '*':
                    result = a * b;
                    break;
                case '/':
                    result = a / b;
                    break;
                default:
                    result = 0;
                    break;
            }

            // Send result to client
            out.println("Result: " + result);

            // Clean up
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
