
public class Main {
	private final static String WIKI_DATA_PATH = "D:/WikiDataa";
	
	public static void main(String[] args) {
		try {
			WikiIndexDataManager widm = new WikiIndexDataManager(WIKI_DATA_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
