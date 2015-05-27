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
		BufferedReader br = new BufferedReader(new FileReader("./resources/problems.txt"));
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
	        int count = 0;
	        for(int index = 0; index < fileString.size(); index+=3) {
	        	String problem = fileString.get(index);
	        	if (!problem.contains("누구입니까?")) {
	        		continue;
	        	}
	        	int rightAnswer = 0;
	        	try {
	        		rightAnswer = Integer.parseInt(fileString.get(index+2));
	        	} catch (NumberFormatException e) {
	        		e.printStackTrace();
	        		continue;
	        	}	        	
	        	
	        	ResultData resultData = problemSolver.solve(new ProblemData(fileString.get(index)));
	        	String[] choices = fileString.get(index+1).split(",");
	        	String rightAnswerString = choices[rightAnswer-1];
	        	String jihyeAnswer  = resultData.getAnswerString();
	        	if(rightAnswerString.equals(jihyeAnswer)) {
	        		rightAnswerNum++;
	        	}else {     		
	        		out.write(problemSolver.problemTF.toString());
	        		out.newLine();
	        		
	        		for(Iterator<Triple<String, Double, String>> it = resultData.getIterator(); it != null && it.hasNext();) {
	        			Triple<String, Double, String> datum = it.next();
	        			out.write(datum.getLeft() + " : " + datum.getMiddle());
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
			
			ArrayList<String> fileString = noChoiceTestProblemsSolver.readFile();
			noChoiceTestProblemsSolver.solveProblems(fileString);
			
			long endTime = System.currentTimeMillis();

			System.out.println( "실행 시간 : " + ( endTime - startTime )/1000.0 );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
