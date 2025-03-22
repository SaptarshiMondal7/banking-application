public interface BankingOperations {
    public void deposit(double amount) throws InvalidAmountException;
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException;
    public double checkBalance();
}
