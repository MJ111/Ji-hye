import java.io.IOException;
import java.util.Scanner;


public class Main {
	private final static String WIKIDATA_PATH = "D:/WikiData";
	private final static int NUM_THREADS = 16;
	
	public static void main(String args[]) {
		//Indexing part
//		try {
//			WikiIndexor wi = new WikiIndexor(WIKIDATA_PATH, NUM_THREADS);
//			wi.startIndexing();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		//Merge part
		try {
			WikiIndexMerger wim = new WikiIndexMerger(WIKIDATA_PATH);
			wim.startMerge();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
