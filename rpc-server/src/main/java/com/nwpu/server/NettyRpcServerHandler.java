package com.nwpu.server;


import com.alibaba.nacos.common.http.handler.RequestHandler;
import com.nwpu.protocaol.RpcRequest;
import com.nwpu.protocaol.RpcResponse;
import com.nwpu.util.factory.SingleFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Junho
 * @date 2022/4/17 21:24
 */
@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    private final RpcRequestHandler requestHandler;

    public NettyRpcServerHandler() {
        this.requestHandler = SingleFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        try {
            if(rpcRequest.getHeartBeat()){
                log.info("接收到客户端心跳包...");
                return;
            }

            log.info("服务端已经接收到请求：{}" , rpcRequest);
            Object result = requestHandler.handle(rpcRequest);

            if (channelHandlerContext.channel().isActive()
                    && channelHandlerContext.channel().isWritable()) {

                channelHandlerContext.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));

            } else {
                log.error("通道不可写");
            }
        } finally {
            ReferenceCountUtil.release(rpcRequest);
        }
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
