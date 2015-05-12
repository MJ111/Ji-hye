package parser.verb;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class VerbKeyFinder {
	public ArrayList<Integer> finder(String verb) {
		ArrayList<Integer> keys = new ArrayList<Integer>();
		
		Table table;
		try {
			table = DatabaseBuilder.open(new File("./src/verbEntry.mdb")).getTable("dbo_vvsuperEntry");
			
			for(Row row : table) {
				if (row.get("orthtext").equals(verb)) {
					keys.add(row.getInt("superEntryKey"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keys;
	}	
}
