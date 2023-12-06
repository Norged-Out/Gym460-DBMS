import entities.Equipment;
import entities.Course;
import entities.Member;
import entities.Package;
import entities.Trainer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class QueryManager {
	
	/**
	 * Retrieves and displays members with a negative balance.
	 *
	 * @param dbconn The database connection.
	 */
	protected static void query1(Connection dbconn) {
		final String query = 
				"SELECT FirstName, LastName, Phone#, Balance"
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
				Float  balance = answer.getFloat("Balance");
				System.out.println("Name: " + firstName + " " + lastName + ", Phone: " + phone + ", Balance: " + balance);
			}
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
	}

	
	/**
	 * This method takes a connection to the DB and a member ID and prints the
	 * 	members schedule for November of the current year.
	 * 
	 * @param dbconn is the connection to the DB.
	 * 
	 * @param mno is the member ID.
	 * 
	 * @return nothing.
	 */
	protected static void query2(Connection dbconn, String mno) {

		// if mno isn't a valid integer, print error message and return
		if (!Validation.validateInt(mno)) {

			System.out.println("*** ERROR: The given Member ID is invalid. ***");
			return;

		}// end if

		// create the query to fetch the data from the DB
		final String query = 
				"SELECT m.FirstName, m.LastName, c.CName, c.StartDate, c.EndDate, c.Day"
				+ " FROM Member m"
				+ " JOIN Package p"
				+ " ON p.PName = m.PName"
				+ " JOIN Course c ON (c.CName = p.C1 OR c.CName = p.C2)"
				+ " WHERE m.M# = " + mno;
		
		// create the statement and result set
		Statement stmt = null;
		ResultSet answer = null;

		// create variables to store data
		String firstName = null;
		String lastName = null;
		LinkedList<Course> courses = new LinkedList<>();

		// try to execute the query
		try {

			// execute the query and store the result
			stmt = dbconn.createStatement();			
			answer = stmt.executeQuery(query);

			// determine if the result has data
			if (answer == null) {
				System.out.println("No Outputs");
				return;
			}

			// retrieve the data from the result set
			while (answer.next()) {

				// store the data for printing
				Course newCourse = new Course();
				firstName			= answer.getString("FirstName");
                lastName			= answer.getString("LastName");
                newCourse.cName 	= answer.getString("CName");
                newCourse.startDate	= answer.getDate("StartDate");
                newCourse.endDate	= answer.getDate("EndDate");
                newCourse.day		= answer.getString("Day");

				// add the new course to the list
				courses.add(newCourse);

            }// end while

			// close the statement
			stmt.close();

		} catch (SQLException e) {

			// handle the exception
			handleSQLException(e, query);

		}// end try/catch

		// print the schedule for each course
		printSchedule(firstName, lastName, courses);

	}// end query2


	/**
	 * This method takes the data from query2 and prints the members schedule 
	 * 	for November of the current year.
	 * 
	 * @param firstName is the first name of the member.
	 * 
	 * @param lastName is the last name of the member.
	 * 
	 * @param courses is a linked list of courses the member is enrolled in.
	 * 
	 * @return nothing.
	 */
	private static void printSchedule(String firstName, String lastName, LinkedList<Course> courses) {
		
		// if the member isn't enrolled in any courses, print message and return
		if (courses.size() == 0) {

			System.out.println(firstName + " " + lastName + " is not enrolled in any courses.");
			return;

		}// end if

		// print the data for each course
		for (Course course : courses) {

			// create date and time objects to determine if the course takes 
			// place in November of the current year
			LocalDate novemberStart		= LocalDate.of(LocalDate.now().getYear(), 11, 1);
			LocalDate novemberEnd		= LocalDate.of(novemberStart.getYear(), 11, 30);
			LocalDateTime startDateTime	= Validation.dateToLocalDateTime(course.startDate);
			LocalDateTime endDateTime	= Validation.dateToLocalDateTime(course.endDate);
			LocalDate startDate			= startDateTime.toLocalDate();
			LocalDate endDate			= endDateTime.toLocalDate();
			LocalTime startTime			= startDateTime.toLocalTime();
			LocalTime endTime			= endDateTime.toLocalTime();

			// create a LinkedList to hold the dates to print in the schedule
			LinkedList<LocalDate> printDates = new LinkedList<>();

			// create the two cases that determine if the course takes place in November of the current year
			//	case1: if the course begins before November 1st and ends after November 1st, it is "in November"
			boolean case1 = startDate.isBefore(novemberStart) && endDate.isAfter(novemberStart);
			
			//	case 2: if the course both begins after November 1st and ends before November 30th, it is "in November"
			boolean case2 = startDate.isAfter(novemberStart) && startDate.isBefore(novemberEnd);

			// if the course takes place in November of the current year
			if (case1 || case2) {

				// create a temp date object
				LocalDate tempDate;

				// determine which date to set as the temp date
				if (case1) {

					// set to first day of November
					tempDate = novemberStart;

				} else {
					
					// set to first day of the course
					tempDate = startDate;

				}// end if

				// loop through days until no longer in november
				while (tempDate.getMonthValue() == 11) {

					// if the day of the week matches the course day
					if (tempDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).equalsIgnoreCase(course.day)) {

						// add the date to the list
						printDates.add(tempDate);

					}// end if

					// increment the temp date
					tempDate = tempDate.plusDays(1);

				}// end while

				// print the schedule header
				System.out.println(firstName + " " + lastName + "'s November " + novemberStart.getYear() + " Schedule for " + course.cName + " is:");

				// if no class days in November
				if (printDates.isEmpty()) {

					// print that there are no class days in November
					System.out.println("*** No class days in November. ***");

				} else {

					// if there are, print the schedule for each day of class
					for (LocalDate date : printDates) {

						System.out.println(course.day + " " + date.toString() + " from " + startTime.toString() + " to " + endTime.toString() + ".");
						System.out.println(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + " " + date.getMonthValue() + "/" + date.getDayOfMonth() + " " + startTime + " - " + endTime);

					}// end for
					

				}// end if

			}// end if

		}// end for

	}// end printSchedule


	/**
	 * This method takes a connection to the DB and prints the trainers' working
	 * 	hours for December of the current year.
	 * 
	 * @param dbconn is the connection to the DB.
	 * 
	 * @return nothing.
	 */
	protected static void query3(Connection dbconn) {

		// create the query to fetch the data from the DB
		final String query =
				"SELECT t.T#, t.FirstName, t.LastName, c.CName, c.StartDate, c.EndDate, c.Day" 
				+ " FROM Trainer t"
				+ " LEFT OUTER JOIN Course c"
				+ " ON t.T# = c.T#";
				
		// create the statement and result set
		Statement stmt = null;
		ResultSet answer = null;

		// create variables to store data
		Integer tNumber = 0;
		String firstName = null;
		String lastName = null;
		HashMap<String, Integer> trainers = new HashMap<>();

		// try to execute the query
		try {

			// execute the query and store the result
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			// determine if the result has data
			if (answer == null) {
				System.out.println("No Outputs.");
				return;
			}

			// retrieve the data from the result set
			while (answer.next()) {

				// store the data for printing
				Course newCourse = new Course();
				tNumber				= answer.getInt("T#");
				firstName			= answer.getString("FirstName");
				lastName			= answer.getString("LastName");
				newCourse.cName		= answer.getString("CName");
				newCourse.startDate	= answer.getDate("StartDate");
				newCourse.endDate	= answer.getDate("EndDate");
				newCourse.day		= answer.getString("Day");

				// create the key for the hashmap
				String key =  tNumber.toString() + " " + firstName + " " + lastName;

				// get the number of course minutes for this course
				int courseMinutes = getCourseMinutes(newCourse);

				// determine if the trainer is already in the hashmap
				if (trainers.containsKey(key)) {

					// if so, add the course minutes to the hashmap
					trainers.put(key, trainers.get(key) + courseMinutes);

				} else {

					// if not, add the trainer and the course minutes to the hashmap
					trainers.put(key, courseMinutes);

				}// end if
				
			}// end while

			// close the statement
			stmt.close();

		} catch (SQLException e) {

			// handle the exception
			handleSQLException(e, query);

		}// end try/catch

		// print the header for trainers working hours in December
		System.out.println("Trainers' Working Hours for December " + LocalDate.now().getYear() + ":");

		for (Map.Entry<String, Integer> entry : trainers.entrySet()) {

			// calculate the number of hours worked
			double hours = entry.getValue() / 60.0;

			// print the trainer's info i this format: "T# firstName LastName : Hours: hours"
			System.out.printf("Trainer: " + entry.getKey() + " : Hours: %.2f\n", hours);

		}// end for

	}// end query3


	/**
	 * This method takes a course from query3 and returns the amount of 
	 * 	minutes that take place in Decemberber of the current year.
	 * 
	 * @param course is the course to check.
	 * 
	 * @return the amount of course minutes that take place in Decemberber of 
	 * 	the current year.
	 */	
	private static int getCourseMinutes(Course course) {

		// create a variable to store the amount of course minutes to return
		int courseMinutes = 0;
		
		// if the course info is empty, return 0 minutes
		if (course.cName == null) {

			return courseMinutes;

		}// end if

		// create date and time objects to determine if the course takes 
		// place in December of the current year
		LocalDate decemberStart		= LocalDate.of(LocalDate.now().getYear(), 12, 1);
		LocalDate decemberEnd		= LocalDate.of(decemberStart.getYear(), 12, 31);
		LocalDateTime startDateTime	= Validation.dateToLocalDateTime(course.startDate);
		LocalDateTime endDateTime	= Validation.dateToLocalDateTime(course.endDate);
		LocalDate startDate			= startDateTime.toLocalDate();
		LocalDate endDate			= endDateTime.toLocalDate();
		LocalTime startTime			= startDateTime.toLocalTime();
		LocalTime endTime			= endDateTime.toLocalTime();

		// create a counter for the number of class days in December
		int classDays = 0;

		// create the two cases that determine if the course takes place in December of the current year
		//	case1: if the course begins before December 1st and ends after December 1st, it is "in December"
		boolean case1 = startDate.isBefore(decemberStart) && endDate.isAfter(decemberStart);
		
		//	case 2: if the course both begins after December 1st and ends before December 31th, it is "in December"
		boolean case2 = startDate.isAfter(decemberStart) && startDate.isBefore(decemberEnd);

		// if the course takes place in December of the current year
		if (case1 || case2) {

			// create a temp date object
			LocalDate tempDate;

			// determine which date to set as the temp date
			if (case1) {

				// set to first day of December
				tempDate = decemberStart;

			} else {
				
				// set to first day of the course
				tempDate = startDate;

			}// end if

			// loop through days until no longer in December
			while (tempDate.getMonthValue() == 12) {

				// if the day of the week matches the course day
				if (tempDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).equalsIgnoreCase(course.day)) {

					// increment the class days counter
					classDays++;

				}// end if

				// increment the temp date
				tempDate = tempDate.plusDays(1);

			}// end while
			
			// if no class days in December
			if (classDays == 0) {

				// return 0 minutes
				return courseMinutes;

			} else {

				// if there are, determine the length of each class day in minutes
				courseMinutes = (endTime.getHour() - startTime.getHour()) * 60;
				courseMinutes += endTime.getMinute() - startTime.getMinute();

				// calculate the total amount of course minutes
				courseMinutes *= classDays;
				
			}// end if

		}// end if

		// return the amount of course minutes that take place in Decemberber of the current year
		return courseMinutes;

	}// end courseMinutes


	
	/**
	 * Retrieves transaction details for a specific equipment type and prints them.
	 *
	 * @param dbconn The database connection.
	 * @param eType  The equipment type.
	 */
	protected static void query4(Connection dbconn, String eType) {
		final String query =
				"SELECT m.FirstName, m.LastName, x.XDate, x.Amount" 
				+ " FROM Transaction x"
				+ " JOIN Member m"
				+ " ON x.M# = m.M#"
				+ " WHERE x.EType = '" + eType +"'"
				+ " AND x.XType = 'Checkout'"
				+ " AND TO_CHAR(x.XDate, 'YYYY') = TO_CHAR(SYSDATE, 'YYYY')";
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer == null) {
				System.out.println("No Outputs.");
				return;
			}
			
					// Displaying the results
			
			
			String mName = null,
				   xDate = null;
			int amount = 0,
				   row = 0;
			
			while(answer.next()) {
				if(row == 0) {
					System.out.println(String.format("%-40s %-16s %-6s", "Name", "Date", "Amount"));
				}
				mName = answer.getString("FirstName") + " " + answer.getString("LastName");
                xDate = Validation.dateToString(answer.getDate("XDate"));
                amount = (int) answer.getFloat("Amount");
                System.out.println(String.format("%-40s %-16s %6d", mName, xDate, amount));
                row++;
			}
			
			if(row == 0) {
				System.out.println("No one checked out the equipment");
			}
			
			stmt.close();
		} catch (SQLException e) {
			handleSQLException(e, query);
		}
		
	}
	
	/**
	 * Lists and displays all members in the database.
	 *
	 * @param dbconn The database connection.
	 */
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
			 int row = 0;
			// Displaying the results
			while (answer.next()) {
				if(row == 0) {
					System.out.println(String.format("%-10s %-20s %-20s", "Member ID", "First Name", "Last Name"));
				}
				row++;
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

	/**
	 * Lists and displays all trainers in the database.
	 *
	 * @param dbconn The database connection.
	 */
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

	/**
	 * Lists and displays all courses in the database.
	 *
	 * @param dbconn The database connection.
	 */
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
	
	/**
	 * Lists and displays all packages in the database.
	 *
	 * @param dbconn The database connection.
	 */
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

	/**
	 * Lists and displays all types of equipment in the database.
	 *
	 * @param dbconn The database connection.
	 */
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
	
	/**
	 * Displays the members enrolled in a specified course.
	 *
	 * @param dbconn The database connection.
	 * @param cName  The course name.
	 */
	protected static void showMembersEnrolled(Connection dbconn, String cName) {
        String query = "SELECT * FROM Member m"
        		+ " JOIN Package p"
        		+ " ON m.PName = p.PName"
        		+ " WHERE p.C1 = '" + cName
        		+ "' OR p.C2 = '" + cName + "'";

        Statement stmt = null;
        ResultSet answer = null;
        try  {

            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if (answer == null) {
                System.out.println("No Member found");
                return;
            }

            while (answer.next()) {
                String firstName = answer.getString("FirstName");
                String lastName = answer.getString("LastName");
                int memberId = answer.getInt("M#");
                System.out.println("Member ID: " + memberId + ", Name: " + firstName + " " + lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Prints the details of a specific transaction.
	 *
	 * @param dbconn  The database connection.
	 * @param xNumber The transaction number.
	 */
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
            	// Extracting details from the ResultSet
	            int transactionNumber = answer.getInt("X#");
	            int memberNumber = answer.getInt("M#");
	            Date transactionDate = answer.getDate("XDate");
	            float amount = answer.getFloat("Amount");
	            String xType = answer.getString("XType");
	            String eType = answer.getString("EType");

	            // Building and formatting the output message
	            StringBuilder output = new StringBuilder();
	            output.append(String.format("%-18s: %s\n", "Transaction Number", transactionNumber));
	            output.append(String.format("%-18s: %s\n", "Transaction Type", xType));
	            output.append(String.format("%-18s: %s\n", "Date", transactionDate));
	            output.append(String.format("%-18s: %s\n", "Member Number", memberNumber));

	            if (!"Credit".equals(xType) && !"Payment".equals(xType)) {
	                int qty = (int) amount;
	                output.append(String.format("%-18s: %d\n", "Quantity", qty));
	            } else {
	                output.append(String.format("%-18s: $%.2f\n", "Amount", amount));
	            }

	            if (eType != null && !"Credit".equals(xType) && !"Payment".equals(xType)) {
	                output.append(String.format("%-18s: %s\n", "Equipment Type", eType));
	            }

	            // Print the constructed message
	            System.out.println(output.toString());
            }
        } catch (SQLException e) {
        	handleSQLException(e, query);
        }
    }
	
	/**
	 * Retrieves a specific course from the database.
	 *
	 * @param dbconn The database connection.
	 * @param cName  The course name.
	 * @return The course object.
	 */
	protected static Course getCourse(Connection dbconn, String cName) {
		if(cName == null) {
			return null;
		}
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
	
	/**
	 * Retrieves a specific package from the database.
	 *
	 * @param dbconn The database connection.
	 * @param pName  The package name.
	 * @return The package object.
	 */
	protected static Package getPackage(Connection dbconn, String pName) {
		if(pName.equals("")) {
			return null;
		}
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
		            		answer.getFloat("Price")
		            		);
		        }
	        }
	    } catch (SQLException e) {
	    	handleSQLException(e, query);
	    }
	    return retval;
	}

	/**
	 * Retrieves a specific member from the database.
	 *
	 * @param dbconn The database connection.
	 * @param mno    The member number.
	 * @return The member object.
	 */
	protected static Member getMember(Connection dbconn, String mno) {
		if(mno.equals("")) {
			return null;
		}
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

	/**
	 * Retrieves a specific trainer from the database.
	 *
	 * @param dbconn The database connection.
	 * @param tNo    The trainer number.
	 * @return The trainer object.
	 */
	protected static Trainer getTrainer(Connection dbconn, String tNo) {
		if(tNo.equals("")) {
			return null;
		}
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
	
	/**
	 * Checks if a certain equipment type exists in the database.
	 *
	 * @param dbconn The database connection.
	 * @param eType  The equipment type.
	 * @return True if the equipment type exists, otherwise false.
	 */
	protected static boolean checkEquipmentType(Connection dbconn, String eType) {
		if(eType.equals("")) {
			return false;
		}
        final String query = "SELECT * FROM Equipment WHERE ETYPE = '" + eType+"'";
        Statement stmt = null;
        ResultSet answer = null;
        try {
            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if (answer != null && answer.next()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            handleSQLException(e, query);
            return false;
        }
    }
	
	/**
	 * Retrieves a list of courses taught by a specific trainer.
	 *
	 * @param dbconn The database connection.
	 * @param tNo    The trainer number.
	 * @return The list of courses.
	 */
	protected static LinkedList<Course> getCoursesByTrainer(Connection dbconn, String tNo) {
        final String query = "SELECT * FROM Course WHERE T# = " + tNo;
        Statement stmt = null;
		ResultSet answer = null;
        LinkedList<Course> coursesList = new LinkedList<>();
        if(tNo.equals("")) {
			return coursesList;
		}
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
	
	/**
	 * Retrieves a list of packages for a specific course.
	 *
	 * @param dbconn The database connection.
	 * @param cName  The course name.
	 * @return The list
	 */
	protected static LinkedList<Package> getPackagesForCourse(Connection dbconn, String cName) {
        String query = "SELECT * FROM Package"
        		+ " WHERE C1 = '" + cName
        		+"' OR C2 = '" + cName + "'";
        Statement stmt = null;
        ResultSet answer = null;
        LinkedList<Package> packageList = new LinkedList<>();

        try  {

            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if (answer == null) {
                System.out.println("No Package found");
                return null;
            }

            while (answer.next()) {
                packageList.add(new Package(
                		answer.getString("PName"),
                		answer.getString("C1"),
                		answer.getString("C2"),
                		answer.getDate("StartDate"),
                		answer.getDate("EndDate"),
                		answer.getFloat("Price")
                		));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return packageList;
    }
	
	
	/**
	 * Retrieves a list of equipments for a specific equipment type.
	 *
	 * @param dbconn The database connection.
	 * @param eType  equipment type.
	 * @return The list
	 */
	protected static LinkedList<Equipment> getEquipmentList(
			Connection dbconn, String eType) {
		String query = "SELECT * FROM Equipment WHERE EType = '" + eType + "'";
		Statement stmt = null;
		ResultSet answer = null;
	    LinkedList<Equipment> equipmentList = new LinkedList<>();
	    if(eType.equals("")) {
			return equipmentList;
		}
	    try {
	        stmt = dbconn.createStatement();
	        answer = stmt.executeQuery(query);
	        
	        if (answer != null) {
		        while (answer.next()) {
		        	// This will be null if the column is SQL NULL
		        	int mNumber = answer.getInt("M#");
                    if (mNumber == 0) {
                        equipmentList.add(new Equipment(
                                answer.getInt("E#"),
                                answer.getString("EType"),
                                null // Can be null
                                ));
                    } 
                    else {
	                    equipmentList.add(new Equipment(
	                            answer.getInt("E#"),
	                            answer.getString("EType"),
	                            mNumber
	                            ));
                    }
		        }
	        }
	    } catch (SQLException e) {
	    	handleSQLException(e, query);
	    }
	    return equipmentList;
	}

	/**
	 * Handles SQLExceptions by printing error details and terminating the program.
	 * 
	 * @param e     The SQLException object.
	 * @param query The SQL query that caused the exception.
	 */
	private static void handleSQLException(SQLException e, String query) {
		System.err.println("*** SQLException:  " + "Could not fetch query results.");
		System.out.println("Query that crashed => " + query);
		System.err.println("\tMessage:   " + e.getMessage());
		System.err.println("\tSQLState:  " + e.getSQLState());
		System.err.println("\tErrorCode: " + e.getErrorCode());
		System.exit(-1);
	}
			

}
