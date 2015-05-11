package jihye;

import jihye.DB.IndexManager;
import jihye.PS.ProblemData;
import jihye.PS.ProblemSolver;
import jihye.PS.ResultData;

public class JihyeController {
	ProblemSolver problemSolver;
	String dataPath;
	IndexManager indexManager;

	public JihyeController() {
		problemSolver = new ProblemSolver();
	}
	
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	
	public void loadIndices() {			
		try {
			this.indexManager = new IndexManager(dataPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultData solve(ProblemData problemData) {
		return problemSolver.solve(problemData);
	}
}