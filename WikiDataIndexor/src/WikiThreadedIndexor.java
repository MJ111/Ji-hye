import java.util.concurrent.Callable;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import tool.xmlparsetool.WikiData;


public class WikiThreadedIndexor implements Callable<IndexedData>{
	private WikiData wikiData;
	private Komoran komoran;
	
	
	public WikiThreadedIndexor(WikiData wikidata) {
		super();
		this.wikiData = wikidata;
		komoran = new Komoran("./resources/models-light");
	}
	

	@Override
	public IndexedData call() throws Exception {
		long id;
		
		try {
			id = Long.parseLong(wikiData.getDocumentID());
		} catch (NumberFormatException e) {
			return null;
		}
		
		IndexedData indexedData = new IndexedData(id);
		
		komoran.analyze(wikiData.getText());
		
		System.out.println(id);
		
		return indexedData;
	}

}
