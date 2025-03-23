import java.sql.*;

public class Database {
    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found");
        }
    }
    public static Connection getConnceted() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/BankingDB", "root", "root");
    }
}