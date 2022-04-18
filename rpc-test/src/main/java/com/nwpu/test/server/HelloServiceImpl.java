package com.nwpu.test.server;

import com.nwpu.annotation.RpcService;
import com.nwpu.test.api.HelloObject;
import com.nwpu.test.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author ziyang
 */
@RpcService
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息：{}", object.getMessage());
        return "这是Impl1方法";
    }

}
