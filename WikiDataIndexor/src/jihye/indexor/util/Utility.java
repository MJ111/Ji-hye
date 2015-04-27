package jihye.indexor.util;

import java.io.File;
import java.io.FilenameFilter;

public class Utility {
	private static Utility fileManager = null;
	
	public static Utility getInstance() {
		if(fileManager == null) {
			fileManager = new Utility();
		}
		return fileManager;
	}
	
	public File[] getFiles(String directory, final String endsWith) {
		File [] files;
		File f = new File(directory);
		files = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(endsWith);
		    }
		});
		return files;
	}
}
