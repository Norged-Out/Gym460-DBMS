import java.io.*;
import java.sql.*;
/* 
 *  Author: Priyansh Nayak
 *  Course: CSC 460
 * Purpose: This Program generates the tables for the
 * 			Final Project on Priyansh's Oracle Database.
 *
 * */
import java.util.Scanner;

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
                + ""
                + ")",
                
                "create table Course ("
                + ""
                + ")",
                
                "create table Package ("
                + "" 
                + ")",
                
                "create table Member ("
                + "" 
                + ")",
                
                "create table Equipment ("
                + ""
                + ")",
                
                "create table Transaction ("
                + "X# integer not null,"
                + "M# integer not null,"
                + "XDate date not null,"
                + "Amount float not null,"
                + "XType varchar2(10) not null,"
                + "PRIMARY KEY (X#),"
                + "FOREIGN KEY (M#) REFERENCES Member"
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
        		+ "4. Member\n5. Equipment\n6. Transaction\n7. ALL");
        System.out.println("\nNOTE:  ONLY CREATE A TABLE "
        		+ "IF ITS PRECEEDING TABLE EXISTS BECAUSE OF FKs\n");
        System.out.print("Choose a number corresponding to a table: ");
        String userInput = sc.nextLine();
        int choice = Integer.valueOf(userInput);
        String query = null;
        boolean allFlag = false;
        
        		// Choose a table to generate
        
        switch (choice) {
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
        	allFlag = true;
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
