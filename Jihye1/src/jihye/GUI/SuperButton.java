package jihye.GUI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SuperButton extends JButton {
	Image image;

	public SuperButton(String fileName) {
		image = new javax.swing.ImageIcon("image/" + fileName).getImage();//
		setButtonMouseEventListener(this);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	private BufferedImage changeBrightness(BufferedImage src, int bright) {
		// copy BufferedImage to BufferedImage
		ColorModel cm = src.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = src.copyData(null);
		BufferedImage dest = new BufferedImage(cm, raster,
				isAlphaPremultiplied, null);

		// changing brightness
		for (int i = 0; i < dest.getWidth(); i++) {
			for (int j = 0; j < dest.getHeight(); j++) {
				int rgb = dest.getRGB(i, j);
				int alpha = (rgb >> 24) & 0x000000FF;
				Color c = new Color(rgb);
				if (alpha != 0) {
					int red = (c.getRed() - 10) <= 0 ? 0 : c.getRed() - bright;
					int green = (c.getGreen() - 10) <= 0 ? 0 : c.getGreen()
							- bright;
					int blue = (c.getBlue() - 10) <= 0 ? 0 : c.getBlue()
							- bright;
					c = new Color(red, green, blue);
					dest.setRGB(i, j, c.getRGB());
				}
			}
		}

		return dest;
	}

	private void setButtonMouseEventListener(final SuperButton btn) {
		// image to BufferedImage
		final Image origin = btn.getImage();
		BufferedImage src = new BufferedImage(origin.getWidth(null),
				origin.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		src.getGraphics().drawImage(origin, 0, 0, null);

		final BufferedImage pressed = changeBrightness(src, 50);
		final BufferedImage entered = changeBrightness(src, 30);

		btn.addMouseListener(new java.awt.event.MouseListener() {

			public void mouseClicked(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {
				btn.setImage(pressed);
			}

			public void mouseReleased(MouseEvent e) {
				btn.setImage(origin);
			}

			public void mouseEntered(MouseEvent e) {
				btn.setImage(entered);
			}

			public void mouseExited(MouseEvent e) {
				btn.setImage(origin);
			}
		});
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		// System.out.printf("%d %d",getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		this.setMargin(new Insets(0, 0, 0, 0));
	}
}