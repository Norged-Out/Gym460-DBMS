package entities;

public class Trainer {
    
    // fields
    public int t;
    public String firstName;
    public String lastName;
    public String phone;
    

    // constructors
    public Trainer(int t, String firstName, String lastName, String phone) {
        
        // set to given values
        this.t = t;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;

    }// end constructor

    public Trainer() {
        
        // set to default values
        this.t = 0;
        this.firstName = null;
        this.lastName = null;
        this.phone = null;

    }// end empty constructor


    // toString
    @Override
    public String toString() {
        
        // return the trainer as a string
        return "Trainer:" + t + ", firstName = " + firstName + ", lastName = " + lastName + ", phone = " + phone + ".";

    }// end toString

}// end Trainer class
