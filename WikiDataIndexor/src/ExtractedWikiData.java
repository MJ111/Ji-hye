import java.util.ArrayList;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;


public class ExtractedWikiData extends ArrayList<String> {
	private long documentID;
	private Komoran komoran;
	
	public ExtractedWikiData(long documentID, Komoran komoran) {
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
