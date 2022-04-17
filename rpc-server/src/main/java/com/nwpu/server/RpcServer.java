package com.nwpu.server;

import com.nwpu.serializer.CommonSerializer;

/**
 * 服务端接口
 * @author Junho
 * @date 2022/4/17 17:24
 */
public interface RpcServer {

    void start();

    <T> void publishService(T service, String serviceName);

}
