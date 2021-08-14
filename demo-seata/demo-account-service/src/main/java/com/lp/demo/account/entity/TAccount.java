package com.lp.demo.account.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TAccount extends Model<TAccount> {

    private static final long serialVersionUID = 6599671720940642268L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private Double amount;

}
