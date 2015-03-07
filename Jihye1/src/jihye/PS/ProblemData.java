package jihye.PS;

import java.util.Arrays;
import java.util.List;

public class ProblemData
{
	public String problem;
	public List<String> choices; 
	
	
	public ProblemData(String problem, List<String> choices)
	{
		this.problem = problem;
		this.choices = choices;
	}
	public ProblemData(String problem, String _c1, String _c2, String _c3, String _c4 )
	{
		this.problem = problem;
		String c1 = _c1.trim().replaceAll(" ", "_");
		String c2 = _c2.trim().replaceAll(" ", "_");
		String c3 = _c3.trim().replaceAll(" ", "_");
		String c4 = _c4.trim().replaceAll(" ", "_");

		this.choices = Arrays.asList(c1, c2, c3, c4);
	}

}
