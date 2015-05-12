package jihye.DB;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

	private static final String DATABASE_NAME = "web_wikidb";
	private Connection connection;

	public DatabaseManager() {
		try {
			connection = null;
			connection = DriverManager.getConnection(
					"jdbc:mysql://knuwooseok.iptime.org", "cheolsu",
					"NaturalIntelligence");
			log("Database connected successfully");
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	public ArrayList<String> getDictionary() {
		ArrayList<String> dictionary = new ArrayList<String>();
		dictionary.add("don't read me");

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from "
					+ DATABASE_NAME + ".word_vectors;");

			while (resultSet != null && resultSet.next()) {
				dictionary.add(getStringFromBLOB(resultSet.getBlob(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dictionary;
	}

	public String getStringFromBLOB(Blob blob) {
		try {
			InputStream is = blob.getBinaryStream();
			byte[] byteData = new byte[is.available() + 1];
			is.read(byteData);
			return new String(byteData, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getRedirectedPageTitle(String page_title) {
		try {
			int CurrentPageId = 0;
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select page_id from "
					+ DATABASE_NAME + ".page where page_title like '"
					+ page_title + "';");
			if (resultSet != null && resultSet.next())
				CurrentPageId = resultSet.getInt(1);

			resultSet = statement.executeQuery("select rd_title from "
					+ DATABASE_NAME + ".redirect where rd_from=" + CurrentPageId
					+ ";");
			if (resultSet != null && resultSet.next()) {
				return getStringFromBLOB(resultSet.getBlob(1)).trim();
			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isRedirectedPage(String page_title) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select page_is_redirect from "
							+ DATABASE_NAME + ".page where page_title='"
							+ page_title + "';");

			if (resultSet != null && resultSet.next()
					&& resultSet.getInt(1) == 1) {
				return true;
			} else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public WikipediaPage getPageFromTitle(String page_title) {
		WikipediaPage page = null;
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select page_title, page_latest from "
							+ DATABASE_NAME + ".page where page_title like '"
							+ page_title
							+ "' and page_is_redirect=0 and page_namespace=0;");

			if (resultSet != null && resultSet.next()) {
				String title = getStringFromBLOB(resultSet.getBlob(1));
				String text = getWikipediaTextFromOldID(resultSet.getInt(2));
				page = new WikipediaPage(title, text);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return page;
	}

	public ArrayList<WikipediaPage> getPagesFromTitle(String page_title) {
		ArrayList<WikipediaPage> pages = new ArrayList<WikipediaPage>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select page_title, page_latest from "
							+ DATABASE_NAME + ".page where page_title like '%"
							+ page_title
							+ "%' and page_is_redirect=0 and page_namespace=0;");

			while (resultSet != null && resultSet.next()) {
				String title = getStringFromBLOB(resultSet.getBlob(1));
				String text = getWikipediaTextFromOldID(resultSet.getInt(2));

				WikipediaPage wikipediaPage = new WikipediaPage(title, text);
				pages.add(wikipediaPage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pages;
	}
	
	public ArrayList<String> getPageTitlesFromPageIDs(int[] page_id) {
		ArrayList<String> titles = new ArrayList<String>();
		
		if(page_id == null) return titles;
		
		for(int ids : page_id) {
			titles.add(getPageFromPageID(ids).getTitle());
		}
		
		return titles;
	}
	
	public WikipediaPage getPageFromPageID(int page_id) {
		int page_latest = 0;
		String page_title = null;
		String text = null;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select page_title, page_latest from "
					+ DATABASE_NAME + ".page where page_id=" + page_id +";");
			
			if(resultSet != null && resultSet.next()) {
				page_title = resultSet.getString(1);
				page_latest = resultSet.getInt(2);
			}
			statement.close();
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		text = getWikipediaTextFromOldID(page_latest);
		
		WikipediaPage wp = new WikipediaPage(page_title, text);		
		
		return wp;
	}

	public String getWikipediaTextFromOldID(int old_id) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select old_text from " + DATABASE_NAME
							+ ".text where old_id=" + old_id + ";");

			if (resultSet != null && resultSet.next()) {
				return getStringFromBLOB(resultSet.getBlob(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void log(String msg) {
		System.out.println("[DATABASEMANAGER]" + msg);
	}
}