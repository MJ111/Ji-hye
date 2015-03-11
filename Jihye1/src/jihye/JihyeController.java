package jihye;

import jihye.PS.ProblemData;
import jihye.PS.ProblemSolver;
import jihye.PS.ResultData;

public class JihyeController {
	ProblemSolver mProblemSolver;

	public JihyeController() {
		mProblemSolver = new ProblemSolver();
	}

	public ResultData solve(ProblemData problemData) {
		return mProblemSolver.solve(problemData);
	}
}