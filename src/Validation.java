import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;

import entities.*;



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

		// TODO: check if enrolled in an active package
		// TODO: check if either courses are active 
		// TODO: update courses enrollCount
	














	/* **********************************
	   ***** User Input Validations *****
	   ********************************** */

	/**
	 * This method takes a string and determines if it can be converted 
	 * 	to an integer.
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
			Integer.parseInt(input);
			
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