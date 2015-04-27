package jihye.indexor.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileManager {
	private static FileManager fileManager = null;
	
	public static FileManager getInstance() {
		if(fileManager == null) {
			fileManager = new FileManager();
		}
		return fileManager;
	}
	
	public File[] getFiles(String directory, String endsWith) {
		File [] files;
		File f = new File(directory);
		files = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("xml");
		    }
		});
		return files;
	}
}
