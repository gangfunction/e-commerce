package com.ecommerce.api.controller.domain.user.dto;

import com.ecommerce.domain.coupon.Coupon;
import com.ecommerce.domain.coupon.service.CouponCommand;
import com.ecommerce.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserMapper {
    public static UserDto.UserResponse toUserResponse(User user) {
        return new UserDto.UserResponse(user.getUsername(), user.getBalance(), user.getCoupons());
    }
    public static UserDto.UserResponse toUserAsyncResponse(CompletableFuture<User> userFuture) {
        User user = userFuture.join();
        return new UserDto.UserResponse(user.getUsername(), user.getBalance(), user.getCoupons());
    }
    public static List<UserDto.CouponResponse> toUserCouponResponseList(List<Coupon> userCoupons) {
        List<UserDto.CouponResponse> convertedResponse = new ArrayList<>();
        for(Coupon coupon : userCoupons){
            convertedResponse.add(new UserDto.CouponResponse(coupon.getCode(), coupon.getDiscountAmount(), coupon.getValidFrom(), coupon.getValidTo(),coupon.isValid()));
        }
        return convertedResponse;
    }

    public static UserDto.IssueStatusResponse toIssueStatusResponse(CouponCommand.Issue user) {
        return new UserDto.IssueStatusResponse(user.userId(), user.couponId(), user.status());
    }
}