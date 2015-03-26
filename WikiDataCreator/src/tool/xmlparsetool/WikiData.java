package tool.xmlparsetool;
import java.io.Serializable;

public class WikiData implements Serializable{
	
	private String strTitle;
	private String strText;
	
	public WikiData() {
	}
	
	public WikiData(String Title, String Text){
		strTitle = Title;
		strText = Text;
	}
	
	public String getTitle() {
		return strTitle;
	}
	
	public String getText() {
		return strText;
	}
}
