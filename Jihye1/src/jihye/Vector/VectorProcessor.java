package jihye.Vector;

import java.util.ArrayList;

import jihye.DB.DatabaseManager;
import jihye.DB.WikipediaPage;
import jihye.NLP.KeywordExtrator;

public class VectorProcessor {
	// ArrayList<String> mDictionary;
	DatabaseManager databaseManager;
	KeywordExtrator keywordExtractor;

	public VectorProcessor(KeywordExtrator keywordExtractor) {
		this.databaseManager = new DatabaseManager();
		this.keywordExtractor = keywordExtractor;
	}

	public TFMap getMaxSimilarityTFMap(TFMap problem, ArrayList<TFMap> choices) {
		double maxSimilarity = -1, similarity;
		TFMap maxSimilarityTFMap = null;

		for (TFMap choice : choices) {
			similarity = problem.getSimilarity(choice);

			if (maxSimilarity < similarity) {
				maxSimilarity = similarity;
				maxSimilarityTFMap = choice;
			}
		}
		return maxSimilarityTFMap;
	}

	public ArrayList<TFMap> getTFMap(String title) {
		ArrayList<TFMap> termFrequencyList = new ArrayList<TFMap>();
		ArrayList<WikipediaPage> pages;
		String redirectTitle = "";
		if (databaseManager.isRedirectedPage(title)) {
			// Redirect 되는 page 의 타이들을 가져와야함.
			redirectTitle = databaseManager.getRedirectedPageTitle(title);
		}

		if (!redirectTitle.equals("")) {
			pages = databaseManager.getPagesFromTitle(redirectTitle);
		} else {
			pages = databaseManager.getPagesFromTitle(title);
		}

		for (WikipediaPage page : pages) {
			ArrayList<String> analyzedDocument = keywordExtractor
					.analyzeDocument(page.getText());
			termFrequencyList.add(new TFMap(title.replaceAll("_", " "),
					analyzedDocument));
		}

		pages.clear();

		return termFrequencyList;
	}
}