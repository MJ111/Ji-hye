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

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import tool.xmlparsetool.WikiData;


public class WikiIndexor {
	private String directory;
	private File[] matchingFiles;
	private int NUM_THREADS;
	private ArrayList<Komoran> komorans;
	
	public WikiIndexor(String directoryPath, int Threads) throws IOException {
		this.directory = directoryPath;
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
			Vector<Future<ExtractedWikiData>> arrFutures = new Vector<Future<ExtractedWikiData>>();
			HashMap<String, SortedSet<Long>> map = new HashMap<String, SortedSet<Long>>();
			
			while(true) {				
				//���� ���̻� ó���� �����Ͱ� ����, ���� �������� �����尡 ������� �����Ѵ�.
				if(wikiDatas.isEmpty()) {
					if(arrFutures.size() == 0) break;
				}
				
				//������ Ǯ�� ����ְ�, ��Ű �����Ͱ� ����������		
				if(arrFutures.size() < NUM_THREADS && !wikiDatas.isEmpty()) {
					arrFutures.add(executorService.submit(new WikiThreadedIndexor(wikiDatas.remove(0), komorans.remove(0))));
				}
				
				//if thread is finished, remove from threads list and process datas.
				for(Iterator<Future<ExtractedWikiData>> it = arrFutures.iterator(); it.hasNext();) {					
					Future<ExtractedWikiData> future = it.next();
					
					if(!future.isDone()) {
						continue;
					} else {		
						//���� �Ϸ�� �����带 ã����, ArrayList �� HashMap�� ���ν�Ų ��, Komoran �� Thread �� Pool �� ����ִ´�.			
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
						komorans.add(future.get().getKomoran()); //Komoran ��ȯ
						it.remove(); //Thread ��ȯ						
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
			throw new IOException("��ü ������ ������ �߻��Ͽ����ϴ�.");
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
			Exception exp = new Exception("��ü ĳ���� ����. (���� ���̺귯�� ��� Ȯ�ο�)");
			throw exp;
		}
		return wikiDatas;
	}
}
