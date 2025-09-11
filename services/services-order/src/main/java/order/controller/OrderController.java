package order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import order.bean.Order;
import order.properties.OrderProperties;
import order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// 表示所有请求均以 “/api/order” 开始
@RequestMapping("/api/order")
@Slf4j
//@RefreshScope       // refresh automatically
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @Value("${order.timeout}")
//    String orderTimeout;
//    @Value("${order.auto-confirm}")
//    String autoConfirm;

    @Autowired
    OrderProperties orderProperties;

    @GetMapping("/config")
    public String config() {
        return  "order.timeout = " + orderProperties.getTimeout() + "; " +
                "order.auto-confirm = " + orderProperties.getAutoConfirm() + "; " +
                "order.dbUrl = " + orderProperties.getDbUrl();
    }

    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId) {
        return orderService.createOrder(userId, productId);
    }

    // 流控模式（链路）--秒杀订单模式
    // 通过设置流控规则，实现某一个路径下的流量限制
    @GetMapping("/seckill")
    @SentinelResource(value = "seckill-order", fallback = "seckillFallback")
    public Order seckill(@RequestParam(value = "userId", required = false) Long userId,
                         @RequestParam(value = "productId", defaultValue = "1000") Long productId) {
        Order order = orderService.createOrder(productId, userId);
        order.setId(Long.MAX_VALUE);

        return order;
    }

    public Order seckillFallback(Long userId, Long productId, BlockException exception) {
        Order order = new Order();
        order.setId(productId);
        order.setUserId(userId);
        order.setAddress("异常信息：" + exception.getClass());

        return order;
    }


    /**
     * readDB 关联 writeDB
     *      readDB可以单独大量访问
     *      writeDB访问量极大时，readDB会被流控
     */
    @GetMapping("writeDB")
    public String writeDB() {
        return "writeDB success!";
    }

    @GetMapping("readDB")
    public String readDB() {
        log.info("readDB success!");
        return "readDB success!";
    }

}
