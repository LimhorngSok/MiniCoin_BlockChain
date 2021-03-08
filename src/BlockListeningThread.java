import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class BlockListeningThread extends Thread{
    private Socket connection;
    @Override
    public void run() {
        super.run();

        try {

            ServerSocket serverSocket = new ServerSocket(8888);
            connection = serverSocket.accept();
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNextLine()) {
                int nBlocks = new File(MCPath.BLOCK_DIR).listFiles().length;
                OutputStream outputStream = new FileOutputStream(MCPath.BLOCK_DIR+"block_"+nBlocks+".txt",true);
                PrintWriter printWriter = new PrintWriter(outputStream,true);
                printWriter.write(scanner.nextLine());
                printWriter.flush();
                outputStream.close();
            }
            inputStream.close();
            scanner.close();
            System.out.println("Listening...");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
