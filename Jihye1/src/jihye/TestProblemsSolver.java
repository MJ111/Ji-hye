package jihye;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jihye.PS.ProblemData;
import jihye.PS.ProblemSolver;
import jihye.PS.ResultData;
import jihye.Vector.TermFrequencyMap;

public class TestProblemsSolver {
	ProblemSolver problemSolver;
	
	public TestProblemsSolver() {
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
			BufferedWriter out = new BufferedWriter(new FileWriter("solveData.txt"));
	
			String[] problemsString = fileString.split("\n");
	        
			int rightAnswerNum = 0;
	        int count = 0;
	        int noData = 0;
	        for(int index = 0; index < problemsString.length; index+=3) {
	        	String problem = problemsString[index];
	        	if (!problem.contains("누구입니까?")) {
	        		continue;
	        	}
	        	int rightAnswer = 0;
	        	try {
	        		rightAnswer = Integer.parseInt(problemsString[index+2]);
	        	} catch (NumberFormatException e) {
	        		e.printStackTrace();
	        		continue;
	        	}
	        	if (rightAnswer > 4) {
	        		continue;
	        	}	        	
	        	
	        	String[] choices = problemsString[index+1].split(",");
	        	ProblemData problemData = new ProblemData(problemsString[index], choices[0], choices[1], choices[2], choices[3]);
	        	ResultData resultData = problemSolver.solve(problemData);
	        	int resultAnswer = resultData.getAnswer();
	        	if (rightAnswer == resultAnswer) {
	        		rightAnswerNum++;
//	        		System.out.println("right answer");
	        	} else {
	        		if (resultData.similiarty.get(rightAnswer-1).isNaN()) {
	        			noData++;
	        		}
	        		
	        		out.write(problemSolver.problemTF.toString());
	        		out.newLine();
	        		for (TermFrequencyMap termFrequencyMap : problemSolver.maxSimilarityChoiceTFList) {
	        			out.write(termFrequencyMap.getName() + ": " + termFrequencyMap.toString());
	        			out.newLine();
	        		}

	        		for (int index1 = 0; index1 < 4; index1++) {
	        			out.write(resultData.choices.get(index1) + ": " + resultData.similiarty.get(index1));
	        			out.newLine();
	        		}
	        		out.write("wrong answer - rightAnswer : " + rightAnswer + " jihye : " + resultAnswer);
	        		out.newLine();
	        	}
	        	count++;
	        }
	        out.write("noData : " + noData); 
	        out.newLine();
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
		TestProblemsSolver testProblemsSolver = new TestProblemsSolver();
		
		try {

			long startTime = System.currentTimeMillis();
			
			String fileString = testProblemsSolver.readFile();
			testProblemsSolver.solveProblems(fileString);
			
			long endTime = System.currentTimeMillis();

			System.out.println( "실행 시간 : " + ( endTime - startTime )/1000.0 );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
