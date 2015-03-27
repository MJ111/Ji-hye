import java.util.List;
import java.util.concurrent.Callable;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;
import tool.xmlparsetool.WikiData;


public class WikiThreadedIndexor implements Callable<ExtractedWikiData>{
	private WikiData wikiData;
	private Komoran komoran;
	
	
	public WikiThreadedIndexor(WikiData wikidata, Komoran komoran) {
		super();
		this.wikiData = wikidata;
		this.komoran = komoran;
	}
	

	@Override
	public ExtractedWikiData call() throws Exception {
		long id;
		
		try {
			id = Long.parseLong(wikiData.getDocumentID());
		} catch (NumberFormatException e) {
			return null;
		}
		
		ExtractedWikiData extractedData = new ExtractedWikiData(id, komoran);
		
		List<List<Pair<String, String>>> result = komoran.analyze(wikiData.getText());
		for (List<Pair<String, String>> eojeolResult : result) {
			for (Pair<String, String> wordMorph : eojeolResult) {
					extractedData.add(wordMorph.getFirst());
			}
		}
		
		return extractedData;
	}

}
