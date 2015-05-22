package jihye.PS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import jihye.DB.DatabaseManager;
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
			indexProcessor = new IndexProcessor("D:/WikiData/DeletedIndex.jhidxd");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
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
		if(!problemData.hasChoice()) {
			// 포스팅을 찾아온다.
			noChoices = true;
			List<ExtractedDocument> postings;
			postings = indexProcessor.getDocumentsFromIndices(problemMorph, 0.9f);
			indexProcessor.comparePostingsWithProblem(postings, problemMorph);
			
//			ResultData rd = new ResultData(null);
//			rd.add(choice, similarity);
//			rd.
			//problemData.choices = databaseManager.getPageTitlesFromPageIDs(postings);
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
		
		if (noChoices) {
			newTop4ResultData(resultData);
		}

		return resultData;
	}
	
	private void newTop4ResultData(ResultData resultData) {
		TreeMap<Double, String> resultMap = new TreeMap<Double, String>(new SimComp());
		
		for (int index=0; index<resultData.choices.size(); index++) {
			if (resultData.similiarty.get(index).isNaN()) continue;
		
			resultMap.put(resultData.similiarty.get(index), resultData.choices.get(index));
		}
		
		resultData.clear();
		Set<Double> set = resultMap.keySet();
        Object []resultMapKeys = set.toArray();
        
        int resultDataSize = resultMapKeys.length;
        int choiceSize = resultDataSize < 4 ? resultDataSize : 4;
        
        for(int i = 0; i < choiceSize; i++) {
        	Double key = (Double)resultMapKeys[i];            
            resultData.add((String)resultMap.get(key), key);
        }
	}
}


class SimComp implements Comparator<Double>{
	 
    @Override
    public int compare(Double d1, Double d2) {
        return d1 > d2 ? -1 : d1 == d2 ? 0 : 1;
    }    
}