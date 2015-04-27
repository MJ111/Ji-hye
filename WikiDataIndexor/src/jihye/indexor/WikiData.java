package jihye.indexor;

import java.io.Serializable;

public class WikiData implements Serializable{
	
	private String strTitle;
	private String strText;
	private String strDocumentID;
	
	public WikiData() {
	}
	
	public WikiData(String Title, String Text, String ID){
		strTitle = Title;
		strText = Text;
		strDocumentID = ID;
	}
	
	public String getTitle() {
		return strTitle;
	}
	
	public String getText() {
		return strText;
	}
	
	public String getDocumentID() {
		return strDocumentID;
	}
}