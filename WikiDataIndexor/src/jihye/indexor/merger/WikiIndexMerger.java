package jihye.indexor.merger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import jihye.indexor.util.Utility;


public class WikiIndexMerger {
	private File[] matchingFiles;
	private SortedSet<String> dictionary;
	
	public WikiIndexMerger(String dictionaryPath) throws Exception{
		matchingFiles = Utility.getInstance().getFiles(dictionaryPath, "jhdex");		
	}
	
	public void startMerge() {
		dictionary = getDictionary();
		System.out.println("Total : " + dictionary.size() + "terms");
	}
	
	private SortedSet<String> getDictionary() {
		SortedSet<String> dic = new TreeSet<String>();
		for (File f : matchingFiles) {
			System.gc();
			HashMap<String, SortedSet<Long>> map = null;
			
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				map = (HashMap<String, SortedSet<Long>>)ois.readObject();
				System.out.println(f);
				System.out.println("total terms in index : " + map.keySet().size());
				ois.close();
				fis.close();				
			} catch (Exception e) {
				System.out.print(e);
			}
			
			for(String key : map.keySet()) {
				dic.add(key);
			}
			map.clear();
			System.out.println(dic.size());
		}
		return dic;
	}
}
