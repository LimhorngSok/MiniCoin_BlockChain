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
            ServerSocket serverSocket = new ServerSocket(1111);
            while(true) {
                connection = serverSocket.accept();
                System.out.println("Received a block...");
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                int nBlocks = new File(MCPath.BLOCK_DIR).listFiles().length;
                OutputStream outputStream = new FileOutputStream(MCPath.BLOCK_DIR+"block_"+nBlocks+".txt",true);
                PrintWriter printWriter = new PrintWriter(outputStream,true);
                printWriter.write(scanner.nextLine());
                //Remove pending transactions
                PrintWriter writer = new PrintWriter(MCPath.PENDING_TRANSACTIONS);
                writer.print("");
                writer.close();

                printWriter.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
