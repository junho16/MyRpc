package com.nwpu.provider;

import com.nwpu.constant.RpcError;
import com.nwpu.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存服务端的服务注册的结果
 * TODO-等待修改，也可能不是这个用处
 * @author Junho
 * @date 2022/4/17 17:51
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider{

    /**
     * 保存服务名以及服务对象实例
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 保存注册的服务名
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if(registeredService.contains(serviceName)){
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName , service);
        log.info("已向接口: {} 注册服务: {}",
                service.getClass().getInterfaces(),
                serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            //异常：找不到对应的服务
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }

}
