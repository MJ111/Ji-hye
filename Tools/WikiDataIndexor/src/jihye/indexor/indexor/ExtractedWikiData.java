package jihye.indexor.indexor;
import java.util.ArrayList;
import java.util.SortedSet;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;

public class ExtractedWikiData extends ArrayList<String> {
	private ArrayList<Integer> wordCount;
	private static final long serialVersionUID = 1L;
	private long documentID;
	private Komoran komoran;
	
	public ExtractedWikiData(long documentID, Komoran komoran) {
		super();
		wordCount = new ArrayList<Integer>();
		this.documentID = documentID;
		this.komoran = komoran;
	}
	
	public @Override boolean add(String term) {
		int indexOfTerm = indexOf(term);
		
		if(indexOfTerm == -1) {
			this.add(term);
			return wordCount.add(indexOfTerm);
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
		float[] ret = new float[this.size()];
		
		for(int i = 0; i < wordCount.size(); i++) {
			if(wordCount.get(i) == 0) {
				ret[i] = 0;
			}else {
				ret[i] = (float) (1 + Math.log(wordCount.get(i)));
			}
		}
		
		return ret;
	}
}
