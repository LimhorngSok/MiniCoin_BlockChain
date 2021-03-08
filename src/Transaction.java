
import java.lang.reflect.Array;
import java.sql.Time;
import java.sql.Timestamp;

public class Transaction {
    private String sender;
    private String receiver;
    private Timestamp timestamp;
    private float amount;
    private String status;
    private int signature;

    public Transaction(String sender, String receiver, float amount, Timestamp timestamp, String status){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }

    public int getSignature() {
        return signature;
    }

    public void setSignature(int signature) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString(){
        String transaction;
        if(this.sender != null){

            transaction = this.sender+"-"+this.receiver+"-"+this.amount+"-"+this.status+"-"+this.timestamp+"-"+this.signature+"\r\n";
        }else{
            transaction = this.receiver + " gets " + this.amount + " at " + timestamp;
        }

        return transaction;
    }
    public int signTransaction(String privateKey){
        int signature = 0;
        Object obj[] = new Object[]{this,privateKey};
        signature = obj.hashCode();
        this.setSignature(signature);
        this.setStatus("pending");

        return signature;
    }

}
