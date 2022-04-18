package com.nwpu.client.netty;

import com.nwpu.client.RpcClient;
import com.nwpu.constant.RpcError;
import com.nwpu.exception.RpcException;
import com.nwpu.protocaol.RpcResponse;
import com.nwpu.serializer.CommonSerializer;
import com.nwpu.util.factory.SingleFactory;
import com.nwpu.discovery.ServiceDiscovery;
import com.nwpu.discovery.nacos.NacosServiceDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.nwpu.loadBalancer.LoadBalancer;
import com.nwpu.loadBalancer.RandomLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import com.nwpu.protocaol.RpcRequest;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author Junho
 * @date 2022/4/17 16:15
 */
@Slf4j
public class NettyClient implements RpcClient {

    private static final EventLoopGroup group;

    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    private final ServiceDiscovery serviceDiscovery;

    private final CommonSerializer serializer;

    private final UnProcessedRequests unProcessedRequests;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }

    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }

    public NettyClient(Integer serializerType, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getSerializer(serializerType);
        this.unProcessedRequests = SingleFactory.getInstance(UnProcessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            //发现服务后获取其地址
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());

            /**
             * 拿到通道的逻辑：
             *  看map里有无，有则获取，
             *  无则使用bootstrap新建一个channel连接目标主机与端口，并在加入map后返回channel
             */
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }

            /**
             * 将一个新的resultFuture，放在未处理完成的请求集合unProcessedRequests中
             * 方便后续获取执行结果
             *
             * 然后向channel中写入请求
             */
            unProcessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    log.error("发送消息时有错误发生: ", future1.cause());
                }
            });
        } catch (InterruptedException e) {
            unProcessedRequests.remove(rpcRequest.getRequestId());
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }
}
