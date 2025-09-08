package order.service.Impl;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import order.bean.Order;
import order.feign.ProductFeignClient;
import order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import product.bean.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFeignClient productFeignClient;

    // 将异常传递给createOrderFallback进行兜底回调，最终交给SpringBoot全局异常处理器处理
    @SentinelResource(value = "createOrder", blockHandler = "createOrderFallback")
    @Override
    public Order createOrder(Long productId, Long userId) {
        /**
         * 若干种获取 Product 的方式
         */
//        Product product = getProductFromRemote(productId);
//        Product product = getProductFromRemoteLoadBalanceWithCode(productId);
//        Product product = getProductFromRemoteLoadBalanceWithAnno(productId);
        Product product = productFeignClient.getProductById(productId);

        Order order = new Order();
        order.setId(userId);
        order.setAddress("NPU");
        // total amount
        product.getPrice().multiply(new BigDecimal(product.getNum()));
        order.setTotalAmount(new BigDecimal("999"));
        // product list
        order.setProductList(Arrays.asList(product));

//        try {
//            SphU.entry("hahaha");
//            order.setAddress("NPU");
//            // total amount
//            product.getPrice().multiply(new BigDecimal(product.getNum()));
//            order.setTotalAmount(new BigDecimal("999"));
//            // product list
//            order.setProductList(Arrays.asList(product));
//        } catch (BlockException e) {
//            // 编码处理
//            throw new RuntimeException(e);
//        }

        return order;
    }

    // Fallback 兜底回调
    public Order createOrderFallback(Long productId, Long userId, BlockException e) {
         Order order = new Order();
         order.setId(userId);
         order.setAddress("Error Message: " + e.getClass());
         order.setNickName("unknow nick name");
         order.setTotalAmount(new BigDecimal("0"));

         return order;
    }

    // Way1
    private Product getProductFromRemote(Long productId) {
        // get all ip & port of product service
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");

        ServiceInstance instance = instances.get(0);
        // make remote url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/product/" + productId;
        log.info("remote req: {}", url);
        // send req remote
        Product product = restTemplate.getForObject(url, Product.class) ;

        return product;
    }

    // Way2: Load balance with Code way
    private Product getProductFromRemoteLoadBalanceWithCode(Long productId) {
        // get all ip & port of product service (Load balance)
        ServiceInstance instance = loadBalancerClient.choose("service-product");
        // make remote url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/product/" + productId;
        log.info("remote req: {}", url);
        // send req remote
        Product product = restTemplate.getForObject(url, Product.class) ;

        return product;
    }

    // Way3: Load balance with Annotation
    private Product getProductFromRemoteLoadBalanceWithAnno(Long productId) {
        // add "@LoadBalanced" in "OrderConfig"
        // make remote url: {service-product} will be replaced automatically
        String url = "http://service-product/product/" + productId;
        log.info("remote req: {}", url);
        // send req remote
        Product product = restTemplate.getForObject(url, Product.class) ;

        return product;
    }
}
