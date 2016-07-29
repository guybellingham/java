package com.gus.poi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
/**
 * <li>You must instantiate a new <code>styleBuilder</code> first.
 * <li>Then you can use it to 'build' cell styles as follows:
 * <pre>
 * XSSFCellStyle style = styleBuilder.fill(Colors.LILAC).alignment(CellStyle.ALIGN_LEFT).font(8,"Courier New").wrap(true).build();
 * </pre>
 * <li>Unfortunately poi cell styles are mutable, so we don't cache them here we have to build a new one 
 * on each request. So once you have a style please try to reuse it.
 * @author Guy
 * @see FontBuilder
 * @see Colors
 * @see CellStyle
 */
public class StyleBuilder {

	private XSSFWorkbook workbook;
	private XSSFCreationHelper helper;
	private FontBuilder fontBuilder; 
	
    /**
     * The currently configured font see {@linkplain #font(short, String, short)} and 
     * {@linkplain #font(Font)} and {@linkplain StyleBuilder#bold()}.
     */
	private Font font;
	/**
	 * The currently configured text alignment see {@linkplain #alignment(short)} 
	 * the default is XSSFCellStyle.ALIGN_LEFT.
	 */
	private short alignment = XSSFCellStyle.ALIGN_LEFT;
	/**
	 * The currently configured fill color see {@linkplain #fill(XSSFColor)}.
	 */
	private XSSFColor fillColor;
	/**
	 * The currently configured fill pattern {@linkplain FillPatternType}. 
	 * If nothing is configured and {@link #fillColor} is configured then 
	 * a flat/plain fill is used.
	 */
	private FillPatternType fillPattern;
	/**
	 * The currently configured data format pattern {@linkplain DataFormat} or null. 
	 */
	private String pattern;
	/**
	 * The currently configured text wrapping <code>true</code> or (default <code>false</code>).
	 * See {@link #wrap(boolean)}.
	 */
	private boolean wrapped = false;
	/**
	 * The currently configured border color see {@linkplain Colors}, default is Black.
	 */
	private XSSFColor borderColor;
	/**
	 * The currently configured left border style see {@linkplain BorderStyle}, default is NONE.
	 */
	private BorderStyle borderLeft;
	/**
	 * The currently configured right border style see {@linkplain BorderStyle}, default is NONE.
	 */
	private BorderStyle borderRight;
	/**
	 * The currently configured top border style see {@linkplain BorderStyle}, default is NONE.
	 */
	private BorderStyle borderTop;
	/**
	 * The currently configured bottom border style see {@linkplain BorderStyle}, default is NONE.
	 */
	private BorderStyle borderBottom;
	
	public StyleBuilder(XSSFWorkbook workbook) {
		this.workbook = workbook;
		this.helper = workbook.getCreationHelper();
		this.fontBuilder = new FontBuilder(workbook);
	}
	
	/**
	 * Configures the {@linkplain Font} to 'Arial' 8pt Bold {@link #fontBold}. 
	 * @return this StyleBuilder
	 */
	public StyleBuilder bold() {
		this.font = getFontBold();
		return this;
	}
	/**
	 * Configures a TOP border for the cells, default is NONE.
	 * @param style - for the cells TOP border see {@linkplain BorderStyle}.
	 * @return this StyleBuilder
	 */
	public StyleBuilder top(BorderStyle style) {
		this.borderTop = style;
		return this;
	}
	
	/**
	 * Configures a XSSFColor for any borders for the cell, the default color is BLACK.
	 * @param borderColor - see {@linkplain Colors}
	 * @return this StyleBuilder
	 */
	public StyleBuilder borderColor(XSSFColor borderColor) {
		this.borderColor = borderColor;
		return this;
	}
	
	/**
	 * Configures a BOTTOM border for the cells, default is NONE.
	 * @param style - see {@linkplain BorderStyle}.
	 * @return this StyleBuilder
	 */
	public StyleBuilder bottom(BorderStyle style) {
		this.borderBottom = style;
		return this;
	}
	
