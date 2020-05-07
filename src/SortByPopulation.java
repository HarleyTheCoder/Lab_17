import java.util.Comparator;

public class SortByPopulation implements Comparator<Country> {

	@Override
	public int compare(Country a, Country b) {
		if (a.getPopulation() > b.getPopulation()) {
			return -1;
		} else if (a.getPopulation() < b.getPopulation()) {
			return 1;
		} else {
			return 0;
		}
	}

}
