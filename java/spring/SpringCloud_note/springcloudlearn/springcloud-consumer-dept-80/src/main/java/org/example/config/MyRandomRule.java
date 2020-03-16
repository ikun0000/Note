package org.example.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class MyRandomRule extends AbstractLoadBalancerRule {

    private int total = 0;      // 服务调用次数
    private int index = 0;      // 当前谁在提供服务

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();     // 获得还活着的服务
            List<Server> allList = lb.getAllServers();          // 获得全部的服务

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }

            if (total++ >= 5) {             // 每个服务轮询五次
                total = 0;
                index = (index + 1) % serverCount;
            }
            server = upList.get(index);

            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            server = null;
            Thread.yield();
        }

        return server;

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub

    }
}
