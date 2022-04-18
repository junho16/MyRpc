package loadBalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡接口
 * @author Junho
 * @date 2022/4/18 11:08
 */
public interface LoadBalancer {

    Instance select(List<Instance> instances);

}