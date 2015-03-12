package jihye.Vector;

import java.util.*;

public class SparseVector {
	private ArrayList<SparseVectorElement> vector;
	private HashMap<Integer, String> keywordList;
	public String name = "";
	private int numberOfWords;

	public SparseVector(Dictionary dictionary, TermFrequencyMap termFrequencyMap) {
		numberOfWords = termFrequencyMap.getWordCount();
		vector = new ArrayList<SparseVectorElement>();
		keywordList = new HashMap<Integer, String>();
		name = termFrequencyMap.getName();
		Iterator<String> iterator = termFrequencyMap.getIterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			DictionaryElement element = dictionary.get(key);
			keywordList.put(element.index, key);

			double tf = termFrequencyMap.getTF(key);
			double idf = Math.log(5.0f / (double) element.documentFrequency);

			vector.add(new SparseVectorElement(element.index, tf * idf));
		}
		Collections.sort(vector, customCoparator);
	}

	public int size() {
		return vector.size();
	}

	public void clear() {
		vector.clear();
	}

	public SparseVectorElement get(int i) {
		return vector.get(i);
	}

	public Iterator<SparseVectorElement> getIterator() {
		return vector.iterator();
	}

	public double getValuebyDimetion(int d) {
		for (SparseVectorElement e : vector) {
			if (e.dimension > d)
				break;

			if (e.dimension == d)
				return e.value;
		}

		return 0;
	}

	public double getTfbyDimension(int d) {
		return getTfbyDimension(d) / numberOfWords;
	}

	public SimilarityResult getSimilarity(SparseVector target) {
		double similarity = 0;
		String matchedKeyword = "";

		double scalar = 0;
		double norm1 = 0, norm2 = 0;

		boolean increment1 = false, increment2 = false;

		Iterator<SparseVectorElement> iterator1 = this.getIterator();
		Iterator<SparseVectorElement> iterator2 = target.getIterator();
		SparseVectorElement element1 = null;
		SparseVectorElement element2 = null;

		if (iterator1.hasNext())
			element1 = iterator1.next();
		if (iterator2.hasNext())
			element2 = iterator2.next();

		while (iterator1.hasNext() && iterator2.hasNext())// question length
		{
			if (increment1) element1 = iterator1.next();
			if (increment2) element2 = iterator2.next();

			if (element1.dimension < element2.dimension) {
				increment1 = true;
				increment2 = false;
			} else if (element1.dimension == element2.dimension) {
				scalar += element1.value * element2.value;
				matchedKeyword += keywordList.get(element1.dimension) + " ";

				increment1 = true;
				increment2 = true;
			} else {
				increment1 = false;
				increment2 = true;
			}
		}

		norm1 = this.getNorm();
		norm2 = target.getNorm();
		similarity = scalar / Math.sqrt(norm1 * norm2);
		System.out.println(target.name + ": " + similarity);
		return new SimilarityResult(matchedKeyword, similarity);
	}

	public double getNorm() {
		double norm = 0;

		for (SparseVectorElement e : vector) {
			norm += e.value * e.value;
		}
		return norm;
	}

	private final static Comparator<SparseVectorElement> customCoparator = new Comparator<SparseVectorElement>() {
		public int compare(SparseVectorElement e1, SparseVectorElement e2) {
			return e1.dimension - e2.dimension;
		}
	};
}
