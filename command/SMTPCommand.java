package command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.SocketFactory;

/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

public class SMTPCommand {
    private Socket clientSocket;
    private boolean debug = true;
    private BufferedReader in;
    private BufferedWriter out;
    private static final int SMTP_PORT = 25;

    public void connect(String host, int port) throws IOException {
        debug = true;

        SocketFactory socketfactory = SocketFactory.getDefault();
        clientSocket = socketfactory.createSocket(host, SMTP_PORT);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        if (debug)
            System.out.println("Connected to the host");
        readResponseLine();
    }

    public void connect(String host) throws IOException {
        connect(host, SMTP_PORT);
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

    public void ehlo() throws IOException {
        sendCommand("EHLO localhost");
    }
    public void login(String username, String password) throws IOException {
        sendCommand("AUTH LOGIN");
        sendCommand(Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8)));
        sendCommand(Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
    }

    public void sendMail(String from, String to, String content) throws IOException {
        sendCommand("MAIL FROM:<" + from + ">");
        sendCommand("RCPT TO:<" + to + ">");
        sendCommand("DATA");
        sendCommand(content);
        sendCommand(".");
    }
    public void logout() throws IOException {
        sendCommand("QUIT");
    }

    public static void main(String[] args) throws IOException {
        SMTPCommand client = new SMTPCommand();
        client.connect("localhost");
        client.ehlo();
        client.login("buivanthanh26082002@gmail.com", "2002002002");
        client.sendMail("buivanthanh26082002@gmail.com", "thckpck11@gmail.com", "Test mail");
    }
}
