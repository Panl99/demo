package com.lp.demo.common.util.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lp
 * @date 2022/5/31 15:36
 * @desc
 **/
@SheetName("1")
@Data
@AllArgsConstructor
@NoArgsConstructor // 导入必须添加无参构造函数
@ColumnWidth(15) // 列宽
@HeadRowHeight(20) // 头行高度
public class ExportTestModel {

    @ExcelProperty(value = "标题", index = 0)
    private String title;

    public ExportTestModel(Integer id) {
        this.title = "标题" + id;
    }

}
