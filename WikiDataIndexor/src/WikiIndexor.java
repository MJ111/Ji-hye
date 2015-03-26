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
		
		if(f.length() == 0) throw new IOException("��Ű �����͸� ã�� �� �����ϴ�! (���Ȯ�ο�)");
		
		//�ڸ�� �����ϴ� ����� Ŀ�� ����ִ� ������� �ڸ�� ��������..
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
				//���� ���̻� ó���� �����Ͱ� ����, ���� �������� �����尡 ������� �����Ѵ�.
				if(wikiDatas.isEmpty()) {
					if(arrFutures.size() == 0) break;
				}
				
				//������ Ǯ�� ����ְ�, ��Ű �����Ͱ� ����������		
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
			Exception exp = new Exception("��ü ĳ���� ����. (���� ���̺귯�� ��� Ȯ�ο�)");
			throw exp;
		}
		return wikiDatas;
	}
}
