import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tool.xmlparsetool.WikiData;


public class WikiIndexor {
	private String directory;
	private File[] matchingFiles;
	private final static int NUM_THREADS = 8;
	
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
				if(wikiDatas.isEmpty()) {
					if(arrFutures.size() != 0) continue;
					else break;
				}				
				
				if(arrFutures.size() > NUM_THREADS) return;
				else {		
					//Add Thread information into Array Futures (Thread Lists)
					arrFutures.add(executorService.submit(new WikiThreadedIndexor(wikiDatas.remove(0))));
				}
				
				//if thread is finished, remove from threads list and process datas.
				for(Iterator<Future<IndexedData>> it = arrFutures.iterator(); it.hasNext();) {
					Future<IndexedData> future = it.next();
					System.out.println(future.get().getDocumentID());					
					it.remove();
				}
				
			}
			
		}
	}
	
	public Vector<WikiData> getWikiDataVector(File file) throws Exception {
		Vector<WikiData> wikiDatas = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);			
			wikiDatas = (Vector<WikiData>)ois.readObject();
		} catch (Exception e) {
			Exception exp = new Exception("객체 캐스팅 에러. (동일 라이브러라 사용 확인요)");
			throw exp;
		}
		return wikiDatas;
	}
}
