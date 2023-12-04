/* 
 *  Author: Priyansh Nayak
 *  Course: CSC 460
 * Purpose: This Program generates the tables for the
 * 			Final Project on Priyansh's Oracle Database.
 * 
 * scp TableGen.java priyanshnayak@lectura.cs.arizona.edu:~/csc/460/p4
 *
 * */

import java.util.*;
import java.sql.*;

public class TableGen {

	public static void main(String[] args) {
		final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = "priyanshnayak",	// Oracle DBMS username
		       password = "a9379";    		// Oracle DBMS password
		
		Scanner sc = new Scanner(System.in);
		
		// load the (Oracle) JDBC driver by initializing its base
        // class, 'oracle.jdbc.OracleDriver'.

        try {

                Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }
        
        String[] tables = {
                "create table Trainer ("
                + "T# integer,"
                + "FirstName varchar2(20) not null,"
                + "LastName varchar2(20) not null,"
                + "Phone# varchar2(10) not null,"
                + "PRIMARY KEY (T#)"
                + ")",
                
                "create table Course ("
                + "CName varchar2(15),"
                + "T# integer not null,"
                + "EnrollCount integer not null,"
                + "Capacity integer not null,"
                + "StartDate date not null,"
                + "EndDate date not null,"
                + "Day varchar2(3) not null,"
                + "PRIMARY KEY (CName),"
                + "FOREIGN KEY (T#) REFERENCES Trainer,"
                + "CHECK (EndDate > StartDate)"
                + ")",
                
                "create table Package ("
                + "PName varchar2(30),"
                + "C1 varchar2(15),"
                + "C2 varchar2(15),"
                + "StartDate date,"
                + "EndDate date,"
                + "Price float not null,"
                + "PRIMARY KEY (PName),"
                + "FOREIGN KEY (C1) REFERENCES Course,"
                + "FOREIGN KEY (C1) REFERENCES Course,"
                + "CHECK (EndDate > StartDate)" 
                + ")",
                
                "create table Tier ("
                + "Tier varchar2(10),"
                + "MinAmount float not null,"
                + "Discount float not null,"
                + "PRIMARY KEY (Tier)"
                + ")",
                
                "create table Member ("
                + "M# integer,"
                + "FirstName varchar2(20) not null,"
                + "LastName varchar2(20) not null,"
                + "Phone# varchar2(10) not null,"
                + "PName varchar2(20) not null,"
                + "Balance float not null,"
                + "Consumption float not null,"
                + "Tier varchar2(10),"
                + "PRIMARY KEY (M#),"
                + "FOREIGN KEY (PName) REFERENCES Package,"
                + "FOREIGN KEY (Tier) REFERENCES Tier" 
                + ")",
                
                "create table Equipment ("
                + "E# integer,"
                + "EType varchar2(20),"
                + "M# integer,"
                + "PRIMARY KEY (E#, EType),"
                + "FOREIGN KEY (M#) REFERENCES Member"
                + ")",
                
                "create table Transaction ("
                + "X# integer,"
                + "M# integer not null,"
                + "XDate date not null,"
                + "Amount float not null,"
                + "XType varchar2(10) not null,"
                + "EType varchar2(20),"
                + "PRIMARY KEY (X#)"
                + ")"
                
        };
        
        Connection dbconn = null;
        try {
        	dbconn = DriverManager.getConnection
                    (oracleURL,username,password);
        } catch (SQLException e) {

        System.err.println("*** SQLException:  "
            + "Could not open JDBC connection.");
        System.err.println("\tMessage:   " + e.getMessage());
        System.err.println("\tSQLState:  " + e.getSQLState());
        System.err.println("\tErrorCode: " + e.getErrorCode());
        System.exit(-1);

        }
        
        System.out.println("LAUNCHING TABLE GENERATION PROCESS\n");
        System.out.println("Pick from the following options:");
        System.out.println("1. Trainer\n2. Course\n3. Package\n"
        		+ "4. Tier\n5. Member\n6. Equipment\n7. Transaction");
        System.out.println("Press 0 to generate all tables.");
        System.out.println("\nNOTE:  ONLY CREATE A TABLE "
        		+ "IF ITS PRECEEDING TABLE EXISTS BECAUSE OF FKs\n");
        System.out.print("Choose a number corresponding to a table: ");
        String userInput = sc.nextLine();
        int choice = Integer.valueOf(userInput);
        String query = null;
        boolean allFlag = false;
        
        		// Choose a table to generate
        
        switch (choice) {
        case 0:
        	allFlag = true;
        	break;
        case 1:
        	query = tables[0];
        	break;
        case 2:
        	query = tables[1];
        	break;
        case 3:
        	query = tables[2];
        	break;
        case 4:
        	query = tables[3];
        	break;
        case 5:
        	query = tables[4];
        	break;
        case 6:
        	query = tables[5];
        	break;
        case 7:
        	query = tables[6];
        	break;
        default:
        	System.out.println("Invalid choice, terminating.");
        	sc.close();
        	System.exit(-1);
        }
        
        		// Execute the table creation query
        
        Statement stmt = null;
        try {
        	stmt = dbconn.createStatement();
        	if (allFlag) {
        		for (String q: tables) {
        			stmt.executeUpdate(q);
        		}
        	}
        	else {
        		stmt.executeUpdate(query);
        	}
        	stmt.close();
        	dbconn.close();
        	 
        } catch (SQLException e) {
        	System.out.println("Query that crashed => " + query);
            System.err.println("*** SQLException:  "
                + "Could not fetch query results.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);

        }       
        sc.close();
    	System.out.println("Table(s) created.");

	}

}
