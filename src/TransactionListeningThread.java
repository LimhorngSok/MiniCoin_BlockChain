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

            while (true) {
                connection = serverSocket.accept();
                InputStream inputStream = this.connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                System.out.println("Recieving");
                OutputStream outputStream = new FileOutputStream(MCPath.PENDING_TRANSACTIONS, true);
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.write(scanner.nextLine()+"\r\n");
                printWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
