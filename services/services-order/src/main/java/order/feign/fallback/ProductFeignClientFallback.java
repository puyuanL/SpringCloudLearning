package order.feign.fallback;

import order.feign.ProductFeignClient;
import org.springframework.stereotype.Component;
import product.bean.Product;

import java.math.BigDecimal;

@Component
public class ProductFeignClientFallback implements ProductFeignClient {

    @Override
    public Product getProductById(Long id) {
        System.out.println("Fallback (兜底回调)");
        Product product = new Product();
        product.setId(id);
        product.setPrice(new BigDecimal("0"));
        product.setName("Unknow Product");
        product.setNum(0);

        return product;
    }
}
