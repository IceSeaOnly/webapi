package site.binghai.lib.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javassist.NotFoundException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExcelUtils {

    public static Builder builder(){
        return new Builder();
    }

    public static void json2Excel(OutputStream outputStream, LinkedHashMap<String,String> mapper, JSONArray data) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        sheet.createFreezePane(0, 1);
        short col = 0;
        for (String key : mapper.keySet()) {
            cteateCell(wb, row, col++, mapper.get(key));
        }

        for (int i = 0; i < data.size(); i++) {
            HSSFRow rowi = sheet.createRow((short) (i+1));
            JSONObject obj = data.getJSONObject(i);
            col = 0;
            for (String key : mapper.keySet()) {
                cteateCell(wb, rowi, col++, obj.getString(key));
            }
        }
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    private static void cteateCell(HSSFWorkbook wb, HSSFRow row, short col, String val) {
        HSSFCell cell = row.createCell(col);
        cell.setCellValue(val);
        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cell.setCellStyle(cellstyle);
    }

    public static class Builder {
        private LinkedHashMap<String,String> mapper;
        private JSONArray data;
        private OutputStream os;


        public Builder() {
            this.mapper = new LinkedHashMap<>();
            this.data = new JSONArray();
            this.os = null;
        }

        public Builder outputStream(OutputStream os){
            this.os = os;
            return this;
        }

        public Builder mapper(String key,String name){
            this.mapper.put(key,name);
            return this;
        }

        public Builder addData(JSONObject object){
            this.data.add(object);
            return this;
        }

        public Builder putAll(JSONArray array){
            this.data.addAll(array);
            return this;
        }

        public void build() throws IOException {
            json2Excel(this.os,this.mapper,this.data);
        }

        public void webBuild(HttpServletResponse response,String fileName) throws IOException {
            this.os = response.getOutputStream();
            response.reset();// 清空输出流
            // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
            if(fileName == null){
                fileName = TimeTools.now() + "__" + data.size();
            }
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            build();
        }
    }



    /**
     * 使用规范
     * 输入的表格格式，第一行为T 类内的对应的字段名，第二行为对应的字段释义，用来方便阅读，工具不会读入第2行
     * 接下来的每一行输入对应的记录值接口，例：
     * ----------------------
     * | name | age | score |
     * ----------------------
     * | 姓名  | 年龄 |  分数 |
     * ----------------------
     * | Tom  |  11 |  100  |
     * ----------------------
     * | Bob  |  11 |  100  |
     */
    public static <T> List<T> readExcelData(MultipartFile file, Class<T> clazz) throws Exception {
        checkFile(file);
        Workbook workbook = getWorkBook(file);
        List<T> list = new ArrayList<>();
        String[] mapper = null;
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                if (lastRowNum - firstRowNum < 2) {
                    throw new Exception("表格内无有效数据!");
                }

                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    if (rowNum == firstRowNum + 1) continue;
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    int firstCellNum = row.getFirstCellNum();
                    int lastCellNum = row.getLastCellNum();
                    if (rowNum == firstRowNum) {
                        mapper = new String[lastCellNum - firstCellNum + 1];
                    }
                    JSONObject item = new JSONObject();
                    //循环当前列
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String val = getCellValue(cell);
                        if (rowNum == firstRowNum) {
                            mapper[cellNum - firstCellNum] = val;
                        } else {
                            item.put(mapper[cellNum - firstCellNum], val);
                        }
                    }
                    if (rowNum != firstRowNum) {
                        list.add(item.toJavaObject(clazz));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws Exception {
        //判断文件是否存在
        if (null == file) {
            throw new NotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            throw new NotSupportedException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            throw e;
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if(cell == null) return null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                } else {
                    DataFormatter dataFormatter = new DataFormatter();
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula() + "";
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = null;
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = null;
                break;
            default:
                cellValue = null;
                break;
        }
        return cellValue;
    }


}
