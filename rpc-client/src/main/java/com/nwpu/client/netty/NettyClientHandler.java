package com.nwpu.client.netty;

import com.nwpu.protocaol.RpcRequest;
import com.nwpu.protocaol.RpcResponse;
import com.nwpu.serializer.CommonSerializer;
import com.nwpu.util.factory.SingleFactory;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Junho
 * @date 2022/4/18 13:58
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final UnProcessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingleFactory.getInstance(UnProcessedRequests.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse msg) throws Exception {
        try {
            log.info(String.format("客户端接收到消息: %s", msg));
            unprocessedRequests.complete(msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

//    FIXME-异常 和 心跳检测
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("过程调用时有错误发生:");
//        cause.printStackTrace();
//        ctx.close();
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.WRITER_IDLE) {
//                log.info("发送心跳包 [{}]", ctx.channel().remoteAddress());
//                Channel channel = ChannelProvider.get(
//                        (InetSocketAddress) ctx.channel().remoteAddress(),
//                        CommonSerializer.getSerializer(CommonSerializer.DEFAULT_SERIALIZER));
//                RpcRequest rpcRequest = new RpcRequest();
//                rpcRequest.setHeartBeat(true);
//                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }
}
