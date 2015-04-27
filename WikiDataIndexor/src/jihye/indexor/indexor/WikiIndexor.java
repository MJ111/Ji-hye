package jihye.indexor.indexor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jihye.indexor.ExtractedWikiData;
import jihye.indexor.WikiData;
import jihye.indexor.util.Utility;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;


public class WikiIndexor {
	private String directory;
	private File[] matchingFiles;
	private int NUM_THREADS;
	private ArrayList<Komoran> komorans;
	
	public WikiIndexor(String directoryPath, int Threads) throws IOException {
		this.directory = directoryPath;
		this.NUM_THREADS = Threads;
				
		
		matchingFiles = Utility.getInstance().getFiles(directoryPath, "jhd");		
		if(matchingFiles.length == 0) throw new IOException("위키 데이터를 찾을 수 없습니다! (경로확인요)");
		
		//코모란 생성하는 비용이 커서 재미있는 방법으로 코모란 돌릴거임..
		komorans = new ArrayList<Komoran>();
		for(int i = 0; i < NUM_THREADS; i++) {
			Komoran k = new Komoran("./resources/models-light");
			komorans.add(k);
		}
		
		System.out.println("Indexor : Threads are ready");
	}
	
	public void startIndexing() throws Exception{
		for(File file : matchingFiles) {
			Vector<WikiData> wikiDatas = getWikiDataVector(file);
			ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
			Vector<Future<ExtractedWikiData>> arrFutures = new Vector<Future<ExtractedWikiData>>();
			HashMap<String, SortedSet<Long>> map = new HashMap<String, SortedSet<Long>>();
			
			while(true) {				
				//만약 더이상 처리할 데이터가 없고, 현재 실행중인 쓰레드가 없을경우 종료한다.
				if(wikiDatas.isEmpty()) {
					if(arrFutures.size() == 0) break;
				}
				
				//쓰레드 풀이 비어있고, 위키 데이터가 남아있으면		
				if(arrFutures.size() < NUM_THREADS && !wikiDatas.isEmpty()) {
					arrFutures.add(executorService.submit(new WikiThreadedIndexor(wikiDatas.remove(0), komorans.remove(0))));
				}
				
				//if thread is finished, remove from threads list and process datas.
				for(Iterator<Future<ExtractedWikiData>> it = arrFutures.iterator(); it.hasNext();) {					
					Future<ExtractedWikiData> future = it.next();
					
					if(!future.isDone()) {
						continue;
					} else {		
						//만약 완료된 쓰레드를 찾으면, ArrayList 와 HashMap을 조인시킨 후, Komoran 과 Thread 를 Pool 에 집어넣는다.			
						ExtractedWikiData ewd = future.get();
						long documentID = future.get().getDocumentID(); //Document ID of current Thread processing
						
						for(String key : ewd) {
							SortedSet<Long> arrHashMapValue = map.get(key);
							if(arrHashMapValue == null) {
								SortedSet<Long> newValue = new TreeSet<Long>();
								newValue.add(documentID);
								map.put(key, newValue);
							} else {
								arrHashMapValue.add(documentID);
							}
						}						
						komorans.add(future.get().getKomoran()); //Komoran 반환
						it.remove(); //Thread 반환						
					}
				}
			}			
			saveIndexedWikiData(map, file);			
		}
	}
	
	private void saveIndexedWikiData(HashMap<String, SortedSet<Long>> data, File currentFile) throws Exception {		
		File file = new File(currentFile.getAbsolutePath() +"ex");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(data);
			
			oos.close();
			fos.close();
		} catch (IOException e) {
			throw new IOException("객체 저장중 오류가 발생하였습니다.");
		}
	}
	
	private Vector<WikiData> getWikiDataVector(File file) throws Exception {
		Vector<WikiData> wikiDatas = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);			
			wikiDatas = (Vector<WikiData>)ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			Exception exp = new Exception("객체 캐스팅 에러. (동일 라이브러라 사용 확인요)");
			throw exp;
		}
		return wikiDatas;
	}
}
