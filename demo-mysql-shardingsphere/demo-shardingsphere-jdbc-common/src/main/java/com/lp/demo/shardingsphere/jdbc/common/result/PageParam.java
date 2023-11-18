package com.lp.demo.shardingsphere.jdbc.common.result;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageHelper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 7180272017431371846L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private String orderBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    public void setKeyword(String keyword) {
        if (keyword != null && keyword.trim().length() > 0) {
            this.keyword = keyword.trim();
        }
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null) {
            return;
        }
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            return;
        }
        this.pageSize = pageSize;
    }

    public void startPage() {
        if (StrUtil.isEmpty(orderBy)) {
            PageHelper.startPage(pageNum, pageSize);
        } else {
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
}
