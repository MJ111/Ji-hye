import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


public class XmlReader {
	
	public String reader(int key) {
		String xml = new String();
		
		Table table;
		try {
			table = DatabaseBuilder.open(new File("./src/verbEntry.mdb")).getTable("dbo_vv_RawXML");
			
			for(Row row : table) {
				if (row.get("KEY").equals(key)) {
					xml = (String)row.get("XML");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}	
}
