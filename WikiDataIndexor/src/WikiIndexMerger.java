import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;


public class WikiIndexMerger {
	private File[] matchingFiles;
	private SortedSet<String> dictionary;
	
	public WikiIndexMerger(String dictionaryPath) throws Exception{
		matchingFiles = getFiles(dictionaryPath);
		dictionary = getDictionary();
	}
	
	public void startMerge() {
		
	}
	
	private SortedSet<String> getDictionary() {
		SortedSet<String> dic = new TreeSet<String>();
		for (File f : matchingFiles) {
			HashMap<String, SortedSet<Long>> map = null;
			
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				map = (HashMap<String, SortedSet<Long>>)ois.readObject();
				
			} catch (Exception e) {
				System.out.print(e);
			}
			
			for(String key : map.keySet()) {
				dic.add(key);
			}
		}
		return dic;
	}
	
	private File[] getFiles(String dictionaryPath) throws IOException {
		File [] files;
		File f = new File(dictionaryPath);
		files = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("jhdex");
		    }
		});
		
		if(f.length() == 0) throw new IOException("위키 데이터를 찾을 수 없습니다! (경로확인요)");
		
		return files;
	}
}
