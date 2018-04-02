package com.xunku.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

//导出Excel工具类by wanghui
public class ExcelUtil<T> {

    public void exportExcel(String title, String[] headers,
	    Collection<T> dataset, OutputStream out) {
	try {
	    HSSFWorkbook wb = this.createHSSFWorkbook();
	    HSSFSheet sheet = this.createHSSFSheet(wb, title);
	    HSSFCellStyle tstyle = this.createHSSFCellStyle(wb);
	    HSSFCellStyle cstyle = this.createHSSFCellStyle(wb);
	    HSSFFont tf = this.createTitleFont(wb);
	    tstyle.setFont(tf);
	    HSSFFont tc = this.createContentFont(wb);
	    cstyle.setFont(tc);
	    // 产生表格标题行
	    HSSFRow row = this.getHSSFRow(sheet, headers, tstyle);
	    // 遍历集合数据，产生数据行
	    this.fillData(dataset, sheet, row, cstyle);
	    wb.write(out);
	} catch (SecurityException e) {
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // 遍历集合数据，产生数据行
    @SuppressWarnings("unchecked")
    private void fillData(Collection<T> dataset, HSSFSheet sheet, HSSFRow row,
	    HSSFCellStyle style) throws SecurityException,
	    NoSuchMethodException, IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException {
	Iterator<T> it = dataset.iterator();
	int index = 0;
	while (it.hasNext()) {
	    index++;
	    row = sheet.createRow(index);
	    T t = (T) it.next();
	    // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
	    Field[] fields = t.getClass().getDeclaredFields();
	    for (short i = 0; i < fields.length; i++) {
		HSSFCell cell = this.createHSSFCell(row, i);
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		cell.setCellStyle(style);
		Field field = fields[i];
		String fieldName = field.getName();
		String getMethodName = "get"
			+ fieldName.substring(0, 1).toUpperCase()
			+ fieldName.substring(1);

		Class tCls = t.getClass();
		Method getMethod = tCls
			.getMethod(getMethodName, new Class[] {});
		Object value = getMethod.invoke(t, new Object[] {});
		String textValue = this.getTypeCompare(value, sheet, cell, row,
			i, index);
		if (textValue != null) {
		    HSSFRichTextString richString = new HSSFRichTextString(
			    textValue);
		    cell.setCellValue(richString.getString());
		}
	    }

	}
    }

    // 判断值的类型后进行强制类型转换
    private String getTypeCompare(Object value, HSSFSheet sheet, HSSFCell cell,
	    HSSFRow row, int i, int index) {
	String textValue = null;
	if (value instanceof Integer) {
	    int intValue = (Integer) value;
	    cell.setCellValue(intValue);
	    textValue = value.toString();
	} else if (value instanceof Float) {
	    float fValue = (Float) value;
	    textValue = new HSSFRichTextString(String.valueOf(fValue))
		    .getString();
	    cell.setCellValue(textValue);
	    textValue = value.toString();
	} else if (value instanceof Double) {
	    double dValue = (Double) value;
	    textValue = new HSSFRichTextString(String.valueOf(dValue))
		    .getString();
	    cell.setCellValue(textValue);
	    textValue = value.toString();
	} else if (value instanceof Long) {
	    long longValue = (Long) value;
	    cell.setCellValue(longValue);
	    textValue = value.toString();
	} else if (value instanceof Boolean) {
	    /*
	     * boolean bValue = (Boolean) value; textValue = "男"; if (!bValue) {
	     * textValue = "女"; } textValue = value.toString();
	     */
	} else if (value instanceof Date) {
	    Date date = (Date) value;
	    SimpleDateFormat sdf = new SimpleDateFormat("");
	    textValue = sdf.format(date);
	    textValue = value.toString();
	} else if (value instanceof byte[]) {
	    // 有图片时，设置行高为60px;
	    row.setHeightInPoints(60);
	    // 设置图片所在列宽度为80px,注意这里单位的一个换算
	    // sheet.setColumnWidth(i, (short) (35.7 * 80));
	    // sheet.autoSizeColumn(i);
	    // byte[] bsValue = (byte[]) value;
	    // HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255,
	    // (short) 6, index, (short) 6, index);
	    textValue = value.toString();
	    // anchor.setAnchorType(2);
	    // patriarch.createPicture(anchor, wb.addPicture(bsValue,
	    // HSSFWorkbook.PICTURE_TYPE_JPEG));
	} else {
	    // 其它数据类型都当作字符串简单处理
	    textValue = value.toString();
	}
	return textValue;
    }

    // 产生表格标题行
    private HSSFRow getHSSFRow(HSSFSheet sheet, String[] headers,
	    HSSFCellStyle style) {
	HSSFRow row = sheet.createRow(0);
	for (short i = 0; i < headers.length; i++) {
	    HSSFCell cell = this.createHSSFCell(row, i);
	    cell.setCellStyle(style);
	    HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	    cell.setCellValue(text.getString());
	}
	return row;
    }

    // 创建Excel文件
    public HSSFWorkbook createHSSFWorkbook() {
	return new HSSFWorkbook();
    }

    // 创建工作薄
    public HSSFSheet createHSSFSheet(HSSFWorkbook wb, String name) {
	HSSFSheet sheet = wb.createSheet(name);
	sheet.setDefaultColumnWidth((short) 15);
	return sheet;
    }

    // 创建行
    public HSSFRow createHSSFRow(HSSFSheet sheet, int rowIndex) {
	return sheet.createRow(rowIndex);
    }

    // 创建单元格
    public HSSFCell createHSSFCell(HSSFRow row, short cellIndex) {
	HSSFCell cell = row.createCell(cellIndex);
	// remove by wujian 
	//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	return cell;
    }

    // 单元格格式
    public HSSFCellStyle createHSSFCellStyle(HSSFWorkbook wb) {
	HSSFCellStyle style = wb.createCellStyle();
	// 设置这些样式
	style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
	style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	return style;
    }

    // 单元格内容格式
    public HSSFDataFormat createHSSFDataFormat(HSSFWorkbook wb) {
	return wb.createDataFormat();
    }

    // 设置字体
    public HSSFFont createTitleFont(HSSFWorkbook wb) {
	HSSFFont font = wb.createFont();
	font.setColor(HSSFColor.BLACK.index);
	font.setFontHeightInPoints((short) 11);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	return font;
    }

    // 设置字体
    public HSSFFont createContentFont(HSSFWorkbook wb) {
	HSSFFont font = wb.createFont();
	font.setColor(HSSFColor.BLACK.index);
	font.setFontHeightInPoints((short) 11);
	font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	return font;
    }

    /*
     * public void exportExcel(Collection<T> dataset, OutputStream out) {
     * exportExcel("测试POI导出EXCEL文档", null, dataset, out); }
     * 
     * public void exportExcel(String[] headers, Collection<T> dataset,
     * OutputStream out) { exportExcel("测试POI导出EXCEL文档", headers, dataset, out); }
     */

    /*
     * public static void main(String[] args) { // 测试图书 ExcelUtil<Book> ex =
     * new ExcelUtil<Book>(); String[] headers = { "图书编号", "图书名称", "图书作者",
     * "图书价格", "图书ISBN", "图书出版社", "封面图片" }; List<Book> dataset = new ArrayList<Book>();
     * try { dataset.add(new Book(1, "jsp", "leno", 300.33f, "1234567", "清华出版社",
     * "")); dataset.add(new Book(2, "java编程思想", "brucl", 300.33f, "1234567",
     * "阳光出版社", "")); dataset.add(new Book(3, "DOM艺术", "lenotang", 300.33f,
     * "1234567", "清华出版社", "")); dataset.add(new Book(4, "c++经典", "leno",
     * 400.33f, "1234567", "清华出版社", "")); dataset.add(new Book(5, "c#入门",
     * "leno", 300.33f, "1234567", "汤春秀出版社", ""));
     * 
     * OutputStream out = new FileOutputStream("E://a.xls"); //
     * ex.exportExcel(dataset, out); ex.exportExcel(headers, dataset, out);
     * out.close(); System.out.println("excel导出成功！"); } catch
     * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) {
     * e.printStackTrace(); } }
     */
}
