import java.util.Scanner;

public class Validator {
	
	//Get integer within a specific range
	public static int getIntInRange(int a, int b, Scanner scan) {
		boolean cont = true;
		while(cont) {
			try {
				int num = Integer.parseInt(scan.nextLine());
				if (num < a || num > b) {
					throw new IndexOutOfBoundsException();
				} else {
					return num;
				}
			} catch (IndexOutOfBoundsException ie) {
				System.out.print("Selection must be between " + a +
						" and " +  b + ". Please try again: ");
			} catch (NumberFormatException e) {
				System.out.print("Input must be a number (" + a + "-" + b + 
									"). Please try again: ");
			}
		}
		return -1;
	}
	
	//check if a string is an integer in correct range
	public static boolean isIntInRange(int a, int b, String input) {
		try {
			int c = Integer.parseInt(input);
			if (c >= a && c <= b) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Get any valid double.
	 */
	public static double getDouble(Scanner scnr) {
		// This approach use "hasNext" look ahead.
		boolean isValid = false;
		do {
			isValid = scnr.hasNextDouble();
			if (!isValid) {
				scnr.nextLine();
				System.out.print("Invalid entry.\nPlease enter a number with no commas: ");
			}
		} while (!isValid);
		double number = scnr.nextDouble();
		scnr.nextLine();
		return number;
	}
	
	public static double getFormattedDouble(Scanner scan) {
		while (true) {
			double number = Validator.getDouble(scan);
			if (number < 0) {
				System.out.print("Invalid entry.\nPlease enter a positive number: ");
			} else if (number % 1 != 0) {
				System.out.print("Invalid entry. \nPlease enter a whole number: ");
			} else {
				return number;
			}
		}
		
	}
	
	/**
	 * Get any string.
	 */
	//I edited this
	public static String getString(Scanner scnr) {
		// This approach uses exception handling.
		while (true) {
			try {
				if (scnr.hasNext()) {
					return scnr.nextLine();
				} else {
					System.out.print("Input is empty. Please try again: ");
				}
				
			} catch (Exception e) {
				System.out.print("Invalid string. Please try again: ");
			}
		}
	}
	
}
