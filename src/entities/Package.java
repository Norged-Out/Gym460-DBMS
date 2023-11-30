package entities;

import java.sql.Date;

public class Package {
    
    // fields
    public String pName;
    public String c1;
    public String c2;
    public Date startDate;
    public Date endDate;
    public double price;


    // constructors
    public Package(String pName, String c1, String c2, Date startDate, Date endDate, double price) {

        // set to given values
        this.pName = pName;
        this.c1 = c1;
        this.c2 = c2;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;

    }// end constructor

    public Package() {

        // set to default values
        this.pName = null;
        this.c1 = null;
        this.c2 = null;
        this.startDate = null;
        this.endDate = null;
        this.price = 0.0;

    }// end empty constructor


    // toString
    @Override
    public String toString() {

        // return the package as a string
        return "Package:" + pName + ", c1 = " + c1 + ", c2 = " + c2 + ", startDate = " + startDate.toString() + ", endDate = " + endDate.toString() + ", price = $" + price + ".";

    }// end toString

}// end Package class
