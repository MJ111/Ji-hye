package jihye.DB;

public class WikipediaPage {
	private String pageTitle;
	private String pageContent;

	public WikipediaPage(String Title, String Text) {
		this.pageTitle = Title;
		this.pageContent = Text;
	}

	public String getTitle() {
		return pageTitle;
	}

	public String getText() {
		return pageContent;
	}

	public void setTitle(String Title) {
		this.pageTitle = Title;
	}

	public void setText(String Text) {
		this.pageContent = Text;
	}
}