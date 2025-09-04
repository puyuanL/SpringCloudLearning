package order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OrderMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }

    // 1. 项目启动就监听配置文件变化
    // 2. 发生变化后拿到变化值
    // 3. 发送邮件

    @Bean
    ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager) {
        return args -> {
            ConfigService configService = nacosConfigManager.getConfigService();
            configService.addListener("service-order.properties",
                    "DEFAULT_GROUP",
                    new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return Executors.newFixedThreadPool(1);
                        }

                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            System.out.println("Changed Configuration Info: " + configInfo);
                            System.out.println("Emailing...");
                        }
                    });
            System.out.println("====== Add Listener Success ======");
        };
    }
}
