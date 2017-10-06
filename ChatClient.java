package maya;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client listens on a PORT hardcoded to IP Address 18.221.102.182 PORT 38001. Client inputs an initial String that will represent
 * the user name. From there socket input is sent as chat. Client can also receive and display chat messages from other users.
 */

public class ChatClient {
    private static final String HOST_IP = "18.221.102.182";
    private static final int PORT = 38001;

    public static void main(String[] args) throws Exception {
        ChatClient chatClient = new ChatClient();
        chatClient.main();
    }

    public void main(){
        try (Socket socket = new Socket(HOST_IP, PORT)) {
            //Read from user input
            InputStreamReader isrInput = new InputStreamReader(System.in);
            BufferedReader brInput = new BufferedReader(isrInput);

            //Read from server
            InputStream is = socket.getInputStream();
            InputStreamReader isrRead = new InputStreamReader(is, "UTF-8");
            BufferedReader brRead = new BufferedReader(isrRead);

            //Write to server
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os, true, "UTF-8");

            Runnable readMessages = () -> {
                String line;
                try {
                    while (true) {
                        line = brRead.readLine();
                        System.out.println(line);
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe);
                }
            };

            //Read from server via Thread
            Thread readThread = new Thread(readMessages);
            readThread.start();

            //Send user input to server
            for (String line = brInput.readLine(); line != null; line = brInput.readLine()) {
                out.println(line);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}