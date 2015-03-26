import java.awt.List;
import java.util.HashMap;


public class IndexedData extends HashMap<String, Integer> {
	private long documentID;
	
	public IndexedData(long documentID) {
		super();
		this.documentID = documentID;
	}
	
	public long getDocumentID() {
		return documentID;
	}
}
