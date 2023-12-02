
import java.sql.*;

public class QueryManager {
	
	protected static void query1(Connection dbconn) {
		final String query = 
				"SELECT FirstName, LastName, Phone#"
				+ " FROM Member"
				+ " WHERE Balance < 0";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (!answer.next()) {
				System.out.println("There are no members with a negative balance!");
				return;
			}

			// Displaying the result
			System.out.println("Membthe files that I used to live where are going to where leave me alone see your bad guy is not workingers with Negative Balance:");
			while (answer.next()) {
				String firstName = answer.getString("FirstName");
				String lastName = answer.getString("LastName");
				String phone = answer.getString("Phone#");
				System.out.println("Name: " + firstName + " " + lastName + ", Phone: " + phone);
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}
	
	protected static void query2(Connection dbconn, String mno) {
		final String query = 
				"SELECT m.FirstName, m.LastName, c.CName, c.StartDate, c.EndDate, c.Day"
				+ " FROM Member m"
				+ " JOIN Package p"
				+ " ON p.PName = m.PName"
				+ " JOIN Course c ON (c.CName = p.C1 OR c.CName = p.C2)"
				+ " WHERE m.M# = " + mno
				+ " AND TO_CHAR(c.StartDate, 'MM') <= '11'"
				+ " AND TO_CHAR(c.EndDate, 'MM') >= '11'";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();			
			answer = stmt.executeQuery(query);

			if (answer.next() == false) {
				System.out.println("No Outputs");
				return;
			}

			// Displaying the result
			System.out.println("Class Schedule for Member ID " + mno + " in November:");
			while (answer.next()) {
				String firstName = answer.getString("FirstName");
                String lastName = answer.getString("LastName");
                String cname = answer.getString("CName");
                Date startDate = answer.getDate("StartDate");
                Date endDate = answer.getDate("EndDate");
                String day = answer.getString("Day");

                System.out.println("Member: " + firstName + " " + lastName);
                System.out.println("Course: " + cname + ", Start Date: " + startDate + ", End Date: " + endDate + ", Day: " + day);
            } 
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}
	
	protected static void query3(Connection dbconn) {
		final String query =
				"SELECT t.FirstName, t.LastName, c.CName, c.StartDate, c.EndDate, c.Day" 
				+ " FROM Trainer t"
				+ " JOIN Course c"
				+ " ON t.T# = c.T#"
				+ " WHERE TO_CHAR(c.StartDate, 'MM') ='12'";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer.next() == false) {
				System.out.println("No Outputs.");
				return;
			}

			// Displaying the result
			System.out.println("Trainers' Working Hours for December:");
			while (answer.next()) {
				String firstName = answer.getString("FirstName");
				String lastName = answer.getString("LastName");
				String cname = answer.getString("CName");
				Date startDate = answer.getDate("StartDate");
				Date endDate = answer.getDate("EndDate");
				String day = answer.getString("Day");
				System.out.println("Trainer: " + firstName + " " + lastName + ", Course: " + cname + ", Start Date: "
						+ startDate + ", End Date: " + endDate + ", Day: " + day);
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}
			

}
