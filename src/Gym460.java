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
import entities.Equipment;
import entities.Member;
import entities.Trainer;
import entities.Package;

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
			
			boolean phCheck = false,
					pnCheck = false,
					cfCheck = false;
			
			while (true) {			
				System.out.print("Enter First Name: ");
				firstName = sc.nextLine().strip();
				System.out.print("Enter Last Name: ");
				lastName = sc.nextLine().strip();
				System.out.print("Enter 10-digit Phone Number: ");
				phoneNo = sc.nextLine().strip();
				phCheck = Validation.validatePhone(phoneNo);
				System.out.println("Choose Package from the following:");
				QueryManager.showAllPackages(dbconn);			
				System.out.print("\nEnter Package Name to enroll into: ");
				pName = sc.nextLine().strip();
				Package p = QueryManager.getPackage(dbconn, pName);
				if(p != null) {
					pnCheck = true;
					Course c1 = QueryManager.getCourse(dbconn, p.c1);
					Course c2 = QueryManager.getCourse(dbconn, p.c2);
					cfCheck = !Validation.isCourseFull(c1) && !Validation.isCourseFull(c2);
				}
				if(phCheck && pnCheck && cfCheck) {
					break;
				}
				System.out.println("\n Invalid Data, Try Again\n");
			}			
			int mno = DataManipulation.insertMember(dbconn, firstName, lastName, phoneNo, pName);			
			System.out.println("Added Member");
			System.out.println("Member id is " + mno);
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
				     	 day = null,
				       start = null,
				         end = null;
						
			boolean cpCheck = false,
					sdCheck = false,
					stCheck = false,
					edCheck = false,
					etCheck = false,
					dwCheck = false,
					tnCheck = false,
					cfCheck = false;
			
			while (true) {
				System.out.print("Enter Course Name: ");
				cName = sc.nextLine().strip();
				if(QueryManager.getCourse(dbconn, cName) != null) {
					System.out.println("Course already exists");
					continue;
				}				
				System.out.print("Enter Capacity: ");
				capacity = sc.nextLine().strip();
				cpCheck = Validation.validateInt(capacity);
				System.out.print("Enter Start Date (YYYY-MM-DD): ");
				startDate = sc.nextLine().strip();
				sdCheck = Validation.validateDate(startDate);
				System.out.print("Enter End Date (YYYY-MM-DD): ");
				endDate = sc.nextLine().strip();
				edCheck = Validation.validateDate(endDate);
				System.out.print("Enter Start Time (HH24:MI): ");
				startTime = sc.nextLine().strip();
				stCheck = Validation.validateTime(startTime);
				System.out.print("Enter End Time (HH24:MI): ");
				endTime = sc.nextLine().strip();
				etCheck = Validation.validateTime(endTime);
				System.out.println("Enter the Day the course will be taught");
				System.out.print("Choose from (MON|TUE|WED|THU|FRI|SAT|SUN): ");
				day = sc.nextLine().strip();
				dwCheck = Validation.validateDay(day);
				day = day.toUpperCase();
				start = Validation.concatDateAndTime(startDate, startTime);
				end = Validation.concatDateAndTime(endDate, endTime);
				System.out.println("Choose a trainer for the course");
				QueryManager.showAllTrainers(dbconn);
				System.out.print("\nEnter T#: ");
				tNo = sc.nextLine().strip();
				if(Validation.validateInt(tNo)) {
					if (QueryManager.getTrainer(dbconn, tNo) != null) {
						tnCheck = true;
						LinkedList<Course> courses = QueryManager.getCoursesByTrainer(dbconn, tNo);
						cfCheck = Validation.noTrainerScheduleConflict(courses, day, start, end);
					}
				}
				
				if(cpCheck && sdCheck && edCheck && stCheck && etCheck && dwCheck && tnCheck && cfCheck) {
					break;
				}
				System.out.println("\n Invalid Data, Try Again\n");
			}
			
			int c = Integer.parseInt(capacity);
			int t = Integer.parseInt(tNo);
			
			DataManipulation.insertCourse(dbconn, cName, t, c, start, end, day);
			
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
			
			boolean c1Check = false,
					c2Check = false,
					prCheck = false,
					cfCheck = false;
			
			Course course1 = null,
				   course2 = null;
			
			while(true) {
				System.out.print("Enter Package Name: ");
				pName = sc.nextLine().strip();
				if(QueryManager.getPackage(dbconn, pName) != null) {
					System.out.println("Package already exists");
					continue;
				}	
				System.out.println("Choose courses from the following");
				QueryManager.showAllCourses(dbconn);			
				System.out.print("\nChoose Course 1: ");
				c1 = sc.nextLine().strip();
				System.out.print("Choose Course 2: ");
				c2 = sc.nextLine().strip();
				System.out.print("Enter a price for the package: ");
				price = sc.nextLine().strip();
				prCheck = Validation.validateFloat(price);
				
				if (c1.equals("")) {
					c1 = null;
					c1Check = true;
				}
				else {
					course1 = QueryManager.getCourse(dbconn, c1);
					if (course1 != null) {
						c1Check = true;
					}
				}
				
				if (c2.equals("")) {
					c2 = null;
					c2Check = true;
				}
				else {
					course2 = QueryManager.getCourse(dbconn, c2);
					if (course2 != null) {
						c2Check = true;
					}
				}
				cfCheck = Validation.noCourseScheduleConflict(course1, course2);
				
				if(c1Check && c2Check && prCheck && cfCheck) {
					break;
				}
				System.out.println("\n Invalid Data, Try Again\n");
			}
			LinkedList<String> dates = Validation.courseToPackageDates(course1, course2);
			String startDate = dates.get(0);
			String endDate = dates.get(1);
			float pcost = Float.parseFloat(price);
			
			DataManipulation.insertPackage(dbconn, pName, c1, c2, pcost, startDate, endDate);
			
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
	
	private static boolean handlePayment(
			Scanner sc, Connection dbconn) {
		String    mno = null,
			   amount = null;
		
		boolean miCheck = false,
				pfCheck = false,
				mbCheck = false;
		
		while (true) {
			System.out.println("Choose Member from the following:");
			QueryManager.showAllMembers(dbconn);			
			System.out.print("\nEnter Member ID: ");
			mno = sc.nextLine().strip();
			miCheck = Validation.validateInt(mno);
			System.out.print("Enter Amount: ");
			amount = sc.nextLine().strip();
			pfCheck = Validation.validateFloat(amount);
			Member m = QueryManager.getMember(dbconn, mno);
			if(m != null) {
				mbCheck = true;
			}
			if(miCheck && pfCheck && mbCheck) {
				break;
			}
			System.out.println("\n Invalid Data, Try Again\n");
		}
		float pay = Float.parseFloat(amount);
		int mid = Integer.parseInt(mno);
		int xid = DataManipulation.makePayment(dbconn, pay, mid);
		System.out.println("Transaction successful");
		QueryManager.printTransactionDetails(dbconn, xid);
		return true;
	}
	
	private static boolean handleEquipment(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Borrow some Equipment");
		System.out.println("\t2. Return some Equipment");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();
		if(userInput.equals("1")) {
			String      mno = null,
					  eType = null,
			       quantity = null;
		
			boolean miCheck = false,
					mbCheck = false,
					etCheck = false,
					evCheck = false;
			
			while (true) {
				System.out.println("Choose Member from the following:");
				QueryManager.showAllMembers(dbconn);
				System.out.print("\nEnter Member ID: ");
				mno = sc.nextLine().strip();
				miCheck = Validation.validateInt(mno);
				Member m = QueryManager.getMember(dbconn, mno);
				if(m != null) {
					mbCheck = true;
				}
				System.out.println("Choose Equipment from the following:");
				QueryManager.showAllEquipment(dbconn);
				System.out.print("\nChoose Equipment Type: ");
				eType = sc.nextLine().strip();
				//Equipment e = QueryManager.getEquipment(dbconn, eType);
				//if(e != null) {
				//	etCheck = true;
				//}
				System.out.print("Enter Amount: ");
				//amount = sc.nextLine().strip();
				//pfCheck = Validation.validateFloat(amount);
				
				if(miCheck && mbCheck) {
					break;
				}
				System.out.println("\n Invalid Data, Try Again\n");
			}
			
			
		}
		else if(userInput.equals("2")) {
			
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
    		System.out.println("\t4. Make a payment for a Member");
    		System.out.println("\t5. Borrow or Return Equipment");
    		System.out.println("\t6. Investigate some general queries");
    		System.out.print("\nPlease enter your choice (1/2/3/4/5/6)"
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
        		executeFlag = handlePayment(sc, dbconn);
        		break;
        	case "5":
        		executeFlag = handleEquipment(sc, userInput, dbconn);
        		break;
        	case "6":
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
