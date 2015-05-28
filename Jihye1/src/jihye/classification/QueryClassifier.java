package jihye.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jihye.NLP.KeywordExtractor;

public class QueryClassifier {
	private KeywordExtractor keywordExtrator;
	private ArrayList<List<String>> tagWords;
	private static final List<String> personWord = Arrays.asList(ClassTag.CHARACTER.toString(), "인물", "사람","발핼인 ","화가", "음악가","작곡가", "철학자", "영화감독", "시인","작가", "정치인", "감독", "선교사", "실학자", "발명가","왕");
	private static final List<String> countryWord = Arrays.asList(ClassTag.COUNTRY.toString(), "나라", "섬나라");
	private static final List<String> lifeWord = Arrays.asList(ClassTag.LIFE.toString(), "생물","공룡", "나무", "식물", "곤충","철새","매화");
	private static final List<String> musicWord = Arrays.asList(ClassTag.MUSIC.toString(), "음악","곡", "타령", "교향곡", "창법", "악기","민요","관악");
	private static final List<String> literatureWord = Arrays.asList(ClassTag.LITERATURE.toString(), "문학","소설", "저서", "책", "신화", "서사시","역사서");
	private static final List<String> artWord = Arrays.asList(ClassTag.ART.toString(), "그림");
	private static final List<String> architectureWord = Arrays.asList(ClassTag.ARCHITECTURE.toString(), "탑", "건축물");
	private static final List<String> placeNameWord = Arrays.asList(ClassTag.PLACENAME.toString(), "지명","도시", "강", "유적", "본거지", "유적지", "지역", "바다", "곳", "강","수도","고개","대륙");
	private static final List<String> eventWord = Arrays.asList(ClassTag.EVENT.toString(), "사건","전투", "혁명", "대첩","전쟁");
	private static final List<String> foodWord = Arrays.asList(ClassTag.FOOD.toString(),"음식","채소");
	private static final List<String> scienceWord = Arrays.asList(ClassTag.SCIENCE.toString(), "비타민","호르몬", "원리", "지층", "핼설", "현상", "화석", "성분", "영양소", "기체", "원소", "기관","금속", "물질", "작용", "단백질","물질");
	
	public enum ClassTag {
		CHARACTER, COUNTRY, LIFE, SCIENCE, MUSIC, LITERATURE, ART, ARCHITECTURE, PLACENAME, EVENT, FOOD;
	}
	
	public QueryClassifier(KeywordExtractor keywordExtrator) {
		this.keywordExtrator = keywordExtrator;
		
		tagWords = new ArrayList<List<String>>();
		tagWords.add(personWord);
		tagWords.add(countryWord);
		tagWords.add(lifeWord);
		tagWords.add(musicWord);
		tagWords.add(literatureWord);
		tagWords.add(artWord);
		tagWords.add(architectureWord);
		tagWords.add(placeNameWord);
		tagWords.add(eventWord);
		tagWords.add(foodWord);
		tagWords.add(scienceWord);
	}
	
	public ClassTag classifyQuery(String lastNNG) {
		if (lastNNG != null) {
			for (List<String> word : tagWords) {
				if (word.contains(lastNNG)) {
					return ClassTag.valueOf(word.get(0));
				}
			}
		}
		return null;
	}
	
	public void classifyAllQueryData() {		
		String choiceProblems = null;
		String shortProblems = null;
		try {			
			choiceProblems = readChoiceFile("choice_problems");			
			shortProblems = readChoiceFile("short_problems");
			
			BufferedWriter out = new BufferedWriter(new FileWriter("analyzedPs.txt"));
			
			String choicePs[] = choiceProblems.split("\n");			
			for (int i=0; i<choicePs.length; i+=3) {
				keywordExtrator.analyzeDocument(choicePs[i]);
				String lastNNG = keywordExtrator.getLastNNG();
				
				if (lastNNG != null) {
					for (List<String> word : tagWords) {
						if (word.contains(lastNNG)) {
							out.write(choicePs[i]);
							out.newLine();
							out.write(word.get(0));
							out.newLine();
							break;
						}
					}
				}
			}		
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String readChoiceFile(String fileName) throws IOException {
		String fileString = new String();
		BufferedReader br = new BufferedReader(new FileReader("./resources/" + fileName + ".txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        fileString = sb.toString();
	    } finally {
	        br.close();
	    }
	    return fileString;
	}
}
