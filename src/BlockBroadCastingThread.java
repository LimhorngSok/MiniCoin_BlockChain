import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class BlockBroadCastingThread extends Thread {
    private Socket connection;
    private String message;

    public BlockBroadCastingThread(String message){
        this.message = message;
    }

    @Override
    public void run() {
        super.run();

        try {
            connection = new Socket("192.168.100.57", 8888);
            OutputStream outputStream = this.connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            String message = this.message;
            printWriter.write(message + "\r\n");
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
