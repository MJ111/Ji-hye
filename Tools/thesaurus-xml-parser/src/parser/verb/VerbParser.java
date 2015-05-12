package parser.verb;

import java.util.ArrayList;

public class VerbParser {
	private VerbKeyFinder verbKeyFinder;
	private XmlReader xmlReader;
	private XmlParser xmlParser;
	
	public VerbParser() {
		verbKeyFinder = new VerbKeyFinder();
		xmlReader = new XmlReader();
		xmlParser = new XmlParser();
	}
	
	public ArrayList<String> getStructure(String verb) {
		ArrayList<String> structures = new ArrayList<String>();
		ArrayList<Integer> keys = verbKeyFinder.finder(verb);
		if(keys != null && keys.size() != 0) {
			for(Integer key : keys) {
				structures.addAll(xmlParser.parser(xmlReader.reader(key)));
			}
		}
		return structures;
	}
}
