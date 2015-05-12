package jihye;

import jihye.PS.ProblemData;
import jihye.PS.ProblemSolver;
import jihye.PS.ResultData;

public class JihyeController {
	ProblemSolver problemSolver;
	String dataPath;

	public JihyeController() {
		problemSolver = new ProblemSolver();
	}

	public ResultData solve(ProblemData problemData) {
		return problemSolver.solve(problemData);
	}
}