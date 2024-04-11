package com.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.entity.Coupon;
import com.admin.repository.CouponUsageRepository;


@Service
public class CouponUsageService {
	
	@Autowired
	   CouponUsageRepository couponUsageRepository;

	 @Transactional
	    public void deleteCouponUsage(Long userId, Coupon coupon) {
	        couponUsageRepository.deleteByUserIdAndCoupon(userId, coupon);
	    }

}
