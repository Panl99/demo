package com.lp.demo.common.util.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2022/5/31 10:11
 * @desc
 **/
public class ExcelListener<T> extends AnalysisEventListener<T> {

    private final List<Object> data = new ArrayList<>();

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        data.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<Object> getData() {
        return data;
    }

}
