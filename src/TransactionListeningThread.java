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
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNextLine()) {
                OutputStream outputStream = new FileOutputStream(MCPath.PENDING_TRANSACTIONS,true);
                PrintWriter printWriter = new PrintWriter(outputStream,true);
                printWriter.write(scanner.nextLine());
                printWriter.flush();
            }
            System.out.println("Listening...");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
