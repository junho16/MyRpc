package com.nwpu.test.server;


import com.nwpu.annotation.RpcService;
import com.nwpu.test.api.ByeService;

/**
 * @author ziyang
 */
@RpcService
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
