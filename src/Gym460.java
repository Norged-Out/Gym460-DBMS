/* 
 *  Author: Priyansh Nayak
 *  Course: CSC 460
 * Purpose: This Program generates the tables for the
 * 			Final Project on Priyansh's Oracle Database.
 * 
 * scp -r ./* priyanshnayak@lectura.cs.arizona.edu:~/csc/460/p4
 *
 * */
import java.util.*;

import entities.Course;

import java.sql.*;

public class Gym460 {
	
	private static boolean handleMember(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Member");
		System.out.println("\t2. Delete a Member");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();
		if(userInput.equals("1")) {
			String firstName = null,
					lastName = null,
					 phoneNo = null,
					   pName = null;
			
			System.out.print("Enter First Name: ");
			firstName = sc.nextLine().strip();
			System.out.print("Enter Last Name: ");
			lastName = sc.nextLine().strip();
			System.out.print("Enter 10-digit Phone Number: ");
			phoneNo = sc.nextLine().strip();
			System.out.println("Choose Package from the following:");
			QueryManager.showAllPackages(dbconn);			
			System.out.print("\nEnter Package Name to enroll into: ");
			pName = sc.nextLine().strip();
			
			System.out.println("Added Member");
			System.out.println("Member id is <insert M#>");
		}
		else if(userInput.equals("2")) {
			System.out.print("Enter the M# to delete: ");
			userInput = sc.nextLine().strip();
			int mNo = Integer.parseInt(userInput);
			System.out.println("M# " + mNo + " is deleted");
		}
		else {
			return false;
		}		
		return true;
	}
	
	private static boolean handleCourse(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Course");
		System.out.println("\t2. Delete a Course");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();
		if(userInput.equals("1")) {
			String     cName = null,
						 tNo = null,
					capacity = null,
				   startDate = null,
				     endDate = null,
				   startTime = null,
				     endTime = null,
				     	 day = null;
			
			boolean valid = false;
			
			while (!valid) {
				System.out.print("Enter Course Name: ");
				cName = sc.nextLine().strip();
				System.out.print("Enter Capacity: ");
				capacity = sc.nextLine().strip();
				System.out.print("Enter Start Date (YYYY-MM-DD): ");
				startDate = sc.nextLine().strip();
				System.out.print("Enter End Date (YYYY-MM-DD): ");
				endDate = sc.nextLine().strip();
				System.out.print("Enter Start Time (HH24:MI): ");
				startTime = sc.nextLine().strip();
				System.out.print("Enter End Time (HH24:MI): ");
				endTime = sc.nextLine().strip();
				System.out.print("Enter the Day the course will be taught: ");
				day = sc.nextLine().strip();
				System.out.println("Choose a trainer for the course:");
				QueryManager.showAllTrainers(dbconn);
				System.out.print("\nEnter T#: ");
				tNo = sc.nextLine().strip();
				
			}			
			
			System.out.println("Added Course " + cName);
		}
		else if(userInput.equals("2")) {
			System.out.print("Enter the Course to delete: ");
			userInput = sc.nextLine().strip();
			Course choice = QueryManager.getCourse(dbconn, userInput);
			System.out.println("Course " + userInput + " is deleted");
		}
		else {
			return false;
		}		
		return true;
	}
	
