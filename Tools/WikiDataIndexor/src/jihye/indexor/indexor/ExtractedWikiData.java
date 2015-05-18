package jihye.indexor.indexor;
import java.util.ArrayList;
import java.util.SortedSet;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;

public class ExtractedWikiData {
	private ArrayList<Integer> wordCount;
	private ArrayList<String> terms;
	private long documentID;
	private Komoran komoran;
	
	public ExtractedWikiData(long documentID, Komoran komoran) {
		this.wordCount = new ArrayList<Integer>();
		this.terms = new ArrayList<String>();
		this.documentID = documentID;
		this.komoran = komoran;
	}
	
	
	public boolean add(String term) {
		int indexOfTerm = terms.indexOf(term);
		
		if(indexOfTerm == -1) {
			terms.add(term);
			return wordCount.add(1);
		}else {
			int count = wordCount.get(indexOfTerm);
			wordCount.set(indexOfTerm, count+1);
			return true;
		}
	}
	
	public long getDocumentID() {
		return documentID;
	}
	
	public Komoran getKomoran() {
		return komoran;
	}
	
	public float[] getTermFrequency() {
		float[] ret = new float[terms.size()];
		
		for(int i = 0; i < wordCount.size(); i++) {
			if(wordCount.get(i) == 0) {
				ret[i] = 0;
			}else {
				ret[i] = (float) (1 + Math.log(wordCount.get(i)));
			}
		}
		
		return ret;
	}
	
	public int size() {
		return terms.size();
	}
	
	public String get(int index) {
		return terms.get(index);
	}
	
	public void clear() {
		wordCount.clear();
		terms.clear();
	}
}
