import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tool.xmlparsetool.WikiData;


public class WikiIndexor {
	private String directory;
	private File[] matchingFiles;
	private final static int NUM_THREADS = 5;
	
	public WikiIndexor(String directory) throws IOException {
		this.directory = directory;
		
		File f = new File(directory);
		matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("jhd");
		    }
		});
		
		if(f.length() == 0) throw new IOException("위키 데이터를 찾을 수 없습니다! (경로확인요)");
	}
	
	public void startIndexing() throws Exception{
		for(File file : matchingFiles) {
			Vector<WikiData> wikiDatas = getWikiDataVector(file);
			ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
			Vector<Future<IndexedData>> arrFutures = new Vector<Future<IndexedData>>();
			
			while(!wikiDatas.isEmpty()) {
				if(arrFutures.size() > NUM_THREADS) return;
				else {		
					//Add Thread data into Array Futures
					arrFutures.add(executorService.submit(new WikiThreadedIndexor(wikiDatas.remove(0))));
				}
				
				//If Thread is done, remove from Thread Lists
				for(Future<IndexedData> future : arrFutures) {
					if(future.isDone()) {
						arrFutures.remove(future);						
					}
				}
				
			}
			
		}
	}
	
	public Vector<WikiData> getWikiDataVector(File file) {
		Vector<WikiData> wikiDatas = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);			
			wikiDatas = (Vector<WikiData>)ois.readObject();
		} catch (Exception e) {
			return null;
		}
		return wikiDatas;
	}
}
