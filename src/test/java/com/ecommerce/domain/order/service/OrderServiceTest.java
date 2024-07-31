package com.ecommerce.domain.order.service;

import com.ecommerce.domain.order.Order;
import com.ecommerce.domain.product.Product;
import com.ecommerce.domain.product.service.ProductService;
import com.ecommerce.domain.user.User;
import com.ecommerce.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    private static final Long ORDER_ID = 1L;


    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    private Order testOrder;
    private User testUser;
    private Product testProduct;
    @BeforeEach
    void setup() {
        User user = new User("testUser", BigDecimal.valueOf(1000));
        Product product = new Product( "test", BigDecimal.TWO, 1000);
        testUser = userService.saveUser(user);
        testProduct = productService.saveAndGet(product);
        Map<Product,Integer> orderItem = Map.of(testProduct, 1);
        Order order = new Order(testUser, orderItem);
        testOrder = orderService.saveAndGet(order);
    }

    @Test
    @DisplayName("주문 ID로 주문을 조회한다")
    void getOrder_ShouldReturnOrder_WhenOrderExists() {

        Order result = orderService.getOrder(testOrder.getId());

        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
    }

    @Test
    @DisplayName("주문 검색 조건으로 주문 목록을 조회한다")
    void getOrders_ShouldReturnOrderList_WhenSearchConditionProvided() {
        OrderCommand.Search searchCommand = new OrderCommand.Search(testOrder.getId());

        List<Order> result = orderService.getOrders(searchCommand);

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("새로운 주문을 생성한다")
    void createOrder_ShouldCreateNewOrder_WhenValidCommandProvided() {
        OrderCommand.Create createCommand = new OrderCommand.Create(testUser.getId(), Map.of(testProduct.getId(), 1));

        Order result = orderService.createOrder(createCommand);

        assertNotNull(result);
        assertEquals(1, result.getOrderItems().size());
    }



}