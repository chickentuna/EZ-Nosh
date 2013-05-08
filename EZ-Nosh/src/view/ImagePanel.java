package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final int CROP = 0;
	public static final int STRETCH = 1;

	private Image img;
	private int mode;

	public ImagePanel(String img) throws IOException {
		this(img, CROP);
	}

	public ImagePanel(String img, int mode) throws IOException {
		this(ImageIO.read(new File(img)), mode);
	}

	public ImagePanel(Image img, int mode) {
		this.mode = mode;
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		if (mode == CROP) {
			g.drawImage(img, 0, 0, null);
		}
		else if (mode == STRETCH) {
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}

	}

}