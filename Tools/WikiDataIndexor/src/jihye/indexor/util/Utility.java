package jihye.indexor.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import jihye.indexor.indexor.WikiIndexor;
import jihye.indexor.indexor.WikiThreadedIndexor;
import jihye.indexor.merger.WikiIndexMerger;
import jihye.indexor.parser.WikiParser;

public class Utility {
	private static Utility fileManager = null;
	public static final int ERROR_DEFAULT = 0;
	public static final int ERROR_FILE = 1;
	public static final int ERROR_OBJECT_CAST = 2;
	public static final int FILE_TYPE_INDEX = 3;
	
	public static Utility getInstance() {
		if(fileManager == null) {
			fileManager = new Utility();
		}
		return fileManager;
	}
	
	public File[] createFiles(String directory, int NumOfFiles, int FileType) {
		File[] files = null;
		if(FileType == FILE_TYPE_INDEX) {
			files = createIndexFiles(directory, NumOfFiles);
		}		
		return files;
	}
	
	private File[] createIndexFiles(String directory, int NumOfFiles) {
		File[] files = null;
		File dir = new File(directory);
		
		if(dir.exists()) {
			files = new File[NumOfFiles];
			for(int i = 0; i < NumOfFiles; i++) {
				String str = String.format("%s/JihyeIndices%02d.jhidx", directory , i);
				files[i] = new File(str);
			}
		}		
		return files;
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
	
	public void log(Object sender , String message) {
		log(sender, message, new Object[0]);		
	}
	
	public void log(Object sender, String format, Object... args) {
		String senderName = "";
		
		if(sender instanceof WikiIndexor) {
			senderName = "[WI]";
		}else if(sender instanceof WikiThreadedIndexor) {
			senderName = "[WTI]";
		}else if(sender instanceof WikiIndexMerger) {
			senderName = "[WM]";
		}else if(sender instanceof WikiParser) {
			senderName = "[WP]";
		}else if(sender instanceof Utility) {
			senderName = "[UT]";
		}else {
			senderName = "[NaN]";
		}
		
		log(senderName + format, args);
	}
	
	
	private void log(String message) {
		System.out.println(message);
	}
	
	private void log (String format, Object... args) {
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
