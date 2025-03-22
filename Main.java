import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class BelowAgeException extends Exception{
    public BelowAgeException(String str){
        super(str);
    }
}

class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException(String str) {
        super(str);
    }
}

class InvalidAmountException extends Exception{
    public InvalidAmountException(String str){
        super(str);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Customer> customers = new HashMap<>();
        while(true){
            System.out.println("Do you want to open a new account or are do you have an existing account?");
            System.out.println("1. Open a new account");
            System.out.println("2. Existing account");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            switch(choice){
                case 1 : {
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
                    System.out.print("Enter the username: ");
                    String newUsername = sc.next();
                    System.out.print("Enter the password: ");
                    String password = sc.next();
                    if(customers.containsKey(newUsername))
                        System.out.println("Account already exits. Choose a new username.");
                    else{
                        try{
                            customers.put(newUsername, new Customer(name, age, new Address(houseNumber, city, PIN), newUsername, password, new BankAccount(500)));
                            System.out.println("Account successfully created.");
                        }catch (BelowAgeException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                }
                case 2 :{
                    System.out.print("Enter the username: ");
                    String userName = sc.next();
                    System.out.print("Enter the password: ");
                    String password = sc.next();
                    Customer customer = customers.get(userName);
                    if(customer == null || !(customer.authenticate(userName, password))) {
                        System.out.println("Account doesn't exist");
                        break;
                    }
                    else{
                        BankAccount bankAccount = customer.getBankAccount();
                        while(true){
                            System.out.println("Choose an option: ");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Exit");
                            int choice2 = sc.nextInt();
                            switch (choice2){
                                case 1: {
                                    System.out.println("Enter the money to be deposited");
                                    double depositedAmount = sc.nextDouble();
                                    try{
                                        bankAccount.deposit(depositedAmount);
                                    }catch (InvalidAmountException e){
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                }
                                case 2: {
                                    System.out.println("Enter the money to be withdrawn");
                                    double withdrawnAmount = sc.nextDouble();
                                    try{
                                        bankAccount.withdraw(withdrawnAmount);
                                    }
                                    catch (InvalidAmountException | InsufficientBalanceException e){
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                }
                                case 3: {
                                    System.out.println("Current Balance: "+ bankAccount.checkBalance());
                                    break;
                                }
                                case 4: {
                                    System.out.println("Exiting the application");
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid input");
                                }
                            }
                        }
                    }
                }
                case 3 : {
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