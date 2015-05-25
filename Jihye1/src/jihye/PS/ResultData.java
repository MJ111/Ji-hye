package jihye.PS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.transformation.SortedList;
import jihye.Vector.SimilarityResult;

public class ResultData {
	//First String : choice, Second Double : Similarity, Third String : matchKeywords
	private int answer;
	private List<Triple<String, Double, String>> data;
	private String analyzedProblem;

	public ResultData(String analyzedProblem) {
		//-2 means, answer is not calculated
		this.answer = -2;
		this.analyzedProblem = analyzedProblem;
		this.data = new ArrayList<Triple<String,Double,String>>();
	}

	public void add(String choice, SimilarityResult result) {
		Triple<String, Double, String> datum = new Triple<String, Double, String>(choice, result.similarity, result.matchedKeyword);
		data.add(datum);
	}
	
	public void add(String choice, Double similarity) {
		Triple<String, Double, String> datum = new Triple<String, Double, String>(choice, similarity, null);
		data.add(datum);
	}
	
	public String getMatchKeyword(int index) {
		if(data == null || data.size() <= index) 
			return "";
		else
			return data.get(index).getRight();
	}
	
	public String getAnalyzedProblem() {
		if(analyzedProblem == null) 
			return "";
		else
			return this.analyzedProblem;
	}
	
	public void clear() {
		data.clear();
	}
	
	public double getSimilarity(int index) {
		if(data != null && data.size() < index) {
			return data.get(index).getMiddle();
		}else {
			return 0.0;
		}
	}
	
	public Iterator<Triple<String, Double, String>> getIterator() {
		if(data != null)
			return data.iterator();
		else
			return null;
	}
	
	public int getAnswer() {
		if(answer != -2) return answer;
		else {
			double max = -2.0f;
			if(data != null) {
				for(Iterator<Triple<String, Double, String>> it  = data.iterator(); it != null && it.hasNext();){
					Triple<String, Double, String> datum = it.next();
					if(datum.getMiddle() > max) {
						max = datum.getMiddle();
						answer = data.indexOf(datum) + 1;
					}
				}
			}
		}
		return answer;
	}
	
	public String getAnswerString() {
		if(answer != -2) return data.get(answer-1).getLeft();
		else {
			getAnswer();
			if(answer == -2) {
				return "";
			}else {
				return data.get(answer-1).getLeft();
			}
		}
	}
	
	/** getChoiceString returns string of given choice
	 *  NOTE THAT CHOICE IS NOT INDEX
	 *  I.E. CHOICE = INDEX + 1
	 **/
	public String getChoiceString(int choice) {
		if(data == null || data.size() <= (choice - 1)) {
			return "";
		}else {
			return data.get(choice - 1).getLeft();
		}
	}
}