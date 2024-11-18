package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileUtil {
	XSSFWorkbook wb;

	//write Constructor to read excel path
	public ExcelFileUtil(String Excelpath)throws Throwable
	{
		FileInputStream fi = new FileInputStream(Excelpath);
		wb = new XSSFWorkbook(fi);
	}

	//method for counting the no.of rows in a sheet
	public int rowCount(String Sheetname) 
	{
		return wb.getSheet(Sheetname).getLastRowNum();
	}

	//method for reading cell data
	public String getCellData(String Sheetname,int row,int column) 
	{
		String data = "";
		if(wb.getSheet(Sheetname).getRow(row).getCell(column).getCellType()==CellType.NUMERIC)
		{
			int celldata = (int) wb.getSheet(Sheetname).getRow(row).getCell(column).getNumericCellValue();
			data = String.valueOf(celldata);
		}
		else
		{
			data = wb.getSheet(Sheetname).getRow(row).getCell(column).getStringCellValue();
		}
		return data;
	}

	//method for setcelldata
	public void setCellData(String Sheetname,int row,int column,String Status,String WriteExcel)throws Throwable
	{
		//get sheet from wb
		XSSFSheet ws = wb.getSheet(Sheetname);
		//get row from sheet
		XSSFRow rownum = ws.getRow(row);
		//create cell
		XSSFCell cell = rownum.createCell(column);
		//write status into created cell
		cell.setCellValue(Status);
		if(Status.equalsIgnoreCase("Pass"))
		{
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.GREEN.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		    
		}
		else if(Status.equalsIgnoreCase("Fail"))
		{
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		}
		else if(Status.equalsIgnoreCase("Blocked"))
		{
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			style.setFont(font);
			rownum.getCell(column).setCellStyle(style);
		}
		FileOutputStream fo = new FileOutputStream(WriteExcel);
		wb.write(fo);
	}
}
