package jihye.DB;

public class WikipediaPage {
	private String Title;
	private String Text;

	public WikipediaPage(String Title, String Text) {
		this.Title = Title;
		this.Text = Text;
	}

	public String getTitle() {
		return Title;
	}

	public String getText() {
		return Text;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public void setText(String Text) {
		this.Text = Text;
	}
}