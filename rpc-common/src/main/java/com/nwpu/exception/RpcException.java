package com.nwpu.exception;

import com.nwpu.constant.RpcError;

/**
 * RPC使用过程中的异常
 * @author Junho
 * @date 2022/4/17 16:44
 */

public class RpcException extends RuntimeException {

    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}
