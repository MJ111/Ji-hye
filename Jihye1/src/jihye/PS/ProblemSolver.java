package jihye.PS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import jihye.DB.DatabaseManager;
import jihye.NLP.KeywordExtractor;
import jihye.Vector.Dictionary;
import jihye.Vector.SimilarityResult;
import jihye.Vector.SparseVector;
import jihye.Vector.TermFrequencyMap;
import jihye.Vector.VectorProcessor;
import jihye.classification.QueryClassifier;
import jihye.classification.QueryClassifier.ClassTag;

public class ProblemSolver {
	private VectorProcessor vectorProcessor;
	private KeywordExtractor keywordExtractor;
	private IndexProcessor indexProcessor;
	private DatabaseManager databaseManager;
	private QueryClassifier queryClassifier;
	public TermFrequencyMap problemTF;
	public ArrayList<TermFrequencyMap> maxSimilarityChoiceTFList;

	public ProblemSolver() {
		keywordExtractor = new KeywordExtractor();
		databaseManager = new DatabaseManager();
		vectorProcessor = new VectorProcessor(keywordExtractor, databaseManager);
		queryClassifier = new QueryClassifier(keywordExtractor);
		try {
			indexProcessor = new IndexProcessor("D:/WikiData/DeletedIndex.jhidxd");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public ResultData solve(ProblemData problemData) {
		maxSimilarityChoiceTFList = new ArrayList<TermFrequencyMap>();

		ArrayList<String> problemMorph = keywordExtractor
				.analyzeDocument(problemData.getProblem());
		
		if(problemMorph == null || problemMorph.size() == 0) {
			return null;
		}

//		ClassTag classTag = queryClassifier.classifyQuery(keywordExtractor.getLastNNG());
//		if (classTag != null) {
//			problemData.setClassTag(classTag);
//		}
		
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
			
			ResultData rd = new ResultData(null);
			for(ExtractedDocument ed : postings) {
				String title = databaseManager.getPageTitleFromPageID(ed.getDocumentID());
				rd.add(title,(double)ed.getSimilarityWithProblem());
			}
			postings.clear();
			return rd;
		}

		// get choice tf
		for(Iterator<String> it = problemData.getChoiceIterator(); it != null && it.hasNext();) {
			String choice = it.next();
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
			getTop4Answers(resultData);
		}

		return resultData;
	}
	
	private void getTop4Answers(ResultData resultData) {
		TreeMap<Double, String> resultMap = new TreeMap<Double, String>(new SimComp());
		
		for(Iterator<Triple<String, Double, String>> it = resultData.getIterator(); it != null && it.hasNext();) {
			Triple<String, Double, String> val = it.next();
			resultMap.put(val.getMiddle(), val.getLeft());
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