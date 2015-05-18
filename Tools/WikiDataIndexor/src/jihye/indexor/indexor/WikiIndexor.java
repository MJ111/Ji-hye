package jihye.indexor.indexor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jihye.indexor.parser.WikiData;
import jihye.indexor.util.Pair;
import jihye.indexor.util.Utility;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;

class PairComparator implements Comparator<Pair<Integer, Float>> {

	@Override
	public int compare(Pair<Integer, Float> o1, Pair<Integer, Float> o2) {
		return Integer.compare(o1.getFirst(), o2.getFirst());
	}	
}

public class WikiIndexor {
	private File[] matchingFiles;
	private int NUM_THREADS;
	private ArrayList<Komoran> komorans;
	
	public WikiIndexor(String directoryPath, int Threads) throws Exception {
		this.NUM_THREADS = Threads;				
		
		matchingFiles = Utility.getInstance().getFiles(directoryPath, "jhd");		
		if(matchingFiles.length == 0) throw Utility.getInstance().makeException(Utility.ERROR_FILE, ".ex 파일 로드 실패");
		
		//코모란 생성하는 비용이 커서 재미있는 방법으로 코모란 돌릴거임..
		komorans = new ArrayList<Komoran>();
		for(int i = 0; i < NUM_THREADS; i++) {
			Komoran k = new Komoran("./resources/models-light");
			komorans.add(k);
		}		
		Utility.getInstance().log(this, "Total " + NUM_THREADS + " threads are ready");
	}
	
	public void startIndexing() throws Exception{
		for(File file : matchingFiles) {
			Vector<WikiData> wikiDatas = getWikiDataVector(file);
			ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
			Vector<Future<ExtractedWikiData>> arrFutures = new Vector<Future<ExtractedWikiData>>();
			Map<String, ArrayList<Pair<Integer, Float>>> map = new HashMap<String, ArrayList<Pair<Integer,Float>>>();
			
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
						int documentID = (int)ewd.getDocumentID(); //Document ID of current Thread processing
						
						float[] tf = ewd.getTermFrequency();
						for(int i = 0; i < ewd.size(); i++) {
							String term = ewd.get(i);
							ArrayList<Pair<Integer,Float>> arrPostings = map.get(term);
							if(arrPostings == null) {
								ArrayList<Pair<Integer,Float>> newValue = new ArrayList<Pair<Integer,Float>>();
								newValue.add(new Pair<Integer, Float>(documentID, tf[i]));
								map.put(term, newValue);
							}else {
								arrPostings.add(new Pair<Integer, Float>(documentID, tf[i]));
							}
						}
						ewd.clear();
						komorans.add(future.get().getKomoran()); //Komoran 반환
						it.remove(); //Thread 반환						
					}
				}
			}
			executorService.shutdown();
			saveIndexedWikiData(map, file);
			wikiDatas.clear();
			map.clear();
		}
	}
	
	private void saveIndexedWikiData(Map<String, ArrayList<Pair<Integer, Float>>> data, File currentFile) throws Exception {		
		File file = new File(currentFile.getAbsolutePath() +"ex");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			Set<String> dic = data.keySet();			
			for(String term : dic) {
				String write = term +",0.0,";
				ArrayList<Pair<Integer,Float>> postings = data.get(term);
				Collections.sort(postings, new PairComparator());
				for(Pair<Integer, Float> posting : postings) {
					write += posting.getFirst() +"," + posting.getSecond() +",";
				}
				write += "\n";
				bos.write(write.getBytes());
			}
			bos.close();
			fos.close();
		} catch (IOException e) {
			throw Utility.getInstance().makeException(Utility.ERROR_FILE, ".ex 데이터 생성 실패");
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
			throw Utility.getInstance().makeException(Utility.ERROR_OBJECT_CAST, "객체 변환 실패");
		}
		return wikiDatas;
	}
}
