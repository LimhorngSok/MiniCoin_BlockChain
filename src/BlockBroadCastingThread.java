import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class BlockBroadCastingThread extends Thread {
    private String message;

    public BlockBroadCastingThread(String message){
        this.message = message;
    }

    @Override
    public void run() {
        super.run();

        try {
            Socket connection = new Socket("192.168.100.57", 1111);
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            String message = this.message;
            System.out.println(message+"dasdasdas");
            printWriter.write(message + "\r\n");
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
