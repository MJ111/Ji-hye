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
		//Map<String, ArrayList<Pair<Integer, Float>>> ret = new TreeMap<String, ArrayList<Pair<Integer,Float>>>();
		//Make files
		File[] files = Utility.getInstance().createFiles(dictionaryPath, 1, Utility.FILE_TYPE_INDEX);
		Utility.getInstance().log(this, "Found " + matchingFiles.length + " to be merged");
		Map<String, String> tempRet = new TreeMap<String, String>();
		
		for(File f : matchingFiles) {
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				System.out.println(f);
				
				while(br.ready()) {
					String line = br.readLine();
					String lines[] = line.split(",");
					
					String term = lines[0];
					String postings = "";
					try {
						for(int i = 2; i < lines.length; i=i+2) {
							postings += lines[i] + "," + lines[i+1] + ",";
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(line);
					}
					
					if(tempRet.containsKey(term)) {
						String p = tempRet.get(term);
						p += postings;
						tempRet.put(term, p);
					}else {
						tempRet.put(term, postings);
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
			
			Set<String> dictionary = tempRet.keySet();
			for(String term : dictionary) {
				String postings = tempRet.get(term);
				String post[] = postings.split(",");
				float idf = (float) Math.log(763541/(post.length/2));				
				ArrayList<Pair<Integer, Float>> list = new ArrayList<Pair<Integer,Float>>();
				for(int i = 0; i < post.length; i=i+2) {
					list.add(new Pair<Integer, Float>(Integer.parseInt(post[i]), Float.parseFloat(post[i+1])));
				}
				list.sort(new PairComparator());
				
				bw.write(term+","+idf+",");
				for(Pair<Integer, Float> p : list) {
					bw.write(p.getFirst() +"," + p.getSecond() + ",");
				}
				bw.write("\n");
				list.clear();
			}
			
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
//		try {
//			FileWriter fw = new FileWriter(files[0]);
//			BufferedWriter bw = new BufferedWriter(fw);
//			class Comp implements Comparator<Integer>{
//				public int compare(Integer o1, Integer o2){ //compara 메소드를 오버라이드 
//					return o1 < o2 ? -1 : (o1 == o2 ? 0 : 1); //위의 if 문을 조건삼항 연산자로 대체
//				}
//			}
//			
//			Set<String> keys = ret.keySet();
//			for(String key : keys){
//				bw.write(key +",");
//				ArrayList<Pair<Integer,Float>> postings = ret.get(key);
//				postings.sort(new PairComparator());
//				
//				for(Pair<Integer,Float> posting : postings) {
//					bw.write(posting.getFirst()+ "," + posting.getSecond() + ",");
//				}				
//				bw.write("\n");
//			}			
//			bw.close();
//			fw.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
