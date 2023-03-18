package com.lp.demo.component.common.constants;

import java.util.Optional;

public enum CodeEnum implements BaseEnum<CodeEnum> {
  /**
   * 整个系统通用编码 xx_xx_xxxx (服务标识_业务_错误编号，便于错误快速定位
   */
  SUCCESS(1, "操作成功"),
  FAIL(0, "操作失败"),

  NOT_FIND_ERROR(10001, "未查询到信息"),
  SAVE_ERROR(10002, "保存信息失败"),
  UPDATE_ERROR(10003, "更新信息失败"),
  VALIDATE_ERROR(10004, "数据检验失败"),
  STATUS_HAS_VALID(10005, "状态已经被启用"),
  STATUS_HAS_INVALID(10006, "状态已经被禁用"),
  SYSTEM_ERROR(10007, "系统异常"),
  BUSINESS_ERROR(10008, "业务异常"),
  PARAM_SET_ILLEGAL(10009, "参数设置非法"),
  TRANSFER_STATUS_ERROR(10010, "当前状态不正确，请勿重复提交"),
  NOT_GRANT(10011,"没有操作该功能的权限，请联系管理员");

  private Integer code;
  private String msg;

  CodeEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @Override
  public Integer getCode() {
    return this.code;
  }

  @Override
  public String getName() {
    return this.msg;
  }

  public static Optional<CodeEnum> of(Integer code) {
    return Optional.ofNullable(BaseEnum.parseByCode(CodeEnum.class, code));
  }
}
