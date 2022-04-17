package com.nwpu.provider;

/**
 * 保存、提供服务实例对象 通用接口
 * @author Junho
 * @date 2022/4/17 17:50
 */
public interface ServiceProvider {

    <T> void addServiceProvider(T service, String serviceName);

    Object getServiceProvider(String serviceName);

}
