package com.nwpu.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 包类型
 * @author Junho
 * @date 2022/4/17 14:16
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
