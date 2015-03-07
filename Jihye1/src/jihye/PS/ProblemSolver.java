package jihye.PS;

import java.util.ArrayList;

import jihye.DB.*;
import jihye.NLP.*;
import jihye.Vector.*;

public class ProblemSolver
{
	// ProblemData 는 문제 풀때마다만 받아오면 되기때무에 노필요.

	VectorProcessor mVectorProcessor;
	KeywordExtrator mKeywordExtractor;

	public ProblemSolver()
	{
		mKeywordExtractor = new KeywordExtrator();
		mVectorProcessor = new VectorProcessor(mKeywordExtractor);
		// initialize wordDimension
	}

	public ResultData solve(ProblemData problemData)
	{

		TFMap problemTF;
		ArrayList<TFMap> maxSimilarityChoiceTFList = new ArrayList<TFMap>();

		// get problem tf
		ArrayList<String> problemMorph = mKeywordExtractor
				.analyzeDocument(problemData.problem);
		problemTF = new TFMap("%problem%", problemMorph);
		System.out.println(problemTF.toString());
		
		Dictionary dictionary = new Dictionary();
		ResultData resultData = new ResultData(problemTF.toString());

		// get choice tf
		for (String choice : problemData.choices)
		{
			// 보기에 like되는 문서들의 TFMap
			ArrayList<TFMap> choiceTFList = mVectorProcessor.getTFMap(choice);

			// 일치되는 문서가 없으면 maxSimilarityChoiceTFList에 빈 TFMap 넣음
			if (choiceTFList.size() == 0)
			{
				maxSimilarityChoiceTFList.add(new TFMap(choice));
			}
			// 하나만 일치하면 maxSimilarityChoiceTFList에 일치되는 TFMap 넣음
			else if (choiceTFList.size() == 1)
			{
				maxSimilarityChoiceTFList.add(choiceTFList.get(0));
			}
			// 여러개가 일치하면 가장 일치도가 높은 TFMap을 maxSimilarityChoiceTFList에 넣음.
			else
			{
				
				for(TFMap t : choiceTFList){
					System.out.println(choice+" : ");
					System.out.println(t.toString());
					System.out.println("\n\n");
				}
				TFMap maxSimilarityChoiceTF = mVectorProcessor
						.getMaxSimilarityTFMap(problemTF, choiceTFList);
				maxSimilarityChoiceTFList.add(maxSimilarityChoiceTF);
			}
		}

		// 사전 생성
		dictionary.add(problemTF);
		for (TFMap ctf : maxSimilarityChoiceTFList)
		{
			dictionary.add(ctf);
		}

		// 벡터 생성 및 일치도 검사
		SparseVector problemVector = new SparseVector(dictionary, problemTF);

		for (TFMap ctf : maxSimilarityChoiceTFList)
		{
			if (ctf != null)
			{
				SparseVector choiceVector = new SparseVector(dictionary, ctf);
				SimilarityResult result = problemVector
						.getSimilarity(choiceVector);
				resultData.add(ctf.getName(), result);
			} 
			else
			{
				resultData.add("", new SimilarityResult("", 0));

			}
		}

		return resultData;
	}
}
