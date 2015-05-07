import java.util.ArrayList;


public class Main {
	public static void main(String args[]) {
		VerbKeyFinder verbKeyFinder = new VerbKeyFinder();
		XmlReader xmlReader = new XmlReader();
		XmlParser xmlParser = new XmlParser();
		
		int key = verbKeyFinder.finder("가감되다");
		
		ArrayList<String> results = xmlParser.parser(xmlReader.reader(key));
		for (String s : results) {
			System.out.println(s);
		}
	}
}
