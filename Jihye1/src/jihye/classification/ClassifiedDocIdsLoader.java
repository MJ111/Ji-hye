package jihye.classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import jihye.classification.QueryClassification.ClassTag;

public class ClassifiedDocIdsLoader {
	private ArrayList<Integer> characterDocIds;
	private ArrayList<Integer> eventDocIds;
	private ArrayList<Integer> architectureDocIds;
	private ArrayList<Integer> countryDocIds;
	private ArrayList<Integer> lifeDocIds;
	private ArrayList<Integer> musicDocIds;
	private ArrayList<Integer> scienceDocIds;
	private ArrayList<Integer> literatureDocIds;
	private ArrayList<Integer> artDocIds;
	private ArrayList<Integer> placeNameDocIds;
	private ArrayList<Integer> foodDocIds;
	
	public ArrayList<Integer> loadMatchingClassDocIdList(ClassTag classTag) {		
		switch (classTag) {
		case CHARACTER:
			return getDocIdList(characterDocIds, "37928");
		case ARCHITECTURE:
			return getDocIdList(architectureDocIds, "240606");
		case COUNTRY:
			return getDocIdList(countryDocIds, "30737");
		case LIFE:
			return getDocIdList(lifeDocIds, "9143");
		case MUSIC:
			return getDocIdList(musicDocIds, "11814");
		case SCIENCE:
			return getDocIdList(scienceDocIds, "10480");
		case LITERATURE:
			return getDocIdList(literatureDocIds, "4392");
		case ART:
			return getDocIdList(artDocIds, "13227");
		case PLACENAME:
			return getDocIdList(placeNameDocIds, "343290");
		case EVENT:
			return getDocIdList(eventDocIds, "254852");
		case FOOD:
			return getDocIdList(foodDocIds, "10475");
		default:
			return null;
		}
	}
	
	private ArrayList<Integer> getDocIdList(ArrayList<Integer> docIds, String classId) {
		if (docIds != null) {
			return docIds;
		} else {
			return readClassifiedDocIdFile(classId);
		}
	}
	
	private ArrayList<Integer> readClassifiedDocIdFile(String fileName) {
		ArrayList<Integer> docIds = new ArrayList<Integer>(); 
		
	    try {
			BufferedReader br = new BufferedReader(new FileReader("./resources/" + fileName + ".txt"));
			
	        String line = br.readLine();
	        while (line != null) {
		        docIds.add(Integer.parseInt(line));
	            line = br.readLine();
	        }

	        br.close();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docIds;
	}
}
