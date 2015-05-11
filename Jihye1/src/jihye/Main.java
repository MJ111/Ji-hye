package jihye;

import java.util.ArrayList;
import java.util.Arrays;

import jihye.DB.IndexManager;
import jihye.GUI.UserInterface;

public class Main {
	private static final String dataPath = "D:/WikiData";
	
	public static void main(String[] args) {
//		JihyeController jc = new JihyeController();
//		jc.setDataPath(dataPath);
//		
//		UserInterface ui = new UserInterface(jc);
		try {
			IndexManager im = new IndexManager(dataPath);
			im.getMergedPostings(Arrays.asList("조선", "시대", "왜군"), 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}