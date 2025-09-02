package order.service.Impl;

import order.bean.Order;
import order.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Order createOrder(Long productId, Long userId) {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(userId);
        order.setAddress("NPU");
        // Todo: product list
        order.setProductList(null);
        // Todo: total amount
        order.setTotalAmount(new BigDecimal("999"));

        return order;
    }

}
