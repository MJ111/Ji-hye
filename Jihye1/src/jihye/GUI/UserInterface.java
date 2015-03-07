package jihye.GUI;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

import jihye.JihyeController;
import jihye.PS.ProblemData;
import jihye.PS.ResultData;
import jihye.TTS.TextToSpeech;

public class UserInterface
{
	MainFrame mainFrame;
	ResultFrame1 resultFrame1;
	ResultFrame2 resultFrame2;
	JihyeController jihyeController;
	String readString;

	public UserInterface(JihyeController jc)
	{
		jihyeController = jc;

		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("nimbusBase", new ColorUIResource(227, 227, 220));
					break;
				}
			}
		} catch (Exception e)
		{
			try
			{
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}

		mainFrame = new MainFrame(this);
		mainFrame.setVisible(true);
	}

	// 리절트 스크린 초기화 (마찬가지)
	public void initResultScreens(ResultData resultData)
	{
		mainFrame.setVisible(false);
		setReadString(resultData);
		resultFrame1 = new ResultFrame1(this, resultData);
		resultFrame1.setVisible(true);
		resultFrame2 = new ResultFrame2(this, resultData);
		onReadAnswer();
	}

	// 각종 이벤트 리스너들
	public void onRequestSolve(ProblemData problemData)
	{
		System.out.println("Solve problem");
		ResultData resultData = jihyeController.solve(problemData);
		initResultScreens(resultData);
	}

	public void onViewDetail()
	{
		resultFrame1.setVisible(false);
		resultFrame2.setVisible(true);
	}

	public void onCloseDetail()
	{
		resultFrame1.setVisible(true);
	}

	public void onResultScreenClosed()
	{
		mainFrame.setVisible(true);
	}

	public void onGoHome()
	{
		resultFrame1.dispose();
		resultFrame2.dispose();
		mainFrame.setVisible(true);
	}

	public void setReadString(ResultData resultData)
	{
		readString = "";

		switch (resultData.getAnswer())
		{
		case 1:
			readString = "정답은 일번  " + resultData.choices.get(0) + "입니다";
			break;
		case 2:
			readString = "정답은 이번  " + resultData.choices.get(1) + "입니다";
			break;
		case 3:
			readString = "정답은 삼번  " + resultData.choices.get(2) + "입니다";
			break;
		case 4:
			readString = "정답은 사번  " + resultData.choices.get(3) + "입니다";
			break;
		}
	}

	public void onReadAnswer()
	{
		TextToSpeech textToSpeech = new TextToSpeech();
		textToSpeech.setText(readString);
		textToSpeech.start();
	}

	public void terminate()
	{
		System.out.println("Program terminate by user");
		System.exit(0);
	}

}
