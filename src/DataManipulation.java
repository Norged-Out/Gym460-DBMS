/*
 * Team:		Nathan Lucero
 * 				Priyansh Nayak
 * 				Utkarsh Upadhyay
 * 				Bhargav Gullipelli
 * 
 * Author:		Utkarsh Upadhyay
 * 
 * Course:		CSC-460 Fall 2023
 * 
 * Assignment:	Program #4: Creating a JDBC and SQL program that operates as 
 * 					an interface with a Oracle DB for use by the Gym460 staff.
 * 
 * Due Date:	12/05/2023
 * 
 * File Name:	DataManipulation.java
 * 
 * Instructor:	L. McCann
 * Graduate TA:	Zhenyu Qi
 * Graduate TA:	Danial Bazmandeh
 * 
 * Description: This Java class, DataManipulation, is part of the Gym460 application. 
 * It provides a set of methods for interacting with an Oracle Database using JDBC. 
 * The class handles various data manipulation tasks related to managing members, courses, packages, 
 * equipment, transactions, and payments within a gym management system. The methods in this 
 * class include functionalities to insert, delete, update, and retrieve data from the database tables. 
 * It interacts with tables such as "Member," "Course," "Package," "Equipment," and "Transaction." 
 * The class also incorporates business logic, such as calculating member balances, updating enrollment counts 
 * for courses, handling equipment borrowing and returning, and managing member tiers and discounts based on their 
 * consumption. It serves as a vital component of the Gym460 application, enabling gym staff to efficiently manage 
 * member data and transactions, track course enrollments, and process payments.
 * 
 * About: This program must be executed from main method in Gym460.java file 
 * 	and has Oracle DB username and password hard-coded for ease of use.
 */
import java.sql.*;
import java.util.*;

public class DataManipulation {
	