	/**
	 * Configures a LEFT border for the cells, default is NONE.
	 * @param style - see {@linkplain BorderStyle}.
	 * @return this StyleBuilder
	 */
	public StyleBuilder left(BorderStyle style) {
		this.borderLeft = style;
		return this;
	}
	
	/**
	 * Configures a RIGHT border for the cells, default is NONE.
	 * @param style - see {@linkplain BorderStyle}.
	 * @return this StyleBuilder
	 */
	public StyleBuilder right(BorderStyle style) {
		this.borderRight = style;
		return this;
	}
	
	/**
	 * Configures the current {@linkplain Font} for cell styles. The default Font  
	 * is {@link #fontNormal}. 
	 * @param height - short in 'points' (default 9)
	 * @param name - font family (default "Arial") default color is black
	 * @return this StyleBuilder
	 */
	public StyleBuilder font(short height, String name) {
		return font(height, name, FontBuilder.BLACK);
	}
	/**
	 * Configures the current {@linkplain Font} for cell styles. 
	 * @param height - short in 'points' (default 9)
	 * @param name - font family (default "Arial") 
	 * @param color - see {@link #FONT_COLOR_BLACK}  ..etc
	 * @return this StyleBuilder
	 */
	public StyleBuilder font(short height, String name, short color) {
		this.font = fontBuilder.height(height).name(name).color(color).build();
		return this;
	}
	/**
	 * Configures the {@linkplain Font} to the given <code>customFont</code>. 
	 * @param customFont  - Configures a 'custom' Font built with the {@linkplain FontBuilder}.
	 * @return this StyleBuilder
	 */
	public StyleBuilder font(Font customFont) {
		this.font = customFont;
		return this;
	}
	/**
	 * @param alignment  - Configures text alignment see {@linkplain XSSFCellStyle}.ALIGN_CENTER, ALIGN_RIGHT ..etc
	 * @return this StyleBuilder
	 */
	public StyleBuilder alignment(short alignment) {
		this.alignment = alignment;
		return this;
	}
	/**
	 * @param fillColor  - Configures a fill color see {@linkplain XSSFColor}.
	 * @return this StyleBuilder
	 */
	public StyleBuilder fill(XSSFColor fillColor) {
		fill(fillColor, null);
		return this;
	}
	public StyleBuilder fill(XSSFColor fillColor, FillPatternType fillPattern) {
		this.fillColor = fillColor;
		this.fillPattern = fillPattern;
		return this;
	}
	/**
	 * @param pattern  - Configures an optional data format pattern.
	 * @return this StyleBuilder
	 */
	public StyleBuilder pattern(String pattern) {
		this.pattern = pattern;
		return this;
	}
	/**
	 * @param wrapped - Configures whether to wrap text in the cell (default is false).
	 * @return this StyleBuilder
	 */
	public StyleBuilder wrap(boolean wrapped) {
		this.wrapped = wrapped;
		return this;
	}
	/**
	 * <li>Builds and returns a new CellStyle based upon whatever 'configuration' methods have been called 
	 * and whatever default values are in place. 
	 * <li>Clears any configuration state ready for the next build.
	 * @return
	 */
	public XSSFCellStyle build() {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(alignment);
		if(pattern != null) {
			short formatNumber = helper.createDataFormat().getFormat(pattern);
			style.setDataFormat(formatNumber);
		}
		style.setFont(getFont());
		if(fillColor != null) {
	    	style.setFillForegroundColor(fillColor);
	    	if(fillPattern != null) {
	    		style.setFillPattern(fillPattern);
	    	} else {
	    		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    	}
	    }
		if(borderLeft != null) {
			style.setBorderColor(BorderSide.LEFT,getBorderColor());
			style.setBorderLeft(borderLeft);
		}
		if(borderRight != null) {
			style.setBorderColor(BorderSide.RIGHT,getBorderColor());
			style.setBorderRight(borderRight);
		}
		if(borderTop != null) {
			style.setBorderColor(BorderSide.TOP,getBorderColor());
			style.setBorderTop(borderTop);
		}
		if(borderBottom != null) {
			style.setBorderColor(BorderSide.BOTTOM,getBorderColor());
			style.setBorderBottom(borderBottom);
		}
		
		style.setWrapText(wrapped);
		clear();
		return style;
	}
	/**
	 * Clears the current style configuration so we are ready to {@link #build()}  
	 * another style.
	 */
	public void clear(){
		this.font = null;
		this.fillColor = null;
		this.fillPattern = null;
		this.alignment = 0;
		this.pattern = null;
		this.wrapped = false;
		clearBorders();
	}
	public void clearBorders() {
		borderColor = null;
		borderLeft = null;
		borderRight = null;
		borderTop = null;
		borderBottom = null;
	}
	
