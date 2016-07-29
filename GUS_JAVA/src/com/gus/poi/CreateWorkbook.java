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
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
		new EmployeeUtilization("Manager, Mike \n(PMP)","Manager",0.76),
		new EmployeeUtilization("Developer, Dixie","Developer",0.95),
		new EmployeeUtilization("Tester, Tim","Tester",0.88)
	);
	public static final XSSFColor COLOR_GRAY =  new XSSFColor(new byte[] {(byte)128,(byte)128,(byte)128});
	public static final XSSFColor COLOR_GRAY_LIGHT =  new XSSFColor(new byte[] {(byte)0xCC,(byte)0xCC,(byte)0xCC});
	public static final XSSFColor COLOR_LEMON = new XSSFColor(new byte[] {(byte)0xDC,(byte)0xDC,(byte)0x08}); 
	public static final XSSFColor COLOR_LILAC = new XSSFColor(new java.awt.Color(128, 0, 128));
	
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
	    
	    int rowCount = app.createDataRows(workbook, spreadsheet, DATA);
	    
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

	private int createDataRows(XSSFWorkbook workbook, XSSFSheet spreadsheet, List<EmployeeUtilization> data) {
		int rowCount = 1;  	//(0 based)
		//Cell formats 
        DataFormat format = workbook.createDataFormat();
        XSSFCellStyle percentStyle = workbook.createCellStyle();
        percentStyle.setDataFormat(format.getFormat("0.0%"));
        percentStyle.setAlignment( XSSFCellStyle.ALIGN_RIGHT );  	//doesn't work?

        //Newlines in a cell requires a cell style with wrap=true:
        XSSFCellStyle textStyle = workbook.createCellStyle();
        percentStyle.setAlignment( XSSFCellStyle.ALIGN_LEFT);
        textStyle.setWrapText(true);
        
		for (Iterator<EmployeeUtilization> iterator = data.iterator(); iterator.hasNext();) {
			EmployeeUtilization employeeUtilization = iterator.next();
			//A row is a series of cells [0], [1], [2] ..etc
			XSSFRow row = spreadsheet.createRow(rowCount++);
			int cellid = 0;   	//(0 based)
			//Name Last, First
	        Cell cell0 = row.createCell(cellid++);
	        cell0.setCellValue(employeeUtilization.getEmployeeName());
	        cell0.setCellStyle(textStyle);
	        //increase row height to accomodate two lines of text
	        row.setHeightInPoints((2*spreadsheet.getDefaultRowHeightInPoints()));
	        //adjust column width to fit the content
	        spreadsheet.autoSizeColumn((short)0);
	        //Role
	        Cell cell1 = row.createCell(cellid++);
	        cell1.setCellValue(employeeUtilization.getRole());
	        //% Utilized
	        Cell cell2 = row.createCell(cellid++);
	        cell2.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
	        cell2.setCellValue(employeeUtilization.getUtilization());
	        cell2.setCellStyle(percentStyle);
	        
		}
		spreadsheet.createFreezePane(0, 1);  //freeze the heading row
//		spreadsheet.setColumnHidden(0, true);  //hide the first column
		//Merge cells using a new CellRangeAddress 
		spreadsheet.addMergedRegion(new CellRangeAddress(
				rowCount, //first row (0-based)
				rowCount, //last row  (0-based)
	            0, //first column (0-based)
	            2  //last column  (0-based)
	    ));
		//Add a Formula cell to SUM a range of cells
		XSSFRow totalRow = spreadsheet.createRow(rowCount);
		XSSFCell cell2 = totalRow.createCell(0);  	//merged cell!
		cell2.setCellType(XSSFCell.CELL_TYPE_FORMULA);
	    cell2.setCellFormula("SUM(C2:C4)");
	    return rowCount;
	}

	
	public XSSFRow createHeading(XSSFSheet spreadsheet, String[] headings, XSSFCellStyle headingStyle) {
		//Create a Row - rows are identified with (0 based) numbers.
	    XSSFRow row = spreadsheet.createRow((short)0);
	    int cellid = 0;   //columns are identified with alphabets 'A,B,C..etc' and a (0 based) index 
        for (String columnHeading : headings)
        {
           Cell cell = row.createCell(cellid);
           cell.setCellValue(columnHeading);
           cell.setCellStyle(headingStyle);
           //adjust column width to fit the heading - before incrementing cellId! 
 	       spreadsheet.autoSizeColumn((short)cellid);
 	       cellid++;
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
