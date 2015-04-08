package com.x.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.w3c.dom.css.RGBColor;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;

public class CreateCode {
	private static final int WIDTH = 238;
	private static final int CODEHEIGHT = 45;
	private static final int HEIGHT = 56;
	private static final int FONTSIZE = 15;
	private static final String IMAGETYPE = "JPEG";

	// public String read(String filePath) throws IOException, NotFoundException
	// {
	// BufferedImage image = ImageIO.read(new File(filePath));
	// LuminanceSource source = new BufferedImageLuminanceSource(image);
	// Binarizer binarizer = new HybridBinarizer(source);
	// BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
	// Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType,
	// Object>();
	// hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
	// Result result = new MultiFormatReader().decode(binaryBitmap, hints);//
	// 对图像进行解码
	//
	// System.out.println("图片中格式：  " + result.getBarcodeFormat());
	// System.out.println("encode： " + result.getText());
	// return result.getText();
	// }

	public boolean createCodeImg(String no) throws Exception {
		boolean flag = false;
		FileOutputStream fos;
		fos = new FileOutputStream(new File("E:\\" + no + ".jpg"));
		Code128Writer writer = new Code128Writer();
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
		return true;
	}

	public void createFont(String no) throws Exception {
		BufferedImage font = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage code = ImageIO.read(new File("E:\\" + no + ".jpg"));

		Graphics g = font.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < no.length(); i++) {
			g.setColor(Color.BLACK);
			Font font_ = new Font("arial", 0, FONTSIZE);
			g.setFont(font_);
			g.drawString(no.charAt(i) + "", (WIDTH - no.length() * 15) / 2
					+ (i - 1) * 20, CODEHEIGHT + HEIGHT - CODEHEIGHT);
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

		File outputfile = new File("E:\\" + no + ".jpg");
		ImageIO.write(font, IMAGETYPE, outputfile);

	}

	public static void main(String[] args) throws Exception {
		CreateCode createCode = new CreateCode();
		for(int i=89;i<=95;i++){
		createCode.createCodeImg("12000010"+i);
		}
	}

}
