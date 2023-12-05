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
			
					// Strings to use for user input
			
			String firstName = null,
					lastName = null,
					 phoneNo = null,
					   pName = null;
			
					// Series of Validation checks
			
			boolean phCheck = false, // phone number is 10 digit integer
					pnCheck = false, // package actually exists
					cfCheck = false; // courses have space for enrollment
			
					// Loop until all input is approved
			
			while (true) {			
				System.out.print("\nEnter First Name: ");
				firstName = sc.nextLine().strip();
				System.out.print("Enter Last Name: ");
				lastName = sc.nextLine().strip();
				System.out.print("Enter 10-digit Phone Number: ");
				phoneNo = sc.nextLine().strip();
				phCheck = Validation.validatePhone(phoneNo);
				System.out.println("\nChoose Package from the following:");
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
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Add the Member to the Database
			
			int mno = DataManipulation.insertMember(dbconn, firstName, lastName, phoneNo, pName);			
			System.out.println("Added Member");
			System.out.println("M# is " + mno);
		}
		else if(userInput.equals("2")) {
			
			String mno = null;			
			boolean miCheck = false;
					
					// Loop until all input is approved
			
			while(true) {
				System.out.println("\nChoose Member from the following:");
				QueryManager.showAllMembers(dbconn);
				System.out.print("\nEnter the M# to delete: ");
				mno = sc.nextLine().strip();
				miCheck = Validation.validateInt(mno); // m# is an integer
				if(miCheck && QueryManager.getMember(dbconn, mno) != null) {
					break;
				}
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
					
					// Verify that balance is non-negative
			
			boolean mbCheck = Validation.memberbalanceGood(QueryManager.getMember(dbconn, mno));
			if(!mbCheck) {
				System.out.println("\nBalance is not paid off, cannot delete");
				return true;
			}
			int m = Integer.parseInt(mno);
			// LinkedList<Equipment> allEquipment = QueryManager.getEquipmentList(dbconn, "", true);
			// LinkedList<Equipment> lostEquipment = Validation.equipmentCheck(allEquipment, m);
			// Mark Equipment as Lost as needed
			DataManipulation.deleteMember(dbconn, m);			
			System.out.println("M# " + mno + " is deleted");
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

					// Strings to use for user input
	
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

					// Series of Validation checks
						
			boolean cpCheck = false,
					sdCheck = false,
					stCheck = false,
					edCheck = false,
					etCheck = false,
					dwCheck = false,
					tnCheck = false,
					cfCheck = false;
			
					// Loop until all input is approved
			
			while (true) {
				System.out.print("\nEnter Course Name: ");
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
				System.out.println("\nChoose a trainer for the course");
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
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Add the Course to the Database
			
			int c = Integer.parseInt(capacity);
			int t = Integer.parseInt(tNo);
			
			DataManipulation.insertCourse(dbconn, cName, t, c, start, end, day);
			
			System.out.println("Added Course " + cName);
		}
		else if(userInput.equals("2")) {
	
			String cName = null;
			Course c = null;	

					// Loop until all input is approved
			
			while(true) {
				System.out.println("\nChoose courses from the following");
				QueryManager.showAllCourses(dbconn);
				System.out.print("\nChoose course to delete: ");
				cName = sc.nextLine().strip();
				c = QueryManager.getCourse(dbconn, cName);
				if(c != null) {
					break;
				}
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
			// If course cannot be deleted, notify the members
			if(!Validation.canDeleteCourse(c)) {
				System.out.println("\nThe following members are enrolled in this course");
				QueryManager.showMembersEnrolled(dbconn, cName);
				System.out.print("\nNotify them to proceed with course deletion? (y/n): ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("y")) {
					System.out.println("Course not deleted");
					return true;
				}
			}
			
			// Obtain a LinkedList of all packages having those courses
			// Update them to not have it anymore
			DataManipulation.deleteCourse(dbconn, cName);
			System.out.println("Course " + cName + " is deleted");
		}
		else {
			return false;
		}		
		return true;
	}
	
	private static LinkedList<String> packageHelper(
			Scanner sc, Connection dbconn){
		LinkedList<String> retval = null;
		
				// Series of Validation checks
			
		boolean c1Check = false,
				c2Check = false,
				prCheck = false,
				cfCheck = false;
		
		System.out.println("\nChoose courses from the following");
		QueryManager.showAllCourses(dbconn);			
		System.out.print("\nChoose Course 1: ");
		String c1 = sc.nextLine().strip();
		System.out.print("Choose Course 2: ");
		String c2 = sc.nextLine().strip();
		System.out.print("Enter a price for the package: ");
		String price = sc.nextLine().strip();
		prCheck = Validation.validateFloat(price);
		
		Course course1 = null, course2 = null;
		
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
			retval = new LinkedList<String>();
			retval.add(0, c1);
			retval.add(1, c2);
			retval.add(2, price);
		}
			
		return retval;
	}
	
	private static boolean handlePackage(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Package");
		System.out.println("\t2. Update a Package");
		System.out.println("\t3. Delete a Package");
		System.out.print("\nPlease enter your choice (1/2/3)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();
		if(userInput.equals("1")) {

			String pName = null;			
			LinkedList<String> info = null;
			
					// Loop until all input is approved
			
			while(true) {
				System.out.print("\nEnter Package Name: ");
				pName = sc.nextLine().strip();
				if(QueryManager.getPackage(dbconn, pName) != null) {
					System.out.println("Package already exists");
					continue;
				}
				info = packageHelper(sc, dbconn);
				if(info != null) {
					break;
				}				
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Get information from the helper
			
			String c1 = info.get(0),
				   c2 = info.get(1);
			float pcost = Float.parseFloat(info.get(2));
					
					// Obtain the two courses for the package
			
			Course course1 = QueryManager.getCourse(dbconn, c1),
				   course2 = QueryManager.getCourse(dbconn, c2);
			
					// Add the Package to the Database
			
			LinkedList<String> dates = Validation.courseToPackageDates(course1, course2);
			String startDate = dates.get(0);
			String endDate = dates.get(1);
			
			DataManipulation.insertPackage(dbconn, pName, c1, c2, pcost, startDate, endDate);			
			System.out.println("Added Package " + pName);
		}
		else if(userInput.equals("2")) {
			
			String pName = null;
			Package p = null;
			LinkedList<String> info = null;

					// Loop until all input is approved
			
			while(true) {
				System.out.println("\nList of all Packages:");
				QueryManager.showAllPackages(dbconn);
				System.out.print("\nWhich package would you like to update? ");
				pName = sc.nextLine().strip();
				p = QueryManager.getPackage(dbconn, pName);
				if(p != null) {
					break;
				}
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Check if package can be updated
			
			Course curr1 = QueryManager.getCourse(dbconn, p.c1),
				   curr2 = QueryManager.getCourse(dbconn, p.c2);
			if(!Validation.canDeletePackage(p, curr1, curr2)) {
				System.out.println("\n***Cannot update this package***"
						+ "There are members currently enrolled in it");
				return true;
			}
			
					// Loop to obtain new details
			
			while(true) {
				info = packageHelper(sc, dbconn);
				if(info != null) {
					break;
				}				
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Get information from the helper
			
			String c1 = info.get(0),
				   c2 = info.get(1);
			float pcost = Float.parseFloat(info.get(2));
					
					// Obtain the two courses for the package
			
			Course course1 = QueryManager.getCourse(dbconn, c1),
				   course2 = QueryManager.getCourse(dbconn, c2);
			
					// Add the Package to the Database
			
			LinkedList<String> dates = Validation.courseToPackageDates(course1, course2);
			String startDate = dates.get(0);
			String endDate = dates.get(1);
			
			// Update the package
			DataManipulation.updatePackage(dbconn, pName, c1, c2, pcost, startDate, endDate);
			System.out.println("Updated Package " + pName);
		}
		else if(userInput.equals("3")) {
			
			String pName = null;
			Package p = null;	

					// Loop until all input is approved
			
			while(true) {
				System.out.println("\nChoose Package from the following:");
				QueryManager.showAllPackages(dbconn);
				System.out.print("\nEnter the PName to delete: ");
				pName = sc.nextLine().strip();
				p = QueryManager.getPackage(dbconn, pName);
				if(p != null) {
					break;
				}
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			Course c1 = QueryManager.getCourse(dbconn, p.c1),
				   c2 = QueryManager.getCourse(dbconn, p.c2);
			if(Validation.canDeletePackage(p, c1, c2)) {
				DataManipulation.deletePackage(dbconn, pName);
				System.out.println("Package " + pName + " is deleted");
			}
			else {
				System.out.println("Unable to delete active package");
			}
			
		}
		else {
			return false;
		}		
		return true;
	}
	
	private static boolean handlePayment(
			Scanner sc, String userInput, Connection dbconn) {

				// Strings to use for user input

		String    mno = null,
			   amount = null;
		
				// Series of Validation checks
		
		boolean miCheck = false,
				pfCheck = false,
				mbCheck = false;
		
				// Loop until all input is approved
		
		while (true) {
			System.out.println("\nChoose Member from the following:");
			QueryManager.showAllMembers(dbconn);			
			System.out.print("\nEnter M#: ");
			mno = sc.nextLine().strip();
			miCheck = Validation.validateInt(mno);
			System.out.print("Enter Amount: ");
			amount = sc.nextLine().strip();
			pfCheck = Validation.validateFloat(amount);
			if(miCheck && QueryManager.getMember(dbconn, mno) != null) {
				mbCheck = true;
			}
			if(miCheck && pfCheck && mbCheck) {
				break;
			}
			System.out.print("\n***Invalid Data Provided***\n"
					+ "Press 'c' to retry, other keys to exit: ");
			userInput = sc.nextLine().strip();
			if(!userInput.toLowerCase().equals("c")) {
				return true;
			}
		}
		
				// Make the payment
		
		float pay = Float.parseFloat(amount);
		int mid = Integer.parseInt(mno);
		int xno = DataManipulation.makePayment(dbconn, pay, mid);
		System.out.println("\nTransaction successful");
		QueryManager.printTransactionDetails(dbconn, xno);
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

					// Strings to use for user input

			String   mno = null,
				   eType = null,
			         qty = null;
			
					// Series of Validation checks
		
			boolean miCheck = false,
					mbCheck = false,
					etCheck = false,
					qtCheck = false,
					avCheck = false;
			
					// Loop until all input is approved
			
			while (true) {
				System.out.println("\nChoose Member from the following:");
				QueryManager.showAllMembers(dbconn);
				System.out.print("\nEnter M#: ");
				mno = sc.nextLine().strip();
				miCheck = Validation.validateInt(mno);
				if(miCheck && QueryManager.getMember(dbconn, mno) != null) {
					mbCheck = true;
				}
				System.out.println("\nChoose Equipment from the following:");
				QueryManager.showAllEquipment(dbconn);
				System.out.print("\nChoose Equipment Type: ");
				eType = sc.nextLine().strip();
				etCheck = QueryManager.checkEquipmentType(dbconn, eType);
				System.out.print("Enter the Quantity you would like to borrow: ");
				qty = sc.nextLine().strip();
				qtCheck = Validation.validateInt(qty);
				if(etCheck) {
					LinkedList<Equipment> list = QueryManager.getEquipmentList(dbconn, eType, false);
					avCheck = Validation.canBorrow(list, qty);
				}
				// validate that enough quantity available
				if(miCheck && mbCheck && etCheck && qtCheck && avCheck) {
					break;
				}
				else if(!avCheck) {
					System.out.println("\nNot enough Equipment available");
					return true;
				}
				else {
					System.out.print("\n***Invalid Data Provided***\n"
							+ "Press 'c' to retry, other keys to exit: ");
					userInput = sc.nextLine().strip();
					if(!userInput.toLowerCase().equals("c")) {
						return true;
					}
				}
			}
			
					// Borrow the Equipment
			
			int m = Integer.parseInt(mno);
			int q = Integer.parseInt(qty);
			int xno = DataManipulation.borrowEquipment(dbconn, m, q, eType);
			System.out.println("\nEquipment borrowed");
			QueryManager.printTransactionDetails(dbconn, xno);			
		}
		else if(userInput.equals("2")) {
			
			String mno = null;
			
					// Loop until all input is approved
			
			while (true) {
				System.out.println("\nChoose Member from the following:");
				QueryManager.showAllMembers(dbconn);
				System.out.print("\nEnter M#: ");
				mno = sc.nextLine().strip();
				if(Validation.validateInt(mno) && QueryManager.getMember(dbconn, mno) != null) {
					break;
				}
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
			// return all equipment associated with them
			int m = Integer.parseInt(mno);
			LinkedList<Integer> xnos = DataManipulation.returnEquipment(dbconn, m);
			System.out.println("\nEquipment returned");
			System.out.println("\nDetails:");
			for(int x : xnos) {
				System.out.println();
				QueryManager.printTransactionDetails(dbconn, x);
			}
			
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
		
		System.out.print("\nPlease enter your choice (1/2/3/4)"
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
        		executeFlag = handlePayment(sc, userInput, dbconn);
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
