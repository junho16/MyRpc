package com.nwpu.test.server;


import com.nwpu.annotation.ServiceScan;
import com.nwpu.serializer.CommonSerializer;
import com.nwpu.server.RpcServer;
import com.nwpu.server.netty.NettyRpcServer;

/**
 * 测试用Netty服务提供者（服务端）
 */
@ServiceScan
public class NettyServerTest {

    public static void main(String[] args) {
        RpcServer server = new NettyRpcServer("127.0.0.1", 9999, CommonSerializer.DEFAULT_SERIALIZER);
        server.start();
    }

}
