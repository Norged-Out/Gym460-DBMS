import entities.Equipment;
import entities.Course;
import entities.Member;
import entities.Package;
import entities.Trainer;
import entities.Transaction;

import java.sql.*;
import java.util.LinkedList;

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

			if (answer == null) {
				System.out.println("There are no members with a negative balance!");
				return;
			}

			// Displaying the result
			while (answer.next()) {
				String firstName = answer.getString("FirstName");
				String lastName = answer.getString("LastName");
				String phone = answer.getString("Phone#");
				System.out.println("Name: " + firstName + " " + lastName + ", Phone: " + phone);
			}
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
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

			if (answer == null) {
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
			handleSQLException(e, query);
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

			if (answer == null) {
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
			handleSQLException(e, query);
		}
	}
	
	protected static void showAllMembers(Connection dbconn) {
		final String query = "SELECT M#, FirstName, LastName FROM Member";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer == null) {
				System.out.println("No members found.");
				return;
			}

			// Displaying the results
			System.out.println(String.format("%-10s %-20s %-20s", "Member ID", "First Name", "Last Name"));
			while (answer.next()) {
				int memberId = answer.getInt("M#");
				String firstName = answer.getString("FirstName");
				String lastName = answer.getString("LastName");
				System.out.println(String.format("%-10d %-20s %-20s", memberId, firstName, lastName));
			}
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
	}

	protected static void showAllTrainers(Connection dbconn) {
		final String query = "SELECT T#, FirstName, LastName FROM Trainer";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer == null) {
				System.out.println("No trainers found.");
				return;
			}

			// Displaying the results
			System.out.println(String.format("%-10s %-20s %-20s", "Trainer ID", "First Name", "Last Name"));
			while (answer.next()) {
				int trainerId = answer.getInt("T#");
				String firstName = answer.getString("FirstName");
				String lastName = answer.getString("LastName");
				System.out.println(String.format("%-10d %-20s %-20s", trainerId, firstName, lastName));
			}
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
	}

	protected static void showAllCourses(Connection dbconn) {
		final String query = "SELECT CName FROM Course";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer == null) {
				System.out.println("No courses found.");
				return;
			}

			// Displaying the results
			System.out.println(String.format("%-15s", "Course Name"));
			while (answer.next()) {
				String courseName = answer.getString("CName");
				System.out.println(String.format("%-15s", courseName));
			}
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
	}
	
	protected static void showAllPackages(Connection dbconn) {
		final String query = "SELECT PName, C1, C2 FROM Package";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer == null) {
				System.out.println("No packages found.");
				return;
			}

			// Displaying the results
			System.out.println(String.format("%-30s %-15s %-15s", "Package Name", "Course 1", "Course 2"));

			while (answer.next()) {
				String packageName = answer.getString("PName");
				String course1 = answer.getString("C1");
				String course2 = answer.getString("C2");
				System.out.println(String.format("%-30s %-15s %-15s", packageName, course1, course2));
			}
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
	}

	protected static void showAllEquipment(Connection dbconn) {
		final String query = "SELECT DISTINCT EType FROM Equipment";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer == null) {
				System.out.println("No equipment types found.");
				return;
			}

			// Displaying the results
			System.out.println(String.format("%-20s", "Equipment Type"));

			while (answer.next()) {
				String equipmentType = answer.getString("EType");
				System.out.println(String.format("%-20s", equipmentType));
			}

			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
	}
	
	protected static void printTransactionDetails(Connection dbconn, int xNumber) {
		final String query = "SELECT * FROM Transaction WHERE X# = " + xNumber;
		Statement stmt = null;
		ResultSet answer = null;
        try {
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);
            
			if (answer == null) {
				System.out.println("No Transaction found with X# = " + xNumber);
				return;
			}

            // if transaction exists.
            if (answer.next()) {

                Transaction transaction = new Transaction(
                		answer.getInt("X#"),
                		answer.getInt("M#"),
                		answer.getDate("XDate"),
                		answer.getFloat("Amount"),
                		answer.getString("XType"),
                		answer.getString("EType")
                );

                System.out.println(transaction); 
            }
        } catch (SQLException e) {
        	handleSQLException(e, query);
        }
    }
	
	protected static Course getCourse(Connection dbconn, String cName) {
		final String query = "SELECT * FROM Course WHERE CName = '" + cName + "'";
		Statement stmt = null;
		ResultSet answer = null;
		Course retval = null;
	    try {
	        stmt = dbconn.createStatement();
	        answer = stmt.executeQuery(query);
	        
	        if (answer != null) {
	        	if (answer.next()) {
		            retval = new Course(
		            		answer.getString("CName"),
		            		answer.getInt("T#"),
		            		answer.getInt("EnrollCount"),
		            		answer.getInt("Capacity"),
		            		answer.getDate("StartDate"),
		            		answer.getDate("EndDate"),
		            		answer.getString("Day")
		            		);
		        }
	        }
	    } catch (SQLException e) {
	    	handleSQLException(e, query);
	    }
	    return retval;
	}
	
	protected static Package getPackage(Connection dbconn, String pName) {
		final String query = "SELECT * FROM Package WHERE PName = '" + pName + "'";
		Statement stmt = null;
		ResultSet answer = null;
		Package retval = null;
	    try {
	        stmt = dbconn.createStatement();
	        answer = stmt.executeQuery(query);

	        if (answer != null) {
		        if (answer.next()) {
		            retval = new Package(
		            		answer.getString("PName"),
		            		answer.getString("C1"),
		            		answer.getString("C2"),
		            		answer.getDate("StartDate"),
		            		answer.getDate("EndDate"),
		            		answer.getDouble("Price")
		            		);
		        }
	        }
	    } catch (SQLException e) {
	    	handleSQLException(e, query);
	    }
	    return retval;
	}

	protected static Member getMember(Connection dbconn, String mno) {
		final String query = "SELECT * FROM Member WHERE M# = " + mno;
		Statement stmt = null;
		ResultSet answer = null;
		Member retval = null;
	    try {
	        stmt = dbconn.createStatement();
	        answer = stmt.executeQuery(query);
	        
	        if (answer != null) {
	        	if (answer.next()) {
		            retval = new Member(
		            		answer.getInt("M#"),
		            		answer.getString("FirstName"),
		            		answer.getString("LastName"),
		            		answer.getString("Phone#"),
		            		answer.getString("PName"),
		            		answer.getDouble("Balance"),
		            		answer.getDouble("Consumption"),
		            		answer.getString("Tier")
		            		);
		        }
	        }
	    } catch (SQLException e) {
	    	handleSQLException(e, query);
	    }
	    return retval;
	}

	protected static Trainer getTrainer(Connection dbconn, String tNo) {
        final String query = "SELECT * FROM Trainer WHERE T# = " + tNo;
        Statement stmt = null;
        ResultSet answer = null;
        Trainer retval = null;
        try {
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if (answer != null) {
            	if (answer.next()) {
                    int TNo = answer.getInt("T#");
                    String fName = answer.getString("FirstName");
                    String lName = answer.getString("LastName");
                    String phoneNo = answer.getString("Phone#");
                    
                    retval = new Trainer(TNo,fName, lName, phoneNo);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, query);
        }
        return retval;
    }
	
	// LinkedList can be empty but not null
	protected static LinkedList<Course> getCoursesByTrainer(Connection dbconn, String tNo) {
        final String query = "SELECT * FROM Course WHERE T# = " + tNo;
        Statement stmt = null;
		ResultSet answer = null;
        LinkedList<Course> coursesList = new LinkedList<>();
        try {
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);
            
            if (answer != null) {
                while (answer.next()) {
                    coursesList.add(new Course(answer.getString("CName"), answer.getInt("T#"), answer.getInt("EnrollCount"),
                    		answer.getInt("Capacity"), answer.getDate("StartDate"), answer.getDate("EndDate"), answer.getString("Day")));
                }
            }
        } catch (SQLException e) {
	    	handleSQLException(e, query);
        }
        return coursesList;
    }
	
	// LinkedList can be empty but not null	
	protected static LinkedList<Equipment> getEquipment(Connection dbconn, String eType) {
		final String query = "SELECT * FROM Equipment WHERE EType = '" + eType + "'";
		Statement stmt = null;
		ResultSet answer = null;
	    LinkedList<Equipment> equipmentList = new LinkedList<>();
	    try {
	        stmt = dbconn.createStatement();
	        answer = stmt.executeQuery(query);
	        
	        if (answer != null) {
		        while (answer.next()) {
		        	Integer mNumber = (Integer) answer.getObject("M#"); // This will be null if the column is SQL NULL
		            equipmentList.add(new Equipment(
		            		answer.getInt("E#"),
		            		answer.getString("EType"),
		            		mNumber // Can be null
		            		));
		        }
	        }
	    } catch (SQLException e) {
	    	handleSQLException(e, query);
	    }
	    return equipmentList;
	}

	private static void handleSQLException(SQLException e, String query) {
		System.err.println("*** SQLException:  " + "Could not fetch query results.");
		System.out.println("Query that crashed => " + query);
		System.err.println("\tMessage:   " + e.getMessage());
		System.err.println("\tSQLState:  " + e.getSQLState());
		System.err.println("\tErrorCode: " + e.getErrorCode());
		System.exit(-1);
	}
			

}
