import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ThreadReader extends Thread{
    private Socket connection;
    public ThreadReader(Socket connection){
        this.connection = connection;
    }
    @Override
    public void run() {
        super.run();
        try {

            InputStream inputStream = this.connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while (true) {
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
