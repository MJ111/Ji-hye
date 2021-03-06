package jihye.indexor.stopwordProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

import jihye.indexor.util.Utility;

public class StopwordProcessor {
	private File[] indicesFiles;
	private String directory;
	private float avgIDF;
	
	public StopwordProcessor(String directoryPath) {	
		this.directory = directoryPath;
		indicesFiles = Utility.getInstance().getFiles(directoryPath, "jhidx");
		avgIDF = getAverageIDF(indicesFiles);
		Utility.getInstance().log(this, "IDF AVG : " + avgIDF);
		this.deleteWords();
		Utility.getInstance().log(this, "NEW IDF AVG : " + getAverageIDFOfDeletedIndices());
	}
	
	public float getAverageIDF(File[] indicesFiles) {
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
					}else {
						System.out.println(read);
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
	
	public float getAverageIDFOfDeletedIndices() {
		File[] newIndicesFiles = Utility.getInstance().getFiles(directory, "jhidxd");
		float avg = getAverageIDF(newIndicesFiles);
		return avg;
	}
	
	public void deleteWords() {	
		Utility.getInstance().log(this, "Deleting stop words");
		for(File f : indicesFiles) {
			try {
				FileReader fr = new FileReader(f);
				FileWriter fw = new FileWriter(directory +"/DeletedIndex" + ".jhidxd");
				BufferedReader br = new BufferedReader(fr);
				BufferedWriter bw = new BufferedWriter(fw);

				String string = null;
				while((string = br.readLine())!=null) {
					string = br.readLine();
					String []term = string.split(",");
					float idf = Float.parseFloat(term[1]);
					if(idf < avgIDF - 2.9) {
						continue;
					}
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
