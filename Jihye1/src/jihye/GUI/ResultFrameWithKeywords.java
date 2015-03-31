package jihye.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import jihye.PS.ResultData;

public class ResultFrameWithKeywords extends javax.swing.JFrame {

	private ResultData resultData;

	private Point mouseDownCompCoords;

	public ResultFrameWithKeywords(UserInterface ui, ResultData resultData) {
		this.resultData = resultData;
		initComponents(ui);
	}

	private void initComponents(UserInterface ui) {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);

		mouseDownCompCoords = null;
		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				mouseDownCompCoords = null;
			}

			public void mousePressed(MouseEvent e) {
				mouseDownCompCoords = e.getPoint();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}

			public void mouseDragged(MouseEvent e) {
				Point currCoords = e.getLocationOnScreen();
				setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y
						- mouseDownCompCoords.y);
			}
		});

		mainJPanel = new javax.swing.JPanel();
		problemJLabel = new javax.swing.JLabel();
		choiceJLabel = new javax.swing.JLabel();
		choiceKeywordJTabbedPane = new javax.swing.JTabbedPane();
		choice1JPanel = new javax.swing.JPanel();
		choice1JLabel = new javax.swing.JTextArea();
		choice2JPanel = new javax.swing.JPanel();
		choice2JLabel = new javax.swing.JTextArea();
		choice3JPanel = new javax.swing.JPanel();
		choice3JLabel = new javax.swing.JTextArea();
		choice4JPanel = new javax.swing.JPanel();
		choice4JLabel = new javax.swing.JTextArea();
		btnJPanel = new javax.swing.JPanel();
		backJButton = new ImageButton("back.png");
		homeJButton = new ImageButton("home.png");
		wikiJButton = new ImageButton("wiki.png");
		problemKeywordJPanel = new javax.swing.JPanel();
		problemKeywordJLabel = new javax.swing.JTextArea();

		makeMultilineLabel(problemKeywordJLabel);
		makeMultilineLabel(choice1JLabel);
		makeMultilineLabel(choice2JLabel);
		makeMultilineLabel(choice3JLabel);
		makeMultilineLabel(choice4JLabel);

		Font labelFont = null;
		Font keywordFont = null;
		try {
			labelFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(
					"resources/NANUMGOTHICBOLD.TTF"));
			keywordFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(
					"resources/NANUMGOTHIC.TTF"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		labelFont = labelFont.deriveFont(Font.PLAIN, 20);

		problemJLabel.setFont(labelFont);
		choiceJLabel.setFont(labelFont);

		keywordFont = keywordFont.deriveFont(Font.PLAIN, 16);

		problemKeywordJLabel.setFont(keywordFont);
		choice1JLabel.setFont(keywordFont);
		choice2JLabel.setFont(keywordFont);
		choice3JLabel.setFont(keywordFont);
		choice4JLabel.setFont(keywordFont);

		keywordFont = keywordFont.deriveFont(Font.PLAIN, 14);

		choiceKeywordJTabbedPane.setFont(keywordFont);

		mainJPanel.setBackground(new java.awt.Color(227, 227, 220));
		mainJPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 106,
				79), 1));

		problemJLabel.setForeground(new Color(44, 57, 65));
		choice1JLabel.setForeground(new Color(44, 57, 65));
		problemJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		problemJLabel.setText("문제 키워드");

		choiceJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		choiceJLabel.setText("보기 키워드");

		// choiceKeywordJTabbedPane
		// .setBackground(new java.awt.Color(231, 233, 230));

		// choice1JPanel.setBackground(new java.awt.Color(231, 233, 230));

		choice1JLabel.setText(resultData.matchKeywords.get(0));

		javax.swing.GroupLayout choice1JPanelLayout = new javax.swing.GroupLayout(
				choice1JPanel);
		choice1JPanel.setLayout(choice1JPanelLayout);
		choice1JPanelLayout.setHorizontalGroup(choice1JPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(choice1JLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE, 533,
						Short.MAX_VALUE));
		choice1JPanelLayout.setVerticalGroup(choice1JPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(choice1JLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE, 265,
						Short.MAX_VALUE));

		choiceKeywordJTabbedPane.addTab(resultData.choices.get(0),
				choice1JPanel);

		// choice2JPanel.setBackground(new java.awt.Color(231, 233, 230));

		choice2JLabel.setText(resultData.matchKeywords.get(1));

		javax.swing.GroupLayout choice2JPanelLayout = new javax.swing.GroupLayout(
				choice2JPanel);
		choice2JPanel.setLayout(choice2JPanelLayout);
		choice2JPanelLayout
				.setHorizontalGroup(choice2JPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 533, Short.MAX_VALUE)
						.addGroup(
								choice2JPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(
												choice2JLabel,
												javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												533, Short.MAX_VALUE)));
		choice2JPanelLayout
				.setVerticalGroup(choice2JPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 265, Short.MAX_VALUE)
						.addGroup(
								choice2JPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(
												choice2JLabel,
												javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												265, Short.MAX_VALUE)));

		choiceKeywordJTabbedPane.addTab(resultData.choices.get(1),
				choice2JPanel);

		// choice3JPanel.setBackground(new java.awt.Color(231, 233, 230));

		choice3JLabel.setText(resultData.matchKeywords.get(2));

		javax.swing.GroupLayout choice3JPanelLayout = new javax.swing.GroupLayout(
				choice3JPanel);
		choice3JPanel.setLayout(choice3JPanelLayout);
		choice3JPanelLayout.setHorizontalGroup(choice3JPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(choice3JLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE, 533,
						Short.MAX_VALUE));
		choice3JPanelLayout.setVerticalGroup(choice3JPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(choice3JLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE, 265,
						Short.MAX_VALUE));

		choiceKeywordJTabbedPane.addTab(resultData.choices.get(2),
				choice3JPanel);

		// choice4JPanel.setBackground(new java.awt.Color(231, 233, 230));

		choice4JLabel.setText(resultData.matchKeywords.get(3));

		javax.swing.GroupLayout choice4JPanelLayout = new javax.swing.GroupLayout(
				choice4JPanel);
		choice4JPanel.setLayout(choice4JPanelLayout);
		choice4JPanelLayout.setHorizontalGroup(choice4JPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(choice4JLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE, 533,
						Short.MAX_VALUE));
		choice4JPanelLayout.setVerticalGroup(choice4JPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(choice4JLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE, 265,
						Short.MAX_VALUE));

		choiceKeywordJTabbedPane.addTab(resultData.choices.get(3),
				choice4JPanel);

		btnJPanel.setBackground(new java.awt.Color(227, 227, 220));
		btnJPanel.setLayout(new java.awt.GridLayout(1, 3, 56, 0));

		btnJPanel.add(backJButton);
		btnJPanel.add(homeJButton);
		btnJPanel.add(wikiJButton);

		// problemKeywordJPanel.setBackground(new java.awt.Color(231, 233,
		// 230));

		problemKeywordJLabel.setText(resultData.analyzedProblem);

		javax.swing.GroupLayout problemKeywordJPanelLayout = new javax.swing.GroupLayout(
				problemKeywordJPanel);
		problemKeywordJPanel.setLayout(problemKeywordJPanelLayout);
		problemKeywordJPanelLayout
				.setHorizontalGroup(problemKeywordJPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(problemKeywordJLabel,
								javax.swing.GroupLayout.DEFAULT_SIZE, 537,
								Short.MAX_VALUE));
		problemKeywordJPanelLayout.setVerticalGroup(problemKeywordJPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						problemKeywordJPanelLayout
								.createSequentialGroup()
								.addComponent(problemKeywordJLabel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										224,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)));

		javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(
				mainJPanel);
		mainJPanel.setLayout(mainJPanelLayout);
		mainJPanelLayout
				.setHorizontalGroup(mainJPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(problemJLabel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(choiceJLabel,
								javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								mainJPanelLayout
										.createSequentialGroup()
										.addContainerGap(89, Short.MAX_VALUE)
										.addComponent(
												choiceKeywordJTabbedPane,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												538,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(89, 89, 89))
						.addGroup(
								mainJPanelLayout
										.createSequentialGroup()
										.addGroup(
												mainJPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																mainJPanelLayout
																		.createSequentialGroup()
																		.addGap(45,
																				45,
																				45)
																		.addComponent(
																				btnJPanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				617,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																mainJPanelLayout
																		.createSequentialGroup()
																		.addGap(89,
																				89,
																				89)
																		.addComponent(
																				problemKeywordJPanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		mainJPanelLayout
				.setVerticalGroup(mainJPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainJPanelLayout
										.createSequentialGroup()
										.addGap(20, 20, 20)
										.addComponent(
												problemJLabel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												36,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(
												problemKeywordJPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(20, 20, 20)
										.addComponent(
												choiceJLabel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												34,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(
												choiceKeywordJTabbedPane,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												260,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												38, Short.MAX_VALUE)
										.addComponent(
												btnJPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												43,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(27, 27, 27)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		setButtonEventListeners(ui);
		pack();

		setLocationRelativeTo(null);
	}

	private void makeMultilineLabel(JTextArea area) {
		area.setBorder(BorderFactory.createEmptyBorder());
		area.setEditable(false);
		// area.setOpaque(false);
		if (area instanceof JTextArea) {
			((JTextArea) area).setWrapStyleWord(true);
			((JTextArea) area).setLineWrap(true);
		}
	}

	private String getResultStr() {
		return resultData.choices.get(resultData.getAnswer() - 1);
	}

	public void setButtonEventListeners(final UserInterface ui) {
		backJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.closeResultFrameWithKeywords();
				dispose();
			}
		});

		homeJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.goMainFrame();
			}
		});

		wikiJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String anwserString = getResultStr();
				openUrl("http://ko.wikipedia.org/wiki/" + anwserString);
			}
		});
	}

	private void openUrl(String url) {
		String os = System.getProperty("os.name");
		Runtime runtime = Runtime.getRuntime();
		try {
			// Block for Windows Platform
			if (os.startsWith("Windows")) {
				String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
				Process p = runtime.exec(cmd);
			}
		} catch (Exception x) {
			System.err.println("Exception occurd while invoking Browser!");
			x.printStackTrace();
		}
	}

	// Variables declaration
	private javax.swing.JButton backJButton;
	private javax.swing.JPanel btnJPanel;
	private javax.swing.JTextArea choice1JLabel;
	private javax.swing.JPanel choice1JPanel;
	private javax.swing.JTextArea choice2JLabel;
	private javax.swing.JPanel choice2JPanel;
	private javax.swing.JTextArea choice3JLabel;
	private javax.swing.JPanel choice3JPanel;
	private javax.swing.JTextArea choice4JLabel;
	private javax.swing.JPanel choice4JPanel;
	private javax.swing.JLabel choiceJLabel;
	private javax.swing.JTabbedPane choiceKeywordJTabbedPane;
	private javax.swing.JButton homeJButton;
	private javax.swing.JPanel mainJPanel;
	private javax.swing.JLabel problemJLabel;
	private javax.swing.JTextArea problemKeywordJLabel;
	private javax.swing.JPanel problemKeywordJPanel;
	private javax.swing.JButton wikiJButton;
	// End of variables declaration
}