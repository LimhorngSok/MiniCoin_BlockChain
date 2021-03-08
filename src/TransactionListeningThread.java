import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TransactionListeningThread extends Thread{
    private Socket connection;
    @Override
    public void run() {
        super.run();


        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            connection = serverSocket.accept();
            Thread thread = new ThreadReader(connection);
            thread.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
