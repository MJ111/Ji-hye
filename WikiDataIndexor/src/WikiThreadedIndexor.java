import java.util.concurrent.Callable;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import tool.xmlparsetool.WikiData;


public class WikiThreadedIndexor implements Callable<IndexedData>{
	private WikiData wikiData;
	private Komoran komoran;
	
	
	public WikiThreadedIndexor(WikiData wikidata, Komoran komoran) {
		super();
		this.wikiData = wikidata;
		this.komoran = komoran;
	}
	

	@Override
	public IndexedData call() throws Exception {
		long id;
		
		try {
			id = Long.parseLong(wikiData.getDocumentID());
		} catch (NumberFormatException e) {
			return null;
		}
		
		IndexedData indexedData = new IndexedData(id, komoran);
		
		komoran.analyze(wikiData.getText());
		
		return indexedData;
	}

}
