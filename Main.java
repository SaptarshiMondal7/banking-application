import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class BelowAgeException extends Exception {
    public BelowAgeException(String str) {
        super(str);
    }
}

class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String str) {
        super(str);
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String str) {
        super(str);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Do you want to open a new account or are do you have an existing account?");
            System.out.println("1. Open a new account");
            System.out.println("2. Existing account");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    System.out.print("Enter the username: ");
                    String newUsername = sc.next();
                    try(Connection conn = Database.getConnceted()){
                        PreparedStatement chckStmt = conn.prepareStatement("SELECT username FROM customers WHERE username = ?");
                        chckStmt.setString(1, newUsername);
                        ResultSet rs = chckStmt.executeQuery();
                        if(rs.next()){
                            System.out.println("Username already exits.");
                            break;
                        }
                    }
                    catch (SQLException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.print("Enter your name: ");
                    String name = sc.next();
                    System.out.print("Enter your age: ");
                    int age = sc.nextInt();
                    System.out.println("Enter your address");
                    System.out.print("\tEnter your house number: ");
                    String houseNumber = sc.next();
                    System.out.print("\tEnter your city: ");
                    String city = sc.next();
                    System.out.print("\tEnter your Postal address: ");
                    int PIN = sc.nextInt();
                    System.out.print("Enter the password: ");
                    String password = sc.next();

                    try(Connection conn = Database.getConnceted()){
                        BankAccount newAccount = new BankAccount();
                        String accountNumber = newAccount.getBankAccountNumber();
                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO customers (name, age, houseNumber, city, PIN, username, password, accountNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        stmt.setString(1, name);
                        stmt.setInt(2, age);
                        stmt.setString(3, houseNumber);
                        stmt.setString(4, city);
                        stmt.setInt(5, PIN);
                        stmt.setString(6, newUsername);
                        stmt.setString(7, password);
                        stmt.setString(8, accountNumber);
                        stmt.executeUpdate();
                        System.out.println("Account created successfully. Your account number is "+accountNumber);
                    } catch (SQLException e) {
                        System.out.println("Error creating the account: "+e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.print("Enter the username: ");
                    String userName = sc.next();
                    System.out.print("Enter the password: ");
                    String password = sc.next();
                    try(Connection conn = Database.getConnceted()) {
                        PreparedStatement stmt = conn.prepareStatement("SELECT accountNumber FROM customers WHERE username = ? AND password = ?");
                        stmt.setString(1, userName);
                        stmt.setString(2, password);
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next()){
                            String accountNumber = rs.getString("accountNumber");
                            BankAccount bankAccount = new BankAccount(accountNumber);
                            System.out.println("Login successful!");
                            boolean continueBanking = true;
                            while (continueBanking) {
                                System.out.println("Choose an option: ");
                                System.out.println("1. Deposit");
                                System.out.println("2. Withdraw");
                                System.out.println("3. Check Balance");
                                System.out.println("4. Exit");
                                int choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1: {
                                        System.out.println("Enter the money to be deposited");
                                        double depositedAmount = sc.nextDouble();
                                        try {
                                            bankAccount.deposit(depositedAmount);
                                        } catch (InvalidAmountException | SQLException e) {
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                    }
                                    case 2: {
                                        System.out.println("Enter the money to be withdrawn");
                                        double withdrawnAmount = sc.nextDouble();
                                        try {
                                            bankAccount.withdraw(withdrawnAmount);
                                        } catch (InvalidAmountException | InsufficientBalanceException |
                                                 SQLException e) {
                                            System.out.println(e.getMessage());
                                        }
                                        break;
                                    }
                                    case 3: {
                                        try {
                                            System.out.println("Current Balance: " + bankAccount.checkBalance());
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        System.out.println("Exiting the application");
                                        continueBanking = false;
                                        break;
                                    }
                                    default: {
                                        System.out.println("Invalid input");
                                    }
                                }
                            }
                        }
                    }
                    catch (SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3: {
                    System.out.println("Exiting...");
                    return;
                }
                default: {
                    System.out.println("Invalid option chosen. Choose again");
                }
            }
        }
    }
}