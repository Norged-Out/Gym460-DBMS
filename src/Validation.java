import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import entities.Equipment;
import entities.Member;


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



	protected static boolean balanceCheck(Member member) {


		// if the member's balance is negative
		if (member.balance < 0) {

			// return false, print balance and do not delete
			return false;

		}// end if

		// TODO: check if enrolled in an active package
		// TODO: check if either courses are active 
		// TODO: update courses enrollCount


		// return false
		return false;

	}// end balanceCheck

	














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
			int number = Integer.parseInt(input);
			
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
			int number = Integer.parseInt(input);

		} catch (Exception e) {

			// if the string is not able to be converted, return false
			return false;

		}// end try/catch

		// if the input was converted, return true
		return true;

	}// end validatePhone


	/**
	 * This method takes a string representation of a date and uses regex to 
	 * 	verify if it is in the format YYYY-MM-DD HH:MI. Returns 
	 * 	true if it is a valid double format, else return false.
	 * 
	 * @param input the string to be validated.
	 * 
	 * @return true if the input is in the valid format, else return false.
	 */
	static boolean validateDate(String input) {
		
		// create a regex pattern for a valid date
		String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";

		// if the input matches the regex pattern
		if (input.matches(regex)) {

			// test for appropriate values in the date/time
			try {
				int year = Integer.parseInt(input.substring(0, 4));
				int month = Integer.parseInt(input.substring(5, 7));
				int day = Integer.parseInt(input.substring(8, 10));
				int hour = Integer.parseInt(input.substring(11, 13));
				int minute = Integer.parseInt(input.substring(14, 16));

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
				
				if (hour < 0 || hour > 23) {

					return false;

				}// end if
				
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

	}// end validateDate


	



	
	
}// end Validation class
