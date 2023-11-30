package entities;

public class Equipment {
    
    // fields
    public int e;
    public String eType;
    public Integer m;
    

    // constructors
    public Equipment(int e, String eType, Integer m) {
        
        // set to given values
        this.e = e;
        this.eType = eType;
        this.m = m;

    }// end constructor

    public Equipment() {
        
        // set to default values
        this.e = 0;
        this.eType = null;
        this.m = null;

    }// end empty constructor


    // toString
    @Override
    public String toString() {
        
        // return the equipment as a string
        return "Equipment:" + e + ", eType = " + eType + ", m# = " + m + ".";

    }// end toString

}// end Equipment class
