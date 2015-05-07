package jihye.indexor.merger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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
		index = getIndexOfIndices(seperate);
		merge();
	}
	
	private void merge() {
		//Make files
		File[] files = Utility.getInstance().createFiles(dictionaryPath, iNumOfIndex, Utility.FILE_TYPE_INDEX);
		
		
	}
	
	private SortedMap<String, Integer> getIndexOfIndices(int seperate) {
		//Index of Indices
		//음.. 몇번 인덱스에 어느 텀이 있는지 알려준다?
		int termCounter = 0;
		int indexCounter = 0;
		SortedMap<String, Integer> Index = new TreeMap<String, Integer>();
		
		for (File f : matchingFiles) {
			HashMap<String, SortedSet<Long>> map = null;
			System.out.println("Merging : " + f);
			
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				map = (HashMap<String, SortedSet<Long>>)ois.readObject();
				ois.close();
				fis.close();
				
				Set<String> keys = map.keySet();
				
				for(String key : keys) {
					if(!Index.containsKey(key)) {						
						//If index of index doesn't contains term, insert into index of index
						Index.put(key, 0);
					}
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		//Assign Index File Numbers
		Set<String> keys = Index.keySet();
		for(String key : keys) {
			Index.replace(key, indexCounter);
			if(termCounter++ > seperate) {
				indexCounter++;
				termCounter = 0;				
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
