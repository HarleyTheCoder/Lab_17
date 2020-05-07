
public class Country implements Comparable<Country>{
	private String name;
	private double population;
	private double fertRate;
	private int medAge;
	
	//Constructor
	public Country(String name, double population,
					double fertRate, int medAge) {
		this.name = name;
		this.population = population;
		this.fertRate = fertRate;
		this.medAge = medAge;
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
	
	public double getFertRate() {
		return fertRate;
	}

	public void setFertRate(double fertRate) {
		this.fertRate = fertRate;
	}

	public int getMedAge() {
		return medAge;
	}

	public void setMedAge(int medAge) {
		this.medAge = medAge;
	}

	@Override //toString
	public String toString() {
		return "Countries [name=" + name + ", population=" +
					population + ", fertRate=" + fertRate +
					", medAge=" + medAge + "]";
	}

	@Override
	public int compareTo(Country o) {
		return this.name.compareTo(o.getName());
	}
}
