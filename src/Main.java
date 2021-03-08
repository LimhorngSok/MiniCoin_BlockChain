import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static Wallet wallet = new Wallet("","",0.0F);


    public static void main(String[] args) throws FileNotFoundException {
        verifyAccount();
        Float balance = calculateBalance();
        while (true){
            System.out.println("Your account balance: MC" + balance);
            System.out.println("Select option by typing number:");
            System.out.println("1. Check account detail");
            System.out.println("2. Send miniCoin to other");
            System.out.println("3. Mine miniCoin");
            System.out.println("4. Exit");
            Scanner userInput = new Scanner(System.in);
            int option = userInput.nextInt();
            switch(option){
                case 1:
                    checkAccount();
                    break;
                case 2:
                    sendCoin();
                    break;
                case 3:
                    mineCoin();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("No such an option!");
            }
        }

    }
    //To check if this machine already registered in MC system
    private static void verifyAccount() throws FileNotFoundException {
        String filePath = "tmp/my_account.txt";
        File file = new File(filePath);
        if(file.exists()){
            try {
                InputStream inputStream = new FileInputStream(filePath);
                Scanner scanner = new Scanner(inputStream);
                if(scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String params[] = line.split("-");
                    if(params[0] != null && params[1] !=null && params[2] != null){
//                        wallet = new Wallet(params[0],params[1],Float.parseFloat(params[2]));
                        wallet.setAmount(Float.parseFloat(params[2]));
                        wallet.setPrivateKey(params[0]);
                        wallet.setPublicKey(params[1]);
                    }else{
                        System.out.println("code in file is not valid");
                        createAccount();
                    }
                }else{
                    System.out.println("file is empty");
                    createAccount();
                }
                scanner.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("file does not exist");
            createAccount();
        }
    }
    private static float calculateBalance(){
        float balance = 0.00F;
        balance = wallet.getAmount();
        return balance;
    }
    private static void checkAccount(){
        System.out.println("Private Key:");
        System.out.println(wallet.getPrivateKey());
        System.out.println("Public Key:");
        System.out.println(wallet.getPublicKey());
        System.out.println("Amount:");
        System.out.println("MC "+wallet.getAmount());
    }
    private static void sendCoin() throws IOException {
        String receiver;
        float amount;
        System.out.println("Put receiver address:");
        Scanner scanReceiver = new Scanner(System.in);
        receiver = scanReceiver.nextLine();
        System.out.println("Amount:");
        Scanner scanAmount = new Scanner(System.in);
        amount = Math.abs(scanAmount.nextFloat());
        System.out.println("Sending...");
        if((wallet.getAmount() - amount) >= 0.0F){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Transaction transaction = new Transaction(wallet.getPublicKey(),receiver,amount,timestamp,"unsigned");
            transaction.signTransaction(wallet.getPrivateKey());
            //Write the file into pending transaction list
            OutputStream outputStream = new FileOutputStream(Path.PENDING_TRANSACTIONS,true);
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.write(transaction.toString());
            printWriter.flush();

            System.out.println("Please wait for confirmation to be made before other can use the MC");
            broadcastTransaction(transaction.toString());
      }else{
            System.out.println("Insufficient Balance");
        }

    }
    private static void mineCoin(){
        System.out.println("Mining...");
        String pendingTransactionsPath = Path.PENDING_TRANSACTIONS;
        boolean isMined = false;
        while (isMined == false){
            
        }

    }
    private static void createAccount() throws FileNotFoundException {
        System.out.println("This machine has not registered yet!");
        System.out.println("Registering...");

        String publicKey = UUID.randomUUID().toString().replace("-", "");
        String privateKey = UUID.randomUUID().toString().replace("-", "");
        float amount = 0.0F;

        OutputStream outputStream = new FileOutputStream(Path.MY_ACCOUNT);
        PrintWriter printWriter = new PrintWriter(outputStream,true);
        wallet.setAmount(amount);
        wallet.setPrivateKey(privateKey);
        wallet.setPublicKey(publicKey);
        printWriter.write(privateKey+"-"+publicKey+"-"+amount);

        printWriter.flush();
        System.out.println("This machine has been registered");


        System.out.println(privateKey+"-"+publicKey+"-"+amount);
    }
    private static void broadcastTransaction(String transaction) throws IOException {

        DatagramSocket socket;
        InetAddress group;
        byte[] buf;

        System.out.println("Broadcasting the transaction...");
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = transaction.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

}
