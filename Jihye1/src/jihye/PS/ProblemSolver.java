package jihye.PS;

import java.util.ArrayList;

import jihye.DB.DatabaseManager;
import jihye.DB.IndexProcessor;
import jihye.NLP.KeywordExtrator;
import jihye.Vector.Dictionary;
import jihye.Vector.SimilarityResult;
import jihye.Vector.SparseVector;
import jihye.Vector.TermFrequencyMap;
import jihye.Vector.VectorProcessor;

public class ProblemSolver {

	VectorProcessor vectorProcessor;
	KeywordExtrator keywordExtractor;
	IndexProcessor indexProcessor;
	DatabaseManager databaseManager;
	public TermFrequencyMap problemTF;
	public ArrayList<TermFrequencyMap> maxSimilarityChoiceTFList;

	public ProblemSolver() {
		keywordExtractor = new KeywordExtrator();
		databaseManager = new DatabaseManager();
		vectorProcessor = new VectorProcessor(keywordExtractor, databaseManager);
		try {
			indexProcessor = new IndexProcessor("D:/WikiData");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultData solve(ProblemData problemData) {
		
		maxSimilarityChoiceTFList = new ArrayList<TermFrequencyMap>();

		// get problem tf
		ArrayList<String> problemMorph = keywordExtractor
				.analyzeDocument(problemData.problem);
		
		if(problemMorph == null || problemMorph.size() == 0)
			return null;
		
		problemTF = new TermFrequencyMap("%problem%", problemMorph);
		System.out.println(problemTF.toString());

		Dictionary dictionary = new Dictionary();
		ResultData resultData = new ResultData(problemTF.toString());
		
		boolean noChoices = false;
		//주관식 문제
		if(!problemData.hasChoice()) {
			//포스팅을 찾아온다.
			noChoices = true;
			int[] postings;
			postings = indexProcessor.getMergedPostings(problemMorph, (int)(problemMorph.size() * 0.7));
			problemData.choices = databaseManager.getPageTitlesFromPageIDs(postings);
		}

		// get choice tf
		for (String choice : problemData.choices) {
			// 보기에 like되는 문서들의 TFMap
			ArrayList<TermFrequencyMap> choiceTFList =  null;
			if(noChoices) {
				choiceTFList = vectorProcessor.getTFMap(choice, true);
			}else {
				choiceTFList = vectorProcessor.getTFMap(choice);	
			}

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
//				for (TermFrequencyMap t : choiceTFList) {
//					System.out.println(choice + " : ");
//					System.out.println(t.toString());
//					System.out.println("\n\n");
//				}
				TermFrequencyMap maxSimilarityChoiceTF = vectorProcessor
						.getMaxSimilarityTFMap(problemTF, choiceTFList);
				maxSimilarityChoiceTFList.add(maxSimilarityChoiceTF);
			}
		}
		
		for (TermFrequencyMap t : maxSimilarityChoiceTFList) {
			System.out.print(t.getName() + " : ");
			System.out.println(t.toString());
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