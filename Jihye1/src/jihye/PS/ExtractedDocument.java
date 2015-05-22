package jihye.PS;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ExtractedDocument implements Comparator<ExtractedDocument> , Comparable<ExtractedDocument>{
	private int documentID;
	private HashMap<String, Float> documentVector;
	private boolean normalized;
	private float similarity;
	
	public ExtractedDocument(int documentID) {
		this.documentID = documentID;
		this.normalized = false;
		documentVector = new HashMap<String, Float>();
	}
	
	public int getDocumentID() {
		return this.documentID;
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
	
	public boolean isNormalized() { 
		return this.normalized;
	}
	
	private void normalize() {
		Set<String> keys = documentVector.keySet();
		float squareSum = 0.0f;
		for(String key : keys) {
			squareSum += Math.pow(documentVector.get(key), 2);
		}
		
		float length = (float) Math.sqrt(squareSum);
		
		for(String key : keys) {
			documentVector.put(key, documentVector.get(key) / length);			
		}		
		this.normalized = true;
	}
	
	public boolean contains(String key) {
		return documentVector.containsKey(key);
	}
	
	public float innerProduct(ExtractedDocument op) {
		if(!normalized) this.normalize();
		if(!op.normalized) op.normalize();
		Set<String> keys = documentVector.keySet();
		float val = 0.0f;
		for(String key : keys) {
			if(op.contains(key)) {
				val += (documentVector.get(key) * op.get(key));
			}
		}		
		similarity = val;
		return val;
	}
	
	public float getSimilarityWithProblem() {
		return this.similarity;
	}

	@Override
	public int compare(ExtractedDocument o1, ExtractedDocument o2) {
		return Float.compare(o1.getSimilarityWithProblem(), o2.getSimilarityWithProblem());
	}

	@Override
	public int compareTo(ExtractedDocument o) {
		return Float.compare(this.getSimilarityWithProblem(), o.getSimilarityWithProblem());
	}
}
