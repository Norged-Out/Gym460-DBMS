package entities;

import java.sql.Date;

public class Transaction {
    
    // fields
    public int x;
    public int m;
    public Date xDate;
    public double amount;
    public String xType;
    public String eType;


    // constructors
    public Transaction(int x, int m, Date xDate, double amount, String xType, String eType) {

        // set to given values
        this.x = x;
        this.m = m;
        this.xDate = xDate;
        this.amount = amount;
        this.xType = xType;
        this.eType = eType;

    }// end constructor

    public Transaction() {

        // set to default values
        this.x = 0;
        this.m = 0;
        this.xDate = null;
        this.amount = 0.0;
        this.xType = null;
        this.eType = null;

    }// end empty constructor


    // toString
    @Override
    public String toString() {

        // return the transaction as a string
        return "Transaction:" + x + ", m# = " + m + ", xDate = " + xDate.toString() + ", amount = $" + amount + ", xType = " + xType + ", eType = " + eType + ".";

    }// end toString

}// end Transaction class