	/**
	 * Builds a cell style for 'default' headings (centered) with 
	 * the bold default font and light gray fill and no border {@linkplain XSSFCellStyle}.BORDER_NONE.
	 * @return default cell style for headings
	 */
	public XSSFCellStyle getHeadingStyle() {
		return getHeadingStyle(Colors.GRAY_LIGHT, XSSFCellStyle.BORDER_NONE);
	}
	/**
	 * Builds a cell style for 'default' headings (centered) with the Bold default font 
	 * and the (optional) <code>fillColor</code>  
	 * and <code>bottomBorder</code> e.g {@linkplain XSSFCellStyle}.BORDER_XXX or zero.
	 * @param fillColor
	 * @param bottomBorder
	 * @return default cell style for headings with the given fillColor and bottom border (zero for none).
	 */
	public XSSFCellStyle getHeadingStyle(XSSFColor fillColor, short bottomBorder) {
		XSSFCellStyle headingStyle = workbook.createCellStyle();
	    headingStyle.setAlignment( XSSFCellStyle.ALIGN_CENTER );
	    headingStyle.setVerticalAlignment( XSSFCellStyle.VERTICAL_CENTER );
	    headingStyle.setFont(getFontBold());
	    //Bottom Border
	    if(bottomBorder > 0) {
		    headingStyle.setBorderBottom( bottomBorder );
		    headingStyle.setBottomBorderColor( IndexedColors.GREY_80_PERCENT.index );
	    }
	    //Background you need BOTH a fill color and a fill pattern here to get any effect 
	    if(fillColor != null) {
	    	headingStyle.setFillForegroundColor(fillColor);
	    	headingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    }
	    return headingStyle;
	}
	/**
	 * Get the currently configured Font (or 'normal' Font). 
	 * See {@link #font(short, String)}. 
	 * @return
	 */
	public Font getFont() {
		if(font == null){
			font = getFontNormal();
		}
		return font;
	}
	/**
	 * Gets a 'default' emboldened font from {@linkplain FontBuilder}.
	 * @return <b>bold</b> Font
	 */
	public Font getFontBold() {
		return fontBuilder.getFontBold();
	}
	/**
	 * Gets a new 'default' normal font from {@linkplain FontBuilder}.
	 * @return normal Font
	 */
	public Font getFontNormal() {
		return fontBuilder.getFontNormal();
	}
	/**
	 * Gets a 'hyperlink' font from {@linkplain FontBuilder}.
	 * @return a new hyperlink Font
	 */
	public Font getFontHyperlink() {
		return fontBuilder.getFontHyperlink();
	}
	
