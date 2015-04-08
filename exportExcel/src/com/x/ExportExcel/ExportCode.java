package com.x.ExportExcel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

public class ExportCode {
	private static final int FONTMARGIN = 15;
	private static final int WIDTH = 438;
	private static final int HEIGHT = 136;
	private static final int FONTSIZE = 30;
	private static final String IMAGETYPE = "JPEG";

	public boolean createCodeImg(String no) throws Exception {
		boolean flag = false;
		FileOutputStream fos;
		fos = new FileOutputStream(new File("E:\\" + no + ".jpg"));
		Code128Writer writer = new Code128Writer();
		int width = WIDTH;
		int height = HEIGHT;
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
		BufferedImage font = new BufferedImage(WIDTH, HEIGHT + 25,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage code = ImageIO.read(new File("E:\\code\\" + no + ".jpg"));
		int codeWidth = code.getWidth();
		int codeHeight = code.getHeight();
		int[] imageArray = new int[codeWidth * codeHeight];
		Graphics g = font.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT + 50);
		for (int i = 0; i < no.length(); i++) {
			System.out.println(no.charAt(i));
			g.setColor(Color.BLACK);
			Font font_ = new Font("arial", 0, FONTSIZE);
			g.setFont(font_);
			System.out.println(font_.getSize());
			g.drawString(no.charAt(i) + "", (WIDTH - no.length() * 15) / 2+ (i - 1) * 20, HEIGHT + 25);
		}
		g.drawImage(code, 0, 0, null);
		g.dispose();
		;

		File outputfile = new File("E:\\code\\" + no + ".jpg");
		ImageIO.write(font, IMAGETYPE, outputfile);

	}

	public void importExcelForCode(int x, int y, int width, int height,
			String no) throws Exception {
		createCodeImg(no);
		createFont(no);
		FileOutputStream fos = new FileOutputStream(new File("E:\\code\\"+no+".xlsx"));
		BufferedImage image = ImageIO.read(new File("E:\\code\\" + no + ".jpg"));
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		XSSFSheet sheet = xssfWorkbook.createSheet("my sheet");
		XSSFDrawing xssfDrawing = sheet.createDrawingPatriarch();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", bao);
		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, x, y, width
				+ x, height + y);
		anchor.setAnchorType(0);
		xssfDrawing.createPicture(anchor, xssfWorkbook.addPicture(
				bao.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
		XSSFRow row = sheet.createRow(6);
		XSSFCell cell = row.createCell(10);
		cell.setCellValue(1122);
		XSSFCellStyle style = xssfWorkbook.createCellStyle();
		xssfWorkbook.write(fos);
	}

	public static void main(String[] args) throws Exception {
		ExportCode exportCode = new ExportCode();
		for(int i=1;i<=3;i++)
		exportCode.createCodeImg("FO03140"+i);

	}
}
