package client;

import protocaol.RpcRequest;
import serializer.CommonSerializer;

/**
 * @author Junho
 * @date 2022/4/17 10:30
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);

}
