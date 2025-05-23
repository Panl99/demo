package com.lp.demo.common.util.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.lp.demo.common.exception.DisplayableException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2022/5/31 10:08
 * @desc
 **/
public class ExcelUtil {

    /**
     *
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> clazz) {
        ExcelListener<T> listener = new ExcelListener<>();
        try (ExcelReader excelReader = EasyExcel.read(inputStream, clazz, listener).build()) {
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        }
        return listener.getData();
    }

    /**
     * 读取 Excel(多个 sheet)
     */
    public static <T> List<T> readExcel(ExcelReader reader, Class<T> rowModel, int sheetCount) {
        if (reader == null) {
            return new ArrayList<>();
        }
        List<ReadSheet> readSheetList = new ArrayList<>();
        ExcelListener<T> excelListener = new ExcelListener<>();
        ReadSheet readSheet = EasyExcel.readSheet(sheetCount)
                .head(rowModel)
                .registerReadListener(excelListener)
                .build();
        readSheetList.add(readSheet);
        reader.read(readSheetList);
        return getExtendsBeanList(excelListener.getData(), rowModel);
    }

    /**
     * 读取 Excel(多个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     * getExtendsBeanList 主要是做Bean的属性拷贝 ，可以通过ExcelReader中添加的数据集直接获取
     *
     * @param excel    文件
     * @param rowModel 实体类映射
     */
    private static List[] readExcel(MultipartFile excel, Integer sheetNo, Class<?>[] rowModel) throws DisplayableException {
        ExcelReader reader = getReader(excel);
        if (reader == null) {
            return new ArrayList[rowModel.length];
        }
        List[] result = new ArrayList[rowModel.length];
        for (int sheetCount = 0; sheetCount < rowModel.length; sheetCount++) {
            if (sheetNo != null && sheetNo != sheetCount) {
                continue;
            }
            result[sheetCount].addAll(readExcel(reader, rowModel[sheetCount], sheetCount));
        }
        return result;
    }

    /**
     * 读取 Excel(多个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     * getExtendsBeanList 主要是做Bean的属性拷贝 ，可以通过ExcelReader中添加的数据集直接获取
     *
     * @param excel    文件
     * @param rowModel 实体类映射
     */
    public static List[] readExcel(MultipartFile excel, Class<?>... rowModel) throws DisplayableException {
        ExcelReader reader = getReader(excel);
        if (reader == null) {
            return new ArrayList[rowModel.length];
        }
        List[] result = new ArrayList[rowModel.length];
        for (int sheetCount = 0; sheetCount < rowModel.length; sheetCount++) {
            result[sheetCount] = new ArrayList<>(readExcel(reader, rowModel[sheetCount], sheetCount));
        }
        return result;
    }

    /**
     * 读取 Excel(单个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     * getExtendsBeanList 主要是做Bean的属性拷贝 ，可以通过ExcelReader中添加的数据集直接获取
     */
    public static <T> List<T> readFirstSheetExcel(MultipartFile excel, Class<T> rowType) throws DisplayableException {
        ExcelReader reader = getReader(excel);
        if (reader == null) {
            return new ArrayList<>();
        }
        return readExcel(reader, rowType, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel    文件
     * @param rowModel 实体类映射
     * @param sheetNo  sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public static <T> List readExcel(MultipartFile excel, Class<T> rowModel, int sheetNo) throws DisplayableException {
        Class[] classes = {rowModel};
        return ExcelUtil.readExcel(excel, sheetNo, classes)[0];
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     * 自定义WriterHandler 可以定制行列数据进行灵活化操作
     *
     * @param response  HttpServletResponse
     * @param data      数据 list
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     */
    public static <T> void writeExcel(HttpServletResponse response,
                                      List<T> data,
                                      String fileName,
                                      String sheetName,
                                      ExcelTypeEnum excelTypeEnum,
                                      Class clazz) throws DisplayableException {
        if (sheetName == null || "".equals(sheetName)) {
            sheetName = "sheet1";
        }

        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        // 表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(getOutputStream(fileName, response, excelTypeEnum), clazz)
                .excelType(excelTypeEnum)
                .sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .doWrite(data);
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     * 自定义WriterHandler 可以定制行列数据进行灵活化操作
     *
     * @param response HttpServletResponse
     * @param list     数据 list
     * @param fileName 导出的文件名
     */
    public static <T> void writeExcel(HttpServletResponse response,
                                      List<T> list,
                                      String fileName,
                                      ExcelTypeEnum excelTypeEnum) throws DisplayableException {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        String sheetName = list.get(0).getClass().getAnnotation(SheetName.class).value();
        sheetName = StringUtils.isNotBlank(sheetName) ? sheetName : "sheet1";
        EasyExcel.write(getOutputStream(fileName, response, excelTypeEnum), list.get(0).getClass()).sheet(sheetName).doWrite(list);
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     * 自定义WriterHandler 可以定制行列数据进行灵活化操作
     *
     * @param response HttpServletResponse
     * @param fileName 导出的文件名
     */
    public static void writeExcel(HttpServletResponse response,
                                  String fileName,
                                  ExcelTypeEnum excelTypeEnum,
                                  List... lists) throws DisplayableException {
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(getOutputStream(fileName, response, excelTypeEnum)).build();
            for (int count = 0; count < lists.length; count++) {
                if (CollectionUtils.isEmpty(lists[count])) {
                    continue;
                }
                String sheetName = lists[count].get(0).getClass().getAnnotation(SheetName.class).value();
                sheetName = StringUtils.isNotBlank(sheetName) ? sheetName : "sheet" + (count + 1);
                WriteSheet writeSheet = EasyExcel.writerSheet(count, sheetName)
                        .head(lists[count].get(0).getClass())
                        .build();
                excelWriter.write(lists[count], writeSheet);
            }
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }


    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName,
                                                HttpServletResponse response,
                                                ExcelTypeEnum excelTypeEnum) throws DisplayableException {
        //创建本地文件
//        fileName = URLEncoder.encode(fileName, "UTF-8");
        fileName += "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filePath = fileName + excelTypeEnum.getValue();
        try {
            fileName = new String(filePath.getBytes(), "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            response.flushBuffer();
            return response.getOutputStream();
        } catch (IOException e) {
            throw new DisplayableException("创建文件失败！");
        }
    }

    /**
     * 返回 ExcelReader
     *
     * @param excel 需要解析的 Excel 文件
     */
    public static ExcelReader getReader(MultipartFile excel) throws DisplayableException {
        String fileName = excel.getOriginalFilename();
        if (fileName == null) {
            throw new DisplayableException("文件格式错误！");
        }
        if (!fileName.toLowerCase().endsWith(ExcelTypeEnum.XLS.getValue()) && !fileName.toLowerCase().endsWith(ExcelTypeEnum.XLSX.getValue())) {
            throw new DisplayableException("文件格式错误！");
        }
        InputStream inputStream;
        try {
            inputStream = excel.getInputStream();
            return EasyExcel.read(inputStream).build();
        } catch (IOException e) {
            //do something
        }
        return null;
    }

    /**
     * 利用BeanCopy转换list
     */
    public static <T> List<T> getExtendsBeanList(List<?> list, Class<T> typeClazz) {
        return MyBeanCopy.convert(list, typeClazz);
    }

    /**
     * 导出文件到本地
     *
     * @param os
     * @param fileName
     */
    public static void writeExcelToLocal(ByteArrayOutputStream os, String fileName) {
        String path = "D:"+ File.separator + "Doc" + File.separator + fileName + "_" + System.currentTimeMillis() + ".xlsx";
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
                os.writeTo(fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
