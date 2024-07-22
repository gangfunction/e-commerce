package com.ecommerce.domain.user.service.repository;

import com.ecommerce.domain.coupon.Coupon;
import com.ecommerce.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(Long id);

    Optional<User> save(User testUser);

    Optional<Coupon> getCouponByUser(long userId, long couponId);

    List<Coupon> getAllCouponsByUserId(Long userId);

    Optional<User> getUserByCoupon(Coupon userCoupon);

    void deleteAll();

    boolean hasCoupon(Long aLong, Long aLong1);

    void saveAll(List<User> users);


    Optional<User> getUserWithCoupon(Long userId);
}
