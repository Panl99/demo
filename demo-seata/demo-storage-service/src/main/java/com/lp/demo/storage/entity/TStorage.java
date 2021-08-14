package com.lp.demo.storage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TStorage extends Model<TStorage> {

    private static final long serialVersionUID = 7372501103464713080L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String commodityCode;

    private String name;

    private Integer count;

}
