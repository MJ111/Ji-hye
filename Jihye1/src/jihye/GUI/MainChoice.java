package jihye.GUI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
/***
 * 가장 처음 화면 
 * @author Kim-Dg
 *
 */
public class MainChoice extends javax.swing.JFrame {
	
	private javax.swing.JPanel mainJPanel;
	private javax.swing.JPanel btnJPanel;
	private javax.swing.JPanel graphJPanel;

	
//	private Button SelectiveButton;
//	private Button EssayButton;

//	private ImageButton SelectiveButton;
//	private ImageButton EssayButton;
	
	private JButton SelectiveButton ;
	private JButton EssayButton ;
	
	public MainChoice(UserInterface ui) {
		initComponents(ui);
		setButtonEventListeners(ui);
	}
	
	/***
	 * 버튼 디자인 위한 .. 
	 * @author Kim-Dg
	 *
	 */
	class StyledButtonUI extends BasicButtonUI {

	    @Override
	    public void installUI (JComponent c) {
	        super.installUI(c);
	        AbstractButton button = (AbstractButton) c;
	        button.setOpaque(false);
	        button.setBorder(new EmptyBorder(5, 15, 5, 15));
	    }

	    @Override
	    public void paint (Graphics g, JComponent c) {
	        AbstractButton b = (AbstractButton) c;
	        paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
	        super.paint(g, c);
	    }

	    private void paintBackground (Graphics g, JComponent c, int yOffset) {
	        Dimension size = c.getSize();
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setColor(c.getBackground().darker());
	        g.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
	        g.setColor(c.getBackground());
	        g.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
	    }
	}
	class ImagePanel extends JPanel {

		  private Image img;

		  public ImagePanel(String img) {
		    this(new ImageIcon(img).getImage());
		  }

		  public ImagePanel(Image img) {
		    this.img = img;
		    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		    setPreferredSize(size);
		    setMinimumSize(size);
		    setMaximumSize(size);
		    setSize(size);
		    setLayout(null);
		  }

		  public void paintComponent(Graphics g) {
		    g.drawImage(img, 0, 0, null);
		  }

		}
	
	private void initComponents(UserInterface ui){
		
		
		mainJPanel = new javax.swing.JPanel();
		btnJPanel = new javax.swing.JPanel();
		
//		SelectiveButton = new Button("multiple choice");
//		EssayButton = new Button("Essay choice");
		
//		SelectiveButton = new ImageButton("Selective.png");
//		EssayButton = new ImageButton("Essay.png");
		
		SelectiveButton = new JButton("Multiple Choice");
		SelectiveButton.setFont(new Font("Calibri", Font.BOLD, 16));
		SelectiveButton.setBackground(new Color(218,81,83));
		SelectiveButton.setForeground(Color.white);
	        // customize the button with your own look
		SelectiveButton.setUI(new StyledButtonUI());
		
		EssayButton = new JButton("Essay Choice");
		EssayButton.setFont(new Font("Calibri", Font.BOLD, 16));
		EssayButton.setBackground(new Color(218,81,83));
		EssayButton.setForeground(Color.white);
	        // customize the button with your own look
		EssayButton.setUI(new StyledButtonUI());
		
		
		ImagePanel panel = new ImagePanel(new ImageIcon("src/MainImage.png").getImage());
		graphJPanel = new javax.swing.JPanel();

		
		graphJPanel.add(panel);
		
		mainJPanel.setBackground(new Color(227, 227, 220));
		mainJPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 106,
				79), 1));

		btnJPanel.setLayout(new java.awt.GridLayout(1, 1, 63, 0));
		btnJPanel.setBackground(new Color(227, 227, 220));

		btnJPanel.add(SelectiveButton);
		btnJPanel.add(EssayButton);

		
		
		javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(
				mainJPanel);
		mainJPanel.setLayout(mainJPanelLayout);
		mainJPanelLayout.setHorizontalGroup(mainJPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						mainJPanelLayout
								.createSequentialGroup()
								.addGap(10, 10, 10)
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
								.addGap(100, 100, 100)));
		mainJPanelLayout
				.setVerticalGroup(mainJPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainJPanelLayout
										.createSequentialGroup()
										.addGap(30, 30, 30)
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
		
		pack();

		setLocationRelativeTo(null);
	}
	
	private void setButtonEventListeners(final UserInterface ui){
		SelectiveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.showMainFrame(true);
				dispose();
			}
		});
		
		EssayButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ui.showMainFrame(false);
				dispose();
			}
		});
	}
}
