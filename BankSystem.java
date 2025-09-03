import java.security.MessageDigest;
import java.sql.*;

public class BankSystem {

    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static boolean register(String username, String password) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String hashed = hashPassword(password);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password_hash) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, hashed);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT user_id FROM users WHERE username='" + username + "'");
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    st.execute("INSERT INTO accounts (user_id, balance) VALUES (" + userId + ", 0)");
                }
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static User login(String username, String password) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String hashed = hashPassword(password);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password_hash=?");
            ps.setString(1, username);
            ps.setString(2, hashed);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static void deposit(int userId, double amount) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance=balance+? WHERE user_id=?");
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();
            recordTransaction(conn, userId, "Deposit", amount);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void withdraw(int userId, double amount) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement check = conn.prepareStatement("SELECT balance FROM accounts WHERE user_id=?");
            check.setInt(1, userId);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance=balance-? WHERE user_id=?");
                ps.setDouble(1, amount);
                ps.setInt(2, userId);
                ps.executeUpdate();
                recordTransaction(conn, userId, "Withdraw", amount);
            } else {
                System.out.println("Insufficient Balance!");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void transfer(int fromUserId, int toUserId, double amount) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement check = conn.prepareStatement("SELECT balance FROM accounts WHERE user_id=?");
            check.setInt(1, fromUserId);
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement withdraw = conn.prepareStatement("UPDATE accounts SET balance=balance-? WHERE user_id=?");
                withdraw.setDouble(1, amount);
                withdraw.setInt(2, fromUserId);
                withdraw.executeUpdate();

                PreparedStatement deposit = conn.prepareStatement("UPDATE accounts SET balance=balance+? WHERE user_id=?");
                deposit.setDouble(1, amount);
                deposit.setInt(2, toUserId);
                deposit.executeUpdate();

                recordTransaction(conn, fromUserId, "Transfer Out", amount);
                recordTransaction(conn, toUserId, "Transfer In", amount);

                conn.commit();
                System.out.println("Transfer successful!");
            } else {
                System.out.println("Insufficient Balance!");
                conn.rollback();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static int getUserIdByUsername(String username) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public static void checkBalance(int userId) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE user_id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) System.out.println("Balance (INR): " + rs.getDouble("balance"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void viewTransactions(int userId) {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM transactions WHERE account_id=(SELECT account_id FROM accounts WHERE user_id=?) ORDER BY timestamp DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
    Transaction txn = new Transaction(
        rs.getInt("transaction_id"),
        rs.getInt("account_id"),
        rs.getString("type"),
        rs.getDouble("amount"),
        rs.getTimestamp("timestamp")
    );
    System.out.println(txn);
}

        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void recordTransaction(Connection conn, int userId, String type, double amount) throws SQLException {
        PreparedStatement getAcc = conn.prepareStatement("SELECT account_id FROM accounts WHERE user_id=?");
        getAcc.setInt(1, userId);
        ResultSet rs = getAcc.executeQuery();
        if (rs.next()) {
            int accId = rs.getInt("account_id");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)");
            ps.setInt(1, accId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        }
    }
}