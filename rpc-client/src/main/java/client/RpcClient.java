package client;

import com.nwpu.protocaol.RpcRequest;
import com.nwpu.serializer.CommonSerializer;

/**
 * @author Junho
 * @date 2022/4/17 10:30
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.JSON_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);

}
