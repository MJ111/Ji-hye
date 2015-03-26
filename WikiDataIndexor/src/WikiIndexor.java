import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.util.Pair;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import tool.xmlparsetool.WikiData;


public class WikiIndexor {
	private String directory;
	private File[] matchingFiles;
	private int NUM_THREADS;
	private ArrayList<Komoran> komorans;
	
	public WikiIndexor(String directory, int Threads) throws IOException {
		this.directory = directory;
		this.NUM_THREADS = Threads;
		
		File f = new File(directory);
		matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith("jhd");
		    }
		});
		
		if(f.length() == 0) throw new IOException("위키 데이터를 찾을 수 없습니다! (경로확인요)");
		
		//코모란 생성하는 비용이 커서 재미있는 방법으로 코모란 돌릴거임..
		komorans = new ArrayList<Komoran>();
		for(int i = 0; i < NUM_THREADS; i++) {
			Komoran k = new Komoran("./resources/models-light");
			komorans.add(k);
		}		
		System.out.println("KOMORANs are ready");
	}
	
	public void startIndexing() throws Exception{
		for(File file : matchingFiles) {
			Vector<WikiData> wikiDatas = getWikiDataVector(file);
			ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
			Vector<Future<IndexedData>> arrFutures = new Vector<Future<IndexedData>>();
			HashMap<String, ArrayList<Long>> map = new HashMap<String, ArrayList<Long>>();
			
			while(true) {				
				//만약 더이상 처리할 데이터가 없고, 현재 실행중인 쓰레드가 없을경우 종료한다.
				if(wikiDatas.isEmpty()) {
					if(arrFutures.size() == 0) break;
				}
				
				//쓰레드 풀이 비어있고, 위키 데이터가 남아있으면		
				if(arrFutures.size() < NUM_THREADS && !wikiDatas.isEmpty()) {
					int iKomoran = arrFutures.size();
					arrFutures.add(executorService.submit(new WikiThreadedIndexor(wikiDatas.remove(0), komorans.remove(0))));
				}
				
				//if thread is finished, remove from threads list and process datas.
				for(Iterator<Future<IndexedData>> it = arrFutures.iterator(); it.hasNext();) {					
					Future<IndexedData> future = it.next();
					
					if(!future.isDone()) {
						continue;
					} else {		
						System.out.println(future.get().getDocumentID());						
						//Set<String> keySet = future.get().keySet();
						//Iterator<String> iterator = keySet.iterator();
						/*
						while(iterator.hasNext()) {
							String key = iterator.next();
							ArrayList<Long> value = map.get(key);
							if(value == null) {
								ArrayList<Long> newValue = new ArrayList<Long>();
								newValue.add(future.get().getDocumentID());
								map.put(key, newValue);
							} else {
								value.add(future.get().getDocumentID());
							}
						}		
						*/
						komorans.add(future.get().getKomoran());
						it.remove();
						
					}
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
