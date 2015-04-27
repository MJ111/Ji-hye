package jihye.indexor.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Utility {
	private static Utility fileManager = null;
	public static final int ERROR_DEFAULT = 0;
	public static final int ERROR_FILE = 1;
	public static final int ERROR_OBJECT_CAST = 2;
	
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
	
	public void log(String message) {
		System.out.println(message);
	}
	
	public void log (String format, Object... args) {
		System.out.println(String.format(format, args));
	}
	
	public Exception makeException(int TYPE, String message) {
		switch (TYPE) {
		case ERROR_FILE : 
			return new IOException(message);
		case ERROR_OBJECT_CAST :
			return new Exception(message);
		default :
			return new Exception(message);
		}
	}
}
