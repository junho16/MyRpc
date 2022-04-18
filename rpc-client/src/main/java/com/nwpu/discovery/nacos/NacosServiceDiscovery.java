package com.nwpu.discovery.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.nwpu.constant.RpcError;
import com.nwpu.exception.RpcException;
import com.nwpu.util.nacos.NacosUtil;
import com.nwpu.discovery.ServiceDiscovery;
import com.nwpu.loadBalancer.LoadBalancer;
import com.nwpu.loadBalancer.RandomLoadBalancer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 对于nacos的服务发现
 * @author Junho
 * @date 2022/4/18 10:58
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer){
        if(loadBalancer == null)
            this.loadBalancer = new RandomLoadBalancer();
        else
            this.loadBalancer = loadBalancer;
    }

    public NacosServiceDiscovery(){
        this.loadBalancer = new RandomLoadBalancer();
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try{
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            if(instances.size() == 0) {
                log.error("未找到对应的服务: " + serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        }catch (NacosException e){
            log.error("获取服务时发生异常:", e);
        }
        return null;
    }

}

