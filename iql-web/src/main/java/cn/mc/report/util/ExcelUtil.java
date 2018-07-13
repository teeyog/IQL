package cn.mc.report.util;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtil {

	private static SXSSFWorkbook workbook;
	
	private static SXSSFSheet sheet;
	
	private static CellStyle headStyle; 
	
	public static String fileName; 
	
	public static String sheetName = "sheet0";
	
	public static void export(HttpServletResponse response) throws IOException {
		// 客户端输出流
	    ServletOutputStream out = response.getOutputStream();
	    response.reset();
	    // stands for binary data.
	    response.setContentType("application/octet-stream; charset=utf-8");
	    // Don't even try to show the file, just save the file.
	    response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
	    workbook.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        workbook.dispose();
	}
	
	/**
     * @Description: 初始化
     */
	public static void init(String fileName, String[] titles) {
		workbook = new SXSSFWorkbook(10000);
		workbook.setCompressTempFiles(true); 
		initHeadCellStyle();
		creatTableHeadRow(titles);
		ExcelUtil.fileName = fileName;
	}
	
	/**
     * @Description: 创建表头行
     */
    private static void creatTableHeadRow(String[] titles) {
    	String safeName = WorkbookUtil.createSafeSheetName(sheetName);
    	sheet = workbook.createSheet(safeName);
    	Row row = sheet.createRow((short)0);
		row.setHeightInPoints(18);
	
		for(int i=0; i<titles.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(headStyle);
		}
    }
    
    /**
     * @Description: 初始化表头行样式
     */
    private static void initHeadCellStyle() {
    	headStyle = workbook.createCellStyle();
    	headStyle.setBorderBottom(BorderStyle.THIN);
    	headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    	headStyle.setBorderLeft(BorderStyle.THIN);
    	headStyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
    	headStyle.setBorderRight(BorderStyle.THIN);
    	headStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
    	headStyle.setBorderTop(BorderStyle.MEDIUM_DASHED);
    	headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
    	headStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
		
		Font font = workbook.createFont();
		font.setBold(true);
		headStyle.setFont(font);
    }
	
    /**
     * @Description: 添加一行
     */
    public static Row addRow(int rownum) {
    	return sheet.createRow(rownum);
    }

}
