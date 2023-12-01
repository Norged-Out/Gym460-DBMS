
import java.sql.*;

public class QueryManager {
	
	// TODO This class will handle the 4 queries
	
	public static void query1(Connection dbconn) {
		final String query = 
				"SELECT FirstName, LastName, Phone#"
				+ " FROM Member"
				+ " WHERE Balance < 0";
		
		ResultSet answer = Gym460.doQuery(dbconn, query);
		
		if (answer != null) {
			try {
				// Displaying the result
		        System.out.println("Members with Negative Balance:");
		        while (answer.next()) {
		            String firstName = answer.getString("FirstName");
		            String lastName = answer.getString("LastName");
		            String phone = answer.getString("Phone#");
		            System.out.println("Name: " + firstName + " " + lastName
		            		+ ", Phone: " + phone);
		        }
				
			} catch (SQLException e) {
	        		System.out.println("Query that crashed => " + query);
	                System.err.println("*** SQLException:  "
	                    + "Could not fetch query results.");
	                System.err.println("\tMessage:   " + e.getMessage());
	                System.err.println("\tSQLState:  " + e.getSQLState());
	                System.err.println("\tErrorCode: " + e.getErrorCode());
	                System.exit(-1);
	
	        }
		}
	}
	
	public static void query2(Connection dbconn, String mno) {
		final String query = 
				"SELECT m.FirstName, m.LastName, c.CName, c.StartDate, c.EndDate, c.Day"
				+ " FROM Member m"
				+ " JOIN Package p"
				+ " ON p.PName = m.PName"
				+ " JOIN Course c"
				+ " WHERE m.M# = " + mno
				+ " AND EXTRACT(MONTH FROM c.StartDate) = 11"
				+ " AND EXTRACT(YEAR FROM c.StartDate) = 2023";
			
		ResultSet answer = Gym460.doQuery(dbconn, query);
			
		if (answer != null) {
			try {
					// Displaying the result
		        System.out.println("Class Schedule for November:");
		        while (answer.next()) {
		            String cname = answer.getString("CName");
		            Date startDate = answer.getDate("StartDate");
		            Date endDate = answer.getDate("EndDate");
		            String day = answer.getString("Day");
		            System.out.println("Course: " + cname + ", Start Date: "
		            		+ startDate.toString() + ", End Date: " + endDate.toString()
		            		+ ", Day: " + day);
		        }
				
			} catch (SQLException e) {
	        		System.out.println("Query that crashed => " + query);
	                System.err.println("*** SQLException:  "
	                    + "Could not fetch query results.");
	                System.err.println("\tMessage:   " + e.getMessage());
	                System.err.println("\tSQLState:  " + e.getSQLState());
	                System.err.println("\tErrorCode: " + e.getErrorCode());
	                System.exit(-1);
	
	        }
			
		}
		
	}
	
	public static void query3(Connection dbconn) {
		
		final String query =
				"SELECT t.FirstName, t.LastName, c.CName, c.StartDate, c.EndDate, c.Day" 
				+ " FROM Trainer t"
				+ " JOIN Course c"
				+ " ON t.T# = c.T#"
				+ " WHERE EXTRACT(MONTH FROM c.StartDate) = 12"
				+ " AND EXTRACT(YEAR FROM c.StartDate) = 2023";
			
		ResultSet answer = Gym460.doQuery(dbconn, query);
			
		if (answer != null) {
			try {
				// Displaying the result
		        System.out.println("Trainers' Working Hours for December:");
		        while (answer.next()) {
		            String firstName = answer.getString("FirstName");
		            String lastName = answer.getString("LastName");
		            String cname = answer.getString("CName");
		            Date startDate = answer.getDate("StartDate");
		            Date endDate = answer.getDate("EndDate");
		            String day = answer.getString("Day");
		            System.out.println("Trainer: " + firstName + " "
				            + lastName + ", Course: " + cname 
				            + ", Start Date: " + startDate.toString() 
				            + ", End Date: " + endDate.toString()
				            + ", Day: " + day);
		        }
				
			} catch (SQLException e) {
	        		System.out.println("Query that crashed => " + query);
	                System.err.println("*** SQLException:  "
	                    + "Could not fetch query results.");
	                System.err.println("\tMessage:   " + e.getMessage());
	                System.err.println("\tSQLState:  " + e.getSQLState());
	                System.err.println("\tErrorCode: " + e.getErrorCode());
	                System.exit(-1);
	
	        }
			
		}
		
	}
		

}
