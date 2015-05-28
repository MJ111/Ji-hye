package jihye.PS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jihye.DB.DatabaseManager;
import jihye.NLP.KeywordExtractor;
import jihye.Vector.Dictionary;
import jihye.Vector.SimilarityResult;
import jihye.Vector.SparseVector;
import jihye.Vector.TermFrequencyMap;
import jihye.Vector.VectorProcessor;
import jihye.classification.ClassifiedDocIdsLoader;
import jihye.classification.QueryClassifier;
import jihye.classification.QueryClassifier.ClassTag;

public class ProblemSolver {
	private VectorProcessor vectorProcessor;
	private KeywordExtractor keywordExtractor;
	private IndexProcessor indexProcessor;
	private DatabaseManager databaseManager;
	private QueryClassifier queryClassifier;
	private ClassifiedDocIdsLoader classifiedDocIdsLoader;
	public TermFrequencyMap problemTF;
	public ArrayList<TermFrequencyMap> maxSimilarityChoiceTFList;

	public ProblemSolver() {
		keywordExtractor = new KeywordExtractor();
		databaseManager = new DatabaseManager();
		vectorProcessor = new VectorProcessor(keywordExtractor, databaseManager);
		queryClassifier = new QueryClassifier(keywordExtractor);
		classifiedDocIdsLoader = new ClassifiedDocIdsLoader();
		try {
			indexProcessor = new IndexProcessor("D:/WikiData/DeletedIndex.jhidxd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultData solve(ProblemData problemData) {
		ArrayList<String> problemMorph = keywordExtractor.analyzeDocument(problemData.getProblem());
		if(problemMorph == null || problemMorph.size() == 0) {
			return null;
		}
		analyzeProblem(problemData, problemMorph);
		
		ResultData resultData = new ResultData(problemTF.toString());
		
		if(!problemData.hasChoice()) {
			return getAnswerForNoChoiceProblem(resultData, problemMorph, problemData);
		} else {
			maxSimilarityChoiceTFList = new ArrayList<TermFrequencyMap>();
			return getAnswerForChoiceProblem(problemData, resultData, false);
		}
	}
	
	private void analyzeProblem(ProblemData problemData, ArrayList<String> problemMorph) {
		ClassTag classTag = queryClassifier.classifyQuery(keywordExtractor.getLastNNG());
		if (classTag != null) {
			problemData.setClassTag(classTag);
		}
		
		problemTF = new TermFrequencyMap("%problem%", problemMorph);
		System.out.println(problemTF.toString());
	}
	
	private ResultData getAnswerForNoChoiceProblem(ResultData resultData, ArrayList<String> problemMorph, ProblemData problemData) {
		List<ExtractedDocument> postings;
		postings = indexProcessor.getDocumentsFromIndices(problemMorph, 0.9f);
		indexProcessor.comparePostingsWithProblem(postings, problemMorph);

		weightingToMatchedClassDocuments(postings, problemData);
		
		for(ExtractedDocument ed : postings) {
			String title = databaseManager.getPageTitleFromPageID(ed.getDocumentID());
			resultData.add(title,(double)ed.getSimilarityWithProblem());
		}
		postings.clear();
		
		return resultData;
	}
	
	private void weightingToMatchedClassDocuments(List<ExtractedDocument> postings, ProblemData problemData) {
		ClassTag classTag = problemData.getClassTag();
		if (classTag == null) {
			return;
		}
		
		ArrayList<Integer> docIds = classifiedDocIdsLoader.getMatchingClassDocIds(classTag);
		
		for (ExtractedDocument extractedDocument : postings) {
			if (docIds.contains(new Integer(extractedDocument.getDocumentID()))) {
				System.out.println("Matched class document : " + databaseManager.getPageTitleFromPageID(extractedDocument.getDocumentID()));
				extractedDocument.weightingToSimilarity();
			}
		}
	}
	
	private ResultData getAnswerForChoiceProblem(ProblemData problemData, ResultData resultData, Boolean noChoices) {
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
		Dictionary dictionary = new Dictionary();
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