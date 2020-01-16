package com.guoye.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.List;

/**
 * 表格工具类
 */
public class ExcelUtilForDetailedOrder {



    public static void CreateExcel(HSSFWorkbook wb, String sname, List<String> headers, List<String[]> datas){
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
        sheet.setColumnWidth(0,256*27);
        sheet.setColumnWidth(1,256*19);
        sheet.setColumnWidth(2,256*11);
        sheet.setColumnWidth(3,256*13);
        sheet.setColumnWidth(4,256*13);
        sheet.setColumnWidth(5,256*25);
        sheet.setColumnWidth(6,256*16);
        sheet.setColumnWidth(7,256*11);
        sheet.setColumnWidth(8,256*15);
        sheet.setColumnWidth(9,256*11);
        sheet.setColumnWidth(10,256*11);
        sheet.setColumnWidth(11,256*11);
        sheet.setColumnWidth(12,256*19);
        sheet.setColumnWidth(13,256*11);
        sheet.setColumnWidth(14,256*21);

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);


        //创建单元格并设置单元格内容
        for(int i = 0;i<headers.size();i++){
            row2.createCell(i).setCellValue(headers.get(i));
        }
        double orderPayCount=0;//订单支付总金额
        double shouldPayed=0;//应付总金额
        int orderCount=0;//商品数量
        for(int i = 0;i<datas.size();i++){
            //在sheet里创建第三行
            HSSFRow rowData=sheet.createRow(i+1);
            String[] dataStr = datas.get(i);
            for(int j = 0;j<dataStr.length;j++){
                rowData.createCell(j).setCellValue(dataStr[j]);
                if (j==7 && dataStr[j] !=""){
                    orderCount+= Integer.parseInt(dataStr[j]);
                }else if (j==8 && dataStr[j] !=""){
                    orderPayCount+= Double.parseDouble(dataStr[j]);
                }else if (j==9 && dataStr[j] !=""){
                    shouldPayed+= Double.parseDouble(dataStr[j]);
                }
            }
        }
        HSSFRow rowcout=sheet.createRow(datas.size()+1);
        rowcout.createCell(15).setCellValue("总计");
        HSSFCell cell = rowcout.createCell(0);
        cell.setCellValue(datas.size());
        cell.setCellStyle(style);
/*      rowcout.createCell(6).setCellValue(countprice);
        rowcout.createCell(7).setCellValue(orderCount);*/
        HSSFCell cell7 = rowcout.createCell(7);
        cell7.setCellValue(orderCount);
        cell7.setCellStyle(style);
        HSSFCell cell8 = rowcout.createCell(8);
        cell8.setCellValue(orderPayCount);
        cell8.setCellStyle(style);
        /*rowcout.createCell(8).setCellValue(orderPayCount);*/
        HSSFCell cell9 = rowcout.createCell(9);
        cell9.setCellValue(shouldPayed);
        cell9.setCellStyle(style);
        /*rowcout.createCell(9).setCellValue(shouldPayed);*/

    }
}
