import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TransactionBroadcastingThread extends Thread{
    private Socket connection;
    private String message;

    public TransactionBroadcastingThread(String message){
        this.message = message;
    }

    @Override
    public void run() {
        super.run();

        try {
            connection = new Socket("192.168.100.57", 9999);
            OutputStream outputStream = this.connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            String message = this.message;
            printWriter.write(message + "\n");
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
