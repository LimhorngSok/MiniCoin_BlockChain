import java.util.List;

public class Block {
    private String preHash;
    private List<String> transactions;
    private int proofOfWork;
    private int blockHash;

    public Block(String preHash, List<String> transactions, int proofOfWork) {
        this.preHash = preHash;
        this.transactions = transactions;
        this.proofOfWork = proofOfWork;
    }

    @Override
    public String toString() {
        return preHash + "|" + transactions + "|" + proofOfWork+"|"+blockHash;
    }

    public int getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(int blockHash) {
        this.blockHash = blockHash;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }
}
