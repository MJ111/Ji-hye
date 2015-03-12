package jihye.Vector;

import java.util.*;

public class TFMap {
	HashMap<String, Integer> termFrequency;
	private int wordCount = 0;
	private String name = "";

	// 생성자
	public TFMap(String name, ArrayList<String> documentMorph) {
		termFrequency = new HashMap<String, Integer>();
		wordCount = documentMorph.size();
		this.name = name;

		for (String word : documentMorph) {
			add(word);
		}
	}

	public TFMap(String name) {
		termFrequency = new HashMap<String, Integer>();
		wordCount = 0;
		this.name = name;
	}

	public String toString() {
		String ret = "";

		Iterator<String> it = termFrequency.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			ret += key + " ";
		}
		return ret;
	}

	public double getTF(String word) {
		return termFrequency.get(word) / (double) wordCount;
	}

	public double getSimilarity(TFMap target) {
		int count = 0;
		int matchedCount = 0;
		Iterator<String> it = termFrequency.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			count++;

			if (target.containKey(key))
				matchedCount++;
		}

		if (count == 0)
			return 0;
		else
			return (double) matchedCount / (double) count;
	}

	// 추가 메소드
	private void add(String word) {
		if (termFrequency.containsKey(word)) {
			int frequency = termFrequency.get(word);
			termFrequency.put(word, frequency + 1);
		} else {
			termFrequency.put(word, 1);
		}
	}

	public Iterator<String> getIterator() {
		return termFrequency.keySet().iterator();
	}

	public String getName() {
		return name;
	}

	public int getWordCount() {
		return wordCount;
	}

	public boolean containKey(String key) {
		return termFrequency.containsKey(key);
	}
}