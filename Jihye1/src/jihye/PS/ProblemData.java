package jihye.PS;

import java.util.Arrays;
import java.util.List;

public class ProblemData {
	public String problem;
	public List<String> choices;

	public ProblemData(String problem, List<String> choices) {
		this.problem = problem;
		this.choices = choices;
	}

	public ProblemData(String problem, String choice1, String choice2, String choice3,
			String choice4) {
		this.problem = problem;
		String c1 = choice1.trim().replaceAll(" ", "_");
		String c2 = choice2.trim().replaceAll(" ", "_");
		String c3 = choice3.trim().replaceAll(" ", "_");
		String c4 = choice4.trim().replaceAll(" ", "_");

		this.choices = Arrays.asList(c1, c2, c3, c4);
	}
}