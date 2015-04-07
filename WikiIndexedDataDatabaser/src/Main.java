
public class Main {
	private final static String WIKI_DATA_PATH = "D:/WikiData";
	
	public static void main(String[] args) {
		try {
			WikiIndexDataManager widm = new WikiIndexDataManager(WIKI_DATA_PATH);
			WikiDatabaseManager widdm = new WikiDatabaseManager(widm);
			widdm.startQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
