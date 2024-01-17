package command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.net.SocketFactory;

public class Pop3Command {
    private Socket clientSocket;
    private boolean debug = false;
    private BufferedReader in;
    private BufferedWriter out;
    private static final int PORT = 110;   //110;

    public boolean isDebug() {
        return debug;
    }

    public void connect(String host, int port) throws IOException {
        debug = true;

        SocketFactory sslsocketfactory =  SocketFactory.getDefault();
        clientSocket = sslsocketfactory.createSocket(host, PORT);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        if (debug)
            System.out.println("Connected to the host");
        readResponseLine();
    }

    public void connect(String host) throws IOException {
        connect(host, PORT);
    }

    public boolean isConnected() {
        return clientSocket != null && clientSocket.isConnected();
    }

    public void disconnect() throws IOException {
        if (!isConnected())
            throw new IllegalStateException("Not connected to a host");
        clientSocket.close();
        in = null;
        out = null;
        if (debug)
            System.out.println("Disconnected from the host");
    }

    protected String readResponseLine() throws IOException{
        String response = in.readLine();
        if (debug) {
            System.out.println("DEBUG [in] : " + response);
        }
        if (response.startsWith("-ERR"))
            throw new RuntimeException("Server has returned an error: " + response.replaceFirst("-ERR ", ""));
        return response;
    }

    protected String sendCommand(String command) throws IOException {
        if (debug) {
            System.out.println("DEBUG [out]: " + command);
        }
        out.write(command + "\n");
        out.flush();
        return readResponseLine();
    }

    public void login(String username, String password) throws IOException {
        sendCommand("USER " + username);
        sendCommand("PASS " + password);
    }

    public void logout() throws IOException {
        sendCommand("QUIT");
    }

    public int getNumberOfNewMessages() throws IOException {
        String response = sendCommand("STAT");
        String[] values = response.split(" ");
        return Integer.parseInt(values[1]); //value[0] - busena, value[1] - pranesimu skaicius value[2] - pranesimu uzimama vieta
    }

    public void getContentMessage() throws IOException {
        String response = sendCommand("RETR 1");
        System.out.println(response);
    }
    public static void main(String[] args) throws IOException {
        Pop3Command client = new Pop3Command();
        System.out.println("Created Pop3Client");
        client.connect("localhost");
        client.login("user2@localhost", "passwd1");
        while(true) {
            System.out.print("Yes or no: ");
            Scanner sc = new Scanner(System.in);
            int cmd = sc.nextInt();
            if (cmd == 1) {
                System.out.println("Number of new emails: " + client.getNumberOfNewMessages());
                break;
            }
        }

        client.getContentMessage();
        client.logout();
        client.disconnect();
    }
}