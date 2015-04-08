package com.x.ExportExcel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class Code_128Utils {

	private static final int WIDTH = 351;
	private static final int CODEHEIGHT = 55;
	private static final int HEIGHT = 78;
	private static final int FONTSIZE = 25;
	private static final String IMAGETYPE = "JPEG";

	public static void createCode(String no) throws Exception {
		FileOutputStream fos;
		fos = new FileOutputStream(new File("E:\\code\\" + no + ".jpg"));
		int width = WIDTH;
		int height = CODEHEIGHT;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix m = new MultiFormatWriter().encode(no,
				BarcodeFormat.CODE_128, width, height, hints);
		MatrixToImageWriter.writeToStream(m, IMAGETYPE, fos);
		fos.flush();
		fos.close();
		createFont(no);

	}

	public static void createFont(String no) throws Exception {
		BufferedImage font = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage code = ImageIO.read(new File("E:\\code\\" + no + ".jpg"));
		Graphics2D g = (Graphics2D) font.getGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		for (int i = 0; i < no.length(); i++) {
			g.setColor(Color.black);
			Font font_ = new Font("Consolas", 0, FONTSIZE);
			g.setFont(font_);
			g.drawString(no.charAt(i) + "", (FONTSIZE * 2 + WIDTH - no.length()
					* FONTSIZE)
					/ 2 + (i - 1) * FONTSIZE, CODEHEIGHT + HEIGHT - CODEHEIGHT);
		}
		g.drawImage(code, 0, 0, null);
		g.dispose();
		int[] rgb = new int[3];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = CODEHEIGHT; j < HEIGHT; j++) {
				int pixel = font.getRGB(i, j);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if (rgb[0] > 125 || rgb[1] > 125 || rgb[2] > 125) {
					font.setRGB(i, j, -1);
				}
				if (rgb[0] < 100 || rgb[1] < 100 || rgb[2] < 100) {
					font.setRGB(i, j, -16777216);
				}
			}
		}

		File outputfile = new File("E:\\code\\" + no + ".jpg");
		ImageIO.write(font, IMAGETYPE, outputfile);
	}


}
