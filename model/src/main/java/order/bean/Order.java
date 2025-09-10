package order.bean;

import lombok.Data;
import product.bean.Product;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String nickName;
    private String address;
    private List<Product> productList;
}
