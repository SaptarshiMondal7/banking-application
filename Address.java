public class Address {
    private String houseNumber;
    private String ciiy;
    private int PIN;
    public Address(String houseNumber, String city, int PIN){
        this.houseNumber = houseNumber;
        this.ciiy = city;
        this.PIN = PIN;
    }
    @Override
    public String toString() {
        return "Address:\n"+"\tFlat Number: "+houseNumber+",\n\tCity: "+ciiy+",\n\tPIN: "+PIN;
    }
}
