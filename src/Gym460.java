/*
 * Team:		Nathan Lucero
 * 				Priyansh Nayak
 * 				Utkarsh Upadhyay
 * 				Bhargav Gullipelli
 * 
 * Authors:		Priyansh Nayak
 * 
 * Course:		CSC-460 Fall 2023
 * 
 * Assignment:	Program #4: Creating a JDBC and SQL program that operates as 
 * 					an interface with a Oracle DB for use by the Gym460 staff.
 * 
 * Due Date:	12/05/2023
 * 
 * File Name:	Gym460.java
 * 
 * Instructor:	L. McCann
 * Graduate TA:	Zhenyu Qi
 * Graduate TA:	Danial Bazmandeh
 * 
 * Description: This Program acts as the user interface presented to the 
 * 	staff in charge of handling the Gym460 DBMS. It allows them to manage
 *  the database and make modifications based on validated input, and also
 *  provides an option to query certain specific information regarding the
 *  database. This Program acts as the frontend and communicates with all
 *  other classes to facilitate the handling of the database. It obtains
 *  information from QueryManager, validates and filters user inputs via
 *  Validation, and updates the database through DataManipulation accordingly.
 *  For simplicity, login details for the program are set to the student
 *  Oracle account of priyanshnayak that houses all the tables.
 * 
 */
import java.util.*;
import entities.Course;
import entities.Equipment;
import entities.Package;
import java.sql.*;

public class Gym460 {
	
	/**
	 * This method takes a scanner object, a string to obtain user input,
	 *  and a database connection object to perform requests related to
	 *  adding or deleting a member from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param userInput is the string used for storing user input.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a boolean value indicating continual access to the DBMS.
	 * 	returns true by default for all successful procedures and
	 *  short-circuit returns due to errors, false for invalid choices.
	 */	
	private static boolean handleMember(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Member");
		System.out.println("\t2. Delete a Member");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();  // Used to determine chosen option
		if(userInput.equals("1")) {
			
					// Strings to use for user input
			
			String firstName = null,
					lastName = null,
					 phoneNo = null,
					   pName = null;
			
					// Series of Validation checks
			
			boolean phCheck = false, // phone number is 10 digit number
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
				
						// Ensure that the package selected is valid
						// and the courses have space to enroll
				
				Package p = QueryManager.getPackage(dbconn, pName);
				if(p != null) {
					pnCheck = true;
					Course c1 = QueryManager.getCourse(dbconn, p.c1);
					Course c2 = QueryManager.getCourse(dbconn, p.c2);
					cfCheck = !Validation.isCourseFull(c1) && !Validation.isCourseFull(c2);
				}
				
						// Confirm all Validations
				
				if(phCheck && pnCheck && cfCheck) {
					break;
				}
				
						// If we get here, try again
				
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
				
						// Confirm all Validations
				
				if(miCheck && QueryManager.getMember(dbconn, mno) != null) {
					break;
				}
				
						// If we get here, try again
				
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
			
					// Unreturned equipment will be marked as lost
					// Can delete the member successfully 
			
			DataManipulation.deleteMember(dbconn, Integer.parseInt(mno));			
			System.out.println("M# " + mno + " is deleted");
		}
		else {
			return false;
		}		
		return true;
	}
	
