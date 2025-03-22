public class BankAccount implements BankingOperations {
    private static String bankAccountCounter = "MUNC"+1000;
    private String accountNumber;
    private double balance;
    public BankAccount(double balance){
        this.accountNumber = bankAccountCounter+1;
        this.balance = balance;
    }

    public void deposit(double amount) throws InvalidAmountException{
        if(amount<=0)
            throw new InvalidAmountException("You have entered an invalid amount to be deposited.");
        balance += amount;
        System.out.println(amount+" rupees was deposited");
    }
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException{
        if(amount<=0)
            throw new InvalidAmountException("You have entered an invalid amount to be withdrawn.");
        if(amount>balance)
            throw new InsufficientBalanceException("You have low balance.");
        balance -= amount;
        System.out.println(amount+" rupees was withdrawn");
    }
    public double checkBalance(){
        return balance;
    }
}
