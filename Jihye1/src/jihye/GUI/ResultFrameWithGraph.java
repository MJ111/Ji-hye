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

import jihye.PS.ResultData;

@SuppressWarnings("serial")
public class ResultFrameWithGraph extends javax.swing.JFrame {
	private ResultData resultData;
	private Point mouseDownCompCoords;
	private boolean choiceProblem;

	public ResultFrameWithGraph(UserInterface ui, ResultData resultData, boolean hasChoice) {
		this.resultData = resultData;
		this.choiceProblem = hasChoice;
		initComponents(ui);
	}

	private void initComponents(UserInterface ui) {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);

		setMouseListener();

		mainJPanel = new javax.swing.JPanel();
		btnJPanel = new javax.swing.JPanel();
		backJButton = new ImageButton("back.png");
		replayJButton = new ImageButton("replay.png");
		moreJButton = new ImageButton("more.png");
		graphJPanel = new javax.swing.JPanel();
		resultJLabel = new javax.swing.JLabel();

		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(
					"resources/NANUMGOTHICBOLD.TTF"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.PLAIN, 20);

		
			
		resultJLabel.setFont(font);
		resultJLabel.setForeground(new Color(44, 57, 65));

		mainJPanel.setBackground(new Color(227, 227, 220));
		mainJPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 106,
				79), 1));

		btnJPanel.setLayout(new java.awt.GridLayout(1, 3, 63, 0));
		btnJPanel.setBackground(new Color(227, 227, 220));

		btnJPanel.add(backJButton);
		btnJPanel.add(replayJButton);
		btnJPanel.add(moreJButton);

		SimilarlityGraph similarlityGraph = new SimilarlityGraph();
		
		if(resultData.size() != 4) {
			graphJPanel = similarlityGraph.getChart(resultData, 0);
		}else {
			graphJPanel = similarlityGraph.getChart(resultData,
					resultData.getAnswer() - 1);
		}

		javax.swing.GroupLayout graphJPanelLayout = new javax.swing.GroupLayout(
				graphJPanel);
		graphJPanel.setLayout(graphJPanelLayout);
		graphJPanelLayout.setHorizontalGroup(graphJPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 511, Short.MAX_VALUE));
		graphJPanelLayout.setVerticalGroup(graphJPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 499, Short.MAX_VALUE));

		resultJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(
				mainJPanel);
		mainJPanel.setLayout(mainJPanelLayout);
		mainJPanelLayout.setHorizontalGroup(mainJPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						mainJPanelLayout
								.createSequentialGroup()
								.addGap(100, 100, 100)
								.addComponent(graphJPanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						mainJPanelLayout
								.createSequentialGroup()
								.addContainerGap(40, Short.MAX_VALUE)
								.addComponent(btnJPanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										631,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(40, 40, 40))
				.addComponent(resultJLabel,
						javax.swing.GroupLayout.Alignment.TRAILING,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		mainJPanelLayout
				.setVerticalGroup(mainJPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainJPanelLayout
										.createSequentialGroup()
										.addGap(30, 30, 30)
										.addComponent(
												resultJLabel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												49,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(27, 27, 27)
										.addComponent(
												graphJPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												40, Short.MAX_VALUE)
										.addComponent(
												btnJPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												43,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(35, 35, 35)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				mainJPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(mainJPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, Short.MAX_VALUE)));

	
		setButtonEventListeners(ui);
		
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

	public void setButtonEventListeners(final UserInterface ui) {
		backJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.showMainFrame(false);//인자넘겨주기위한 ... 
				dispose();
			}
		});

		replayJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.readAnswerString();
			}
		});
		moreJButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.showResultFrameWithGraph();
			}
		});
	}

	// Variables declaration
	private javax.swing.JButton backJButton;
	private javax.swing.JPanel btnJPanel;
	private javax.swing.JPanel graphJPanel;
	private javax.swing.JPanel mainJPanel;
	private javax.swing.JButton moreJButton;
	private javax.swing.JButton replayJButton;
	private javax.swing.JLabel resultJLabel;
	// End of variables declaration
}