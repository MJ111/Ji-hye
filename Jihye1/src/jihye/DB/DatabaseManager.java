package jihye.DB;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

public class DatabaseManager
{

	private Connection mConnection;
	private String DatabaseName = "wikidb";

	public DatabaseManager()
	{
		try
		{
			mConnection = null;
			mConnection = DriverManager.getConnection(
					"jdbc:mysql://knuwooseok.iptime.org", "cheolsu",
					"NaturalIntelligence");
			log("Database connected successfully");
		} catch (SQLException e2)
		{
			e2.printStackTrace();
		}
	}

	public ArrayList<String> getDictionary()
	{
		ArrayList<String> dictionary = new ArrayList<String>();
		dictionary.add("don't read me");

		try
		{
			java.sql.Statement statement = mConnection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select * from " + DatabaseName +".word_vectors;");

			while (resultSet != null && resultSet.next())
			{
				dictionary.add(getStringFromBLOB(resultSet.getBlob(2)));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return dictionary;
	}

	public String getStringFromBLOB(Blob blob)
	{
		try
		{
			InputStream is = blob.getBinaryStream();
			byte[] byteData = new byte[is.available() + 1];
			is.read(byteData);
			return new String(byteData, "UTF-8");
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		} catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getRedirectedPageTitle(String page_title)
	{
		try
		{
			int CurrentPageId = 0;
			java.sql.Statement statement = mConnection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select page_id from " + DatabaseName + ".page where page_title like '"
							+ page_title + "';");
			if (resultSet != null && resultSet.next())
				CurrentPageId = resultSet.getInt(1);

			resultSet = statement
					.executeQuery("select rd_title from " + DatabaseName + ".redirect where rd_from="
							+ CurrentPageId + ";");
			if (resultSet != null && resultSet.next())
			{
				return getStringFromBLOB(resultSet.getBlob(1)).trim();
			} else
				return null;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public boolean isRedirectedPage(String page_title)
	{
		try
		{
			java.sql.Statement statement = mConnection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select page_is_redirect from " + DatabaseName + ".page where page_title='"
							+ page_title + "';");

			if (resultSet != null && resultSet.next()
					&& resultSet.getInt(1) == 1)
			{
				return true;
			} else
				return false;

		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<WikipediaPage> getPagesFromTitle(String page_title)
	{
		ArrayList<WikipediaPage> pages = new ArrayList<WikipediaPage>();

		try
		{
			java.sql.Statement statement = mConnection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select page_title, page_latest from " + DatabaseName + ".page where page_title like '%"
							+ page_title + "%' and page_is_redirect=0 and page_namespace=0;");

			while (resultSet != null && resultSet.next())
			{
				String title = getStringFromBLOB(resultSet.getBlob(1));
				String text = getWikipediaTextFromOldID(resultSet.getInt(2));

				WikipediaPage wikipediaPage = new WikipediaPage(title, text);
				pages.add(wikipediaPage);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return pages;
	}

	public String getWikipediaTextFromOldID(int old_id)
	{
		try
		{
			java.sql.Statement statement = mConnection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("select old_text from " + DatabaseName + ".text where old_id="
							+ old_id + ";");

			if (resultSet != null && resultSet.next())
			{
				return getStringFromBLOB(resultSet.getBlob(1));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void log(String msg)
	{
		System.out.println("[DATABASEMANAGER]" + msg);
	}

}