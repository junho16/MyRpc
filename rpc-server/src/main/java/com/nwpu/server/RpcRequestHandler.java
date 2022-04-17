package com.nwpu.server;

import com.nwpu.constant.ResponseCode;
import com.nwpu.protocaol.RpcRequest;
import com.nwpu.protocaol.RpcResponse;
import com.nwpu.provider.ServiceProvider;
import com.nwpu.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 处理远程调用的请求
 * @author Junho
 * @date 2022/4/17 22:14
 */
@Slf4j
public class RpcRequestHandler {

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {

        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);

    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;

        try {

            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());

            result = method.invoke(
                    service,
                    rpcRequest.getParameters()
            );

            log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }

        return result;
    }

}
