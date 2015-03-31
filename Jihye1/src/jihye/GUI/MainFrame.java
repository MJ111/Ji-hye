package jihye.GUI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import jihye.PS.ProblemData;

public class MainFrame extends javax.swing.JFrame {
	private Point mouseDownCompCoords = null;
	private Font hintFont;
	private Boolean noChoiceModeFlag;
	private ActionListener noChoiceActionListener;
	private ActionListener choiceActionListener;

	/**
	 * Creates new form frame1
	 */
	public MainFrame(UserInterface ui) {
		noChoiceModeFlag = true;
		initComponents();
		setButtonEventListeners(ui);
	}
	
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);
		
		setMouseListener();

		mainJPanel = new javax.swing.JPanel();
		problemJScrollPane = new javax.swing.JScrollPane();
		problemTextArea = new javax.swing.JTextArea();
		problemJPanel = new javax.swing.JPanel();
		choice1TextField = new javax.swing.JTextField();
		choice2TextField = new javax.swing.JTextField();
		choice3TextField = new javax.swing.JTextField();
		choice4TextField = new javax.swing.JTextField();
		btnJPanel = new javax.swing.JPanel();
//		closeBtn = new ImageButton("close.png");
//		solveBtn = new ImageButton("jihye.png");
//		refreshBtn = new ImageButton("refresh.png");
		closeBtn = new Button("닫기");
		solveBtn = new Button("문제풀기");
		refreshBtn = new Button("새로고침");
		changeModeButton = new Button("모드변경");
		
		mainJPanel.setBackground(new Color(227, 227, 220));
		mainJPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 106,
				79), 1));

		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(
					"resources/NANUMGOTHIC.TTF"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		problemJScrollPane.setBorder(null);
		problemTextArea.setLineWrap(true);
		problemTextArea.setColumns(30);
		problemTextArea.setRows(10);
		problemTextArea.setBackground(new Color(255, 255, 255));
		problemTextArea.setBorder(BorderFactory.createLineBorder(new Color(242,
				108, 99), 1));
		setProblemTextAreaTabAction();
		setFocusAction(problemTextArea, "문제를 입력해주세요^^", font, 16);

		hintFont = font.deriveFont(Font.ITALIC, 14);
		setTextField(choice1TextField, hintFont, "1번");
		setTextField(choice2TextField, hintFont, "2번");
		setTextField(choice3TextField, hintFont, "3번");
		setTextField(choice4TextField, hintFont, "4번");
		
		choice1TextField.setVisible(false);
		choice2TextField.setVisible(false);
		choice3TextField.setVisible(false);
		choice4TextField.setVisible(false);

		problemJScrollPane.setViewportView(problemTextArea);
		
		problemJPanel.setBackground(new Color(227, 227, 220));
		problemJPanel.setLayout(new java.awt.GridLayout(1, 4, 30, 0));

		btnJPanel.setLayout(new java.awt.GridLayout(1, 3, 60, 0));
		btnJPanel.setBackground(new Color(227, 227, 220));
		
		btnJPanel.add(closeBtn);
		btnJPanel.add(solveBtn);
		btnJPanel.add(refreshBtn);
		btnJPanel.add(changeModeButton);

		javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(
				mainJPanel);
		mainJPanel.setLayout(mainJPanelLayout);
		mainJPanelLayout
				.setHorizontalGroup(mainJPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainJPanelLayout
										.createSequentialGroup()
										.addGap(40, 40, 40)
										.addGroup(
												mainJPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																problemJScrollPane)
														.addComponent(
																problemJPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																btnJPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																625,
																Short.MAX_VALUE))
										.addContainerGap(40, Short.MAX_VALUE)));
		mainJPanelLayout.setVerticalGroup(mainJPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				mainJPanelLayout
						.createSequentialGroup()
						.addContainerGap(40, Short.MAX_VALUE)
						.addComponent(problemJScrollPane,
								javax.swing.GroupLayout.PREFERRED_SIZE, 236,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(30, 30, 30)
						.addComponent(problemJPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(30, 30, 30)
						.addComponent(btnJPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE, 43,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(20, 20, 20)));

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

		pack();

		setLocationRelativeTo(null);
	}
	
	private void setMouseListener() {
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
	}

	private void setFocusAction(final JTextComponent textCompo,
			final String hintStr, Font font, int fontSize) {
		final Font normalFont = font.deriveFont(Font.PLAIN, fontSize);
		final Font hintFont = font.deriveFont(Font.ITALIC, fontSize);

		textCompo.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				if (textCompo.getText().isEmpty()) {
					textCompo.setFont(hintFont);
					textCompo.setText(hintStr);
					// textCompo.setForeground(new Color(165, 154, 126));
				}
				textCompo.setBorder(BorderFactory.createEmptyBorder());
			}

			public void focusGained(FocusEvent e) {
				if (textCompo.getText().equals(hintStr)) {
					textCompo.setFont(normalFont);
					textCompo.setText("");
					// textCompo.setForeground(new Color(0, 0, 0));
				}
				textCompo.setBorder(BorderFactory.createLineBorder(new Color(
						242, 108, 99), 1));
			}
		});
	}

	private void setTextField(JTextField textField, Font hintFont,
			String hintStr) {
		textField.setPreferredSize(new Dimension(90, 30));
		textField.setText(hintStr);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		// textField.setBackground(new Color(239, 239, 239));
		textField.setBackground(new Color(255, 255, 255));
		// textField.setForeground(new Color(165, 154, 126));
		textField.setBorder(BorderFactory.createEmptyBorder());
		problemJPanel.add(textField);
		textField.setFont(hintFont);
		setFocusAction(textField, hintStr, hintFont, 14);
	}

	private void setProblemTextAreaTabAction() {
		problemTextArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					// System.out.println(e.getModifiers());
					if (e.getModifiers() > 0)
						problemTextArea.transferFocusBackward();
					else
						problemTextArea.transferFocus();
					e.consume();
				}
			}
		});

	}

	private void setButtonEventListeners(final UserInterface ui) {
		noChoiceActionListener = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				solveNoChoiceProblem(ui);
			}
		};
		
		choiceActionListener = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				solveChoiceProblem(evt, ui);
			}
		};
		
		closeBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dispose();
				ui.terminateMainProgram();
			}
		});

		solveBtn.addActionListener(noChoiceActionListener);
		
		refreshBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshBtnActionPerformed(evt);
			}
		});
		
		changeModeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (noChoiceModeFlag) {
					choice1TextField.setVisible(true);
					choice2TextField.setVisible(true);
					choice3TextField.setVisible(true);
					choice4TextField.setVisible(true);
					
					solveBtn.removeActionListener(noChoiceActionListener);
					solveBtn.addActionListener(choiceActionListener);
					
					noChoiceModeFlag = false;
				} else {
					choice1TextField.setVisible(false);
					choice2TextField.setVisible(false);
					choice3TextField.setVisible(false);
					choice4TextField.setVisible(false);
					
					solveBtn.removeActionListener(choiceActionListener);
					solveBtn.addActionListener(noChoiceActionListener);
					
					noChoiceModeFlag = true;
				}
				
			}
		});
	}

	private void refreshBtnActionPerformed(ActionEvent evt) {
		this.problemTextArea.setText("문제를 입력해주세요^^");
		setTextField(choice1TextField, hintFont, "1번");
		setTextField(choice2TextField, hintFont, "2번");
		setTextField(choice3TextField, hintFont, "3번");
		setTextField(choice4TextField, hintFont, "4번");
	}

	private void solveChoiceProblem(ActionEvent evt, final UserInterface ui) {
		ProblemData problemData = new ProblemData(problemTextArea.getText(),
				choice1TextField.getText(), choice2TextField.getText(),
				choice3TextField.getText(), choice4TextField.getText());
		ui.requestSolveChoiceProblem(problemData);
	}
	
	private void solveNoChoiceProblem(final UserInterface ui) {
		ui.requestSolveNoChoiceProblem();
	}

	// Variables declaration
	private javax.swing.JPanel btnJPanel;
	private javax.swing.JTextField choice1TextField;
	private javax.swing.JTextField choice2TextField;
	private javax.swing.JTextField choice3TextField;
	private javax.swing.JTextField choice4TextField;
//	private ImageButton closeBtn;
	private javax.swing.JPanel mainJPanel;
	private javax.swing.JPanel problemJPanel;
	private javax.swing.JScrollPane problemJScrollPane;
	private javax.swing.JTextArea problemTextArea;
//	private ImageButton refreshBtn;
//	private ImageButton solveBtn;
	private Button closeBtn;
	private Button refreshBtn;
	private Button solveBtn;
	private Button changeModeButton;
	// End of variables declaration
}