package jihye.NLP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class KeywordExtrator {
	private List<String> morphTag = Arrays.asList("NNG", "NNP", "NR", "SN", "VV", "VA", "VX", "XR", "NF", "NV", "XPN");
	private List<String> verbTag = Arrays.asList("VV", "VA", "VX");
	private Komoran komoran;
	
	public KeywordExtrator() {
		komoran = new Komoran("./resources/models-light");
	}

	public ArrayList<String> analyzeDocument(String document) {
		ArrayList<String> morphArrayList = new ArrayList<String>();

		List<List<Pair<String, String>>> result = komoran.analyze(document);
		for (List<Pair<String, String>> eojeolResult : result) {
			for (Pair<String, String> wordMorph : eojeolResult) {
				if (morphTag.contains(wordMorph.getSecond())){
					if(verbTag.contains(wordMorph.getSecond())) {
						morphArrayList.add(wordMorph.getFirst() + "다");
					} else {
						morphArrayList.add(wordMorph.getFirst());
					}
				}
			}
		}
		return morphArrayList;
	}
}