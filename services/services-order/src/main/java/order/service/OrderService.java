package order.service;

import order.bean.Order;

public interface OrderService {
    Order createOrder(Long productId, Long userId);
}
