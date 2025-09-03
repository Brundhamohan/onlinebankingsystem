import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String type;
    private double amount;
    private Timestamp timestamp;

    public Transaction(int transactionId, int accountId, String type, double amount, Timestamp timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
public String toString() {
    return "Transaction #" + transactionId + " | Account ID: " + accountId + 
           " | " + type + " of INR " + amount + " on " + timestamp;
}
}