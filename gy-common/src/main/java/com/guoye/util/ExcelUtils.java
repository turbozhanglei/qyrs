package com.guoye.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtils {

    /**
     * 获取excel里面的数据
     *
     * @param wb
     * @return
     */
    public static List<Dto> getListByExcel(Workbook wb, String columns[]) {
        Sheet sheet = null;
        Row row = null;
        List<Dto> list = null;
        String cellData = null;

        if (wb != null) {
            //用来存放表中数据
            list = new LinkedList<Dto>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i < rownum; i++) {
                Dto map = new BaseDto();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }
        return list;
    }

    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}
