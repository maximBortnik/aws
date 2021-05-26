package com.epam.attachment.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration(proxyBeanMethods = false)
public class LoadBalancerConfiguration {

    @Autowired(required = false)
    private AWSCredentialsProvider awsCredentialsProvider;

    @Autowired(required = false)
    private RegionProvider regionProvider;

    @Lazy
    @Bean(destroyMethod = "shutdown")
    public AmazonElasticLoadBalancing amazonLoadBalancer() throws Exception {
        AmazonWebserviceClientFactoryBean<AmazonElasticLoadBalancingAsyncClient> clientFactoryBean =
                new AmazonWebserviceClientFactoryBean<>(AmazonElasticLoadBalancingAsyncClient.class,
                        this.awsCredentialsProvider, this.regionProvider);
        clientFactoryBean.afterPropertiesSet();
        return clientFactoryBean.getObject();
    }
}
