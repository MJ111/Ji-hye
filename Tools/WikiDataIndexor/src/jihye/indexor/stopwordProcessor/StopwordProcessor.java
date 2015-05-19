package jihye.indexor.stopwordProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jihye.indexor.util.Utility;

public class StopwordProcessor {
	private File[] indicesFiles;
	public StopwordProcessor(String directoryPath) {		
		indicesFiles = Utility.getInstance().getFiles(directoryPath, "jhidx");
		System.out.println(getAverageIDF());
	}
	
	public float getAverageIDF() {
		float sum = 0.0f;
		int terms = 0;
		
		try {
			for(File f : indicesFiles) {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				while(br.ready()) {
					String str = br.readLine();
					String[] postings = str.split(",");
					terms++;
					sum += Float.parseFloat(postings[1]);
				}
				br.close();
				fr.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (sum/terms);
	}
	

	public void deleteWords() {		
		for(File f : indicesFiles) {
			try {
				FileReader fr = new FileReader(f);
				FileWriter fw = new FileWriter(f+"d");
				BufferedReader br = new BufferedReader(fr);
				BufferedWriter bw = new BufferedWriter(fw);

				String string = null;
				while((string = br.readLine())!=null) {
					string = br.readLine();

					String []term = string.split(",");
					float idf = Float.parseFloat(term[1]);
					System.out.println(term[0] + " : " + idf);
					if(term[0].compareTo("가") >= 0 ){ 
						System.out.println(string);
						bw.write(string);
						bw.newLine();
					}
					else if(term[0].length()<8 &&Integer.parseInt(term[0]) < 10000){
						System.out.println(string);
						bw.write(string);
						bw.newLine();
					}
				}
				bw.close();
				br.close();
				fw.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
