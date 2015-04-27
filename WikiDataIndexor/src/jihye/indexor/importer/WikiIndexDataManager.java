package jihye.indexor.importer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.SortedSet;


public class WikiIndexDataManager {
	private File[] matchingFiles;
	private int iFileIndex;
	
	public WikiIndexDataManager(String directory) throws Exception{
		iFileIndex = -1;
		
		File f = new File(directory);
		matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("jhdex");
		    }
		});
		
		if(f.length() == 0) throw new IOException("File not found exceptioin");	
		else iFileIndex = 0;
	}
	
	public boolean avilable() {
		if(-1 < iFileIndex && iFileIndex <= matchingFiles.length) return true;
		else return false;
	}
	
	public HashMap<String, SortedSet<Long>> getData() {
		HashMap<String, SortedSet<Long>> ret = null;
		if(this.avilable()) {
			try {
				FileInputStream fis = new FileInputStream(matchingFiles[iFileIndex]);
				ObjectInputStream ois = new ObjectInputStream(fis);
				ret = (HashMap<String, SortedSet<Long>>)ois.readObject();
				ois.close();
				fis.close();
				iFileIndex++;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return ret;
	}	
}
