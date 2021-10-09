package com.lp.demo.common.dto;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author lp
 **/
@Data
public class RequestHeaderDto implements Serializable {

    private static final long serialVersionUID = -1441336039072548833L;

    /**
     * 请求id
     */
    String requestId;

    /**
     * 客户端id
     */
    String clientId;

    /**
     * 密钥
     */
    String secret;

    public RequestHeaderDto() {
    }

    public RequestHeaderDto(HttpServletRequest request) {
        this.requestId = request.getHeader("X-RequestId");
        this.clientId = request.getHeader("X-ClientId");
        this.secret = request.getHeader("X-Secret");
    }
}
