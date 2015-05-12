package jihye;

import java.util.ArrayList;
import java.util.Arrays;

import jihye.DB.DatabaseManager;
import jihye.DB.IndexProcessor;
import jihye.DB.WikipediaPage;
import jihye.GUI.UserInterface;

public class Main {
	private static final String dataPath = "D:/WikiData";
	
	public static void main(String[] args) {
		JihyeController jc = new JihyeController();
		//jc.setDataPath(dataPath);
		
		UserInterface ui = new UserInterface(jc);
		
//		try {
//			DatabaseManager dm = new DatabaseManager();
//			IndexProcessor im = new IndexProcessor(dataPath);
//			int[] a = im.getMergedPostings(Arrays.asList("조선", "시대", "왜군", "명량", "해전", "거북선"), 6);
//
//			for(int num : a) {
//				WikipediaPage p = dm.getPageFromPageID(num);
//				System.out.println(p.getTitle());
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}