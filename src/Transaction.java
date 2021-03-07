public class Transaction {
    private String sender;
    private String receiver;
    private long timestamp;
    private float amount;
    private boolean status;
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString(){
        String transaction;
        if(this.sender != null){
            transaction = this.sender+" pays " + this.receiver + " MC" + this.amount + " at " + timestamp;
        }else{
            transaction = this.receiver + " gets " + this.amount + " at " + timestamp;
        }

        return transaction;
    }
    public static boolean checkBalance(){

        return false;
    }
}
