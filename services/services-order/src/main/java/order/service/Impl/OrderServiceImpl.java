package order.service.Impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
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

    @SentinelResource(value = "createOrder")
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

        order.setId(1L);
        order.setUserId(userId);
        order.setAddress("NPU");
        // total amount
        product.getPrice().multiply(new BigDecimal(product.getNum()));
        order.setTotalAmount(new BigDecimal("999"));
        // product list
        order.setProductList(Arrays.asList(product));

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
