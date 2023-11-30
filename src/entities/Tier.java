package entities;

public class Tier {
    
    // fields
    public String tier;
    public double minAmount;
    public double discount;


    // constructors
    public Tier(String tier, double minAmount, double discount) {

        // set to given values
        this.tier = tier;
        this.minAmount = minAmount;
        this.discount = discount;

    }// end constructor

    public Tier() {

        // set to default values
        this.tier = new String();
        this.minAmount = 0.0;
        this.discount = 0.0;

    }// end empty constructor


    // toString
    @Override
    public String toString() {

        // return the tier as a string
        return "Tier:" + tier + ", minAmount = " + minAmount + ", discount = " + discount + ".";

    }// end toString



}// end Tier class
