package jihye.PS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;


public class IndexProcessor {
	Map<String, Triple<Float, int[], float[]>> indices = null;
	
	public IndexProcessor(String indexPath) throws Exception{
		loadIndex(indexPath);
	}
	
	private void loadIndex(String indexPath) throws IOException{
		if(indices != null) return;
		indices = new TreeMap<String, Triple<Float, int[], float[]>>();
		//TODO : Something wrong with this... 파일을 메인에서 받아야할텐데..
		File file = new File(indexPath);
		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		
		while(br.ready()) {
			String line = br.readLine();
			String[] seperated = line.split(",");
			int postingsSize = (seperated.length - 1)/2;
			
			String term = seperated[0];
			float idf = Float.parseFloat(seperated[1]);
			int[] postings = new int[postingsSize+1];
			float[] tfidfs = new float[postingsSize+1];
			
			for(int i = 2; i < seperated.length; i=i+2) {
				postings[i/2] = Integer.parseInt(seperated[i]);
				tfidfs[i/2] = Float.parseFloat(seperated[i+1]);
			}			
			
			Triple<Float, int[], float[]> posting = new Triple<Float, int[], float[]>(idf, postings, tfidfs);
			indices.put(term, posting);			
		}		
		br.close();
		isr.close();
		fis.close();
	}
	
	public List<Pair<Integer, Float>> getMergedPostings(List<String> terms, float rate) {
		List<Triple<Float, int[], float[]>> postings = new ArrayList<Triple<Float, int[],float[]>>();
		
		for(String term : terms) {
			if(indices.containsKey(term))
				postings.add(indices.get(term));
		}
		
		return mergePostings(postings, rate);
	}
	
	private List<Pair<Integer, Float>> mergePostings(List<Triple<Float, int[], float[]>> postings, float propotion) {		
		//IDF 가 proposition 보다 높은가?
		float average = 0;		
		for(Triple<Float, int[], float[]> posting : postings) {
			average += posting.getLeft();
		}
		average /= postings.size();
		
		for(Iterator<Triple<Float,int[],float[]>> it = postings.iterator(); it.hasNext();) {
			Triple<Float, int[], float[]> posting = it.next();
			if(posting.getLeft() <= average * propotion) {
				it.remove();
			}
		}		
		
		//Posting counting 이 propotion 보다 높은가?
		int [] merged = null;
//		for(int[] arr : postings) {
//			merged = ArrayUtils.addAll(merged, arr);
//		}
//		
//		Arrays.sort(merged);	
//		
//		int counter = 1;
//		int maxCounter = 0;
//		int last = merged[0];
//		
//		for(int i = 1; i < merged.length; i++) {
//			if(merged[i] == last) {
//				counter++;				
//			}else {
//				last = merged[i];
//				counter = 1;
//			}
//			
//			if(counter > maxCounter) {
//				maxCounter = counter;
//			}
//		}	
//		
//		int documentCount = (int) (maxCounter * propotion);
//		System.out.println("Finding : Doc > " + documentCount);
//		
//		int postingCounter = 0;
//		int[] ret = null;
//		
//		for(int i = 1; i < merged.length; i++) {
//			if(merged[i] == last) {
//				counter ++;
//			} else {
//				last = merged[i];
//				counter = 1;
//			}
//			
//			if(counter >= documentCount) {
//				 postingCounter++;
//				ret = ArrayUtils.add(ret, last);
//				while(i+1 < merged.length && merged[i+1] == last) {
//					i++;
//				}
//			}
//		}
//		
//		System.out.println("Fount Total Doc : " + postingCounter);
//		
//		return ret;
		return null;
	}
}
