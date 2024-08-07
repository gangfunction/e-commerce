package com.ecommerce.infra.coupon;

import com.ecommerce.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CouponJPARepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT c.quantity FROM Coupon c WHERE c.id = :couponId")
    Integer getRemainingQuantity(Long couponId);
}