	/**
	 * This method takes a scanner object, a string to obtain user input,
	 *  and a database connection object to perform requests related to
	 *  adding or deleting a course from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param userInput is the string used for storing user input.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a boolean value indicating continual access to the DBMS.
	 * 	returns true by default for all successful procedures and
	 *  short-circuit returns due to errors, false for invalid choices.
	 */	
	private static boolean handleCourse(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Course");
		System.out.println("\t2. Delete a Course");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();  // Used to determine chosen option
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
						
			boolean cpCheck = false, // capacity is a positive integer
					sdCheck = false, // start date is valid
					stCheck = false, // start time is valid
					edCheck = false, // end date is valid
					etCheck = false, // end time is valid
					dwCheck = false, // day of the week is valid
					tnCheck = false, // trainer exists
					cfCheck = false; // trainer doesn't have scheduling conflicts
			
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
				
						// Sanitize the Day-of-Week and Date objects
				
				day = day.toUpperCase();
				start = Validation.concatDateAndTime(startDate, startTime);
				end = Validation.concatDateAndTime(endDate, endTime);
				System.out.println("\nChoose a trainer for the course");
				QueryManager.showAllTrainers(dbconn);
				System.out.print("\nEnter T#: ");
				tNo = sc.nextLine().strip();
				
						// Ensure that there are no scheduling conflicts
				
				if(Validation.validateInt(tNo)) {
					if (QueryManager.getTrainer(dbconn, tNo) != null) {
						tnCheck = true;
						LinkedList<Course> courses = QueryManager.getCoursesByTrainer(dbconn, tNo);
						cfCheck = Validation.noTrainerScheduleConflict(courses, day, start, end);
					}
				}
				
						// Check all Validations
				
				if(cpCheck && sdCheck && edCheck && stCheck && etCheck && dwCheck && tnCheck && cfCheck) {
					break;
				}
				
						// If we get here, try again
				
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
					break;  // course exists
				}
				
						// If we get here, try again
				
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
			
					// Update all packages associated with the course
			
			LinkedList<Package> needUpdates = QueryManager.getPackagesForCourse(dbconn, cName);
			for(Package p: needUpdates) {
				String c1 = p.c1, c2 = p.c2;
				String sd = (p.startDate == null) ? null : Validation.dateToString(p.startDate);
				String ed = (p.endDate == null) ? null : Validation.dateToString(p.endDate);
				if(c1 == null && c2 != null) {
					c2 = c1;
				}
				else if(c1 != null && c2 == null) {
					c1 = c2;
				}
				else {
					c1 = (c1.equals(cName)) ? c2 : c1;
					c2 = null;					
				}
				DataManipulation.updatePackage(dbconn, p.pName, c1, c2, p.price, sd, ed);
			}
			
					// Delete the course
			
