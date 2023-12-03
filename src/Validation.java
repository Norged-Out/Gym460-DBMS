import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;

import entities.Course;
import entities.Equipment;
import entities.Member;
import entities.Package;
// import entities.Tier;
// import entities.Trainer;
// import entities.Transaction;


public class Validation {

	/* ******************************
	   ***** Member Validations *****
	   ****************************** */
	
	/**
	 * This method takes a list of equipment and a memberID and returns a list 
	 * 	of equipment that was loaned to the member.
	 * 
	 * @param equipment is the list of all loaned equipment.
	 * 
	 * @param memberID is the memberID of the member.
	 * 
	 * @return a list of equipment that was loaned to the member or if none, return null.
	 */
	protected static LinkedList<Equipment> equipmentCheck(LinkedList<Equipment> equipment, int memberID) {

		// create a list to hold the lost equipment
		LinkedList<Equipment> lostEquipment = new LinkedList<>();

		// loop through the equipment objects
		for (Equipment equip : equipment) {
			
			// if the equipment is loaned to the member
			if (equip.m == memberID) {

				// add it to the lost equipment list
				lostEquipment.add(equip);
				
			}// end if

		}// end for

		// return null, if the member has no unreturned equipment
		return null;

	}// end equipmentCheck


	/**
	 * This method takes a member and returns true if the member has a 
	 * 	non-negative balance, or false if the member has a negative balance.
	 * 
	 * @param member is the member to be checked.
	 * 
	 * @return true if the member has a non-negative balance, else return false.
	 */
	protected static boolean balanceCheck(Member member) {


		// if the member's balance is negative
		if (member.balance < 0) {

			// return false, print balance and do not delete
			return false;

		}// end if

		// if balance is non-negative, return true
		return true;

	}// end balanceCheck





	/* ******************************
	   ***** Course Validations *****
	   ****************************** */

	/**
	 * This method takes a Course object and determines if it is active.
	 * 
	 * @param course is the course to be checked.
	 * 
	 * @return true if the course is active, else return false.
	 */
	protected static boolean isCourseActive(Course course) {

		// if the course is null, return false
		if (course == null) {

			return false;

		}// end if
		
		// create a date object for the current date
		Date currentDate = new Date();

		// if the course is "active"
		if (course.startDate.before(currentDate) && course.endDate.after(currentDate)) {

			// return true
			return true;

		}// end if

		// if the course is not active, return false
		return false;

	}// end isCourseActive


	/**
	 * This method takes a Course object and determines if its 
	 * 	capacity is full.
	 * 
	 * @param course is the course to be checked.
	 * 
	 * @return true if the course is full, else return false.
	 */
	protected static boolean isCourseFull(Course course) {
		
		// if the course is null, return true so that no one can enroll in it
		if (course == null) {

			return true;

		}// end if

		// if the course is full
		if (course.enrollCount >= course.capacity) {

			// return true
			return true;

		}// end if

		// if the course is not full, return false
		return false;

	}// end isCourseFull


	/**
	 * This method takes a Course object and determines if enrollCount is zero.
	 * 
	 * @param course is the course to be checked.
	 * 
	 * @return true if the course is empty, else return false.
	 */
	protected static boolean isCourseEmpty(Course course) {
		
		// if the course is null, return true so that a package can be deleted
		if (course == null) {

			return true;

		}// end if

		// if the course is empty
		if (course.enrollCount == 0) {

			// return true
			return true;

		}// end if

		// if the course is not empty, return false
		return false;

	}// end isCourseEmpty


