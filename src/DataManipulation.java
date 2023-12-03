import java.sql.*;

public class DataManipulation {
	
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
	         c1 = coursesResultSet.getString("C1");}
	        if(coursesResultSet.next()) {
	         c2 = coursesResultSet.getString("C2");}
	        
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
	        	 c1 = coursesResultSet.getString("C1");}
	        if (coursesResultSet.next()) {
	        	 c2 = coursesResultSet.getString("C2");}
	        
	        //********************Delete the member********************
	        // Create a DELETE statement to remove the member with the specified mId
	        String deleteQuery = "DELETE FROM Member WHERE M# = " + mId;
	        stmt.executeUpdate(deleteQuery);
	        
	        //***********Update enrollment counts*************
	        if(c1!=null)
	        {updateEnrollmentCount(dbconn, c1, "- 1");}
	        if(c2!=null)
	        {updateEnrollmentCount(dbconn, c2, "- 1");}
	        stmt.close(); // Close the Statement
	        return 1; // Deletion successful
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // Return -1 to indicate an error
	}

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
	            borrowEquipmentHelper(dbconn,Mno,equipmentNumber);
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
	            "VALUES (" + XID + ", " + Mno + ", TO_DATE(SYSDATE, 'YYYY-MM-DD'), 0 , 'Checkout', '"+eType+"')";
	        // Execute the INSERT statement
	        stmt.executeUpdate(insertQuery);
	        return XID;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}
	private static int borrowEquipmentHelper(Connection dbconn, int Mno, int ENo) {
		Statement stmt = null;
		try {
	        stmt = dbconn.createStatement();
	        
	        //********************set M# in the Equipment table********************
	        String updateQuery = "UPDATE Equipment SET M# = " + Mno + " WHERE E# = " + ENo;
	        // Execute the UPDATE statement
	        stmt.executeUpdate(updateQuery);
	        return 1;
	       
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	    
	}
	protected static int makePayment(Connection dbconn, float amount, int mId) {
	    Statement stmt = null;
	    try {
	        stmt = dbconn.createStatement();
	        
	        //************************Get member's tier*************************
	        String tierQuery="SELECT Tier,Consumption "
	        		+ "from Member"
	        		+ "where m#="+mId;
	        ResultSet rs = stmt.executeQuery(tierQuery);
	        String tier="";
	        if (rs.next()){
	        	 tier=rs.getString("Tier");}
	        float oldConsumption=0;
	        if (rs.next()){
	        	 oldConsumption=rs.getFloat("Consumption");}
	        float discount=0;
	        //*************************Get their discount*************************
	        if(tier!=null) {
	        	String discountQuery="SELECT Discount "
		        		+ "from Tier"
		        		+ "where Tier = "+tier;
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
	        updateQuery = "UPDATE member SET Tier = " + updatedTier + " WHERE memberId = " + mId;
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
	            "VALUES (" + XID + ", " + mId + ", TO_DATE(SYSDATE, 'YYYY-MM-DD'), "+discAmount+" , 'Payment', NULL)";
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