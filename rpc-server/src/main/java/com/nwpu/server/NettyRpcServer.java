package com.nwpu.server;

import com.nwpu.codec.CommonDecoder;
import com.nwpu.codec.CommonEncoder;
import com.nwpu.provider.ServiceProviderImpl;
import com.nwpu.registry.nacos.NacosServiceRegistry;
import com.nwpu.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Junho
 * @date 2022/4/17 19:42
 */
@Slf4j
public class NettyRpcServer extends AbstRpcServer{

    private final CommonSerializer serializer;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;


    public NettyRpcServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyRpcServer(String host, int port, Integer serializerType) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getSerializer(serializerType);
        scanServices();
    }


    @Override
    public void start() {
//        TODO-ShutdownHook
//        ShutdownHook.getShutdownHook().addClearAllHook();

        bossGroup = new NioEventLoopGroup();

        workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new CommonEncoder(serializer))
                                    .addLast(new CommonDecoder())
                                    .addLast(new NettyRpcServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
