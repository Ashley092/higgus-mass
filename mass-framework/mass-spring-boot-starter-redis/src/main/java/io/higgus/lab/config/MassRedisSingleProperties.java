package io.higgus.lab.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

//@ConfigurationProperties(prefix = "mass.redis.cluster")
@ConfigurationProperties(prefix = "mass.redis.single")
public class MassRedisSingleProperties {

    private boolean enabled = false;

//    private String masterName = "mymaster";

//    private List<String> sentinelAddresses = new ArrayList<>(List.of(
//            "redis://106.54.232.126:26379",
//            "redis://106.54.232.126:26380",
//            "redis://106.54.232.126:26381"
//    ));
    private String address = "redis://127.0.0.1:6380";

    private int database = 6;

    private int masterConnectionPoolSize = 64;

    private int slaveConnectionPoolSize = 64;

    private int connectTimeout = 3000;

    private int timeout = 3000;

    private int retryAttempts = 3;

    private int retryInterval = 1500;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDatabase() {
        return database;
    }

    public String getAddress() { return address; }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getMasterConnectionPoolSize() {
        return masterConnectionPoolSize;
    }

    public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
        this.masterConnectionPoolSize = masterConnectionPoolSize;
    }

    public int getSlaveConnectionPoolSize() {
        return slaveConnectionPoolSize;
    }

    public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }
}