	/**
	 * Inserts a new member into the database with the provided information. This method performs
	 * several steps to create a new member record in the "Member" table. It first retrieves the
	 * next available member ID (M#) by finding the maximum existing M# and incrementing it. It then
	 * fetches relevant course information associated with the specified package, calculates the
	 * initial balance for the member based on the package price, and inserts a new member record
	 * with the provided details. Additionally, this method updates the enrollment counts for any
	 * courses associated with the package and records a credit transaction for the package price.
	 * 
	 * @param dbconn     the database connection to use for the operation.
	 * @param firstName  the first name of the member.
	 * @param lastName   the last name of the member.
	 * @param phone      the phone number of the member.
	 * @param pName      the package name associated with the member.
	 * 
	 * @return the ID (M#) of the newly inserted member if the insertion is successful,
	 *         or -1 if an error occurs during the database operation.
	 */
	protected static int insertMember(Connection dbconn, String firstName, String lastName, 
			String phone, String pName) {
	    Statement stmt = null;
	    ResultSet answer = null;

	    try {
	    	stmt = dbconn.createStatement();
	    	// **************** Get the member ID****************
	    	String query = "SELECT MAX(m#) FROM Member";
	        answer = stmt.executeQuery(query);
	        
	        int mId = 1; // Default value if the table is empty

	        // Check if there is a result (maximum "m#" value)
	        if (answer.next()) {
	            mId = answer.getInt(1) + 1; // Increment the maximum "m#" value
	        }
	        
	        // **************** Retrieve C1 and C2 values from the Package table ****************
	        String getCoursesQuery = "SELECT C1, C2 FROM Package WHERE PName = '" + pName + "'";
	        ResultSet coursesResultSet = stmt.executeQuery(getCoursesQuery);
	        String c1="",c2="";
	        if(coursesResultSet.next()) {
	        	c1 = coursesResultSet.getString("C1");
	        	c2 = coursesResultSet.getString("C2");
	        }
	        
	        // ******************** Get cost of package *******************************
	        String getCostQuery = "SELECT Price FROM Package WHERE PName = '" + pName + "'";
	        ResultSet costResultSet = stmt.executeQuery(getCostQuery);
	        float price=0;
	        if(costResultSet.next()) {
	         price = costResultSet.getFloat("Price");}
	        
	        float balance = -1*price; // Default balance value
	        
	        
	        //  **************** Create an SQL INSERT statement ****************
	        String insertQuery = "INSERT INTO Member (M#, FirstName, LastName, Phone#, PName, Balance, Consumption, Tier) " +
	                "VALUES (" + mId + ", '" + firstName + "', '" + lastName + "', '" + phone + "', '" +
	                pName + "', " + balance + ", 0 , NULL )";
	        stmt.executeUpdate(insertQuery);
	        
	        //************************ Update the enrollment count********************************************
	        if (c1!=null)
	        {updateEnrollmentCount(dbconn, c1,"+ 1");} /////Update when not null
	        if (c2!=null) 
	        {updateEnrollmentCount(dbconn, c2, "+ 1");}
	        
		     //********************Add a transaction**************************************
	        int XID = 1; // Default value if the table is empty
	        query = "SELECT MAX(X#) FROM Transaction";
	        answer = stmt.executeQuery(query);
	        if (answer.next()) {
	            XID = answer.getInt(1) + 1; 
	        }

	        insertQuery = "INSERT INTO Transaction (X#, M#, XDate, Amount, XType, EType) " +
	            "VALUES (" + XID + ", " + mId + ", SYSDATE, " + price + ", 'Credit', NULL)";

	        // Execute the INSERT statement
	        stmt.executeUpdate(insertQuery);
		  
	        stmt.close();
	        return mId;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // Return -1 to indicate an error
	}
	/**
	 * Updates the enrollment count for a specified course in the database. This method allows
	 * for the dynamic adjustment of course enrollment counts by either incrementing or decrementing
	 * the count based on the provided operation. It uses the course name (CName) to identify the
	 * target course and the provided operation (e.g., "+ 1" or "- 1") to determine whether to
	 * increase or decrease the enrollment count. The updated count is reflected in the "Course" table.
	 * 
	 * @param dbconn      the database connection to use for the update.
	 * @param courseName  the name of the course for which the enrollment count should be modified.
	 * @param operation   the operation to be performed on the enrollment count ("+ 1" to increment,
	 *                    "- 1" to decrement).
	 * 
	 * @throws SQLException if a database access error occurs during the update.
	 */
	private static void updateEnrollmentCount(Connection dbconn, String courseName, String operation) {
	    try {
	        // Update the enrollment count for the given course
	        String incrementEnrollmentQuery = "UPDATE Course SET EnrollCount = EnrollCount "+operation+" WHERE CName = '" + courseName + "'";
	        Statement incrementEnrollmentStatement = dbconn.createStatement();
	        incrementEnrollmentStatement.executeUpdate(incrementEnrollmentQuery);
	        incrementEnrollmentStatement.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * Deletes a member and associated records from the database based on the provided member ID (M#).
	 * This method first retrieves the courses (C1 and C2) associated with the member being deleted.
	 * It then proceeds to delete the member's record from the "Member" table, update enrollment counts
	 * for any associated courses by decrementing their counts, and mark any equipment borrowed by the
	 * member as lost (by setting M# to -1) in the "Equipment" table. This method is used to effectively
	 * remove a member's presence from the gym system.
	 * 
	 * @param dbconn  the database connection to use for the deletion.
	 * @param mId     the member ID (M#) of the member to be deleted.
	 * 
	 * @return 1 if the member deletion is successful, indicating successful removal of records,
	 *         or -1 if an error occurs during the database operation.
	 */
	protected static int deleteMember(Connection dbconn, int mId) {
	    try {
	        Statement stmt = dbconn.createStatement();

	        //********************Retrieve the courses associated with the member being deleted********************
	        String getCoursesQuery = "SELECT p.C1, p.C2 FROM " +
	        	    "PACKAGE p, MEMBER m " + // Added space after 'm'
	        	    "WHERE p.PName = m.PName " +
	        	    "AND m.M# = " + mId;
	        ResultSet coursesResultSet = stmt.executeQuery(getCoursesQuery);
	        String c1="",c2="";
	        if (coursesResultSet.next()) {
	        	c1 = coursesResultSet.getString("C1");
	        	c2 = coursesResultSet.getString("C2");
	        }
	        
	        //********************Delete the member********************
	        // Create a DELETE statement to remove the member with the specified mId
	        String deleteQuery = "DELETE FROM Member WHERE M# = " + mId;
	        stmt.executeUpdate(deleteQuery);
	        
	        //***********Update enrollment counts*************
	        if(c1!=null)
	        {updateEnrollmentCount(dbconn, c1, "- 1");}
	        if(c2!=null)
	        {updateEnrollmentCount(dbconn, c2, "- 1");}
	        //********************Mark equipment as lost*******************
	        String updateQuery=" UPDATE Equipment SET M# = -1 WHERE M# = "+mId;
	        stmt.executeUpdate(updateQuery);
	        
	        stmt.close(); // Close the Statement
	        return 1; // Deletion successful
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // Return -1 to indicate an error
	}
	/**
	 * Inserts a new course into the database with the provided information. This method allows
	 * the addition of a course record to the "Course" table. It takes various parameters, including
	 * course name (CName), trainer number (T#), capacity, start and end timestamps, and the day of
	 * the course. The method constructs an SQL INSERT statement and executes it to add the course
	 * details to the database.
	 * 
	 * @param dbconn          the database connection to use for the insertion.
	 * @param Cname           the name of the course to be inserted.
	 * @param Tno             the trainer number associated with the course.
	 * @param capacity        the maximum capacity of the course.
	 * @param startTimestamp  the starting date and time of the course in 'YYYY-MM-DD HH24:MI' format.
	 * @param endTimestamp    the ending date and time of the course in 'YYYY-MM-DD HH24:MI' format.
	 * @param day             the day of the week on which the course is scheduled.
	 * 
	 * @return the number of rows inserted into the "Course" table if the insertion is successful,
	 *         or -1 if an error occurs during the database operation.
	 */
	protected static int insertCourse(Connection dbconn, String Cname, int Tno, 
			int capacity, String startTimestamp, String endTimestamp,
	        String day) {
	    String insertQuery = "";
	    try {

	        // Create an SQL INSERT statement with values concatenated as strings and TO_DATE
	        insertQuery = "INSERT INTO Course (CName, T#, EnrollCount, Capacity, StartDate, EndDate, Day) " +
	                "VALUES ('" + Cname + "', " + Tno + ", 0, " + capacity + ", TO_DATE('" +
	                startTimestamp + "', 'YYYY-MM-DD HH24:MI'), TO_DATE('" +
	                endTimestamp + "', 'YYYY-MM-DD HH24:MI'), '" + day + "')";

	        // Create a Statement to execute the SQL query
	        Statement statement = dbconn.createStatement();

	        // Execute the INSERT statement
	        int rowsInserted = statement.executeUpdate(insertQuery);

	        // Check if the insertion was successful
	        if (rowsInserted > 0) {
	            return rowsInserted; // Return the number of rows inserted
	        }

	        statement.close();
	    } catch (SQLException e) {
	        System.out.println("Query that crashed => " + insertQuery);
	        e.printStackTrace();
	    }
	    return -1; // Return -1 to indicate an error
	}
	/**
	 * Deletes a course from the database based on the provided course name (CName). This method
	 * allows for the removal of a course record from the "Course" table. It constructs and executes
	 * an SQL DELETE statement using the specified course name to identify the target course. If the
	 * deletion is successful and at least one row is deleted, it returns the number of rows deleted.
	 * If no rows are deleted, it returns -1 to indicate that the course was not found in the database.
	 * 
	 * @param dbconn  the database connection to use for the deletion.
	 * @param Cname   the name of the course to be deleted.
	 * 
	 * @return the number of rows deleted from the "Course" table if the deletion is successful,
	 *         or -1 if the course is not found or an error occurs during the database operation.
	 */
	protected static int deleteCourse(Connection dbconn, String Cname) {
	    try {
	        // Create a DELETE statement to remove the course with the specified Cname
	        String deleteQuery = "DELETE FROM Course WHERE CName = '" + Cname + "'";

	        // Create a Statement to execute the DELETE query
	        Statement deleteStatement = dbconn.createStatement();

	        // Execute the DELETE statement
	        int rowsDeleted = deleteStatement.executeUpdate(deleteQuery);

	        // Check if the deletion was successful
	        if (rowsDeleted > 0) {
	            return rowsDeleted; // Return the number of rows deleted (1)
	        } else {
	            return -1; // No rows were deleted (course not found)
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // Return -1 to indicate an error
	}
	/**
	 * Inserts a new package into the database with the provided information. This method allows
	 * the addition of a package record to the "Package" table. It takes various parameters, including
	 * package name (PName), associated courses (C1 and C2), price, start and end dates, and formats
	 * these values for database insertion. The method constructs an SQL INSERT statement and executes
	 * it to add the package details to the database.
	 * 
	 * @param dbconn     the database connection to use for the insertion.
	 * @param pName      the name of the package to be inserted.
	 * @param c1         the name of the first associated course (or NULL if none).
	 * @param c2         the name of the second associated course (or NULL if none).
	 * @param price      the price of the package.
	 * @param startDate  the start date and time of the package validity in 'YYYY-MM-DD HH24:MI' format.
	 * @param endDate    the end date and time of the package validity in 'YYYY-MM-DD HH24:MI' format.
	 * 
	 * @return the number of rows inserted into the "Package" table if the insertion is successful,
	 *         or -1 if an error occurs during the database operation.
	 */
	protected static int insertPackage(Connection dbconn, String pName, String c1, String c2, 
	        float price, String startDate, String endDate) {
	    Statement stmt = null;
	    try {
	        stmt = dbconn.createStatement();
	        // Handle nulls and format values
	        c1 = c1 != null ? "'" + c1 + "'" : "NULL";
	        c2 = c2 != null ? "'" + c2 + "'" : "NULL";
	        startDate = startDate != null ? "TO_DATE('" + startDate + "', 'YYYY-MM-DD HH24:MI')" : "NULL";
	        endDate = endDate != null ? "TO_DATE('" + endDate + "', 'YYYY-MM-DD HH24:MI')" : "NULL";

	        // Create the INSERT query
	        String insertQuery = "INSERT INTO Package (PName, C1, C2, StartDate, EndDate, Price) " +
	                             "VALUES ('" + pName + "', " + c1 + ", " + c2 + ", " + startDate + ", " +
	                             endDate + ", " + price + ")";
	        // Execute the INSERT statement
	        int rowsInserted = stmt.executeUpdate(insertQuery);
	        stmt.close();
	        return rowsInserted;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1; // Indicate error
	    } 
	}
	/**
	 * Deletes a package from the database based on the provided package name (PName). This method
	 * allows for the removal of a package record from the "Package" table. It constructs and executes
	 * an SQL DELETE statement using the specified package name to identify the target package. .
	 * 
	 * @param dbconn  the database connection to use for the deletion.
	 * @param pName   the name of the package to be deleted.
	 * 
	 * @return the number of rows deleted from the "Package" table if the deletion is successful,
	 *         or -1 if the package is not found or an error occurs during the database operation.
	 */
	protected static int deletePackage(Connection dbconn, String pName) {
	    Statement stmt = null;
	    try {
	        stmt = dbconn.createStatement();

	        // Create the DELETE query to remove the package by PName
	        String deleteQuery = "DELETE FROM Package WHERE PName = '" + pName + "'";

	        // Execute the DELETE statement
	        int rowsDeleted = stmt.executeUpdate(deleteQuery);

	        // Check if the deletion was successful
	        
	        if (rowsDeleted > 0) {
	        	stmt.close();
	            return rowsDeleted; // Return the number of rows deleted (1)
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    return -1; // Return -1 to indicate an error
	}
	/**
	 * Updates an existing package in the database with the provided information. This method allows
	 * for the modification of a package's details in the "Package" table. It takes various parameters,
	 * including the package name (PName), associated courses (C1 and C2), price, start and end dates,
	 * and formats these values for database updating. The method constructs and executes an SQL UPDATE
	 * statement using the specified package name to identify the target package and applies the provided
	 * modifications. If the update is successful, it returns the number of rows affected by the update
	 * operation.
	 * 
	 * @param dbconn     the database connection to use for the update.
	 * @param pName      the name of the package to be updated.
	 * @param c1         the name of the first associated course (or NULL if none).
	 * @param c2         the name of the second associated course (or NULL if none).
	 * @param price      the updated price of the package.
	 * @param startDate  the updated start date and time of the package validity in 'YYYY-MM-DD HH24:MI' format.
	 * @param endDate    the updated end date and time of the package validity in 'YYYY-MM-DD HH24:MI' format.
	 * 
	 * @return the number of rows updated in the "Package" table if the update is successful,
	 *         or -1 if the package is not found or an error occurs during the database operation.
	 */
	protected static int updatePackage(Connection dbconn, String pName, String c1, String c2, 
	        float price, String startDate, String endDate) {
		Statement stmt = null;
	    try {
	        stmt = dbconn.createStatement();
	        // Handle nulls and format values
	        c1 = c1 != null ? "'" + c1 + "'" : "NULL";
	        c2 = c2 != null ? "'" + c2 + "'" : "NULL";
	        startDate = startDate != null ? "TO_DATE('" + startDate + "', 'YYYY-MM-DD HH24:MI')" : "NULL";
	        endDate = endDate != null ? "TO_DATE('" + endDate + "', 'YYYY-MM-DD HH24:MI')" : "NULL";

	        // Create the UPDATE query
	        String updateQuery = "UPDATE Package SET C1 = "+c1+" , C2 = "+c2+" , StartDate = "+startDate
	        		+" , EndDate = "+endDate+" , Price = "+price+" WHERE PName = '"+pName+"'";
	        // Execute the INSERT statement
	        int rowsInserted = stmt.executeUpdate(updateQuery);
	        stmt.close();
	        return rowsInserted;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1; // Indicate error
	    } 
	}
	/**
	 * Allows a member to borrow a specified quantity of equipment of a given type. This method facilitates
	 * the borrowing process by retrieving available equipment matching the provided equipment type (EType)
	 * and assigns it to the member (Mno). The method takes into account the quantity of equipment to be borrowed
	 * and ensures that the member does not exceed the available quantity. It updates the equipment records by
	 * setting the member's ID (M#) for the borrowed equipment and records the transaction in the "Transaction"
	 * table. The method returns the transaction number (X#) associated with the borrowing transaction.
	 * 
	 * @param dbconn    the database connection to use for the borrowing operation.
	 * @param Mno       the member ID (M#) of the member borrowing the equipment.
	 * @param quantity  the quantity of equipment to be borrowed.
	 * @param eType     the type of equipment to be borrowed (e.g., 'Treadmill', 'Dumbbell').
	 * 
	 * @return the transaction number (X#) associated with the borrowing transaction if the borrowing process
	 *         is successful; -1 if no equipment is available, the member exceeds the available quantity, or
	 *         an error occurs during the database operation.
	 */
	protected static int borrowEquipment(Connection dbconn, int Mno, int quantity, String eType) {
		Statement stmt = null;
	    ResultSet resultSet = null;
	    int counter=1;
	    try {
	        stmt = dbconn.createStatement();

	        // Retrieve the E# values of equipment that have M# as null and match the specified EType
	        String query = "SELECT E# FROM Equipment WHERE M# IS NULL AND EType = '" + eType + "'";
	        resultSet = stmt.executeQuery(query);

	        // Collect the E# values in the availableEquipmentNumbers ArrayList
	        while (resultSet.next() && counter<=quantity) {
	            int equipmentNumber = resultSet.getInt("E#");
	            borrowEquipmentHelper(dbconn,Mno,equipmentNumber, eType);
	            counter++;
	        }
	        
	        //********************Add a transaction**************************************
	        int XID = 1; // Default value if the table is empty
	        query = "SELECT MAX(X#) FROM Transaction";
	        ResultSet answer = stmt.executeQuery(query);
	        if (answer.next()) {
	            XID = answer.getInt(1) + 1; 
	        }
	        String insertQuery = "INSERT INTO Transaction (X#, M#, XDate, Amount, XType, EType) " +
	            "VALUES (" + XID + ", " + Mno + ", SYSDATE, " + quantity + ", 'Checkout', '"+eType+"')";
	        // Execute the INSERT statement
	        stmt.executeUpdate(insertQuery);
	        return XID;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}
	/**
	 * Helper method for the equipment borrowing process. This method updates the equipment records by
	 * assigning a specific piece of equipment (identified by ENo) to a member (identified by Mno) and
	 * records the equipment type (EType). It is used within the equipment borrowing process to update
	 * individual equipment records when a member borrows equipment.
	 * 
	 * @param dbconn  the database connection to use for updating equipment records.
	 * @param Mno     the member ID (M#) of the member borrowing the equipment.
	 * @param ENo     the equipment number (E#) of the equipment being borrowed.
	 * @param EType   the type of equipment being borrowed (e.g., 'Treadmill', 'Dumbbell').
	 * 
	 * @return 1 if the equipment assignment is successful; -1 if an error occurs during the database operation.
	 */
	private static int borrowEquipmentHelper(Connection dbconn, int Mno, int ENo, String EType) {
		Statement stmt = null;
		try {
	        stmt = dbconn.createStatement();
	        
	        //********************set M# in the Equipment table********************
	        String updateQuery = "UPDATE Equipment SET M# = " + Mno
	        					 + " WHERE E# = " + ENo
	        					 + " AND EType = '" + EType + "'";
	        // Execute the UPDATE statement
	        stmt.executeUpdate(updateQuery);
	        return 1;
	       
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	    
	}
	/**
	 * This method facilitates the return of borrowed equipment by a member. It retrieves the equipment
	 * types (EType) that the member has borrowed, updates the equipment records to mark them as available,
	 * and records return transactions for each type of equipment returned. The method returns a list of
	 * transaction IDs associated with the equipment returns.
	 * 
	 * @param dbconn  the database connection to use for updating equipment records and transactions.
	 * @param Mno     the member ID (M#) of the member returning equipment.
	 * 
	 * @return a linked list of transaction IDs for each equipment type returned, or an empty list if no equipment
	 *         was returned, or null if an error occurs during the database operation.
	 */
	protected static LinkedList<Integer> returnEquipment(Connection dbconn, int Mno) {
		Statement stmt = null;
		ResultSet rs = null;
		LinkedList<String> eTypes = new LinkedList<>();
		LinkedList<Integer> transactions = new LinkedList<>();
		String query = "SELECT DISTINCT EType FROM Equipment WHERE M# = " + Mno;
		try {
	        stmt = dbconn.createStatement();
	        rs = stmt.executeQuery(query);
	        
	        //********************First get all the eTypes that the member has borrowed*************
	        while (rs.next()) {
                eTypes.add(rs.getString("EType"));
            }
	        //*********************Get the current max XID***********************
	        int XID = 1; // Default value if the table is empty
	        query = "SELECT MAX(X#) FROM Transaction";
	        ResultSet answer = stmt.executeQuery(query);
	        if (answer.next()) {
	            XID = answer.getInt(1) + 1; 
	        }
	        //********************For each eType, return all the equipments*******************
	        for(String equipment:eTypes) {
	        	String updateQuery = "UPDATE Equipment SET M# = NULL WHERE EType = '" 
	        							+ equipment + "' AND M# = " + Mno;
	        	int rowsModified=stmt.executeUpdate(updateQuery);
	        	
	        	//*******************Create a transaction***********************************
		        String insertQuery = "INSERT INTO Transaction (X#, M#, XDate, Amount, XType, EType) " +
		            "VALUES (" + XID + ", " + Mno + ", SYSDATE, " + rowsModified + ", 'Return', '"+equipment+"')";
		        // Execute the INSERT statement
		        stmt.executeUpdate(insertQuery);
		        transactions.add(XID);
		        XID++;
	        }
	        stmt.close();
		}
		catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
		return transactions;
	}
	/**
	 * Manages the payment process for a gym member, enhancing their membership experience. This method
	 * facilitates transactions by updating the member's balance and consumption, applying discounts based
	 * on their membership tier, and potentially upgrading their tier if they meet specific criteria.
	 * It records payment transactions, making membership management seamless and rewarding.
	 * 
	 * @param dbconn  the database connection for performing updates and recording transactions.
	 * @param amount  the payment amount to be added to the member's account.
	 * @param mId     the unique ID (M#) of the member making the payment.
	 * 
	 * @return the transaction ID associated with the payment, or -1 if a database operation encounters an error.
	 */
	protected static int makePayment(Connection dbconn, float amount, int mId) {
	    Statement stmt = null;
	    try {
	        stmt = dbconn.createStatement();
	        
	        //************************Get member's tier*************************
	        String tierQuery="SELECT Tier, Consumption"
	        		+ " from Member"
	        		+ " where m#="+mId;
	        ResultSet rs = stmt.executeQuery(tierQuery);
	        String tier="";
	        float oldConsumption=0;
	        if (rs.next()){
	        	 tier=rs.getString("Tier");
	        	 oldConsumption=rs.getFloat("Consumption");
	        }
	        float discount=0;
	        //*************************Get their discount*************************
	        if(tier!=null) {
	        	String discountQuery="SELECT Discount"
		        		+ " from Tier"
		        		+ " where Tier = '" + tier +"'";
	        	rs=stmt.executeQuery(discountQuery);
	        	if (rs.next()) {
	        		discount=rs.getFloat("Discount");}
	        }
	        float Consumption=oldConsumption+amount-(amount*discount);
	        //**************************Update the Balance/Consumption of the member***********
	        String updateQuery = "UPDATE Member SET Balance = Balance + " + amount + 
	                             ", Consumption = " + Consumption +
	                             " WHERE M# = " + mId;
	        // Execute the update statement and return the number of rows affected
	        stmt.executeUpdate(updateQuery);
	        //**************************Update the tier of the member**********************
	        String updatedTier="null";
	        if (Consumption>=500 && Consumption <1000) {
	        	updatedTier="'Diamond'";
	        }
	        if (Consumption>=1000) {
	        	updatedTier="'Gold'";
	        }
	        updateQuery = "UPDATE member SET Tier = " + updatedTier + " WHERE m# = " + mId;
	        stmt.executeUpdate(updateQuery);
	        
	        //********************Add a transaction**************************************
	        float discAmount=amount-(amount*discount);
	        int XID = 1; // Default value if the table is empty
	        String query = "SELECT MAX(X#) FROM Transaction";
	        ResultSet answer = stmt.executeQuery(query);
	        if (answer.next()) {
	            XID = answer.getInt(1) + 1; 
	        }
	        String insertQuery = "INSERT INTO Transaction (X#, M#, XDate, Amount, XType, EType) " +
	            "VALUES (" + XID + ", " + mId + ", SYSDATE, "+discAmount+" , 'Payment', NULL)";
	        // Execute the INSERT statement
	        stmt.executeUpdate(insertQuery);
	        stmt.close();
	        return XID;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1; // Indicate error
	    } 
	}
}