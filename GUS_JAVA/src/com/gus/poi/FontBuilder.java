package com.gus.poi;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * <li>You must instantiate a new <code>fontBuilder</code> first.
 * <li>Then you can use it to 'build' Fonts as follows:
 * <pre>
 * Font myFont = fontBuilder.name("Courier New").height(8).color(DARKBLUE).build();
 * </pre>
 * <li>Unfortunately poi Fonts are mutable, so we don't cache them here; we have to build a new one 
 * on each request. So once you have a font, please try to reuse it.
 * <li>Unfortunately poi Fonts still use the 'old' {@linkplain IndexedColors} which are short numbers 
 * instead of an enum.   
 * @author Guy
 * @see Font
 */
public class FontBuilder {
	
	public static final short BLACK = IndexedColors.BLACK.index;
	public static final short WHITE = IndexedColors.WHITE.index;
	public static final short RED = IndexedColors.RED.index;
	public static final short DARKBLUE = IndexedColors.DARK_BLUE.index;
	
	private XSSFWorkbook workbook;
	/**
	 * Default font height is 8pt or set in the constructor method.
	 */
	public final short defaultFontHeight;
	/**
	 * Default font family name is "Arial" or set in the constructor method.
	 */
	public final String defaultFontName;
	/**
	 * see {@link #name(String)}
	 */
	protected String fontName;
	/**
	 * see {@link #height(short)} in 'points' (defaults to {@link #defaultFontHeight}).
	 */
	protected short fontHeight = 0;
	/**
	 * see {@link #color(short)} and {@linkplain IndexedColors}.DARK_BLUE.getIndex().
	 */
	protected short fontColor = 0;
	/**
	 * see {@link #weight(short)} and {@linkplain Font}.BOLDWEIGHT_BOLD ..etc
	 */
	protected short fontWeight = 0;
	
	public FontBuilder(XSSFWorkbook workbook) {
		this(workbook, (short)8, "Arial");
	}
	public FontBuilder(XSSFWorkbook workbook, short height, String name) {
		this.workbook = workbook;
		this.defaultFontHeight = height;
		this.defaultFontName = name;
	}
	
	public Font getFontBold() {
		return getFontBold(defaultFontHeight, defaultFontName);
	}
	public Font getFontBold(short height, String fontName) {
		Font boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints(height);
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		boldFont.setFontName(fontName);
		return boldFont;
	}
	public Font getFontNormal() {
		return getFontNormal(defaultFontHeight, defaultFontName);
	}
	public Font getFontNormal(short height, String fontName) {
		Font boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints(height);
		boldFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		boldFont.setFontName(fontName);
		return boldFont;
	}
	public Font getFontError() {
		return getFontError(defaultFontHeight, defaultFontName);	
	}
	public Font getFontError(short height, String fontName) {
		Font errorFont = workbook.createFont();
		errorFont.setFontHeightInPoints(height);
		errorFont.setColor(IndexedColors.RED.getIndex());
		errorFont.setFontName(fontName);
		return errorFont;
	}
	public Font getFontHyperlink() {
		return getFontHyperlink(defaultFontHeight, defaultFontName);
	}
	public Font getFontHyperlink(short height, String fontName) {
		Font hyperlinkFont = workbook.createFont();
		hyperlinkFont.setFontHeightInPoints(height);
		hyperlinkFont.setUnderline(Font.U_SINGLE);
		hyperlinkFont.setColor(DARKBLUE);
		hyperlinkFont.setFontName(fontName);
		return hyperlinkFont;
	}

	public short getDefaultFontHeight() {
		return defaultFontHeight;
	}

	public String getDefaultFontName() {
		return defaultFontName;
	}
	
	/**
	 * @param name - font fmail name   e.g "Courier New"
	 * @return this FontBuilder
	 */
	public FontBuilder name(String name) {
		fontName = name;
		return this;
	}
	/**
	 * @param height in points
	 * @return this FontBuilder
	 */
	public FontBuilder height(short height) {
		fontHeight = height;
		return this;
	}
	/**
	 * see {@linkplain IndexedColors}.RED.getIndex() ..etc
	 * @param color - short
	 * @return this FontBuilder
	 */
	public FontBuilder color(short color) {
		fontColor = color;
		return this;
	}
	/**
	 * see {@linkplain Font}.BOLDWEIGHT_BOLD ..etc
	 * @param weight - short
	 * @return this FontBuilder
	 */
	public FontBuilder weight(short weight) {
		fontWeight = weight;
		return this;
	}
	/**
	 * Builds the currently configured Font. See {@linkplain #name(String)}, 
	 * {@linkplain #height(short)}, {@linkplain #color(short)} and {@linkplain #weight(short)}.
	 * @return a new Font based on current configuration or defaults.
	 */
	public Font build() {
		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontHeight > 0? fontHeight : defaultFontHeight);
		font.setFontName(fontName != null? fontName : defaultFontName);
		if(fontColor > 0) {
			font.setColor(fontColor);
		}
		if(fontWeight > 0) {
			font.setBoldweight(fontWeight);
		}
		clear();
		return font;
	}
	protected void clear() {
		this.fontName = null;
		this.fontHeight = 0;
		this.fontColor = 0;
		this.fontWeight = 0;
	}
}
