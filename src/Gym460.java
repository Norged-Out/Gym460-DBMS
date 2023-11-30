/* 
 *  Author: Priyansh Nayak
 *  Course: CSC 460
 * Purpose: This Program generates the tables for the
 * 			Final Project on Priyansh's Oracle Database.
 * 
 * scp GymManagement.java priyanshnayak@lectura.cs.arizona.edu:~/csc/460/p4
 *
 * */

import java.util.*;
import java.sql.*;

public class Gym460 {
	
	private static boolean handleMember(Scanner sc, String userInput) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Member");
		System.out.println("\t2. Delete a Member");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine();
		switch (userInput) {
		case "1":
			String firstName = null,
				    lastName = null,
				     phoneNo = null,
				       pName = null;
			
			System.out.print("Enter First Name: ");
			firstName = sc.nextLine();
			System.out.print("Enter Last Name: ");
			lastName = sc.nextLine();
			System.out.print("Enter 10-digit Phone Number: ");
			phoneNo = sc.nextLine();
			
			// TODO: print all packages
			
			System.out.print("Enter Package name to enroll into: ");
			pName = sc.nextLine();
			
			System.out.println("Added Member");
			System.out.println("Member id is <insert M#>");
			break;
		case "2":
			System.out.print("Enter the M# to delete: ");
			userInput = sc.nextLine();
			int mno = Integer.parseInt(userInput);
			System.out.println("M# " + mno + " is deleted");
			break;
		default:
			return false;
		}		
		return true;
	}
	
	private static boolean handleCourse(Scanner sc, String userInput) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Course");
		System.out.println("\t2. Delete a Course");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine();
		switch (userInput) {
		case "1":
			String     cName = null,
				         tno = null,
				    capacity = null,
				   startDate = null,
				     endDate = null,
				   startTime = null,
					 endTime = null,
				    	 day = null;
			
			System.out.print("Enter Course Name: ");
			cName = sc.nextLine();
			System.out.print("Enter Capacity: ");
			capacity = sc.nextLine();
			System.out.print("Enter Start Date (YYYY-MM-DD): ");
			startDate = sc.nextLine();
			System.out.print("Enter End Date (YYYY-MM-DD): ");
			endDate = sc.nextLine();
			System.out.print("Enter Start Time (HH24:MI): ");
			startTime = sc.nextLine();
			System.out.print("Enter End Time (HH24:MI): ");
			endTime = sc.nextLine();
			System.out.print("Enter the Day the course will be taught: ");
			day = sc.nextLine();
			
			// TODO: print all trainers
			
			System.out.print("Choose a trainer for the course: ");
			tno = sc.nextLine();
			
			System.out.println("Added Course " + cName);
			break;
		case "2":
			System.out.print("Enter the Course to delete: ");
			userInput = sc.nextLine();
			System.out.println("Course " + userInput + " is deleted");
			break;
		default:
			return false;
		}		
		return true;
	}
	
	private static boolean handlePackage(Scanner sc, String userInput) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Package");
		System.out.println("\t2. Update a Package");
		System.out.println("\t2. Delete a Package");
		System.out.print("\nPlease enter your choice (1/2/3)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine();
		switch (userInput) {
		case "1":
			String  pName = null,
					   c1 = null,
					   c2 = null,
					price = null;
			System.out.print("Enter Package Name: ");
			pName = sc.nextLine();

			
			// TODO: print all courses
			
			System.out.print("Choose Course 1: ");
			c1 = sc.nextLine();
			System.out.print("Choose Course 2: ");
			c2 = sc.nextLine();
			System.out.print("Enter a price for the package: ");
			price = sc.nextLine();
			
			System.out.println("Added Package " + pName);
			break;
		case "2":
			System.out.print("Enter the Package Name: ");
			userInput = sc.nextLine();
			
			System.out.println("idek");
			break;
		case "3":
			System.out.print("Enter the Package to delete: ");
			userInput = sc.nextLine();
			int mno = Integer.parseInt(userInput);
			System.out.println("M# " + mno + " is deleted");
			
			break;
		default:
			return false;
		}		
		return true;
	}
	
	private static boolean handleQueries(Scanner sc, String userInput) {
		System.out.println("TODO QUERY SYSTEM");
		
		return true;
	}

	public static void main(String[] args) {
		final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = "priyanshnayak",	// Oracle DBMS username
		       password = "a9379";    		// Oracle DBMS password
		
		Scanner sc = new Scanner(System.in);
		String userInput = null;
		
		// load the (Oracle) JDBC driver by initializing its base
        // class, 'oracle.jdbc.OracleDriver'.
/*
        try {

                Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }
 */       
        System.out.println("\n\tWELCOME TO THE GYM 460 MANAGEMENT SYSTEM");
    	System.out.println("------------------------"
    			+ "--------------------------------");
    	boolean executeFlag = true;
    	
    	while(executeFlag) {

				// Print out the menu
    		
    		System.out.println("\nWhat would you like to do today?"
    				+ "\n\nHere are your options:");
    		System.out.println("\t1. Add or Delete a Member");
    		System.out.println("\t2. Add or Delete a Course");
    		System.out.println("\t3. Add, Update, or Delete a Package");
    		System.out.println("\t4. Investigate some general queries");
    		System.out.print("\nPlease enter your choice (1/2/3/4)"
    				+ "\nEnter any other key to quit: ");
        	userInput = sc.nextLine();
        	switch (userInput) {
        	case "1":
        		executeFlag = handleMember(sc, userInput);
        		break;
        	case "2":
        		executeFlag = handleCourse(sc, userInput);
        		break;
        	case "3":
        		executeFlag = handlePackage(sc, userInput);
        		break;
        	case "4":
        		executeFlag = handleQueries(sc, userInput);
        		break;
        	default:
        		executeFlag = false;
        	}
        	
        			// Ask the user if they want to continue
        	
        	if(executeFlag) {        	
	        	System.out.print("\nContinue using the system? (y/n) ");
	        	userInput = sc.nextLine();
	        	if(!userInput.equals("y")) {
	        		executeFlag = false;
	        	}
	        	else {
	            	System.out.println("\n-----------------------"
	            			+ "---------------------------------");
	        	}
        	}
        	
        	if(!executeFlag) {
        		System.out.println("\nThank you for using the management"
        				+ " system.\nTerminating the Program.");
        		sc.close();
        	}
        	
    	}

	}

}
