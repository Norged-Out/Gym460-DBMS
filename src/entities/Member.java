package entities;

public class Member {
    
    // fields
    public int m;
    public String firstName;
    public String lastName;
    public String phone;
    public String pName;
    public double balance;
    public double consumption;


    // constructors
    public Member(int m, String firstName, String lastName, String phone, String pName, double balance, double consumption) {

        // set to given values
        this.m = m;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.pName = pName;
        this.balance = balance;
        this.consumption = consumption;

    }// end constructor

    public Member() {

        // set to default values
        this.m = 0;
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.pName = null;
        this.balance = 0.0;
        this.consumption = 0.0;

    }// end empty constructor


    // toString
    @Override
    public String toString() {

        // return the member as a string
        return "Member:" + m + ", firstName = " + firstName + ", lastName = " + lastName + ", phone = " + phone + ", pName = " + pName + ", balance = $" + balance + ", consumption = $" + consumption + ".";

    }// end toString

}// end Member class
