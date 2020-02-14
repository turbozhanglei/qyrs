package com.gy.resource.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest implements Serializable {
    private static final long serialVersionUID = 8163035469984309204L;
    @ApiModelProperty(notes = "token")
    private String token;
}
