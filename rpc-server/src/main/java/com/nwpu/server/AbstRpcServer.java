package com.nwpu.server;

import com.nwpu.serializer.CommonSerializer;

/**
 * @author Junho
 * @date 2022/4/17 17:26
 */
public abstract class AbstRpcServer implements RpcServer{
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
}
