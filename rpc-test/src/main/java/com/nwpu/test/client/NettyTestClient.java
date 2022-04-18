package com.nwpu.test.client;

import com.nwpu.client.RpcClient;
import com.nwpu.client.netty.NettyClient;
import com.nwpu.serializer.CommonSerializer;
import com.nwpu.test.api.ByeService;
import com.nwpu.test.api.HelloObject;
import com.nwpu.test.api.HelloService;
import com.nwpu.proxy.RpcClientProxy;
/**
 * 测试用Netty消费者
 *
 * @author ziyang
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);

        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }

}