	// TODO: check for Trainer schedule conflicts noScheduleConflict()
	/**
	 * This method takes a LinkedList of Course objects, the day of week, the 
	 * 	new course start time and end time and determines if it conflicts with 
	 * 	another course.
	 * 
	 * @param courses is the list of courses that the trainer is already 
	 * 	teaching.
	 * 
	 * @param dow is the day of the week that the new course is to be taught.
	 * 
	 * @param startTime is the start time of the new course.
	 * 
	 * @param startDate is the start date of the new course.
	 * 
	 * @param endTime is the end time of the new course.
	 * 
	 * @param endDate is the end date of the new course.
	 * 
	 * @return true if there are no conflicts with the new course and the 
	 * 	other courses, else return false.
	 */
	protected static boolean noTrainerScheduleConflict(LinkedList<Course> courses, String dow, String startTime, String startDate, String endTime, String endDate) {
		
		// convert the strings to integers for year, month, day, hour, and minute
		int startYear	= Integer.parseInt(startDate.substring(0, 4));
		int startMonth	= Integer.parseInt(startDate.substring(5, 7));
		int startDay	= Integer.parseInt(startDate.substring(8, 10));
		int startHour	= Integer.parseInt(startTime.substring(0, 2));
		int startMinute	= Integer.parseInt(startTime.substring(3, 5));
		int endYear		= Integer.parseInt(endDate.substring(0, 4));
		int endMonth	= Integer.parseInt(endDate.substring(5, 7));
		int endDay		= Integer.parseInt(endDate.substring(8, 10));
		int endHour		= Integer.parseInt(endTime.substring(0, 2));
		int endMinute	= Integer.parseInt(endTime.substring(3, 5));

		// create LocalDateTime objects for the new course start date and end date
		LocalDateTime newStartDateTime	= LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
		LocalDateTime newEndDateTime	= LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);

