package com.nwpu.codec;

import com.nwpu.constant.PackageType;
import com.nwpu.protocaol.RpcRequest;
import com.nwpu.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * @author Junho
 * @date 2022/4/17 21:25
 */
public class CommonEncoder extends MessageToByteEncoder {

    //搞个魔数
    private static int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 协议格式：
     *  魔数  包类型（请求或响应）  序列化方式（默认已设为json）    数据字节长度（防止粘包）    数据（请求或者响应）
     *  +---------------+---------------+-----------------+-------------+
     * |  Magic Number |  Package Type | Serializer Type | Data Length |
     * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
     * +---------------+---------------+-----------------+-------------+
     * |                          Data Bytes                           |
     * |                   Length: ${Data Length}                      |
     * +---------------------------------------------------------------+
     *
     * @param channelHandlerContext
     * @param obj
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        //byteBuf输出流 ，obj信息
        byteBuf.writeInt(MAGIC_NUMBER);

        if (obj instanceof RpcRequest) {
            byteBuf.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            byteBuf.writeInt(PackageType.RESPONSE_PACK.getCode());
        }

        byteBuf.writeInt(serializer.getCode());

        byte[] bytes = serializer.serialize(obj);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

    }
}
