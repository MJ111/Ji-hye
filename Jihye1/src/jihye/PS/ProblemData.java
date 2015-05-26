package jihye.PS;

import java.util.Arrays;
import java.util.List;

import jihye.classification.QueryClassifier.ClassTag;

public class ProblemData {
	public String problem;
	public List<String> choices;
	private boolean hasChoices;
	private ClassTag classTag;

	public ProblemData(String problem) {
		this.problem = problem;
		this.choices = null;
		this.hasChoices = false;
	}
	
	public ProblemData(String problem, List<String> choices) {
		this.problem = problem;
		this.choices = choices;
		this.hasChoices = true;
	}

	public ProblemData(String problem, String choice1, String choice2, String choice3,
			String choice4) {		
		this.problem = problem;
		String c1 = choice1.trim().replaceAll(" ", "_");
		String c2 = choice2.trim().replaceAll(" ", "_");
		String c3 = choice3.trim().replaceAll(" ", "_");
		String c4 = choice4.trim().replaceAll(" ", "_");

		this.choices = Arrays.asList(c1, c2, c3, c4);
		this.hasChoices = true;
	}
	
	public void setClassTag(ClassTag classTag) {
		this.classTag = classTag;
	}
	
	public ClassTag getClassTag() {
		return classTag;
	}

	public boolean hasChoice() {
		return this.hasChoices;
	}
}