package jihye.indexor.importer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;

import jihye.indexor.util.Utility;


public class WikiDatabaseManager {
	private WikiIndexDataManager widm;
	private long iTermCounter;
	private long iPostingCounter;
	private long iTotalQueryCounter;
	private Connection connection;
	private ArrayList<String> queries;
	
	public WikiDatabaseManager(WikiIndexDataManager widm) throws SQLException{
		this.widm = widm;
		iTotalQueryCounter = 0;
		iTermCounter = 0;
		iPostingCounter = 0;
		queries = new ArrayList<String>();
		
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
					termMap.put(key, Long.valueOf((++iTermCounter)));
					termID = iTermCounter;
					queries.add("INSERT INTO WIKIDB_INDEX.TERMS (term) values ('" + key + "');");
				}else {
					termID = termMap.get(key);
				}
				
				//get Postings
				SortedSet<Long> postings = data.get(key);				
				for(Long posting : postings) {
					queries.add("INSERT INTO WIKIDB_INDEX.POSTINGS VALUES (" + termID + "," + posting + ");");
					iPostingCounter++;
				}
				
				if(queries.size() > 1000) {
					sendQuery();
				}
			}			
		}
		Utility.getInstance().log(this, "Total " + termMap.size() + " words");
		Utility.getInstance().log(this, "Total " + iPostingCounter + " postings");
	}
	
	private void sendQuery() {
		try {
			Statement stmt = connection.createStatement();
			for (String queri : queries) {
				stmt.execute(queri);
			}
			iTotalQueryCounter += queries.size();
			queries.clear();
		}catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		Utility.getInstance().log(this, "Query OK, " + iTotalQueryCounter +" rows affected");
	}
}
