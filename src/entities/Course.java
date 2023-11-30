package entities;

import java.sql.Date;

public class Course {
    
    // fields
    public String cName;
    public int t;
    public int enrollCount;
    public int capacity;
    public Date startDate;
    public Date endDate;
    public String day;


    // constructors
    public Course(String cName, int t, int enrollCount, int capacity, Date startDate, Date endDate, String day) {

        // set to given values
        this.cName = cName;
        this.t = t;
        this.enrollCount = enrollCount;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.day = day;

    }// end constructor

    public Course() {

        // set to default values
        this.cName = null;
        this.t = 0;
        this.enrollCount = 0;
        this.capacity = 0;
        this.startDate = null;
        this.endDate = null;
        this.day = null;

    }// end empty constructor


    // toString
    @Override
    public String toString() {

        // return the course as a string
        return "Course:" + cName + ", t# = " + t + ", enrollCount = " + enrollCount + ", capacity = " + capacity + ", startDate = " + startDate.toString() + ", endDate = " + endDate.toString() + ", day = " + day + ".";

    }// end toString

}// end Course class
