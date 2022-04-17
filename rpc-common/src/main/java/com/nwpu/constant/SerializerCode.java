package com.nwpu.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化方式編號
 * @author Junho
 * @date 2022/4/17 14:16
 */
@Getter
@AllArgsConstructor
public enum SerializerCode {
    
    KRYO(0),
    JSON(1),
    HESSIAN(2),
    PROTOBUF(3);

    private final int code;

}
