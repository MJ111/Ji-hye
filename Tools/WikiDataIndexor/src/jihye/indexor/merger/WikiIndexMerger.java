package jihye.indexor.merger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.lang3.ArrayUtils;

import jihye.indexor.util.Utility;


public class WikiIndexMerger {
	private File[] matchingFiles;
	private SortedMap<String, Integer> index;
	private String dictionaryPath;
	private int iNumOfIndex;
	public WikiIndexMerger(String dictionaryPath) throws Exception{
		this.dictionaryPath = dictionaryPath;
		matchingFiles = Utility.getInstance().getFiles(dictionaryPath, "jhdex");
	}
	
	public void startMerge(int seperate) {
		//index = getIndexOfIndices(seperate);
		//index = readIndexOfIndicesFromFile(seperate);
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
	
	private SortedMap<String, Integer> readIndexOfIndicesFromFile(int seperate) {
		SortedMap<String, Integer> ret = null;
		
		File[] f = Utility.getInstance().getFiles(dictionaryPath, "jhdindex");
		if(f.length != 0) {
			Utility.getInstance().log(this, "Load Data From :" + f[0]);
			try {
				FileInputStream fis = new FileInputStream(f[0]);
				ObjectInputStream ois = new ObjectInputStream(fis);
				ret = (SortedMap<String, Integer>)ois.readObject();
				fis.close();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			Utility.getInstance().log(this, "Index of Index Data Not Found : " + dictionaryPath);
		}
		iNumOfIndex = ret.size() / seperate + 1;
		return ret;		
	}
	
	private SortedMap<String, Integer> getIndexOfIndices(int seperate) {
		//Index of Indices
		//음.. 몇번 인덱스에 어느 텀이 있는지 알려준다?
		int termCounter = 0;
		int indexCounter = 1;
		boolean isNumber = false;
		SortedMap<String, Integer> Index = new TreeMap<String, Integer>();
		
		for (File f : matchingFiles) {
			Utility.getInstance().log(this, "Merging : " + f);
			
			try {				
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				
				while(br.ready()) {
					String line = br.readLine();
					String key = line.split(",")[0];
					if(!Index.containsKey(key)) {
						Index.put(key, 0);
					}					
				}				
				br.close();
				fr.close();
					
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		//Assign Index File Numbers
		Set<String> keys = Index.keySet();
		for(String key : keys) {
			try {
				Integer.parseInt(key);
				isNumber = true;
			} catch (Exception e) {
				isNumber = false;
			} finally {
				if(isNumber) {
					Index.put(key, 0);
				}else {
					Index.put(key, indexCounter);
					if(termCounter ++ > seperate) {
						indexCounter++;
						termCounter = 0;
					}
				}
			}
		}		
		
		System.out.println(String.format("Total %d indices, %d terms", indexCounter, Index.size()));	
		this.iNumOfIndex = indexCounter;
		
		try {
			FileOutputStream fos = new FileOutputStream(dictionaryPath +  "/Index.jhdindex");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			//Primitive of Index : SortedMap<String, Integer>
			oos.writeObject(Index);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return Index;
	}
}
