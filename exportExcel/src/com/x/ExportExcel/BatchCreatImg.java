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
import com.google.zxing.oned.Code128Writer;

public class BatchCreatImg {
	private static final int WIDTH = 351;
	private static final int CODEHEIGHT = 55;
	private static final int HEIGHT = 78;
	private static final int FONTSIZE = 25;
	private static final String IMAGETYPE = "JPEG";

	public boolean createCodeImg(String no) throws Exception {

		FileOutputStream fos;
		fos = new FileOutputStream(new File("E:\\" + no + ".jpg"));
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

		File outputfile = new File("E:\\" + no + ".jpg");
		ImageIO.write(font, IMAGETYPE, outputfile);

	}

	public void importExcelForCode(int x, int y, int width, int height,
			String no) throws Exception {
		InputStream i = new FileInputStream("E:\\美国仓库货架条形码列表3.30-c.xlsx");

		createCodeImg(no);
		createFont(no);
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
		i.close();
		FileOutputStream fos = new FileOutputStream(new File(
				"E:\\美国仓库货架条形码列表3.30-c.xlsx"));
		xssfWorkbook.write(fos);
		new File("E:\\" + no + ".jpg").delete();
	}

	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		BatchCreatImg exportCode = new BatchCreatImg();
		//打印单个
//		for(int i=1;i<=6;i++)
//			exportCode.importExcelForCode(0, 0, 3, 3, "FO03150"+i);
		
		
		 for(int i=1;i<=6;i++){
		 exportCode.importExcelForCode(3, (i-1)*4, 3, 3, "FO03150"+i);
		 }
		// 生成A列

//		for (int i = 1; i <= 5; i++) {
//			for (int j = 1; j <= 5; j++) {
//				for (int k = 1; k <= 2; k++) {
//					exportCode.importExcelForCode(0, (k - 1) * 4 + (j - 1) * 8+ (i - 1) * 40, 3, 3, "A-0" + i + "-0" + j + "-0"
//							+ k);
//				}
//			}
//		}
//		for (int i = 1; i <= 1; i++) {
//			for (int j = 1; j <= 5; j++) {
//				for (int k = 1; k <= 3; k++) {
//					exportCode.importExcelForCode(3,(k - 1) * 4 + (j - 1) * 12+(i - 1) * 60, 3, 3, "B-0" + i + "-0" + j+ "-0" + k);
//				}
//			}
//		}
//		//不规则生成
//		for(int i=1;i<=3;i++){
//		if(i==1){
//		for (int j = 1; j <= 5; j++) {
//			for (int k = 1; k <= 2; k++) {
//				exportCode.importExcelForCode(6, (k - 1) * 4 + (j - 1) * 8+(i-1)*j*k*4, 3,3, "C-0"+i+"-0" + j + "-0" + k);
//			}
//		}}
//		if(i==2){
//			for (int j = 1; j <= 4; j++) {
//				for (int k = 1; k <= 2; k++) {
//					exportCode.importExcelForCode(6, (k - 1) * 4 + (j - 1) * 8+(i-1)*40, 3,3, "C-0"+i+"-0" + j + "-0" + k);
//				}
//			}}
//		if(i==3){
//			for (int j = 1; j <= 3; j++) {
//				for (int k = 1; k <= 2; k++) {
//					exportCode.importExcelForCode(6, (k - 1) * 4 + (j - 1) * 8+72, 3,3, "C-0"+i+"-0" + j + "-0" + k);
//				}
//			}}
//		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin + "毫秒");
	}
}
