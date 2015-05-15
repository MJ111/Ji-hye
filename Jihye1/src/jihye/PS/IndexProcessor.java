package jihye.PS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;

public class IndexProcessor {
	Map<String, int[]> indices = null;
	
	public IndexProcessor(String indexPath) throws Exception{
		loadIndex(indexPath);
	}
	
	private void loadIndex(String indexPath) throws IOException{
		if(indices != null) return;
		indices = new TreeMap<String, int[]>();
		//TODO : Something wrong with this... 파일을 메인에서 받아야할텐데..
		File file = new File(indexPath);
		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);	
		
		while(br.ready()) {
			String line = br.readLine();
			String[] seperated = line.split(",");
			int postingsSize = seperated.length - 1;
			
			String term = seperated[0];
			int[] postings = new int[postingsSize];
			
			for(int i = 0; i < postingsSize; i++) {
				postings[i] = Integer.parseInt(seperated[i+1]);
			}			
			indices.put(term, postings);			
		}		
		br.close();
		isr.close();
		fis.close();
	}
	
	public int[] getPostings(String term) {
		return indices.get(term);
	}
	
	public int[] getMergedPostings(List<String> terms, float rate) {
		List<int[]> postings = new ArrayList<int[]>();		
		
		for(String term : terms) {
			if(indices.containsKey(term))
				postings.add(indices.get(term));
		}
		
		return mergePostings(postings, rate);
	}
	
	public int[] mergePostings(List<int[]> postings, float propotion) {
		int [] merged = null;
		for(int[] arr : postings) {
			merged = ArrayUtils.addAll(merged, arr);
		}
		
		Arrays.sort(merged);	
		
		int counter = 1;
		int maxCounter = 0;
		int last = merged[0];
		
		for(int i = 1; i < merged.length; i++) {
			if(merged[i] == last) {
				counter++;				
			}else {
				last = merged[i];
				counter = 1;
			}
			
			if(counter > maxCounter) {
				maxCounter = counter;
			}
		}	
		
		int documentCount = (int) (maxCounter * propotion);
		System.out.println("Finding : Doc > " + documentCount);
		
		int postingCounter = 0;
		int[] ret = null;
		
		for(int i = 1; i < merged.length; i++) {
			if(merged[i] == last) {
				counter ++;
			} else {
				last = merged[i];
				counter = 1;
			}
			
			if(counter >= documentCount) {
				 postingCounter++;
				ret = ArrayUtils.add(ret, last);
				while(i+1 < merged.length && merged[i+1] == last) {
					i++;
				}
			}
		}
		
		System.out.println("Fount Total Doc : " + postingCounter);
		
		return ret;
	}
}
