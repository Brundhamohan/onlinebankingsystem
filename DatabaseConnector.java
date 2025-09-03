import java.sql.*;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/online_banking?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  
    private static final String PASSWORD = "#brundha@9704"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}