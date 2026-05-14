package cn.com.springCai.generic.impl;

import cn.com.springCai.generic.inter.TestInter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author caiJH
 * @Date 2025/5/30 11:14 AM
 * @Version 1.0
 * 泛型与桥接方法 JDK层面
 */
public class TestInterImpl implements TestInter<String,Integer> {

    @Override
    public String getT(Integer integer) {
        return null;
    }

    @Override
    public Integer getV(String s) {
        return null;
    }

    public static void main(String[] args) {

        TestInterImpl.test();
    }

    public static void test(){
        String filePath = "/Users/caijinhui/Downloads/11111.xlsx"; // 替换为你的Excel文件路径
        int columnIndex = 13; // 替换为你想要读取的列的索引（从0开始）
        List<String> columnData = readColumnFromExcel(filePath, columnIndex);
        int count = 1;
        for (String data : columnData) {
            if(count != 1){
                changeData(data);
            }
            count++;
        }
    }

    public static void changeData(String str){
        // 使用StringBuilder进行替换
        String newSubstring = "&*&";
        String field = "#{";
        String field1 = "}";
        int index = str.indexOf(field);
        int index1 = str.indexOf(field1);
        Map<Integer,Integer> map = new HashMap<>();
        int num = 0;
        while (index != -1){
            map.put(index, index1);
//            System.out.println("index字段首次出现的位置：" + index);
            index = str.indexOf(field, index + field.length());

//            System.out.println("index1字段首次出现的位置：" + index1);
            index1 = str.indexOf(field1, index1 + field.length());
            num++;
        }
        System.out.println("num有" + num + "个");
        String end = "&";
        for (int i = 1; i <= num; i++){
            index = str.indexOf(field);
            index1 = str.indexOf(field1);
            StringBuilder sb = new StringBuilder(str);
            sb.replace(index, index1+1, newSubstring);
            int endNum = str.lastIndexOf(end);
            if((endNum + 1) == index){
                sb.insert(index,",");
            }
            str = sb.toString();
            if(i+1 == num){
                System.out.println(str);
                System.out.printf("\n");
            }
        }
    }

    public static List<String> readColumnFromExcel(String filePath, int columnIndex) {
        List<String> columnData = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表，可以根据需要修改
            for (Row row : sheet) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            columnData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            columnData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            columnData.add(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        default:
                            columnData.add("");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return columnData;
    }
}
