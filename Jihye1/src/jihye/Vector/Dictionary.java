package jihye.Vector;

import java.util.*;

public class Dictionary {

	private LinkedHashMap<String, DictionaryElement> wordMap;

	public Dictionary() {
		wordMap = new LinkedHashMap<String, DictionaryElement>();
	}

	public DictionaryElement get(String key) {
		if (wordMap.containsKey(key))
			return wordMap.get(key);
		else
			return null;
	}

	public void add(TermFrequencyMap termFrequencyMap) {

		if (termFrequencyMap == null)
			return;

		// documentCount++;
		Iterator<String> it = termFrequencyMap.getIterator();

		while (it.hasNext()) {
			String key = it.next();

			// 이미 단어가 존재함
			if (wordMap.containsKey(key)) {
				DictionaryElement orgElement = wordMap.get(key);
				orgElement.documentFrequency++;
				wordMap.put(key, orgElement);
			}
			// 단어가 존재하지 않음.
			else {
				DictionaryElement insertedElement = new DictionaryElement(
						wordMap.size(), 1);
				wordMap.put(key, insertedElement);

			}
		}
	}
}
