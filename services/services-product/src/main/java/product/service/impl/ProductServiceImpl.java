package product.service.impl;

import org.springframework.stereotype.Service;
import product.bean.Product;
import product.service.ProductService;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("99"));
        product.setName("Apple" + productId);
        product.setNum(2);

        System.out.println("Return product by this client");

        return product;
    }

}
