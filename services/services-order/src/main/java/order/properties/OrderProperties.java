package order.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "order")  // 配置批量绑定在nacos下，无需@RefreshScope
@Data
public class OrderProperties {
    String Timeout;
    String AutoConfirm;
    String dbUrl;
}