			System.out.println("Associated Packages Updated");
			DataManipulation.deleteCourse(dbconn, cName);
			System.out.println("Course " + cName + " is deleted");
		}
		else {
			return false;
		}		
		return true;
	}
	
	/**
	 * This method takes a scanner object, and a database connection
	 *  object to provide assistance in obtaining relevant information
	 *  for adding or updating a package from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a linked list of strings containing details about the
	 * 	chosen courses and price for the package being edited.
	 */	
	private static LinkedList<String> packageHelper(
			Scanner sc, Connection dbconn){
		LinkedList<String> retval = null;
		
				// Series of Validation checks
			
		boolean c1Check = false, // course 1 is valid
				c2Check = false, // course 2 is valid
				prCheck = false, // price is a positive float
				cfCheck = false; // courses don't have overlap
		
		System.out.println("\nChoose courses from the following");
		QueryManager.showAllCourses(dbconn);			
		System.out.print("\nChoose Course 1: ");
		String c1 = sc.nextLine().strip();
		System.out.print("Choose Course 2: ");
		String c2 = sc.nextLine().strip();
		System.out.print("Enter a price for the package: ");
		String price = sc.nextLine().strip();
		prCheck = Validation.validateFloat(price);
		
				// Ensure that courses are selected properly
				// A Package can have between 0-2 courses
		
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
			
				// Check all Validations and Conflicts
		
		cfCheck = Validation.noCourseScheduleConflict(course1, course2);		
		if(c1Check && c2Check && prCheck && cfCheck) {
			retval = new LinkedList<String>();
			retval.add(0, c1);
			retval.add(1, c2);
			retval.add(2, price);
		}
			
		return retval;
	}
	
	/**
	 * This method takes a scanner object, a string to obtain user input,
	 *  and a database connection object to perform requests related to
	 *  adding, updating, or deleting a package from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param userInput is the string used for storing user input.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a boolean value indicating continual access to the DBMS.
	 * 	returns true by default for all successful procedures and
	 *  short-circuit returns due to errors, false for invalid choices.
	 */	
	private static boolean handlePackage(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Add a new Package");
		System.out.println("\t2. Update a Package");
		System.out.println("\t3. Delete a Package");
		System.out.print("\nPlease enter your choice (1/2/3)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();  // Used to determine chosen option
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
				
						// Obtain relevant information
				
				info = packageHelper(sc, dbconn);
				if(info != null) {
					break;
				}
				
						// If we get here, try again
				
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Decipher information from the helper
			
			String c1 = info.get(0),
				   c2 = info.get(1);
			float pcost = Float.parseFloat(info.get(2));
					
					// Obtain the two courses for the package
			
			Course course1 = QueryManager.getCourse(dbconn, c1),
				   course2 = QueryManager.getCourse(dbconn, c2);
			
					// Determine the start and end date of the package
			
			LinkedList<String> dates = Validation.courseToPackageDates(course1, course2);
			String startDate = dates.get(0);
			String endDate = dates.get(1);
			
					// Add the Package to the Database
			
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
					break; // package exists
				}
				
						// If we get here, try again
				
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
				System.out.println("\n***Cannot update this package***\n"
						+ "There are members currently enrolled in it");
				return true;
			}
			
					// Loop to obtain new details using helper
			
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
			
					// Decipher information from the helper
			
			String c1 = info.get(0),
				   c2 = info.get(1);
			float pcost = Float.parseFloat(info.get(2));
					
					// Obtain the two courses for the package
			
			Course course1 = QueryManager.getCourse(dbconn, c1),
				   course2 = QueryManager.getCourse(dbconn, c2);
			
					// Determine the start and end date of the package
			
			LinkedList<String> dates = Validation.courseToPackageDates(course1, course2);
			String startDate = dates.get(0);
			String endDate = dates.get(1);
			
					// Update the package with the new information
			
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
					break;  // package exists
				}
				
						// If we get here, try again
				
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Determine if package deletion is possible
			
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
	
	/**
	 * This method takes a scanner object, a string to obtain user input,
	 *  and a database connection object to perform requests related to
	 *  making a payment by a member from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param userInput is the string used for storing user input.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a boolean value indicating continual access to the DBMS.
	 * 	returns true by default for all successful procedures and
	 *  short-circuit returns due to errors, false for invalid choices.
	 */	
	private static boolean handlePayment(
			Scanner sc, String userInput, Connection dbconn) {

				// Strings to use for user input

		String    mno = null,
			   amount = null;
		
				// Series of Validation checks
		
		boolean miCheck = false, // member id is positive integer
				pfCheck = false, // amount is a positive float
				mbCheck = false; // member exists
		
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
			
					// Check all Validations
			
			if(miCheck && QueryManager.getMember(dbconn, mno) != null) {
				mbCheck = true;
			}
			if(miCheck && pfCheck && mbCheck) {
				break;
			}
			
					// If we get here, try again
			
			System.out.print("\n***Invalid Data Provided***\n"
					+ "Press 'c' to retry, other keys to exit: ");
			userInput = sc.nextLine().strip();
			if(!userInput.toLowerCase().equals("c")) {
				return true;
			}
		}
		
				// Make the payment and print transaction details
		
		float pay = Float.parseFloat(amount);
		int mid = Integer.parseInt(mno);
		int xno = DataManipulation.makePayment(dbconn, pay, mid);
		System.out.println("\nTransaction successful");
		QueryManager.printTransactionDetails(dbconn, xno);
		return true;
	}
	
	/**
	 * This method takes a scanner object, a string to obtain user input,
	 *  and a database connection object to perform requests related to
	 *  borrowing or returning some equipment from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param userInput is the string used for storing user input.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a boolean value indicating continual access to the DBMS.
	 * 	returns true by default for all successful procedures and
	 *  short-circuit returns due to errors, false for invalid choices.
	 */	
	private static boolean handleEquipment(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nPlease choose from the following:");
		System.out.println("\t1. Borrow some Equipment");
		System.out.println("\t2. Return some Equipment");
		System.out.print("\nPlease enter your choice (1/2)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();  // Used to determine chosen option
		if(userInput.equals("1")) {

					// Strings to use for user input

			String   mno = null,
				   eType = null,
			         qty = null;
			
					// Series of Validation checks
		
			boolean miCheck = false, // member id is positive integer
					mbCheck = false, // member exists
					etCheck = false, // equipment type exists
					qtCheck = false, // quantity is positive integer
					avCheck = false; // sufficient equipment is available
			
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
				
						// Validate that enough equipment is available
				
				if(etCheck) {
					LinkedList<Equipment> list = QueryManager.getEquipmentList(dbconn, eType);
					avCheck = Validation.canBorrow(list, qty);
				}
				if(miCheck && mbCheck && etCheck && qtCheck && avCheck) {
					break;
				}
				else if(!etCheck) {
					System.out.println("\nChosen Equipment does not exist.");
					return true;
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
						// If we get here, try again
				
				System.out.print("\n***Invalid Data Provided***\n"
						+ "Press 'c' to retry, other keys to exit: ");
				userInput = sc.nextLine().strip();
				if(!userInput.toLowerCase().equals("c")) {
					return true;
				}
			}
			
					// Return all equipment borrowed by the member
			
			LinkedList<Integer> xnos = DataManipulation.returnEquipment(dbconn, Integer.parseInt(mno));
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
	
	/**
	 * This method takes a scanner object, a string to obtain user input,
	 *  and a database connection object to perform requests related to
	 *  querying information from our database.
	 * 
	 * @param sc is the scanner object for System.in.
	 * 
	 * @param userInput is the string used for storing user input.
	 * 
	 * @param dbconn is the connection to the database.
	 * 
	 * @return a boolean value indicating continual access to the DBMS.
	 * 	returns true by default for all successful procedures and
	 *  short-circuit returns due to errors, false for invalid choices.
	 */	
	private static boolean handleQueries(
			Scanner sc, String userInput, Connection dbconn) {
		System.out.println("\nHere are some queries you may use:");
		System.out.println("\n1. List all member's names and phone numbers who"
    			+ "\n   now has a negative balance.");
		System.out.println("\n2. Check and see a member's class schedule for"
    			+ "\n   November.");
		System.out.println("\n3. Check and see all trainers' working hours for"
    			+ "\n   December.");
		System.out.println("\n4. Provided an equipment type, display the"
				+ "\n   members who borrowed it this year, the amount"
				+ "\n   borrowed, and when they borrowed it.");		
		System.out.print("\nPlease enter your choice (1/2/3/4)"
				+ "\nEnter any other key to quit: ");
		userInput = sc.nextLine().strip();  // Used to determine chosen option
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
			System.out.println("\nList of all Members:");
			QueryManager.showAllMembers(dbconn);
			System.out.print("\nEnter the M#: ");
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
			System.out.println("\nList of all Equipment Types:");
			QueryManager.showAllEquipment(dbconn);
			System.out.print("\nEnter the EType: ");
			userInput = sc.nextLine().strip();
			if(QueryManager.checkEquipmentType(dbconn, userInput)) {
				QueryManager.query4(dbconn, userInput);
			}
			else {
				System.out.println("Chosen Equipment does not exist.");
			}
			break;
		default:
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) {

		final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
		
				// I am providing my own details for consistency

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
	    	    		
				// Begin the GYM 460 DBMS Process
		
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
    		
    				// Determine chosen option and perform action
    		
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
        	
        			// Terminate process if required
        	
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
