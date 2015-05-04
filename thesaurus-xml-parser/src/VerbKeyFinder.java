import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class VerbKeyFinder {
	public int finder(String verb) {
		int key = 1;
		
		Table table;
		try {
			table = DatabaseBuilder.open(new File("./src/verbEntry.mdb")).getTable("dbo_vvsuperEntry");
			
			for(Row row : table) {
				if (row.get("orthtext").equals(verb)) {
					key = row.getInt("superEntryKey");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}	
}
