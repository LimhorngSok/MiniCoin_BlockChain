import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

public class Main {
    private static Wallet wallet = new Wallet("","",0.0F);


    public static void main(String[] args) throws IOException {
        verifyAccount();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Thread thread = new TransactionListeningThread();
                thread.run();
            }
        };
        new Thread(r).start();
        Runnable r1 = new Runnable(){
            @Override
            public void run() {
                Thread thread = new BlockListeningThread();
                thread.run();
            }
        };
        new Thread(r1).start();


        while (true){
            Float balance = calculateBalance();
            System.out.println("Your account balance: MC" + balance);
            System.out.println("Select option by typing number:");
            System.out.println("1. Check account detail");
            System.out.println("2. Send MiniCoin to other");
            System.out.println("3. Mine MiniCoin");
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
                    System.exit(0);
                    return;
                default:
                    System.out.println("No such an option!");
            }
        }

    }
    //To check if this machine already registered in MC system
    private static void verifyAccount() throws FileNotFoundException, UnknownHostException {
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
    private static float calculateBalance() throws FileNotFoundException {
        float balance = 0.00F;
        //Get length of block
        int nBlocks = new File(MCPath.BLOCK_DIR).listFiles().length;
        //Read from transactions from blocks
        if(nBlocks > 0){
            for(int i=0 ; i < nBlocks ; i++){
                //Read Block and Take Transaction
                InputStream inputStream = new FileInputStream(MCPath.BLOCK_DIR+"block_"+i+".txt");
                Scanner scanner = new Scanner(inputStream);
                if(scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String params[] = line.split("\\|");
                    if(params[0] != null && params[1] !=null && params[2] != null){
                        String s1 = params[1].replace("[","");
                        String s = s1.replace("]","");
                        //Convert Transaction to List
                        List<String> transactions = new ArrayList<String>(Arrays.asList(s.split(", ")));
                        //Read Transaction
                        for(int j = 0; j < transactions.size(); j++){
                            String transaction = transactions.get(j);
                            String attr[] = transaction.split("-");
                            //Add Balance
                            if(attr.length >2) {
                                if (attr[0].equals(wallet.getPublicKey())) {

                                    balance -= Float.parseFloat(attr[2]);
                                }else if(attr[1].equals(wallet.getPublicKey()))
                                {
                                    balance += Float.parseFloat(attr[2]);
                                }
                            }
                        }
                    }else{
                        System.out.println("code in file is not valid");
                    }
                }else{
                    System.out.println("file is empty");
                }
            }
        }

        //Read transactions from pending
        InputStream inputStream = new FileInputStream(MCPath.PENDING_TRANSACTIONS);
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String params[] = line.split("-");
            if (params[0].equals(wallet.getPublicKey())) {
                balance -= Float.parseFloat(params[2]);
            }
        }

        wallet.setAmount(balance);
        return balance;
    }
    //[OPTION] Return the Account information
    private static void checkAccount() throws FileNotFoundException {
        calculateBalance();
        System.out.println("Private Key:");
        System.out.println(wallet.getPrivateKey());
        System.out.println("Public Key:");
        System.out.println(wallet.getPublicKey());
        System.out.println("Amount:");
        System.out.println("MC "+wallet.getAmount());
    }
    //[OPTION] for sending the coin to someone
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
            OutputStream outputStream = new FileOutputStream(MCPath.PENDING_TRANSACTIONS,true);
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.write(transaction.toString()+"\r\n");
            printWriter.flush();

            System.out.println("Please wait for confirmation to be made before other can use the MC");
            broadcastTransaction(transaction.toString());
      }else{
            System.out.println("Insufficient Balance");
        }

    }
    //[OPTION] mine bitcoin
    private static void mineCoin() throws IOException {
        System.out.println("Mining...");
        String prevHash = null;
        List<String> transactions = new ArrayList<String>();
        Block confirmedBlock = null;
        File  pendingTransactionsPath = new File(MCPath.PENDING_TRANSACTIONS);
        File tmpTransactionsPath = new File(MCPath.TMP_TRANSACTIONS);
        boolean isMined = false;


        //Copy Transactions from pending to tmp
        Files.copy(pendingTransactionsPath.toPath(), tmpTransactionsPath.toPath(), StandardCopyOption.REPLACE_EXISTING);

        //Scan all transactions for confirmation
        InputStream inputTmpTransactions = new FileInputStream(tmpTransactionsPath);
        Scanner scannerTmpTransactions = new Scanner(inputTmpTransactions);
        while (scannerTmpTransactions.hasNext()){
            transactions.add(scannerTmpTransactions.nextLine());
        }

        //Find the number of blocks
        int nBlocks = new File(MCPath.BLOCK_DIR).listFiles().length;
        //check if there is existing blocks
        if(nBlocks == 0){
            prevHash = null;
        }else{
            //Open the last block and get the prevHash
            String lastBlockPath = "block_"+(nBlocks-1)+".txt";
            InputStream inputStream = new FileInputStream(MCPath.BLOCK_DIR+lastBlockPath);
            Scanner scanner = new Scanner(inputStream);
            if(scanner.hasNext()){
                String blockContent = scanner.nextLine();
                String params[] = blockContent.split("\\|");
                prevHash = params[3];
                //Close all the inputSteam and Scanner
                inputTmpTransactions.close();
                scannerTmpTransactions.close();
                inputStream.close();
                scanner.close();
            }
        }

        //Get transactions from tmp
        int n = 0;
        while (isMined == false){

            //Generate proof of work
            n++;

            //Hash all of them together
            Block block = new Block(prevHash,transactions,n);
            int hashedBlock = block.hashCode();
            System.out.println(hashedBlock);
            String sHashedBlock = String.valueOf(hashedBlock);

            //If the hashedBlock is started with "11"
            if(sHashedBlock.charAt(0) == '1' && sHashedBlock.charAt(1) == '1'){
                confirmedBlock = block;
                confirmedBlock.setBlockHash(hashedBlock);

                //add new transaction for rewarding
                transactions.add("System-"+wallet.getPublicKey()+"-50.0-succeed-"+ new Timestamp(System.currentTimeMillis())+"-"+hashedBlock);
                confirmedBlock.setTransactions(transactions);


                //Return isMined = true;
                isMined = true;
                System.out.println("You have mined a block, and you will be rewarded 50MC");
            }

        }
        //Add new transaction to block
        //Store Block
        OutputStream outputStream = new FileOutputStream(MCPath.BLOCK_DIR+"block_"+nBlocks+".txt",true);
        PrintWriter printWriter = new PrintWriter(outputStream,true);
        printWriter.write(confirmedBlock.toString());
        printWriter.flush();
        outputStream.close();

        //Remove pending
        PrintWriter writer = new PrintWriter(MCPath.PENDING_TRANSACTIONS);
        writer.print("");
        writer.close();

        //Broadcast it to other nodes
        broadcastBlock(confirmedBlock.toString());

    }
    private static void createAccount() throws FileNotFoundException, UnknownHostException {
        System.out.println("This machine has not registered yet!");
        System.out.println("Registering...");
        String params[] = Inet4Address.getLocalHost().toString().split("/");
        String myIP = params[1];
        System.out.println("Your IP Address: "+myIP);
        //Store new node
        if(myIP != null){
            OutputStream outputStream = new FileOutputStream(MCPath.NODE_TXT,true);
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.write(myIP+"\r\n");
            printWriter.flush();
        }
        //Generate Public Key and Private Key
        String publicKey = UUID.randomUUID().toString().replace("-", "");
        String privateKey = UUID.randomUUID().toString().replace("-", "");
        float amount = 0.0F;

        OutputStream outputStream = new FileOutputStream(MCPath.MY_ACCOUNT);
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
        Thread thread = new TransactionBroadcastingThread(transaction);
        thread.run();
    }
    private static void broadcastBlock(String block){
        Thread thread = new BlockBroadCastingThread(block);
        thread.run();
    }

}
