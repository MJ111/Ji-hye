import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;


public class WikiDatabaseManager {
	private WikiIndexDataManager widm;
	private String strQuery;
	private int iQueryCounter;
	private long iTermCounter;
	private long iPairCounter;
	private long iTotalQueryCounter;
	private Connection connection;
	
	public WikiDatabaseManager(WikiIndexDataManager widm) throws SQLException{
		this.widm = widm;
		strQuery = "";
		iQueryCounter = 0;
		iTotalQueryCounter = 0;
		iTermCounter = 0;
		iPairCounter = 0;
		
		try {
			connection = null;
			connection = DriverManager.getConnection(
					"jdbc:mysql://knuwooseok.iptime.org", "cheolsu",
					"NaturalIntelligence");
		} catch (SQLException e) {
			throw new SQLException("Database connection failed");
		}
	}
	
	public void startQuery() {
		HashMap<String, Long> termMap = new HashMap<String, Long>();
		
		while(widm.avilable()) {
			HashMap<String, SortedSet<Long>> data = widm.getData();
			
			Set<String> keys = data.keySet();
			
			for(String key : keys) {	
				
				//termID find
				Long termID;
				if(!termMap.containsKey(key)) {
					termMap.put(key, Long.valueOf((iTermCounter)));
					termID = ++iTermCounter;
					//strQuery += "INSERT INTO WIKIDB_INDEX.TERM "
				}else {
					termID = termMap.get(key);
				}
				
				//get Postings
				SortedSet<Long> postings = data.get(key);				
				for(Long posting : postings) {
					strQuery += "";
				}
				
				if(iQueryCounter > 1000) {
					sendQuery();
				}
			}			
		}
	}
	
	private void sendQuery() {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute(strQuery);
			iTotalQueryCounter += iQueryCounter;
			iQueryCounter = 0;
			strQuery = "";
		}catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Query OK, " + iTotalQueryCounter +" rows affected");
	}
}
