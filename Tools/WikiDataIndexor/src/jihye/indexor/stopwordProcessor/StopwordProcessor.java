package jihye.indexor.stopwordProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

import jihye.indexor.util.Utility;

public class StopwordProcessor {
	private File[] indicesFiles;
	float averageIDF;
	public StopwordProcessor(String directoryPath) {		
		indicesFiles = Utility.getInstance().getFiles(directoryPath, "jhidx");
		averageIDF = getAverageIDF();
		Utility.getInstance().log(this, "IDF AVG : " + averageIDF);
		this.deleteWords();
	}
	
	public float getAverageIDF() {
		float sum = 0.0f;
		int terms = 0;
		
		try {
			for(File f : indicesFiles) {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
								
				while(br.ready()) {
					String read = br.readLine();
					String[] postings = read.split(",");				
					
					float idf = Float.parseFloat(postings[1]);
					if(idf < 100 ){
						terms++;
						sum += idf;
					}
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
					if(term[0].compareTo("가") >= 0 ){
						bw.write(string);
						bw.newLine();
					}
					else if(term[0].length()<8 &&Integer.parseInt(term[0]) < 10000){
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
