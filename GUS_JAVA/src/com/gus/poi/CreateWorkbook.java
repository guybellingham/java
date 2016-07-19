package com.gus.poi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateWorkbook {
	
	public static final List<EmployeeUtilization> DATA = Arrays.asList(
		new EmployeeUtilization("Manager, Mike","Manager",76.0),
		new EmployeeUtilization("Developer, Dixie","Developer",95.0),
		new EmployeeUtilization("Tester, Tim","Tester",90.0)
	);
	public static final XSSFColor COLOR_GRAY =  new XSSFColor(new byte[] {(byte)128,(byte)128,(byte)128});
	public static final XSSFColor COLOR_GRAY_LIGHT =  new XSSFColor(new byte[] {(byte)0xCC,(byte)0xCC,(byte)0xCC});
	public static final XSSFColor COLOR_LEMON = new XSSFColor(new byte[] {(byte)0xDC,(byte)0xDC,(byte)0x08});
	
	public static void main(String[] args) {
		CreateWorkbook app = new CreateWorkbook();
		//Create Blank workbook
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    //Create a blank spreadsheet
	    XSSFSheet spreadsheet = workbook.createSheet("Your Report");
	    
	    String[] headings = new String[] { "Resource", "Role", "Utilization%" };
	    // Center Align Cell Contents 
	    XSSFCellStyle headingStyle = workbook.createCellStyle();
	    headingStyle.setAlignment( XSSFCellStyle.ALIGN_CENTER );
	    headingStyle.setVerticalAlignment( XSSFCellStyle.VERTICAL_CENTER );
	    //Bottom Border
	    headingStyle.setBorderBottom( XSSFCellStyle.BORDER_THICK );
	    headingStyle.setBottomBorderColor( IndexedColors.BLUE.getIndex() );
	    //Background you need BOTH a fill color and a fill pattern here to get any effect 
	    //headingStyle.setFillBackgroundColor( COLOR_LEMON ); 
	    //headingStyle.setFillPattern( XSSFCellStyle.LEAST_DOTS );
	    headingStyle.setFillForegroundColor(COLOR_LEMON);
	    headingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    //Font is part of the cell style - as is rotation
	    XSSFFont font = workbook.createFont();
	      font.setFontHeightInPoints((short) 14);
	      font.setFontName("IMPACT");
	      //font.setItalic(true);
	      font.setColor(HSSFColor.DARK_BLUE.index);
	    headingStyle.setFont(font);
	    
	    app.createHeading(spreadsheet, headings, headingStyle);
	    
	    int rowCount = app.createDataRows(spreadsheet, DATA);
	    
	    //set print area with indexes
	    workbook.setPrintArea(
	      0, //sheet index
	      0, //start column
	      2, //end column
	      0, //start row
	      rowCount //end row
	    );
	    //set paper size
	    spreadsheet.getPrintSetup().setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
	    //set display grid lines or not
	    spreadsheet.setDisplayGridlines(true);
	    //set print grid lines or not
	    spreadsheet.setPrintGridlines(true);
	    
	    //Create file system using specific name
	    File file = new File("createworkbook.xlsx");
	    
		try {
			app.writeWorkbook(file, workbook);
		    System.out.println("createworkbook.xlsx written successfully");
		} catch (FileNotFoundException e) {
			System.out.println("Failed to create createworkbook.xlsx");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to write createworkbook.xlsx");
			e.printStackTrace();
		}
	    
		try {
			XSSFWorkbook workbook2 = app.readWorkbook(file);
		    System.out.println("Read createworkbook.xlsx successfully");
		    XSSFSheet spreadsheet2 = workbook2.getSheetAt(0);
		    app.printSpreadsheet(spreadsheet2);
		} catch (FileNotFoundException e) {
			System.out.println("Failed to find createworkbook.xlsx file?");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to read createworkbook.xlsx");
			e.printStackTrace();
		}
	}

	private int createDataRows(XSSFSheet spreadsheet, List<EmployeeUtilization> data) {
		int rowCount = 1;
		for (Iterator<EmployeeUtilization> iterator = data.iterator(); iterator.hasNext();) {
			EmployeeUtilization employeeUtilization = iterator.next();
			//A row is a series of cells 
			XSSFRow row = spreadsheet.createRow(rowCount++);
			int cellid = 0;
	        Cell cell0 = row.createCell(cellid++);
	        cell0.setCellValue(employeeUtilization.getEmployeeName());
	        Cell cell1 = row.createCell(cellid++);
	        cell1.setCellValue(employeeUtilization.getRole());
	        Cell cell2 = row.createCell(cellid++);
	        cell2.setCellValue(employeeUtilization.getUtilization());
		}
		XSSFRow totalRow = spreadsheet.createRow(rowCount);
		XSSFCell cell2 = totalRow.createCell(2);
		cell2.setCellType(XSSFCell.CELL_TYPE_FORMULA);
	    cell2.setCellFormula("SUM(C2:C4)" );
	    return rowCount;
	}

	public XSSFWorkbook readWorkbook(File file) throws FileNotFoundException, IOException {
		XSSFWorkbook workbook = null;
		if(file.isFile() && file.exists()) {
			try (FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);){
				//Get the workbook instance for XLSX file 
				workbook = new XSSFWorkbook(bis);
			}
		} else {
			throw new FileNotFoundException("Failed to find file "+file);
		}
		return workbook;
	}
	
	public void writeWorkbook(File file, XSSFWorkbook workbook) throws FileNotFoundException, IOException {
		if(file.isFile()) {
			try (FileOutputStream fos = new FileOutputStream(file);
					BufferedOutputStream bos = new BufferedOutputStream(fos);) {
				workbook.write(bos);
			}
		} else {
			throw new FileNotFoundException("Failed to find file "+file);
		}

	}
	
	public XSSFRow createHeading(XSSFSheet spreadsheet, String[] headings, XSSFCellStyle headingStyle) {
		//Create a Row - columns are identified with alphabets and rows with numbers.
	    XSSFRow row = spreadsheet.createRow((short)0);
	    int cellid = 0;
        for (String columnHeading : headings)
        {
           Cell cell = row.createCell(cellid++);
           cell.setCellValue(columnHeading);
           cell.setCellStyle(headingStyle);
        }
	    return row;
	}
	
	public void printSpreadsheet(XSSFSheet spreadsheet) {
		XSSFRow row = null;
		Iterator < Row > rowIterator = spreadsheet.iterator();
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator < Cell > cellIterator = row.cellIterator();
			while ( cellIterator.hasNext()) 
			{
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) 
				{
				case Cell.CELL_TYPE_NUMERIC:
					System.out.print( cell.getNumericCellValue() + " \t\t " );
					break;
				case Cell.CELL_TYPE_STRING:
					System.out.print( cell.getStringCellValue() + " \t\t " );
					break;
				}
			}
			System.out.println();
		}
	}
	
	public static class EmployeeUtilization {
		String employeeName;
		String role;
		Double utilization;
		
		public EmployeeUtilization(String employeeName,String role,Double utilization) {
			setEmployeeName(employeeName);
			setRole(role);
			setUtilization(utilization);
		}
		
		public String getEmployeeName() {
			return employeeName;
		}
		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public Double getUtilization() {
			return utilization;
		}
		public void setUtilization(Double utilization) {
			this.utilization = utilization;
		}
	}
}
