package com.lp.demo.common.util.easyexcel;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.JsonResult;
import com.lp.demo.common.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2022/5/31 15:34
 * @desc
 **/
@Slf4j
@RestController
public class EasyExcelUtilController {


    @GetMapping("getExportData")
    public void getExportData(HttpServletResponse response) {
        List<ExportTestModel> data = new ArrayList<>();
        for (int i = 0; i <= 1000; i++) {
            data.add(new ExportTestModel(i));
        }
        String fileName = "导出测试";
        try {
            ExcelUtil.writeExcel(response, data, fileName, fileName, ExcelTypeEnum.XLSX, ExportTestModel.class);
        } catch (DisplayableException e) {
            log.info("" + e);
        }
    }

    @GetMapping("getExportDataMultiSheet")
    public JsonResult getExportDataMultiSheet(HttpServletResponse response) {
        List<ExportTestModel> list = new ArrayList<>();
        for (int i = 0; i <= 1000; i++) {
            list.add(new ExportTestModel(i));
        }
        List<ExportTestModel2> list2 = new ArrayList<>();
        for (int i = 100; i >= 0; i--) {
            list2.add(new ExportTestModel2(i));
        }
        try {
            ExcelUtil.writeExcel(response, "导出测试", ExcelTypeEnum.XLSX, list, list2);
        } catch (DisplayableException e) {
            log.info(""+e);
        }
        return new JsonResult<>(ResultEnum.SUCCESS);
    }

    @PostMapping("importExcel")
    public JsonResult importExcel(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        String uploadedFile = itr.next();
        List<MultipartFile> files = request.getFiles(uploadedFile);
        if (CollectionUtils.isEmpty(files)) {
            return new JsonResult<>(ResultEnum.FAIL.getCode(), "请选择文件!");
        }
        try {
            List<ExportTestModel> list = ExcelUtil.readFirstSheetExcel(files.get(0), ExportTestModel.class);
            return new JsonResult<>(ResultEnum.SUCCESS, JSON.toJSONString(list, SerializerFeature.PrettyFormat));
        } catch (DisplayableException e) {
            log.info(""+e);
            return new JsonResult<>(ResultEnum.FAIL.getCode(), e.getMessage());
        }
    }

    @PostMapping("importExcelMultiSheet")
    public JsonResult importExcelMultiSheet(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        String uploadedFile = itr.next();
        List<MultipartFile> files = request.getFiles(uploadedFile);
        if (CollectionUtils.isEmpty(files)) {
            return new JsonResult<>(ResultEnum.FAIL.getCode(), "请选择文件!");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            List[] result = ExcelUtil.readExcel(files.get(0), ExportTestModel.class, ExportTestModel2.class);
            map.put("1", result[0]);
            map.put("2", result[1]);
            return new JsonResult<>(ResultEnum.SUCCESS, JSON.toJSONString(map, SerializerFeature.PrettyFormat));
        } catch (DisplayableException e) {
            log.info(""+e);
            return new JsonResult<>(ResultEnum.FAIL.getCode(), e.getMessage());
        }
    }
}
