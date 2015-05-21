package jihye.PS;

import java.util.HashMap;

public class ExtractedDocument {
	private int documentID;
	private HashMap<String, Float> documentVector;
	
	public ExtractedDocument(int documentID) {
		this.documentID = documentID;
		documentVector = new HashMap<String, Float>();
	}
	
	public void add(String term , float tfidf) {
		documentVector.put(term, tfidf);
	}
	
	public void cleaer() {
		documentVector.clear();
	}
	
	
}
