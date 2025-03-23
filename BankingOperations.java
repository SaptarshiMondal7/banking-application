import java.sql.SQLException;

public interface BankingOperations {
    public void deposit(double amount) throws InvalidAmountException, SQLException;
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException, SQLException;
    public double checkBalance() throws SQLException;
}