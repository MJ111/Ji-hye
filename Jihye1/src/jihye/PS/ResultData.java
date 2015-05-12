package jihye.PS;

import java.util.ArrayList;

import jihye.Vector.SimilarityResult;

public class ResultData {

	public ArrayList<Double> similiarty;
	public ArrayList<String> choices;
	public String analyzedProblem;
	public ArrayList<String> matchKeywords;

	public ResultData(String analyzedProblem) {

		this.analyzedProblem = analyzedProblem;
		similiarty = new ArrayList<Double>();
		choices = new ArrayList<String>();
		matchKeywords = new ArrayList<String>();
	}

	public void add(String choice, SimilarityResult result) {
		choices.add(choice);
		similiarty.add(result.similarity);
		matchKeywords.add(result.matchedKeyword);
	}
	
	public void add(String choice, Double similarity) {
		choices.add(choice);
		similiarty.add(similarity);
	}
	
	public void clear() {
		choices.clear();
		similiarty.clear();
		matchKeywords.clear();
	}

	public int getAnswer() {
		if (similiarty.size() == 0) {
			return 1;
		}
		
		double max = -2.0f;
		int answer = 0;
		for (int i = 0; i < 4; i++) {
			if (similiarty.get(i) > max) {
				max = similiarty.get(i);
				answer = i;
			}
		}
		return answer + 1;
	}
}