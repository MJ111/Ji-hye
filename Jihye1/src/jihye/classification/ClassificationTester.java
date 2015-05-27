package jihye.classification;

import jihye.NLP.KeywordExtractor;

public class ClassificationTester {
	public static void main(String[] args) {
		QueryClassifier queryClassification = new QueryClassifier(new KeywordExtractor());
		queryClassification.classifyAllQueryData();
	}
}
