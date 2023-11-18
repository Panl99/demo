package com.lp.demo.shardingsphere.jdbc.common.result;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class CommonPage<T> implements Serializable {

    private static final long serialVersionUID = 6236412523320052148L;

    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 结果集
     */
    private List<T> list;


    public static <T> CommonPage<T> resultPage(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        CommonPage<T> result = new CommonPage<>();
        result.setPages(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    public static <T> CommonPage<T> resultPage(List<T> list, int pageSize, int pageNum, int pages, long total) {
        CommonPage<T> result = new CommonPage<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setPages(pages);
        result.setTotal(total);
        result.setList(list);
        return result;
    }

    public static <T, K> CommonPage<T> resultPage(List<T> list, PageInfo<K> pageInfo) {
        CommonPage<T> result = new CommonPage<>();
        result.setPages(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(list);
        return result;
    }

    public static void startPage(PageParam pageParam) {
        if (StringUtils.isEmpty(pageParam.getOrderBy())) {
            PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        } else {
            PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.getOrderBy());
        }
    }
}
