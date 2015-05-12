package jihye.GUI;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import jihye.JihyeController;
import jihye.PS.ProblemData;
import jihye.PS.ResultData;
import jihye.TTS.TextToSpeech;

public class UserInterface {
	private MainFrame mainFrame;
	private ChoiceResultFrameWithGraph resultFrameWithhGraph;
	private ChoiceResultFrameWithKeywords resultFrameWithKeywords;
	private NoChoiceResultFrame noChoiceResultFrame;
	private JihyeController jihyeController;
	private String answerInformationString;

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

		mainFrame = new MainFrame(this);
	}

	public void initChoiceResultScreens(ResultData resultData) {
		mainFrame.setVisible(false);
		setAnswerString(resultData);
		resultFrameWithhGraph = new ChoiceResultFrameWithGraph(this, resultData);
		resultFrameWithhGraph.setVisible(true);
		resultFrameWithKeywords = new ChoiceResultFrameWithKeywords(this, resultData);
		readAnswerString();
	}
	
	public void initNoChoiceResultScreens(ResultData resultData) {
		mainFrame.setVisible(false);
		noChoiceResultFrame = new NoChoiceResultFrame(this);
		noChoiceResultFrame.setVisible(true);
	}

	// 각종 이벤트 리스너들
	public void requestSolveChoiceProblem(ProblemData problemData) {
		System.out.println("Solve problem");
		ResultData resultData = jihyeController.solve(problemData);
		initChoiceResultScreens(resultData);
	}
	
	public void requestSolveNoChoiceProblem(ProblemData problemData) {
		System.out.println("Solve no choice problem");
		ResultData resultData = jihyeController.solve(problemData);		
		initNoChoiceResultScreens(resultData);
	}

	public void showResultFrameWithGraph() {
		resultFrameWithhGraph.setVisible(false);
		resultFrameWithKeywords.setVisible(true);
	}

	public void closeResultFrameWithKeywords() {
		resultFrameWithhGraph.setVisible(true);
	}

	public void showMainFrame() {
		mainFrame.setVisible(true);
	}

	public void goMainFrame() {
		//Dispose Result Frames
		resultFrameWithhGraph.dispose();
		resultFrameWithKeywords.dispose();
		
		//Make main frame visible
		mainFrame.setVisible(true);
	}

	public void setAnswerString(ResultData resultData) {
		answerInformationString = "";

		switch (resultData.getAnswer()) {
		case 1:
			answerInformationString = "정답은 일번  " + resultData.choices.get(0) + "입니다";
			break;
		case 2:
			answerInformationString = "정답은 이번  " + resultData.choices.get(1) + "입니다";
			break;
		case 3:
			answerInformationString = "정답은 삼번  " + resultData.choices.get(2) + "입니다";
			break;
		case 4:
			answerInformationString = "정답은 사번  " + resultData.choices.get(3) + "입니다";
			break;
		}
	}

	public void readAnswerString() {
		TextToSpeech textToSpeech = new TextToSpeech();
		textToSpeech.setText(answerInformationString);
		textToSpeech.start();
	}

	public void terminateMainProgram() {
		System.out.println("Program terminated by user");
		System.exit(0);
	}
}