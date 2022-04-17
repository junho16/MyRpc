package com.nwpu.server;

import com.nwpu.protocaol.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Junho
 * @date 2022/4/17 21:24
 */
@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

    }


    /**
     *  TODO-对异常的处理和心跳检测
     */
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("处理过程调用时有错误发生:");
//        cause.printStackTrace();
//        ctx.close();
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.READER_IDLE) {
//                log.info("长时间未收到心跳包，断开连接...");
//                ctx.close();
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }
}
