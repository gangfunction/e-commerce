package com.ecommerce.api.controller.usecase;

import com.ecommerce.domain.order.Order;
import com.ecommerce.domain.order.service.OrderCommand;
import com.ecommerce.domain.order.service.OrderService;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class PopularProductUseCaseTest {
    @Autowired
    private PopularProductUseCase popularProductUseCase;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentUseCase paymentUseCase;


    @BeforeEach
    void setUp() {
        int userCount = 1000;
        int productCount = 1000;
        int orderCount = 10000;

        createUsers(userCount);
        createProducts(productCount);
        createOrders(orderCount, userCount, productCount);
    }

    private void createUsers(int count) {
        System.out.println("createUsers");
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(new User("user" + i, BigDecimal.valueOf(100000000)));
        }
        userService.saveAll(users);
        System.out.println("createUsers end");
    }

    private void createProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(new Product("product" + i, BigDecimal.valueOf(10 + i), 1000000000));
        }
        productService.saveAll(products);
    }

    private void createOrders(int orderCount, int userCount, int productCount) {
        List<User> users = userService.getAllUsers();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < orderCount; i++) {
            User user = users.get(random.nextInt(userCount));
            Map<Long, Integer> orderItems = new HashMap<>();
            for (int j = 0; j < random.nextInt(productCount)+1; j++) {
                orderItems.put((long)random.nextInt(productCount) + 1, random.nextInt(10)+1);
            }
            OrderCommand.Create command = new OrderCommand.Create(user.getId(), orderItems);
            Order order = orderService.createOrder(command);
            OrderCommand.Payment payment = new OrderCommand.Payment(user.getId(),order.getId());
            futures.add(CompletableFuture.runAsync(() -> paymentUseCase.payOrder(payment)));
//            paymentUseCase.payOrder(payment);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();


    }

    @Test
    @DisplayName("100만건에대한 테스트")
    void getPopularProducts() {
        // given
        // when

        List<Product> popularProducts = popularProductUseCase.getPopularProducts();
        // then
        for(Product product : popularProducts){
            System.out.println(product.getName()+ ":" + "재고수량" + product.getStock());
        }
        assertThat(popularProducts).isNotEmpty();
        assertThat(popularProducts.size()).isEqualTo(5);


    }


}