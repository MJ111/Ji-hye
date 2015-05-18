package jihye.indexor.merger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;

import jihye.indexor.util.Pair;
import jihye.indexor.util.PairComparator;
import jihye.indexor.util.Utility;


public class WikiIndexMerger {
	private File[] matchingFiles;
	private String dictionaryPath;
	public WikiIndexMerger(String dictionaryPath) throws Exception{
		this.dictionaryPath = dictionaryPath;
		matchingFiles = Utility.getInstance().getFiles(dictionaryPath, "jhdex");
	}
	
	public void startMerge(int seperate) {
		merge(seperate);
	}
	
	private void merge(int seperate) {
		Map<String, ArrayList<Pair<Integer, Float>>> ret = new TreeMap<String, ArrayList<Pair<Integer,Float>>>();
		//Make files
		File[] files = Utility.getInstance().createFiles(dictionaryPath, 1, Utility.FILE_TYPE_INDEX);
		Utility.getInstance().log(this, "Found " + matchingFiles.length + " to be merged");
		int termCounter = 0;
		
		for(File f : matchingFiles) {
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				
				while(br.ready()) {
					String line = br.readLine();
					String lines[] = line.split(",");
					
					String term = lines[0];
					float idf = Float.parseFloat(lines[1]);
					//Pair<Integer,Float>[] postings;
					ArrayList<Pair<Integer,Float>> postings = new ArrayList<Pair<Integer,Float>>();
					for(int i = 2; i < lines.length; i=i+2) {
						try {
							int a = Integer.parseInt(lines[i]);
							float b = Float.parseFloat(lines[i+1]);
							postings.add(new Pair<Integer,Float>(a, b));
						} catch (Exception e) {
							Utility.getInstance().log(this, f.toString());
							Utility.getInstance().log(this, line);
							e.printStackTrace();
						}
					}
					
					if(ret.containsKey(term)) {
						ArrayList<Pair<Integer,Float>> a = ret.get(term);
						a.addAll(postings);
						//Arrays.
					}else {
						ret.put(term, postings);
					}
				}
				br.close();
				fr.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter fw = new FileWriter(files[0]);
			BufferedWriter bw = new BufferedWriter(fw);
			class Comp implements Comparator<Integer>{
				public int compare(Integer o1, Integer o2){ //compara 메소드를 오버라이드 
					return o1 < o2 ? -1 : (o1 == o2 ? 0 : 1); //위의 if 문을 조건삼항 연산자로 대체
				}
			}
			
			Set<String> keys = ret.keySet();
			for(String key : keys){
				bw.write(key +",");
				ArrayList<Pair<Integer,Float>> postings = ret.get(key);
				postings.sort(new PairComparator());
				
				for(Pair<Integer,Float> posting : postings) {
					bw.write(posting.getFirst()+ "," + posting.getSecond() + ",");
				}				
				bw.write("\n");
			}			
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
