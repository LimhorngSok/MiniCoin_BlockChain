import java.io.*;
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
    private static void sendCoin(){
        String receiver;
        float amount;
        System.out.println("Put receiver address:");
        Scanner scanReceiver = new Scanner(System.in);
        receiver = scanReceiver.nextLine();

        System.out.println("Amount:");
        Scanner scanAmount = new Scanner(System.in);
        amount = scanAmount.nextInt();

    }
    private static void mineCoin(){
        System.out.println("Mining...");
        String transactionDir = Path.TRANSACTION_DIR;


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

}
