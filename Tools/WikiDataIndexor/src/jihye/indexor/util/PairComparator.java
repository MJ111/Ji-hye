package jihye.indexor.util;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair<Integer, Float>> {

	@Override
	public int compare(Pair<Integer, Float> o1, Pair<Integer, Float> o2) {
		return Integer.compare(o1.getFirst(), o2.getFirst());
	}	
}
