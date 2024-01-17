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

package client;

import java.io.IOException;
import java.util.Scanner;

import command.Pop3Command;

public class POP3Client {

    private static void start() throws IOException {
        Scanner sc = new Scanner(System.in);
        String username, password;
        System.out.println("POP3 and SMTP connection");
        System.out.println("Please enter your credentials to login");
        System.out.print("Username: ");
        username = sc.nextLine();
        System.out.print("Password: ");
        password = sc.nextLine();
        System.out.println(username);
        System.out.println(password);
        Pop3Command pop3Command = new Pop3Command();
        pop3Command.connect("localhost");
        pop3Command.login(username, password);
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
