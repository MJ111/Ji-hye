package jihye.classification;

import jihye.NLP.KeywordExtrator;

public class ClassificationTester {
	public static void main(String[] args) {
		QueryClassification queryClassification = new QueryClassification(new KeywordExtrator());
		queryClassification.classifyAllQueryData();
	}
}
