import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CountriesApp {

	public static void main(String[] args) {
		//New scanner
		Scanner scan = new Scanner(System.in);
		
		//Declare regular variables
		int selection;
		
		//Create menu options
		String[] menuOptions = {"List countries", "Add a country", "Quit"};
		
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
		
		//Start of loop - keep going until user quits
		while (true) {

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
				CountriesApp.showCountries(countries);
				break;
			case 2: 
				CountriesApp.addCountry(countries, path, scan);;
				break;
			case 3: 
				scan.close();
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
		String pattern = "##,###,###,##0";
		DecimalFormat popFormatter = new DecimalFormat(pattern);
		//Show countries and populations one line at a time
		for (Country country: countries) {
			String population = popFormatter.format(country.getPopulation());
			String s = country.getName() + " (pop " + population + ")";
			System.out.println(s);
		}
	}
	
	//Add a country to the list and text file
	public static void addCountry(List<Country> countries, Path path, Scanner scan) {
		String name;
		Double population;
		
		//Get name and population
		System.out.println();
		System.out.print("Enter country: ");
		name = Validator.getString(scan);
		System.out.print("Enter population: ");
		population = Validator.getFormattedDouble(scan);
		
		//Add new country to list and file
		Country country = new Country(name, population);
		countries.add(country);
		CountriesApp.appendFile(country, path);;
		System.out.println("This country has been saved!");
		
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
			String line = country.getName() + "@" + country.getPopulation();
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
		lines.add(country.getName() + "@" + country.getPopulation());
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
					Double.parseDouble(holder[1]));

			countries.add(country);
		}
	}
	
	

}
