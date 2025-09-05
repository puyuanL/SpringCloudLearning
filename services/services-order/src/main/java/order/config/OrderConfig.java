package order.config;

import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import feign.Logger;

@Configuration
public class OrderConfig {

//    // Retry
//    @Bean
//    Retryer retryer() {
//        return new Retryer.Default();
//    }

    // Logger
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @LoadBalanced   // load balance with annotation
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
