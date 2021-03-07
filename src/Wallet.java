public class Wallet {
    private String privateKey;
    private String publicKey;
    private float amount;

    public Wallet(String privateKey, String publicKey, float amount){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.amount = amount;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    public String toString(){
        return this.privateKey+"-"+this.publicKey+"-"+this.amount;
    }
}