		// loop through the courses to check for conflicts
		for (Course course : courses) {

			// create a LocalDateTime object for the course start date and end date
			LocalDateTime courseStartDateTime	= dateToLocalDateTime(course.startDate);
			LocalDateTime courseEndDateTime		= dateToLocalDateTime(course.endDate);

			// LocalDateTime courseStartDateTime	= LocalDateTime.of(course.startDate.getYear(), course.startDate.getMonth(), course.startDate.getDay(), course.startDate.getHours(), course.startDate.getMinutes());
			// LocalDateTime courseEndDateTime		= LocalDateTime.of(course.endDate.getYear(), course.endDate.getMonth(), course.endDate.getDay(), course.endDate.getHours(), course.endDate.getMinutes());

			// if the new course start date is before the course start date
			if (newStartDateTime.isBefore(courseStartDateTime)) {

				// if the new course end date is before the course start date
				if (newEndDateTime.isBefore(courseStartDateTime)) {

					// skip the course
					continue;

				}// end if

				// if the new course end date is after the course end date
				if (newEndDateTime.isAfter(courseEndDateTime)) {

					// skip the course
					continue;

				}// end if

				// if the new course end date is the same as the course end date
				if (newEndDateTime.isEqual(courseEndDateTime)) {

					// if the new course end time is after the course end time
					if (newEndDateTime.isAfter(courseEndDateTime)) {

						// skip the course
						continue;

					}// end if

				}// end if

				// if the new course end date is before the course end date
				if (newEndDateTime.isBefore(courseEndDateTime)) {

				}// end if
				
			}// end if

		}// end for
return false;
	}// end noTrainerScheduleConflict

	/**
	 * this method takes two course objects and determines if they have a 
	 * 	scedule conflict.
	 * 
	 * @param c1 is the first course.
	 * 
	 * @param c2 is the second course.
	 * 
	 * @return true if the two courses do not have a schedule conflict, else return false.
	 */
	protected static boolean noCourseScheduleConflict(Course c1, Course c2) {
		
		// convert the course dates to LocalDateTime objects
		LocalDateTime c1StartDateTime	= dateToLocalDateTime(c1.startDate);
		LocalDateTime c1EndDateTime		= dateToLocalDateTime(c1.endDate);
		LocalDateTime c2StartDateTime	= dateToLocalDateTime(c2.startDate);
		LocalDateTime c2EndDateTime		= dateToLocalDateTime(c2.endDate);

		// check if the two courses have a schedule conflict
		// return noScheduleConflict(c1StartDateTime, c1EndDateTime, c2StartDateTime, c2EndDateTime);

		return false;
		
	}// end noCourseScheduleConflict




	/* *******************************
	   ***** Package Validations *****
	   ******************************* */

	/**
	 * This method takes a Package object and determines if it is active.
	 * 
	 * @param thePackage is the package to be checked.
	 * 
	 * @return true if the package is active, else return false.
	 */
	protected static boolean isPackageActive(Package thePackage) {

		// if the package is null, return false
		if (thePackage == null) {

			return false;

		}// end if

		// if the dates are null, return false
		if (thePackage.startDate == null || thePackage.endDate == null) {

			return false;

		}// end if
		
		// create a date object for the current date
		Date currentDate = new Date();

		// if the package is "active"
		if (thePackage.startDate.before(currentDate) && thePackage.endDate.after(currentDate)) {

			// return true
			return true;

		}// end if

		// if the package is not active, return false
		return false;

	}// end isPackageActive
	
	

	
	














	/* **********************************
	   ***** User Input Validations *****
	   ********************************** */

	/**
	 * This method takes a string and determines if it can be converted 
	 * 	to an integer and if it is non-negative.
	 * 
	 * @param input is the string to be validated.
	 * 
	 * @return true if the input can be converted to an integer, 
	 * 	else return false.
	 */
	static boolean validateInt(String input) {
		
		// check if the input is a valid integer
		try {
		
			// try to convert input to an integer
			int number = Integer.parseInt(input);

			// make sure the input is not negative
			if (number < 0) {

				// if the input is negative, return false
				return false;

			}// end if
			
		} catch (Exception e) {
	
			// if the string is not able to be converted, return false
			return false;
	
		}// end try/catch
			
		// if the input was converted, return true
		return true;

	}// end validateInt


	/**
	 * This method takes a string and determines if it can be converted 
	 * 	to a float and if it is non-negative.
	 * 
	 * @param input is the string to be validated.
	 * 
	 * @return true if the input can be converted to an float, 
	 * 	else return false.
	 */
	static boolean validateFloat(String input) {
		
		// check if the input is a valid float
		try {
		
			// try to convert input to an float
			Float number = Float.parseFloat(input);

			// make sure the input is not negative
			if (number.compareTo(Float.valueOf("0")) < 0) {

				// if the input is negative, return false
				return false;

			}// end if
			
		} catch (Exception e) {
	
			// if the string is not able to be converted, return false
			return false;
	
		}// end try/catch
			
		// if the input was converted, return true
		return true;

	}// end validateInt


	/**
	 * This method takes a string representation of a phone number and 
	 * 	verifies that it is a valid phone number format.
	 * 
	 * @param input the string to be validated.
	 * 
	 * @return true if the input is in a valid phone number format, else 
	 * 	return false.
	 */
	static boolean validatePhone(String input) {
		
		// if the input is not 10 characters long
		if (input.length() != 10) {

			// return false
			return false;

		}// end if

		// check if the input is a valid phone number
		try {

			// try to convert input to an integer
			Integer.parseInt(input);

		} catch (Exception e) {

			// if the string is not able to be converted, return false
			return false;

		}// end try/catch

		// if the input was converted, return true
		return true;

	}// end validatePhone


	/**
	 * This method takes a string representation of a date (no time portion)
	 * 	and uses regex to verify if it is in the format YYYY-MM-DD. Returns 
	 * 	true if it is a valid date format, else return false.
	 * 
	 * @param input the string to be validated.
	 * 
	 * @return true if the input is in the valid format, else return false.
	 */
	static boolean validateDate(String input) {
		
		// create a regex pattern for a valid date
		String regex = "\\d{4}-\\d{2}-\\d{2}";

		// if the input matches the regex pattern
		if (input.matches(regex)) {

			// test for appropriate values in the date/time
			try {
				int year	= Integer.parseInt(input.substring(0, 4));
				int month	= Integer.parseInt(input.substring(5, 7));
				int day		= Integer.parseInt(input.substring(8, 10));

				// test the year
				if (year < 0000) {

					return false;

				}// end if

				// test the month
				if (month < 1 || month > 12) {

					return false;

				}// end if

				// test the day
				if (day < 1 || day > 31) {

					return false;

				}// end if

			} catch (Exception e) {

				// if there was an exception during parsing, return false
				return false;

			}// end try/catch

			// If we get here, the date is valid
			return true;

		}// end if

		// If we get here, the date is not valid
		return false;

	}// end validateDate


	/**
	 * This method takes a string representation of a time and uses regex to 
	 * 	verify if it is in the format HH:MI. Returns true if it is a valid 
	 * 	tume format, else return false.
	 * 
	 * @param input the string to be validated.
	 * 
	 * @return true if the input is in the valid format, else return false.
	 */
	static boolean validateTime(String input) {
		
		// create a regex pattern for a valid date
		String regex = "\\d{2}:\\d{2}";

		// if the input matches the regex pattern
		if (input.matches(regex)) {

			// test for appropriate values in the date/time
			try {
				int hour	= Integer.parseInt(input.substring(0, 2));
				int minute	= Integer.parseInt(input.substring(3, 5));
				
				// test the hour
				if (hour < 0 || hour > 23) {

					return false;

				}// end if
				
				// test the minute
				if (minute < 0 || minute > 59) {

					return false;

				}// end if

			} catch (Exception e) {

				// if there was an exception during parsing, return false
				return false;

			}// end try/catch

			// If we get here, the date is valid
			return true;

		}// end if

		// If we get here, the date is not valid
		return false;

	}// end validateTime


	/**
	 * This method takes a three letter abbreviation of a day of the week and 
	 * 	verifies if it is valid.
	 * 
	 * @param day is the string to be validated.
	 * 
	 * @return true if the input is a valid day of the week, else return false.
	 */
	protected static boolean validateDay(String day) {

		// create a regex pattern for a valid day of the week
		String regex = "MON|TUE|WED|THU|FRI|SAT|SUN";

		// if the input matches the regex pattern
		if (day.toUpperCase().matches(regex)) {

			// return true
			return true;

		}// end if

		// if the input does not match the regex pattern, return false
		return false;

	}// end validateDay





	/* ********************************
	   ***** Date Related Methods *****
	   ******************************** */
	
	/**
	 * This method takes a string representation of a date and a string 
	 * 	representation of a time and returns a concatenation of the two that 
	 * 	can be used with the SQL toDate() function.
	 * 
	 * @param date is the string representation of a date.
	 * 
	 * @param time is the string representation of a time.
	 * 
	 * @return a concatenation of the date and time strings.
	 */
	protected static String concatDateAndTime(String date, String time) {

		// make sure the date and time are valid
		if (!validateDate(date) || !validateTime(time)) {

			// if either is invalid, return null
			return "0000-00-00 00:00";

		}// end if

		// return the concatenation of the date and time strings
		return date + " " + time;

	}// end concatDateAndTime


	/**
	 * Thid method takes two Course objects and determines the startDate and 
	 * 	endDate to be used for setting a Packages startDate and endDate.
	 * 
	 * @param c1 is the first course.
	 * 
	 * @param c2 is the second course.
	 * 
	 * @return a linked list of strings to be used with the SQL toDate() 
	 * 	function. The startDate string will be at index 0, and endDate will 
	 * 	be at index 1.
	 */
	protected static LinkedList<String> courseToPackageDates(Course c1, Course c2) {
		
		// create a linked list to hold the date strings
		LinkedList<String> dates = new LinkedList<>();

		// check if c1 is null and c2 is not null
		if (c1 == null && c2 != null) {

			// add c2's startDate to the LinkedList at index 0
			dates.add(0, dateToString(c2.startDate));

			// add c2's endDate to the LinkedList at index 1
			dates.add(1, dateToString(c2.endDate));

			// return the dates
			return dates;

		}// end if

		// check if c2 is null and c1 is not null
		if (c2 == null && c1 != null) {

			// add c2's startDate to the LinkedList at index 0
			dates.add(0, dateToString(c1.startDate));

			// add c2's endDate to the LinkedList at index 1
			dates.add(1, dateToString(c1.endDate));

			// return the dates
			return dates;

		}// end if

		// check if c2 is null and c1 is not null
		if (c2 == null && c1 == null) {

			// add c2's startDate to the LinkedList at index 0
			dates.add(null);

			// add c2's endDate to the LinkedList at index 1
			dates.add(null);

			// return the dates
			return dates;

		}// end if

		// if c1 starts before c2
		if (c1.startDate.before(c2.startDate)) {

			// add c1's startDate to the LinkedList at index 0
			dates.add(0, dateToString(c1.startDate));

		} else {

			// add c2's startDate to the LinkedList at index 0
			dates.add(0, dateToString(c2.startDate));

		}// end if

		// if c1 ends after c2
		if (c1.endDate.after(c2.endDate)) {

			// add c1's endDate to the LinkedList at index 1
			dates.add(1, dateToString(c1.endDate));

		} else {

			// add c2's endDate to the LinkedList at index 1
			dates.add(1, dateToString(c2.endDate));

		}// end if

		// return the dates
		return dates;

	}// end courseToPackageDates


	/**
	 * This method takes a date object and returns a string representation for 
	 * 	use with the SQL toDate() function.
	 * The date string format is YYYY-MM-DD HH:MI.
	 * 
	 * @param date is the date object to be converted.
	 * 
	 * @return a string representation of the date object.
	 */
	protected static String dateToString(Date date) {
		
		// create SimpleDateFormat objects for the date and time fields
		SimpleDateFormat yearFormat		= new SimpleDateFormat("yyyy");
		SimpleDateFormat monthFormat	= new SimpleDateFormat("MM");
		SimpleDateFormat dayFormat		= new SimpleDateFormat("dd");
		SimpleDateFormat hourFormat		= new SimpleDateFormat("HH");
		SimpleDateFormat minuteFormat	= new SimpleDateFormat("mm");
		
		// build the string representation of the date object
		String dateString =	yearFormat.format(date) + "-" + 
							monthFormat.format(date) + "-" + 
							dayFormat.format(date) + " " + 
							hourFormat.format(date) + ":" + 
							minuteFormat.format(date);

		// return the string representation of the date object
		return dateString;

	}// end dateToString


	/**
	 * This method takes a date object and returns a three letter abbreviation 
	 * 	string representation of the day of the week.
	 * 
	 * @param date is the date object to be converted.
	 * 
	 * @return a three letter abbreviation string representation of the day.
	 */
	protected static String dateToDOW(Date date) {
		
		// create SimpleDateFormat objects for the date and time fields
		SimpleDateFormat dowFormat	= new SimpleDateFormat("EEE");

		// return the string representation of the date object
		return dowFormat.format(date);

	}// end dateToDOW


	/**
	 * This method converts a date object to a LocalDateTime object.
	 * 
	 * @param date is the date object to be converted.
	 * 
	 * @return a LocalDateTime object.
	 */
	protected static LocalDateTime dateToLocalDateTime(Date date) {
		
		// create a LocalDateTime object
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.ofHours(0));

		// return the LocalDateTime object
		return ldt;

	}// end dateToLocalDateTime

	
	
}// end Validation class

				// int hour = Integer.parseInt(input.substring(11, 13));
				// int minute = Integer.parseInt(input.substring(14, 16));