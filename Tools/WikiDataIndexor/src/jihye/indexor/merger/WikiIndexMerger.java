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

import jihye.indexor.util.Utility;


public class WikiIndexMerger {
	private File[] matchingFiles;
	private String dictionaryPath;
	public WikiIndexMerger(String dictionaryPath) throws Exception{
		this.dictionaryPath = dictionaryPath;
		matchingFiles = Utility.getInstance().getFiles(dictionaryPath, "jhdex");
	}
	
	public void startMerge(int seperate) {
		merge();
	}
	
	private void merge() {
		Map<String, Integer[]> ret = new TreeMap<String, Integer[]>();
		//Make files
		File[] files = Utility.getInstance().createFiles(dictionaryPath, 1, Utility.FILE_TYPE_INDEX);
		Utility.getInstance().log(this, "Found " + matchingFiles.length + " to be merged");
		
		for(File f : matchingFiles) {
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				
				while(br.ready()) {
					String line = br.readLine();
					String lines[] = line.split(",");
					
					String term = lines[0];
					ArrayList<Integer> postings = new ArrayList<Integer>();
					for(int i = 1; i < lines.length; i++) {
						postings.add(Integer.parseInt(lines[i]));
					}
					
					if(ret.containsKey(term)) {
						Integer a[] = ret.get(term);
						Integer b[] = postings.toArray(new Integer[postings.size()]);
						Integer[] merged = ArrayUtils.addAll(a, b);
						ret.put(term, merged);
						//Arrays.
					}else {
						ret.put(term, postings.toArray(new Integer[postings.size()]));
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
				Integer[] postings = ret.get(key);
				Arrays.sort(postings, new Comp());
				
				for(Integer posting : postings) {
					bw.write(posting.toString() + ",");
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
