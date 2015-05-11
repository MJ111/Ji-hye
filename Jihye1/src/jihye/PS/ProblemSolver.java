package jihye.PS;

import java.util.ArrayList;

import jihye.NLP.KeywordExtrator;
import jihye.Vector.*;

public class ProblemSolver {

	VectorProcessor vectorProcessor;
	KeywordExtrator keywordExtractor;

	public ProblemSolver() {
		keywordExtractor = new KeywordExtrator();
		vectorProcessor = new VectorProcessor(keywordExtractor);
	}

	public ResultData solve(ProblemData problemData) {

		TermFrequencyMap problemTF;
		ArrayList<TermFrequencyMap> maxSimilarityChoiceTFList = new ArrayList<TermFrequencyMap>();

		// get problem tf
		ArrayList<String> problemMorph = keywordExtractor
				.analyzeDocument(problemData.problem);
		problemTF = new TermFrequencyMap("%problem%", problemMorph);
		System.out.println(problemTF.toString());

		Dictionary dictionary = new Dictionary();
		ResultData resultData = new ResultData(problemTF.toString());
		
		if(!problemData.hasChoice()) {
			
		}

		// get choice tf
		for (String choice : problemData.choices) {
			// 보기에 like되는 문서들의 TFMap
			ArrayList<TermFrequencyMap> choiceTFList = vectorProcessor.getTFMap(choice);

			// 일치되는 문서가 없으면 maxSimilarityChoiceTFList에 빈 TFMap 넣음
			if (choiceTFList.size() == 0) {
				maxSimilarityChoiceTFList.add(new TermFrequencyMap(choice));
			}
			// 하나만 일치하면 maxSimilarityChoiceTFList에 일치되는 TFMap 넣음
			else if (choiceTFList.size() == 1) {
				maxSimilarityChoiceTFList.add(choiceTFList.get(0));
			}
			// 여러개가 일치하면 가장 일치도가 높은 TFMap을 maxSimilarityChoiceTFList에 넣음.
			else {

				for (TermFrequencyMap t : choiceTFList) {
					System.out.println(choice + " : ");
					System.out.println(t.toString());
					System.out.println("\n\n");
				}
				TermFrequencyMap maxSimilarityChoiceTF = vectorProcessor
						.getMaxSimilarityTFMap(problemTF, choiceTFList);
				maxSimilarityChoiceTFList.add(maxSimilarityChoiceTF);
			}
		}

		// 사전 생성
		dictionary.add(problemTF);
		for (TermFrequencyMap ctf : maxSimilarityChoiceTFList) {
			dictionary.add(ctf);
		}

		// 벡터 생성 및 일치도 검사
		SparseVector problemVector = new SparseVector(dictionary, problemTF);

		for (TermFrequencyMap ctf : maxSimilarityChoiceTFList) {
			if (ctf != null) {
				SparseVector choiceVector = new SparseVector(dictionary, ctf);
				SimilarityResult result = problemVector
						.getSimilarity(choiceVector);
				resultData.add(ctf.getName(), result);
			} else {
				resultData.add("", new SimilarityResult("", 0));

			}
		}

		return resultData;
	}
}