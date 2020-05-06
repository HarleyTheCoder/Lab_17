
public class Country {
	private String name;
	private double population;
	
	//Constructor
	public Country(String name, double population) {
		this.name = name;
		this.population = population;
	}
	
	//Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPopulation() {
		return population;
	}

	public void setPopulation(double population) {
		this.population = population;
	}
	
	@Override //toString
	public String toString() {
		return "Countries [name=" + name + ", population=" +
					population + "]";
	}
}
