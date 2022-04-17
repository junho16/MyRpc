package com.nwpu.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.nwpu.constant.RpcError;
import com.nwpu.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import com.nwpu.registry.ServiceRegistry;
import com.nwpu.util.nacos.NacosUtil;

import java.net.InetSocketAddress;

/**
 * nacos注册服务
 * @author Junho
 * @date 2022/4/17 16:39
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务时发生异常:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
