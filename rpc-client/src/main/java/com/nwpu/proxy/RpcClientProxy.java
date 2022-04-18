package com.nwpu.proxy;

import com.nwpu.client.netty.NettyClient;
import com.nwpu.client.RpcClient;
import lombok.extern.slf4j.Slf4j;
import com.nwpu.protocaol.RpcRequest;
import com.nwpu.protocaol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class RpcClientProxy implements InvocationHandler {


    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());

        RpcRequest rpcRequest = new RpcRequest(
                UUID.randomUUID().toString(),
                method.getDeclaringClass().getName(),
                method.getName(),
                args,
                method.getParameterTypes(),
                false);

        RpcResponse rpcResponse = null;

        if (client instanceof NettyClient) {
            try {
                CompletableFuture<RpcResponse> completableFuture =
                        (CompletableFuture<RpcResponse>) client.sendRequest(rpcRequest);

                rpcResponse = completableFuture.get();
            } catch (Exception e) {
                log.error("方法调用请求发送失败", e);
                return null;
            }
        }
//        FIXME-暂时只有netty的实现
//        if (com.nwpu.client instanceof SocketClient) {
//            rpcResponse = (RpcResponse) com.nwpu.client.sendRequest(rpcRequest);
//        }
//        RpcMessageChecker.check(rpcRequest, rpcResponse);

        return rpcResponse.getData();
    }
}
