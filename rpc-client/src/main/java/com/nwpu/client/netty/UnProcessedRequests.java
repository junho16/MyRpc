package com.nwpu.client.netty;

import com.nwpu.protocaol.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于耗时的 未处理完成的请求的处理
 * @author Junho
 * @date 2022/4/18 11:38
 */
public class UnProcessedRequests {

    private static ConcurrentHashMap<String , CompletableFuture<RpcResponse>>
            unProcessedResponseFutures = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        unProcessedResponseFutures.put(requestId, future);
    }

    public void remove(String requestId) {
        unProcessedResponseFutures.remove(requestId);
    }

    public void complete(RpcResponse rpcResponse) {

        CompletableFuture<RpcResponse> future = unProcessedResponseFutures.remove(rpcResponse.getRequestId());

        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }

    }

}
