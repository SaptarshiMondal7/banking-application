import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount implements BankingOperations {
//    private static int bankAccountCounter = 1000;
    private static final String prefix = "MUN";
    private String bankAccountNumber;
    private double balance;

    private String generateAccountNumber(){
        try (Connection conn = Database.getConnceted()) {
            // Ensure the counter table exists
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS account_counter (id INT PRIMARY KEY, last_counter INT)");
            // Insert initial value if not present
            conn.createStatement().execute(
                    "INSERT IGNORE INTO account_counter (id, last_counter) VALUES (1, 1000)");

            // Atomically increment and get the counter
            PreparedStatement updatePs = conn.prepareStatement(
                    "UPDATE account_counter SET last_counter = last_counter + 1 WHERE id = 1");
            updatePs.executeUpdate();

            PreparedStatement selectPs = conn.prepareStatement(
                    "SELECT last_counter FROM account_counter WHERE id = 1");
            ResultSet rs = selectPs.executeQuery();
            if (rs.next()) {
                int counter = rs.getInt("last_counter");
                String newAccountNumber = prefix + counter;
                System.out.println("Generated account number: " + newAccountNumber); // Debug
                return newAccountNumber;
            }
            throw new SQLException("Failed to generate account number");
        } catch (SQLException e) {
            throw new RuntimeException("Error generating account number: " + e.getMessage());
        }
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    //for new customers
    public BankAccount(){
        this.bankAccountNumber = generateAccountNumber();
        this.balance = 0;
        try(Connection conn = Database.getConnceted()){
            PreparedStatement ps = conn.prepareStatement("INSERT INTO bankAccounts (accountNumber, balance) VALUES (?, ?)");
            ps.setString(1, bankAccountNumber);
            ps.setDouble(2, balance);
            ps.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //for existing customers
    public BankAccount(String accountNumber) throws SQLException{
        this.bankAccountNumber = accountNumber;
        this.balance = checkBalance();
    }
    public void deposit(double amount) throws InvalidAmountException, SQLException {
        if(amount<=0)
            throw new InvalidAmountException("You have entered an invalid amount to be deposited.");
        Connection conn = Database.getConnceted();
        PreparedStatement ps = conn.prepareStatement("UPDATE bankAccounts SET balance = balance + ? WHERE accountNumber = ?");
        ps.setDouble(1, amount);
        ps.setString(2, bankAccountNumber);
        ps.executeUpdate();
        System.out.println(amount+" rupees was deposited");
    }
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException, SQLException{
        if(amount<=0)
            throw new InvalidAmountException("You have entered a negative amount to be withdrawn.");
        Connection conn = Database.getConnceted();
        PreparedStatement checkBal = conn.prepareStatement("SELECT balance FROM bankAccounts WHERE accountNumber = ?");
        checkBal.setString(1, bankAccountNumber);
        ResultSet rs = checkBal.executeQuery();
        if(rs.next()){
            double bal = rs.getDouble("balance");
            if(amount>bal)
                throw new InsufficientBalanceException("You have low balance.");
            PreparedStatement ps = conn.prepareStatement("UPDATE bankAccounts SET balance = balance - ? WHERE accountNumber = ?");
            ps.setDouble(1, amount);
            ps.setString(2, bankAccountNumber);
            ps.executeUpdate();
            System.out.println(amount+" rupees was withdrawn");
        }
    }
    public double checkBalance() throws SQLException{
        Connection conn = Database.getConnceted();
        PreparedStatement ps = conn.prepareStatement("SELECT balance FROM bankAccounts WHERE accountNumber = ?");
        ps.setString(1, bankAccountNumber);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return rs.getDouble("balance");
        return 0;
    }
}
