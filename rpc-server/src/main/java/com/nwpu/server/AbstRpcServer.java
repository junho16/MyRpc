package com.nwpu.server;

import com.nwpu.annotation.RpcService;
import com.nwpu.annotation.ServiceScan;
import com.nwpu.constant.RpcError;
import com.nwpu.exception.RpcException;
import com.nwpu.provider.ServiceProvider;
import com.nwpu.registry.ServiceRegistry;
import com.nwpu.serializer.CommonSerializer;
import com.nwpu.util.common.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author Junho
 * @date 2022/4/17 17:26
 */
@Slf4j
public abstract class AbstRpcServer implements RpcServer{

    // 默认设置为json
//    protected int DEFAULT_SERIALIZER = CommonSerializer.DEFAULT_SERIALIZER;

    protected String host;

    protected int port;

    /**
     * 服务注册（注册中心）
     */
    protected ServiceRegistry serviceRegistry;

    /**
     * 用来注册并提供服务（本地）
     */
    protected ServiceProvider serviceProvider;

    public void scanServices() {

        String mainClassName = ReflectUtil.getStackTrace();

        Class<?> startClass;

        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(ServiceScan.class)) {
                log.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }

        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }

        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);

        for(Class<?> clazz : classSet) {
            if(clazz.isAnnotationPresent(RpcService.class)) {
                String serviceName = clazz.getAnnotation(RpcService.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface: interfaces){
                        publishService(obj, oneInterface.getCanonicalName());
                    }
                } else {
                    publishService(obj, serviceName);
                }
            }
        }
    }


    /**
     * 发布 / 注册
     * 分别保存到nacos和本地集合中
     * @param service
     * @param serviceName
     * @param <T>
     */
    @Override
    public <T> void publishService(T service, String serviceName) {

        serviceProvider.addServiceProvider(service, serviceName);

        serviceRegistry.register(serviceName, new InetSocketAddress(host, port));

    }
}
