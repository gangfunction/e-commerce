package com.ecommerce.api.order.service;

import com.ecommerce.api.order.service.repository.OrderRepository;
import com.ecommerce.api.order.service.repository.UserRepository;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public Order getOrder(Long customerId) {
        return orderRepository.getById(customerId).orElseThrow(
                () -> new RuntimeException("주문이 존재하지 않습니다.")
        );
    }

    public List<Order> getOrders(OrderCommand.Search search){
        return orderRepository.getOrders(search);
    }

    public Order createOrder(OrderCommand.Create command) {
        User user = userRepository.getById(command.id()).orElseThrow(
                ()-> new RuntimeException("사용자가 존재하지 않습니다.")
        );
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(command.items());
        return orderRepository.saveAndGet(order).orElseThrow(
                () -> new RuntimeException("주문 생성에 실패하였습니다.")
        );
    }
}
