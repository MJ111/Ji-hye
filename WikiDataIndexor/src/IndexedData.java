import java.awt.List;
import java.util.HashMap;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;


public class IndexedData extends HashMap<String, Integer> {
	private long documentID;
	private Komoran komoran;
	
	public IndexedData(long documentID, Komoran komoran) {
		super();
		this.documentID = documentID;
		this.komoran = komoran;
	}
	
	public long getDocumentID() {
		return documentID;
	}
	
	public Komoran getKomoran() {
		return komoran;
	}
}
