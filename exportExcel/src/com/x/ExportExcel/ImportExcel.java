package com.x.ExportExcel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

public class ImportExcel {
	private static final int WIDTH = 381;
	private static final int CODEHEIGHT = 90;
	private static final int HEIGHT = 106;
	private static final int FONTSIZE = 20;
	private static final String IMAGETYPE = "JPEG";

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
		int codeWidth = code.getWidth();
		int codeHeight = code.getHeight();
		int[] imageArray = new int[codeWidth * codeHeight];
		Graphics g = font.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < no.length(); i++) {
			System.out.println(no.charAt(i));
			g.setColor(Color.BLACK);
			Font font_ = new Font("Î¢ÈíÑÅºÚ", 0, FONTSIZE);
			g.setFont(font_);
			System.out.println(font_.getSize());
			g.drawString(no.charAt(i) + "", (WIDTH - no.length() * 15) / 2+ (i - 1) * 20, CODEHEIGHT + HEIGHT-CODEHEIGHT);
		}
		g.drawImage(code, 0, 0, null);
		g.dispose();
		;

		File outputfile = new File("E:\\" + no + ".jpg");
		ImageIO.write(font, IMAGETYPE, outputfile);

	}

	public void importExcelForCode(int x, int y, int width, int height,
			String no) throws Exception {
		createCodeImg(no);
		createFont(no);
		InputStream i=new FileInputStream("E:\\Ä£°å2.xlsx");
		FileOutputStream fos = new FileOutputStream(new File("E:\\"+no+".xlsx"));
		BufferedImage image = ImageIO.read(new File("E:\\" + no + ".jpg"));
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(i);
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
		XSSFDrawing xssfDrawing = sheet.createDrawingPatriarch();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", bao);
		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, x, y, width
				+ x, height + y);
		anchor.setAnchorType(0);
		xssfDrawing.createPicture(anchor, xssfWorkbook.addPicture(
				bao.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
		xssfWorkbook.write(fos);
	}

	public static void main(String[] args) throws Exception {
		ImportExcel exportCode = new ImportExcel();
		exportCode.importExcelForCode(0, 25,13, 4, "B03-01-01-02");

	}
}
