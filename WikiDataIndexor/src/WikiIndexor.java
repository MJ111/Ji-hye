import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import tool.xmlparsetool.*;


public class WikiIndexor {
	private String directory;
	private File[] matchingFiles;
	
	public WikiIndexor(String directory) throws IOException {
		this.directory = directory;
		
		File f = new File(directory);
		matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("jhd");
		    }
		});
		
		if(f.length() == 0) throw new IOException("Can not find Wikidata files");
	}
	
	public void startIndexing() throws Exception{
		for(File file : matchingFiles) {
			HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();			
			Komoran komoran = new Komoran("./resources/models-light");
			
			try {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Vector<WikiData> wikidata = (Vector<WikiData>)ois.readObject();					
				
			} catch (Exception e) {
				throw e;
			}
		}
	}
}
