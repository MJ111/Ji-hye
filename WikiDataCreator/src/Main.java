
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import tool.xmlparsetool.WikiData;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class Main {
	private static String TableName = "word_vectors";

	public static void main(String[] args) {

		final String strWikiDumpPath = "D:/kowiki/150227.xml";
		final String strWikiDataPath = "../XmlParseTool_dumps/WikiData";

		makeWikiData(strWikiDumpPath, strWikiDataPath);
	}
	
	public static void makeWikiData(String XMLFilePath, String WikiDataFilePath) {

		long lnStartTime, lnEndTime;
		final Vector<WikiData> tempvecWikiData = new Vector<WikiData>();
		//Here, make wiki data

		lnStartTime = System.currentTimeMillis();
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(XMLFilePath);

		try{
			wxsp.setPageCallback(new PageCallbackHandler() 
			{
				int fileCounter = 1;
				int pageCounter = 0;

				public void process(WikiPage page) 
				{
					pageCounter++;
					String strTitle = deleteEscape(page.getTitle());
					String strTextWithoutInfoBox = page.getText();						

					WikiData wd = new WikiData(strTitle, strTextWithoutInfoBox);
					tempvecWikiData.add(wd);

					if(pageCounter % 50000 == 0) {
						try {
							File outfile = new File("D:/kowiki/WikiData" + (fileCounter++) + ".jhd");
							FileOutputStream fos = new FileOutputStream(outfile);
							ObjectOutputStream oos = new ObjectOutputStream(fos); 
							
							oos.writeObject(tempvecWikiData);
							tempvecWikiData.clear();
							
							oos.close();
							fos.close();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}
			});
			wxsp.parse();			
		}catch(Exception e) {
			e.printStackTrace();
		} 
		
		
		if(tempvecWikiData.size() != 0) {
			try {
				File outfile = new File("D:/kowiki/WikiData0" + ".jhd");
				FileOutputStream fos = new FileOutputStream(outfile);
				ObjectOutputStream oos = new ObjectOutputStream(fos); 

				oos.writeObject(tempvecWikiData);
				tempvecWikiData.clear();

				oos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		lnEndTime = System.currentTimeMillis();

		System.out.println("WikiData 생성 시간 : " + (lnEndTime - lnStartTime) / 1000);

	}
	
	
	/*
	public static boolean isWikidbExists(Connection con) throws SQLException {
		java.sql.Statement st = null;
		st = con.createStatement();
		ResultSet rs = null;

		if(st.execute("show databases;")) {
			rs = st.getResultSet();
		}
		while(rs != null && rs.next()) {
			String str = rs.getNString(1);
			if(str.equals("wikidb")) {
				System.out.println("wikidb Exists");
				return true;
			}
		}
		return false;
	}

	public static boolean isWordVectorTableExists(Connection con) throws SQLException {
		java.sql.Statement st = con.createStatement();
		ResultSet rs = null;

		if(st.execute("show tables;")) {
			rs = st.getResultSet();
		}
		while(rs != null && rs.next()) {
			String str = rs.getNString(1); 
			if(str.equals(TableName)) {
				System.out.println(TableName+" exists");
				return true;
			}
		}
		return false;
	}

	public static void createWordVectorTable(Connection con) throws SQLException {
		System.out.println("Starting make bag of whole words");

		java.sql.Statement st = con.createStatement();
		ResultSet rs = null;

		st.execute("create table " + TableName + " ("
				+ "word_id int unsigned auto_increment,"
				+ "word blob not null,"
				+ "primary key (word_id)"
				+ ");");

		System.out.println("Table " + TableName + " created successfully");
	}

	public static SortedSet<String> getWholdWords(Vector<Wiki> vecDicData) {
		System.out.println("Starting make bag of whole words");
		SortedSet<String> testData = new TreeSet();
		for(DictionaryData dd : vecDicData) {
			ArrayList<String> content = dd.getContent();

			for(String str : content) {
				testData.add(str);
			}
		}
		return testData;
	}

	public static void insertWordVectorToDatabase (SortedSet<String> set) {		

		try {
			Connection con = null;
			con = DriverManager.getConnection("jdbc:mysql://knuwooseok.iptime.org", "mell03", "Natural");			

			java.sql.Statement st = con.createStatement();
			ResultSet rs = null;

			//rs = st.executeQuery("SHOW DATABASES");

			if(isWikidbExists(con)) {
				st.execute("use wikidb;");
			}
			else {
				System.out.println("can not find wikidb from database");				
				return;
			}

			if(isWordVectorTableExists(con)) {
				//TODO 지금은 DROP Table 을 하지만, 유저에게 물어봅시다.
				
				//st.execute("drop table " + TableName + ";");	
				//System.out.println("dropped table" + TableName); 
				
				return;
			}
			createWordVectorTable(con);			

			int count = 0;
			PreparedStatement pstmt = con.prepareStatement("insert into " + TableName +"(word) values (?)");
			for(String dd : set) {

				pstmt.setBytes(1, dd.getBytes());
				//pstmt.setString(1, dd);
				pstmt.execute();
				count++;

				if(count % 1000 == 0) {
					System.out.println("Processed " + count + " datas");
				}
			}



		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());

		}		
	}
	*/
	
	
	public static String deleteEscape(String Text) {
		int nExcapePos = Text.lastIndexOf('\n');
		String ret = Text.substring(0, nExcapePos);
		return ret;
	}
	
	/*
	 * makeWikiData for make Object File of Vector<Wikidata> from
	 * dumped Wikipedia xml file.
	 * or if WikiData file already exists, get object from it.
	 * @param XMLFilePath for Wiki dumped xml file
	 * @param DestFilePath for ArrayList<WikiData> File path
	 */
	
}