	private static boolean handlePackage(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Package");
		System.out.println("\t2. Update a Package");
		System.out.println("\t2. Delete a Package");
		System.out.print("\nPlease enter your choice (1/2/3)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();
		if(userInput.equals("1")) {
			String pName = null,
					  c1 = null,
					  c2 = null,
				   price = null;
			System.out.print("Enter Package Name: ");
			pName = sc.nextLine().strip();
			System.out.println("Choose courses from the following:");
			QueryManager.showAllCourses(dbconn);			
			System.out.print("\nChoose Course 1: ");
			c1 = sc.nextLine().strip();
			System.out.print("Choose Course 2: ");
			c2 = sc.nextLine().strip();
			System.out.print("Enter a price for the package: ");
			price = sc.nextLine().strip();
			
			System.out.println("Added Package " + pName);
		}
		else if(userInput.equals("2")) {
			System.out.println("List of all Packages:");
			QueryManager.showAllPackages(dbconn);
			String oldPName = null,
				   newPName = null,
				   newC1 = null,
				   newC2 = null,
				   newPrice = null;
			
			System.out.print("\nWhich package would you like to update? ");
			oldPName = sc.nextLine().strip();
			
			// TODO if package name is correct, return an object
			// Obtain new details
		}
		else if(userInput.equals("3")) {
			System.out.print("Enter the Package to delete: ");
			userInput = sc.nextLine().strip();
			int mno = Integer.parseInt(userInput);
			System.out.println("M# " + mno + " is deleted");
		}
		else {
			return false;
		}		
		return true;
	}
	
	private static boolean handleQueries(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nHere are some queries you may use:");
		System.out.println("\n1. List all member's names and phone numbers who"
    			+ "\n   now has a negative balance.");
		System.out.println("\n2. Check and see a member's class schedule for"
    			+ "\n   November.");
		System.out.println("\n3. Check and see a trainer's working hours for"
    			+ "\n   December.");
		System.out.println("\n4. TODO");
		
		System.out.print("\nPlease enter your choice (1/2/3)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();
		switch (userInput) {
		case "1":
			System.out.println("\n-----------------");
			System.out.println("Exectuing Query 1");
			System.out.println("-----------------\n");
			QueryManager.query1(dbconn);
			break;
		case "2":
			System.out.println("\n-----------------");
			System.out.println("Exectuing Query 2");
			System.out.println("-----------------\n");
			System.out.print("Enter the member id: ");
			userInput = sc.nextLine().strip();
			QueryManager.query2(dbconn, userInput);
			break;
		case "3":
			System.out.println("\n-----------------");
			System.out.println("Exectuing Query 3");
			System.out.println("-----------------\n");
			QueryManager.query3(dbconn);
			break;
		case "4":
			System.out.println("\n-----------------");
			System.out.println("Exectuing Query 4");
			System.out.println("-----------------\n");
			break;
		default:
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) {

		final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = "priyanshnayak",	// Oracle DBMS username
		       password = "a9379";    		// Oracle DBMS password
		
				
		
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
		
		        // make and return a database connection to the user's
		        // Oracle database
		
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
	    	    		
		
		Scanner sc = new Scanner(System.in);
		String userInput = null;
   
		System.out.println("----------------------"
			+ "----------------------------------");
        System.out.println("\n\tWELCOME TO THE GYM 460 MANAGEMENT SYSTEM");
    	boolean executeFlag = true;
    	
    	while(executeFlag) {

				// Print out the menu

        	System.out.println("\n-----------------------"
        			+ "---------------------------------");   		
    		System.out.println("\nWhat would you like to do today?"
    				+ "\n\nHere are your options:");
    		System.out.println("\t1. Add or Delete a Member");
    		System.out.println("\t2. Add or Delete a Course");
    		System.out.println("\t3. Add, Update, or Delete a Package");
    		System.out.println("\t4. Investigate some general queries");
    		System.out.print("\nPlease enter your choice (1/2/3/4)"
    				+ "\nEnter any other key to quit: ");
        	userInput = sc.nextLine().strip();
        	switch (userInput) {
        	case "1":
        		executeFlag = handleMember(sc, userInput, dbconn);
        		break;
        	case "2":
        		executeFlag = handleCourse(sc, userInput, dbconn);
        		break;
        	case "3":
        		executeFlag = handlePackage(sc, userInput, dbconn);
        		break;
        	case "4":
        		executeFlag = handleQueries(sc, userInput, dbconn);
        		break;
        	default:
        		executeFlag = false;
        	}
        	
        	System.out.println("\n-----------------------"
        			+ "---------------------------------"); 
        	
        			// Ask the user if they want to continue
        	
        	if(executeFlag) {        	
	        	System.out.print("\nContinue using the system? (y/n) ");
	        	userInput = sc.nextLine().strip();
	        	if(!userInput.equals("y")) {
	        		executeFlag = false;
	        	}
        	}
        	
        	if(!executeFlag) {
        		System.out.println("\nThank you for using the management"
        				+ " system.\nTerminating the Program.");
        		sc.close();
        	}
        	
    	}
    	
    			// Shut down the connection to the DBMS.
		
    	try {
    		dbconn.close();
    	} catch (SQLException e) {
    		System.err.println("*** SQLException:  "
		            + "Could not close connection.");
    		System.err.println("\tMessage:   " + e.getMessage());
    		System.err.println("\tSQLState:  " + e.getSQLState());
    		System.err.println("\tErrorCode: " + e.getErrorCode());
    		System.exit(-1);
    		
    	}
    	
	}

}
