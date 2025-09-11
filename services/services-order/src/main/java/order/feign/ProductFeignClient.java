package order.feign;

import order.feign.fallback.ProductFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import product.bean.Product;


// 标名 feign 客户端
@FeignClient(value = "service-product", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {

    /**
     * mvc注解的使用逻辑
     *  1. 标注在 Controller 上，表示接受这样的请求
     *  2. 标注在 FeignClient 上，表示发送这样的请求
     */
    //
    @GetMapping("/api/product/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
