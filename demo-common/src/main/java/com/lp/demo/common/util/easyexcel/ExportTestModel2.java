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
@SheetName("2")
@Data
@AllArgsConstructor
@NoArgsConstructor // 导入必须添加无参构造函数
@ColumnWidth(15) // 列宽
@HeadRowHeight(20) // 头行高度
public class ExportTestModel2 {

    @ExcelProperty(index = 0, value = "标题")
    private String title;
    @ExcelProperty(index = 1, value = "描述")
    private String desc;

    public ExportTestModel2(Integer id) {
        this.title = "标题" + id;
        this.desc = "描述" + id;
    }

}
