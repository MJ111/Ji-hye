package jihye.PS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.ArrayUtils;

public class IndexProcessor {
	private Map<String, Triple<Float, int[], float[]>> indices = null;
	
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
			int[] postings = new int[postingsSize];
			float[] tfidfs = new float[postingsSize];
			
			for(int i = 0; i < seperated.length-2; i=i+2) {
				postings[i/2] = Integer.parseInt(seperated[i+2]);
				tfidfs[i/2] = Float.parseFloat(seperated[i+3]);
			}			
			
			Triple<Float, int[], float[]> posting = new Triple<Float, int[], float[]>(idf, postings, tfidfs);
			indices.put(term, posting);			
		}		
		br.close();
		isr.close();
		fis.close();
	}
	
	public List<ExtractedDocument> getDocumentsFromIndices(List<String> terms, float rate) {
		return getMergedPostings(terms, rate);
	}
	
	private List<ExtractedDocument> getMergedPostings(List<String> terms, float rate) {
		List<Triple<Float, int[], float[]>> postings = new ArrayList<Triple<Float, int[],float[]>>();
		List<String> dictionary = new ArrayList<String>();
		
		for(String term : terms) {
			if(dictionary.contains(term)) {
				continue;
			}
			else if(indices.containsKey(term)) {
				dictionary.add(term);
				postings.add(indices.get(term));
			}
		}
		
		return mergePostings(dictionary, postings, rate);
	}
	
	private List<ExtractedDocument> mergePostings(List<String> dictionary ,List<Triple<Float, int[], float[]>> postings, float propotion) {		
		//IDF 가 proposition * 평균 보다 높은가? (High - IDF)
		float average = 0;		
		for(Triple<Float, int[], float[]> posting : postings) {
			average += posting.getLeft();
		}
		average /= postings.size();
		
		for(Iterator<Triple<Float,int[],float[]>> it = postings.iterator(); it.hasNext();) {
			Triple<Float, int[], float[]> posting = it.next();
			int index = postings.indexOf(posting);
			if(posting.getLeft() <= average * propotion) {
				it.remove();
				dictionary.remove(index);
			}
		}
		
		//Posting counting 이 propotion 보다 높은가?
		int[] merged = null;
		for(Iterator<Triple<Float, int[], float[]>> it = postings.iterator(); it.hasNext();) {
			Triple<Float, int[], float[]> posting = it.next();
			merged = ArrayUtils.addAll(merged, posting.getMiddle());
		}
		if(merged != null)
			Arrays.sort(merged);
		else
			return new ArrayList<ExtractedDocument>();
		
		//가장 많이 겹치는 문서의 개수를 가져온다.
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
		
		//가장 많이 겹치는 문서의 개수 * propotion
		int documentCount = (int) (maxCounter * propotion);		
		int[] documents = null;
		
		for(int i = 1; i < merged.length; i++) {
			if(merged[i] == last) {
				counter ++;
			} else {
				last = merged[i];
				counter = 1;
			}
			
			if(counter >= documentCount) {
				documents = ArrayUtils.add(documents, last);
				while(i+1 < merged.length && merged[i+1] == last) {
					i++;
				}
			}
		}		
		
		// 돌려줄 ExtractedDocument 를 만듭시다
		List<ExtractedDocument> ret = new ArrayList<ExtractedDocument>();
		if(documents == null) return ret;
		for(int document : documents) {
			ExtractedDocument ed = new ExtractedDocument(document);
			for(String term : dictionary) {
				Triple<Float,int[], float[]> posting = indices.get(term);
				int indexOfPosting = ArrayUtils.indexOf(posting.getMiddle(), document);
				if(indexOfPosting == -1) {
					continue;
				}else {
					ed.add(term, posting.getRight()[indexOfPosting]);
				}
			}
			ret.add(ed);
		}
		
		return ret;
	}

	public void comparePostingsWithProblem(List<ExtractedDocument> postings, List<String> problemMorph) {
		//Returns nothing, just sort.
		Set<String> setProblemMoph = new TreeSet<String>();	
		setProblemMoph.addAll(problemMorph);
		
		ExtractedDocument problem = new ExtractedDocument(0);
		for(String key : setProblemMoph) {
			problem.add(key, 1.0f);
		}
		
		for(ExtractedDocument posting : postings) {
			posting.innerProduct(problem);
		}
		
		postings.sort(new Comparator<ExtractedDocument>() {
			@Override
			public int compare(ExtractedDocument o1, ExtractedDocument o2) {
				return Float.compare(o1.getSimilarityWithProblem(), o2.getSimilarityWithProblem());
			}
		});
	}
	
}
