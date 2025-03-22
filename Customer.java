public class Customer {
    private String name;
    private int age;
    private Address address;
    private String userName;
    private String password;
    private BankAccount bankAccount;
    public String getName(){
        return name;
    }
    public BankAccount getBankAccount(){
        return bankAccount;
    }
    public Customer(String name, int age, Address address, String userName, String password, BankAccount bankAccount) throws BelowAgeException{
        if(age<18)
            throw new BelowAgeException("You are not eligible to open a bank account.");
        this.name = name;
        this.age = age;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.bankAccount = bankAccount;
    }
    public boolean authenticate(String userName, String password) {
        return this.userName.equals(userName) && this.password.equals(password);
    }
    @Override
    public String toString() {
        try{
            if(age<18)
                throw new BelowAgeException("You are below 18 years old");
        } catch (BelowAgeException e) {
            System.out.println("You need to be above 18 for opening a bank account");
        }
        return "Name: "+name+",\n"+"Age: "+age+",\n"+address;
    }
}