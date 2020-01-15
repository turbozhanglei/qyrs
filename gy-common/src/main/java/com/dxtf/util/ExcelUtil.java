package com.dxtf.util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * 表格工具类
 */
public class ExcelUtil {

    public static void CreateExcel(HSSFWorkbook wb,String sname, List<String> headers, List<String[]> datas){
        HSSFSheet sheet = wb.createSheet(sname);
//        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//        HSSFRow row1=sheet.createRow(0);
//        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        HSSFCell cell=row1.createCell(0);
//        //设置单元格内容
//        cell.setCellValue("学员考试成绩一览表");
//        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
        //在sheet里创建第二行
        HSSFRow row2=sheet.createRow(0);
        //创建单元格并设置单元格内容
        for(int i = 0;i<headers.size();i++){
            row2.createCell(i).setCellValue(headers.get(i));
        }
        for(int i = 0;i<datas.size();i++){
            //在sheet里创建第三行
            HSSFRow rowData=sheet.createRow(i+1);
            String[] dataStr = datas.get(i);
            for(int j = 0;j<dataStr.length;j++){
                rowData.createCell(j).setCellValue(dataStr[j]);
            }
        }

    }
}
