package jihye.GUI;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import jihye.JihyeController;
import jihye.PS.ProblemData;
import jihye.PS.ResultData;
import jihye.TTS.TextToSpeech;

public class UserInterface {
	private MainFrame mainFrame;
	private ResultFrameWithGraph resultFrameWithGraph;
	private ResultFrameWithKeywords resultFrameWithKeywords;
	private JihyeController jihyeController;
	private String answerText;
	
	private MainChoice mainchoice;
	
	

	public UserInterface(JihyeController jc) {
		jihyeController = jc;

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("nimbusBase", new ColorUIResource(227, 227,
							220));
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

//		mainFrame = new MainFrame(this, true); 주석 처리 ..
		mainchoice = new MainChoice(this);
		
		
	}

	public void initResultScreen(ResultData resultData, boolean hasChoice) {
		mainFrame.setVisible(false);
		resultFrameWithGraph = new ResultFrameWithGraph(this, resultData, hasChoice);
		resultFrameWithGraph.setVisible(true);
		resultFrameWithKeywords = new ResultFrameWithKeywords(this, resultData);
		setAnswerString(resultData, hasChoice);
		readAnswerString();
	}

	public void requestSolveProblem(ProblemData problemData) {
		System.out.println("Solve problem");
		ResultData resultData = jihyeController.solve(problemData);
		initResultScreen(resultData, problemData.hasChoice());
	}

	public void showResultFrameWithGraph() {
		resultFrameWithGraph.setVisible(false);
		resultFrameWithKeywords.setVisible(true);
	}

	public void closeResultFrameWithKeywords() {
		resultFrameWithGraph.setVisible(true);
	}

	public void showMainFrame(boolean setmode) {
		mainFrame = new MainFrame(this, setmode);
		// 인자값을 넘겨주기 위해 showMainFrame 안으로 ..
		mainFrame.setVisible(true);
		
	}
	
	public void showMainChoiceFrame() {
		mainchoice.setVisible(true);
	}


	public void goMainFrame() {
		resultFrameWithGraph.dispose();
		resultFrameWithKeywords.dispose();
		
		mainFrame.setVisible(true);
	}

	public void setAnswerString(ResultData resultData, boolean hasChoice) {
		String newAnswerText = "";
		
		switch (resultData.getAnswer()) {
		case 1:
			newAnswerText = "정답은 일번 ";
		case 2:
			newAnswerText = "정답은 이번 ";
		case 3:
			newAnswerText = "정답은 삼번 ";
		case 4:
			newAnswerText = "정답은 사번 ";
		default :
			newAnswerText = "정답은 ";
		}
		
		newAnswerText += resultData.getAnswerString() + " 입니다";
		
		this.answerText = newAnswerText;
	}

	public void readAnswerString() {
		TextToSpeech textToSpeech = new TextToSpeech();
		textToSpeech.setText(answerText);
		textToSpeech.start();
	}

	public void terminateMainProgram() {
		System.out.println("Program terminated by user");
		System.exit(0);
	}
}