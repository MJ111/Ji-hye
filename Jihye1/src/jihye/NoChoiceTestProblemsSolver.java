package jihye;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jihye.PS.ProblemData;
import jihye.PS.ProblemSolver;
import jihye.PS.ResultData;

public class NoChoiceTestProblemsSolver {
	ProblemSolver problemSolver;
	
	public NoChoiceTestProblemsSolver() {
		problemSolver = new ProblemSolver();
	}
	
	public String readFile() throws IOException {
		String fileString = new String();
		BufferedReader br = new BufferedReader(new FileReader("./resources/problems.txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        fileString = sb.toString();
	    } finally {
	        br.close();
	    }
	    return fileString;
	}
	
	public void solveProblems(String fileString) {
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter("noChoiceSolveData.txt"));
	
			String[] problems = fileString.split("\r\n");
	        
			int rightAnswerNum = 0;
	        int count = 0;
	        for(int index = 0; index < problems.length; index+=3) {
	        	String problem = problems[index];
	        	if (!problem.contains("누구입니까?")) {
	        		continue;
	        	}
	        	int rightAnswer = 0;
	        	try {
	        		rightAnswer = Integer.parseInt(problems[index+2]);
	        	} catch (NumberFormatException e) {
	        		e.printStackTrace();
	        		continue;
	        	}	        	
	        	
	        	ResultData resultData = problemSolver.solve(new ProblemData(problems[index]));
	        	String[] choices = problems[index+1].split(",");
	        	String rightAnswerString = choices[rightAnswer-1];
	        	String jihyeAnswer = resultData.choices.get(resultData.getAnswer()-1);
	        	if (rightAnswerString.equals(jihyeAnswer)) {
	        		rightAnswerNum++;
	        	} else {     		
	        		out.write(problemSolver.problemTF.toString());
	        		out.newLine();

	        		for (int index1 = 0; index1 < 4; index1++) {
	        			out.write(resultData.choices.get(index1) + ": " + resultData.similiarty.get(index1));
	        			out.newLine();
	        		}
	        		out.write("정답 : " + rightAnswerString + " jihye : " + jihyeAnswer);
	        		out.newLine();
	        	}
	        	count++;
	        }
	        out.write("rightAnswerNum : " + rightAnswerNum);
	        out.newLine();
	        out.write("all : " + count);
	        out.newLine();
		    out.write("percent : " + (double)rightAnswerNum/count);
		    out.newLine();
		      
		    out.close();
		} catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		NoChoiceTestProblemsSolver noChoiceTestProblemsSolver = new NoChoiceTestProblemsSolver();
		
		try {
			long startTime = System.currentTimeMillis();
			
			String fileString = noChoiceTestProblemsSolver.readFile();
			noChoiceTestProblemsSolver.solveProblems(fileString);
			
			long endTime = System.currentTimeMillis();

			System.out.println( "실행 시간 : " + ( endTime - startTime )/1000.0 );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
