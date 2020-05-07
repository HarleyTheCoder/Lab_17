import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

//Country stats taken from: 
//https://www.worldometers.info/world-population/population-by-country/

public class CountriesApp {

	public static void main(String[] args) {
		//New scanner
		Scanner scan = new Scanner(System.in);
		
		//Declare regular variables
		int selection;
		boolean cont = true;
		
		//Create menu options
		String[] menuOptions = {"List countries", "Add country", "Modify country",
								"Delete country", "Quit"};
		
		// Create empty list of countries, set file path
		List <Country> countries = new ArrayList<>();
		String fileName = "countriesFile.txt";
		String dirPath = "C:/Coding/eclipse_workspace/Lab17/src/";
		Path path = Paths.get(dirPath + fileName);
		
		//Create new text file if it doesn't exist
		//This is only in case it gets deleted or something
		if (Files.notExists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				System.out.println("Error: unable to create file.");
				e.printStackTrace();
			}
		}
		
		// Create countries from file, put them in a list
		CountriesApp.createCountriesFromFile(path, countries);
		
		//Welcome message
		System.out.println("Welcome to the Countries Maintenance Application!");
		
		//Start of loop - keep going until user quits
		while (cont) {

			//Show the menu
			System.out.println();
			CountriesApp.showMenu(menuOptions);
			System.out.println();
			
			//Ask user to pick an option
			selection = CountriesApp.selectOption(menuOptions.length + 1, scan);
			
			//Perform action by what the user chose
			switch(selection) {
			case 1: 
				System.out.println();
				CountriesApp.sortCountries(countries, path, scan);
				System.out.println();
				CountriesApp.showCountries(countries);
				break;
			case 2: 
				CountriesApp.addCountry(countries, path, scan);
				break;
			case 3:
				System.out.println();
				CountriesApp.modifyCountries(countries, path, scan);
				break;
			case 4: 
				System.out.println();
				CountriesApp.deleteCountry(countries, path, scan);
				break;
			case 5: 
				scan.close();
				cont = false;
				CountriesApp.quit();
				break;
			default: System.out.println("An error occured.");
				break;
			}
			//(Edit above if menu items are added)
		}
	}
	
	//Show the menu
	public static void showMenu(String[] options) {
		for (int i = 0; i < options.length; i ++) {
			System.out.println(i + 1 + ": " + options[i]);
		}
	}
	
	//Select a menu item
	public static int selectOption(int numOptions, Scanner scan) {
		int option;
		System.out.print("Enter menu number: ");
		option = Validator.getIntInRange(1, numOptions, scan);
		return option;
	}
	
	//Show the list of countries
	public static void showCountries(List<Country> countries) {
		//to format population number
		int counter = 1;
		String pattern1 = "##,###,###,##0";
		DecimalFormat popFormatter = new DecimalFormat(pattern1);
		//Show countries and populations one line at a time
		for (Country country: countries) {
			String population = popFormatter.format(country.getPopulation());
			String s = counter + ". " + country.getName() + " (pop " + population + ")" + 
						"\n\tFertility rate: " + country.getFertRate() +
						"\n\tMedian age: " + country.getMedAge();
			System.out.println(s);
			counter++;
		}
	}
	
	//Sort countries
	public static void sortCountries(List<Country> countries, Path path, Scanner scan) {
		int howSort;
		//Find out how to sort them
		System.out.println("1. By name");
		System.out.println("2. By population");
		System.out.print( "How would you like the list sorted?: ");
		howSort = Validator.getIntInRange(1, 2, scan);
		
		//Sort by user's decision
		//Used Comparator and Comparable
		if (howSort == 1) {
			Collections.sort(countries);
		} else {
			Collections.sort(countries, new SortByPopulation());
		}
		//rewrite file accordingly to keep updated
		CountriesApp.rewriteFile(countries, path);
		
	}
	
	//Add a country to the list and text file
	public static void addCountry(List<Country> countries, Path path, Scanner scan) {
		String name;
		Double population;
		Double fertRate;
		int medAge;
		
		//Get country attributes
		System.out.println();
		System.out.print("Enter country: ");
		name = Validator.getString(scan);
		System.out.print("Enter population: ");
		population = Validator.getFormattedDouble(scan);
		System.out.print("Enter fertility rate: ");
		fertRate = Validator.getDouble(scan);
		System.out.print("Enter the median age: ");
		medAge = Validator.getIntInRange(1, 100, scan);
		
		//Add new country to list and file
		Country country = new Country(name, population, fertRate, medAge);
		countries.add(country);
		CountriesApp.appendFile(country, path);
		System.out.println();
		System.out.println("This country has been saved!");
		
	}
	//Modify a country - two similar methods
	public static void modifyCountries(List<Country> countries, Path path, Scanner scan) {
		System.out.print("Which country do you want to modify: ");
		//Get country
		String input = Validator.getString(scan);
		
		//If the input matches a number next to the countries,
		//modify that country
		if (Validator.isIntInRange(1, countries.size() , input)) {
			int a = Integer.parseInt(input) - 1;
			CountriesApp.modifyCountry(countries.get(a), scan);
			CountriesApp.rewriteFile(countries, path);
			System.out.println();
			System.out.println("Country has been modified!");
		} else { //Or if it matches the name of a country, modify country
			int a = -1;
			boolean found = false;
			for (int i = 0; i < countries.size(); i++) {
				String s = countries.get(i).getName().toLowerCase();
				if (countries.get(i).getName().equalsIgnoreCase(input)) {
					a = i;
					found = true;
				}
			}
			if (found) {
				CountriesApp.modifyCountry(countries.get(a), scan);
				CountriesApp.rewriteFile(countries, path);
				System.out.println();
				System.out.println("Country has been modified!");
			} else { //Or if there's no match, return to the menu
				System.out.println();
				System.out.println("Country could not be found.");
			}
		}		
	}
	
	//this is used inside of the above method. Makes it easier
	public static void modifyCountry(Country country, Scanner scan) {
		int a;
		System.out.println("\nYou chose " + country.getName() + ".");
		System.out.println("1. Name\n2. Population" +
							"\n3. Fertility Rate\n4. Median Age");
		System.out.print("\nWhich of these would you like to modify? (1-4): ");
		
		//get number to modify
		a = Validator.getIntInRange(1, 4, scan);
		System.out.println();
		
		//Modify depending
		switch(a) {
		case 1:
			System.out.print("Enter the name: ");
			String newName = Validator.getString(scan);
			country.setName(newName);
			break;
		case 2:
			System.out.print("Enter the population: ");
			double newPop = Validator.getDouble(scan);
			country.setPopulation(newPop);
			break;
		case 3:
			System.out.print("Enter the fertility rate: ");
			double newFR = Validator.getDouble(scan);
			country.setFertRate(newFR);
			break;
		case 4:
			System.out.print("Enter the median age: ");
			int newMedAge = Validator.getIntInRange(1, 100, scan);
			country.setMedAge(newMedAge);
			break;
		default:
			System.out.println("Error: Something went wrong.");
			break;
		}
	}
	
	
	//Delete a country
	public static void deleteCountry(List<Country> countries, Path path, Scanner scan) {
		System.out.print("Which country do you want to delete?: ");
		//Get country
		String input = Validator.getString(scan);
		
		//If the input matches a number next to the countries,
		//delete that country
		if (Validator.isIntInRange(1, countries.size() , input)) {
			int a = Integer.parseInt(input) - 1;
			countries.remove(a);
			CountriesApp.rewriteFile(countries, path);
			System.out.println();
			System.out.println("Country has been removed!");
		} else { //Or if it matches the name of a country, delete the country
			int a = -1;
			boolean found = false;
			for (int i = 0; i < countries.size(); i++) {
				String s = countries.get(i).getName().toLowerCase();
				if (countries.get(i).getName().equalsIgnoreCase(input)) {
					a = i;
					found = true;
				}
			}
			if (found) {
				countries.remove(a);
				CountriesApp.rewriteFile(countries, path);
				System.out.println();
				System.out.println("Country has been removed!");
			} else { //Or if there's no match, return to the menu
				System.out.println();
				System.out.println("Country could not be found.");
			}
		}		
	}
	
	//Exit program
	public static void quit() {
		System.out.println("\nBye bye!");
		System.exit(0);
	}
	
	
	/*(I'm aware I can use FileHelper, I want to try this way first so I can
	 *get used to writing code like this)*/
	
	
	//Rewrite the file
	public static void rewriteFile(List<Country> countries, Path path) {
		//Prepare list of countries in correct format to write file
		List<String> lines = new ArrayList<>();
		for (Country country: countries) {
			String line = country.getName() + "@" + country.getPopulation() +
					"@" + country.getFertRate() + "@" + country.getMedAge();
			lines.add(line);
		}
		//Rewrite
		try {
			Files.write(path, lines, StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			System.out.println("Error: Unable to rewrite file.");
			e.printStackTrace();
		}
		
	}
	
	//I just realized I was supposed to APPEND to the file,
	//not rewrite it... So I'm keeping both.
	public static void appendFile(Country country, Path path) {
		//Use a list since my notes say you must use a list to write this
		List<String> lines = new ArrayList<>();
		lines.add(country.getName() + "@" + country.getPopulation() +
				country.getFertRate() + "@" + country.getMedAge());
		//Add to file
		try {
			Files.write(path, lines, StandardOpenOption.WRITE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("Error: Unable to add to file.");
			e.printStackTrace();
		}
	}
	
	//Read the text file with countries, create a new list from them
	public static void createCountriesFromFile (Path path, List<Country> countries) {
		//Create arraylist to store the lines
		List<String> lines = new ArrayList<>();
		
		//Put all lines into an array
		try {
			List<String> temp = Files.readAllLines(path);
			lines = temp;
			//Since the list doesn't seem to stay after the try catch ends
			
		} catch (IOException e) {
			System.out.println("Error loading file.");
			e.printStackTrace();
		}
		//Turn each line into a country
		//Put that country into list "countries"
		for (String line: lines) {
			
			String[] holder = line.split("@");
			Country country = new Country(holder[0],
					Double.parseDouble(holder[1]),
					Double.parseDouble(holder[2]),
					Integer.parseInt(holder[3]));

			countries.add(country);
		}
	}
	
	

}
