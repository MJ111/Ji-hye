package jihye.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jihye.NLP.KeywordExtrator;

public class QueryClassification {
	private KeywordExtrator keywordExtrator;
	private ArrayList<List<String>> tagWords;
	private static final List<String> personWord = Arrays.asList(ClassTag.CHARACTER.getClassTag(), "사람","발핼인 ","화가", "음악가","작곡가", "철학자", "영화감독", "시인","작가", "정치인", "감독", "선교사", "실학자", "발명가","왕");
	private static final List<String> countryWord = Arrays.asList(ClassTag.COUNTRY.getClassTag(), "섬나라");
	private static final List<String> lifeWord = Arrays.asList(ClassTag.LIFE.getClassTag(), "공룡", "나무", "식물", "곤충","철새","매화");
	private static final List<String> musicWord = Arrays.asList(ClassTag.MUSIC.getClassTag(), "곡", "타령", "교향곡", "창법", "악기","민요");
	private static final List<String> literatureWord = Arrays.asList(ClassTag.LITERATURE.getClassTag(), "소설", "저서", "책", "신화", "서사시","역사서");
	private static final List<String> artWord = Arrays.asList(ClassTag.ART.getClassTag(), "그림");
	private static final List<String> architectureWord = Arrays.asList(ClassTag.ARCHITECTURE.getClassTag(), "탑", "건축물");
	private static final List<String> placeNameWord = Arrays.asList(ClassTag.PLACENAME.getClassTag(), "도시", "강", "유적", "본거지", "유적지", "지역", "바다", "곳", "강","수도","고개","대륙");
	private static final List<String> eventWord = Arrays.asList(ClassTag.EVENT.getClassTag(), "전투", "혁명", "대첩","전쟁");
	private static final List<String> foodWord = Arrays.asList(ClassTag.FOOD.getClassTag());
	private static final List<String> scienceWord = Arrays.asList(ClassTag.SCIENCE.getClassTag(), "비타민","호르몬", "원리", "지층", "핼설", "현상", "화석", "성분", "영양소", "기체", "원소", "기관","금속", "물질", "작용", "단백질","물질");
	
	public enum ClassTag {
		CHARACTER("인물"), COUNTRY("나라"), LIFE("생물"), SCIENCE("과학"), MUSIC("음악"), LITERATURE("문학"), ART("미술"), ARCHITECTURE("건축"), PLACENAME("지명"), EVENT("사건"), FOOD("음식");
		
		private String classTag;
		
		ClassTag (String classTag) {
			this.classTag = classTag;
		}
		
		public String getClassTag() {
			return this.classTag;
		}
	}
	
	public QueryClassification(KeywordExtrator keywordExtrator) {
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
	
	public ClassTag classifyQuery(String query) {
		ArrayList<String> NNGs = keywordExtrator.analyzeNNG(query);
		
		if (NNGs.size() > 0) {
			for (List<String> word : tagWords) {
				if (word.contains(NNGs.get(NNGs.size()-1))) {
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
				ArrayList<String> NNGs = keywordExtrator.analyzeNNG(choicePs[i]);
				
				if (NNGs.size() > 0) {
					for (List<String> word : tagWords) {
						if (word.contains(NNGs.get(NNGs.size()-1))) {
							out.write(choicePs[i]);
							out.newLine();
							out.write(word.get(0));
							out.newLine();
							break;
						}
					}
				}
			}
			
//			String shortPs[] = shortProblems.split("\n"); 
//			for (int i=0; i<shortPs.length; i+=2) {
//				ArrayList<String> NNGs = keywordExtrator.analyzeNNG(shortPs[i]);
//			}			
			
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
