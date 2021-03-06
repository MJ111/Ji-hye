﻿package jihye.indexor;
import jihye.indexor.indexor.WikiIndexor;
import jihye.indexor.merger.WikiIndexMerger;
import jihye.indexor.parser.WikiParser;
import jihye.indexor.stopwordProcessor.StopwordProcessor;

public class Main {
	private final static String WIKIDATA_PATH = "D:/WikiData";
	private final static int NUM_THREADS = 8;
	private final static int NUM_SPLIT = 30000;
	private final static int NUM_INDEX_SPLIT = 100000;
	
	public static void main(String args[]) {
		//Parse part
//		try {
//			WikiParser wp = new WikiParser(WIKIDATA_PATH);
//			wp.startParse(NUM_SPLIT);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		
////		//Indexing part
//		try {
//			WikiIndexor wi = new WikiIndexor(WIKIDATA_PATH, NUM_THREADS);
//			wi.startIndexing();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		
//		//Merge part
//		try {
//			WikiIndexMerger wim = new WikiIndexMerger(WIKIDATA_PATH);
//			wim.startMerge(NUM_INDEX_SPLIT);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//Stopword Deleting part
		try {
			StopwordProcessor sp = new StopwordProcessor(WIKIDATA_PATH);				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
