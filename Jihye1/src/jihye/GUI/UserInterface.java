package jihye.GUI;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import jihye.JihyeController;
import jihye.PS.ProblemData;
import jihye.PS.ResultData;
import jihye.TTS.TextToSpeech;

public class UserInterface {
	MainFrame mainFrame;
	ResultFrameWithGraph resultFrameWithhGraph;
	ResultFrameWithKeywords resultFrameWithKeywords;
	JihyeController jihyeController;
	String answerInformationString;

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
		mainFrame.setVisible(true);
	}

	// 리절트 스크린 초기화 (마찬가지)
	public void initResultScreens(ResultData resultData) {
		mainFrame.setVisible(false);
		setAnswerString(resultData);
		resultFrameWithhGraph = new ResultFrameWithGraph(this, resultData);
		resultFrameWithhGraph.setVisible(true);
		resultFrameWithKeywords = new ResultFrameWithKeywords(this, resultData);
		readAnswerString();
	}

	// 각종 이벤트 리스너들
	public void requestSolve(ProblemData problemData) {
		System.out.println("Solve problem");
		ResultData resultData = jihyeController.solve(problemData);
		initResultScreens(resultData);
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
		System.out.println("Program terminate by user");
		System.exit(0);
	}
}