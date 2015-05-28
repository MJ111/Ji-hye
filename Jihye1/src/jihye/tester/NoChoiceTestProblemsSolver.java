package jihye.tester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jihye.PS.ProblemData;
import jihye.PS.ProblemSolver;
import jihye.PS.ResultData;
import jihye.PS.Triple;

public class NoChoiceTestProblemsSolver {
	ProblemSolver problemSolver;
	
	public NoChoiceTestProblemsSolver() {
		problemSolver = new ProblemSolver();
	}
	
	public ArrayList<String> readFile() throws IOException {
		ArrayList<String> fileString = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("./resources/short_problems.txt"));
	    try {
	        String line = br.readLine();

	        while (line != null) {	  
	        	fileString.add(line);
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    return fileString;
	}
	
	public void solveProblems(ArrayList<String> fileString) {
		try { 
			BufferedWriter out = new BufferedWriter(new FileWriter("noChoiceSolveData.txt"));
			
			int rightAnswerNum = 0;
	        int problemCount = 0;
	        for(int index = 0; index < fileString.size(); index+=2) {
	        	String problem = fileString.get(index);

	        	ResultData resultData = problemSolver.solve(new ProblemData(problem));
	        	
	        	String rightAnswerString = fileString.get(index+1);
	        	String jihyeAnswer  = resultData.getAnswerString();
	        	
	        	if(rightAnswerString.equals(jihyeAnswer)) {
	        		rightAnswerNum++;
	        	}else {     		
	        		out.write("문제: " + problemSolver.problemTF.toString());
	        		out.newLine();
	        		
	        		for(Iterator<Triple<String, Double, String>> it = resultData.getIterator(); it != null && it.hasNext();) {
	        			Triple<String, Double, String> datum = it.next();
	        			out.write(datum.getLeft() + " : " + datum.getMiddle());
	        			out.newLine();
	        		}
	        		out.write("정답 : " + rightAnswerString + " jihye : " + jihyeAnswer);
	        		out.newLine();
	        	}
	        	problemCount++;
	        }
	        out.write("rightAnswerNum : " + rightAnswerNum);
	        out.newLine();
	        out.write("all : " + problemCount);
	        out.newLine();
		    out.write("percent : " + (double)rightAnswerNum/problemCount);
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
			
			ArrayList<String> fileString = noChoiceTestProblemsSolver.readFile();
			noChoiceTestProblemsSolver.solveProblems(fileString);
			
			long endTime = System.currentTimeMillis();

			System.out.println( "실행 시간 : " + ( endTime - startTime )/1000.0 );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
