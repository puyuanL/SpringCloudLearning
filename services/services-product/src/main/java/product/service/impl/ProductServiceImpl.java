package product.service.impl;

import org.springframework.stereotype.Service;
import product.bean.Product;
import product.service.ProductService;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("99"));
        product.setName("Apple" + productId);
        product.setNum(2);

//        try {
//            TimeUnit.SECONDS.sleep(100);    // read timeout
//        }
//        catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return product;
    }

}
