package jihye.PS;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
	
	public float get(String term) {
		if(documentVector.containsKey(term)) {
			return documentVector.get(term);
		}else {
			return 0.0f;
		}
	}
	
	public void normalize() {
		Set<String> keys = documentVector.keySet();
		float squareSum = 0.0f;
		for(String key : keys) {
			squareSum += Math.pow(documentVector.get(key), 2);
		}
		
		float length = (float) Math.sqrt(squareSum);
		
		for(String key : keys) {
			documentVector.put(key, documentVector.get(key) / length);			
		}
	}
	
	public float innerProduct(ExtractedDocument op) {
		return 0.0f;
	}
}
