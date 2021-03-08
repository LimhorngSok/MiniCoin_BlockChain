import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TransactionBroadcastingThread extends Thread{

    private String message;

    public TransactionBroadcastingThread(String message){
        this.message = message;
    }

    @Override
    public void run() {
        super.run();

        try {
            Socket connection = new Socket("192.168.100.57", 9999);
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            String message = this.message;
            printWriter.write(message);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