	public XSSFCellStyle getDateStyle(Locale locale) {
		//String countryCode = locale.getCountry();  = "US"
		SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
		String pattern = format.toPattern();
		return getDateStyle(pattern);
	}
	public XSSFCellStyle getDateStyle(String pattern) {
		XSSFCellStyle defaultDateStyle = workbook.createCellStyle();
		defaultDateStyle = workbook.createCellStyle();
		short formatNumber = helper.createDataFormat().getFormat(pattern);
		defaultDateStyle.setDataFormat(formatNumber);
		defaultDateStyle.setFont(getFontNormal());
		defaultDateStyle.setAlignment( XSSFCellStyle.ALIGN_LEFT );
		defaultDateStyle.setWrapText(false);
		return defaultDateStyle;
	}
	public XSSFCellStyle getTextStyle() {
		XSSFCellStyle defaultTextStyle = workbook.createCellStyle();
		defaultTextStyle = workbook.createCellStyle();
		defaultTextStyle.setFont(getFontNormal());
		defaultTextStyle.setAlignment( XSSFCellStyle.ALIGN_LEFT );
		defaultTextStyle.setWrapText(false);
		return defaultTextStyle;
	}
	public XSSFCellStyle getNumberStyle(Locale locale) {
		DecimalFormat format = (DecimalFormat)DecimalFormat.getInstance(locale);
		format.setMaximumFractionDigits(0);
		format.setMaximumIntegerDigits(12);
		String pattern = format.toPattern();
		return getNumberStyle(pattern);
	}
	public XSSFCellStyle getNumberStyle(String pattern) {
		XSSFCellStyle defaultNumberStyle = workbook.createCellStyle();
		defaultNumberStyle = workbook.createCellStyle();
		short formatNumber = helper.createDataFormat().getFormat(pattern);
		defaultNumberStyle.setDataFormat(formatNumber);
		defaultNumberStyle.setFont(getFontNormal());
		defaultNumberStyle.setAlignment( XSSFCellStyle.ALIGN_RIGHT );
		return defaultNumberStyle;
	}
	public XSSFCellStyle getDecimalStyle(Locale locale) {
		DecimalFormat format = (DecimalFormat)DecimalFormat.getInstance(locale);
		format.setMaximumFractionDigits(2);
		format.setMaximumIntegerDigits(12);
		String pattern = format.toPattern();
		return getDecimalStyle(pattern);
	}
	public XSSFCellStyle getDecimalStyle(String pattern) {
		XSSFCellStyle defaultDecimalStyle = workbook.createCellStyle();
		defaultDecimalStyle = workbook.createCellStyle();
		short formatNumber = helper.createDataFormat().getFormat(pattern);
		defaultDecimalStyle.setDataFormat(formatNumber);
		defaultDecimalStyle.setFont(getFontNormal());
		defaultDecimalStyle.setAlignment( XSSFCellStyle.ALIGN_RIGHT );
		return defaultDecimalStyle;
	}
	
	public XSSFCellStyle getPercentStyle() {
		XSSFCellStyle defaultPercentStyle = workbook.createCellStyle();
		defaultPercentStyle = workbook.createCellStyle();
		short formatNumber = helper.createDataFormat().getFormat("0.0%");
		defaultPercentStyle.setDataFormat(formatNumber);
		defaultPercentStyle.setFont(getFontNormal());
		defaultPercentStyle.setAlignment( XSSFCellStyle.ALIGN_RIGHT );
		return defaultPercentStyle;
	}
	
	public XSSFCellStyle getMoneyStyle(Locale locale) {
		XSSFCellStyle defaultMoneyStyle = workbook.createCellStyle();
		DecimalFormat format = (DecimalFormat)DecimalFormat.getCurrencyInstance(locale);
		format.setMaximumFractionDigits(2);
		short formatNumber = helper.createDataFormat().getFormat(format.toPattern());
		defaultMoneyStyle = workbook.createCellStyle();
		defaultMoneyStyle.setDataFormat(formatNumber);
		defaultMoneyStyle.setFont(getFontNormal());
		defaultMoneyStyle.setAlignment( XSSFCellStyle.ALIGN_RIGHT );
		return defaultMoneyStyle;
	}
	public XSSFColor getBorderColor() {
		return borderColor == null ? Colors.BLACK : borderColor;
	}
	public BorderStyle getBorderLeft() {
		return borderLeft;
	}
	public BorderStyle getBorderRight() {
		return borderRight;
	}
	public BorderStyle getBorderTop() {
		return borderTop;
	}
	public BorderStyle getBorderBottom() {
		return borderBottom;
	}
	
}
