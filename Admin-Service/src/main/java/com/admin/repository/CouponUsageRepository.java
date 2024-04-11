package com.admin.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin.entity.Coupon;
import com.admin.entity.CouponUsage;



@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
	
    boolean existsByUserIdAndCoupon(Long userId, Coupon coupon);
    
 

	void deleteByUserIdAndCoupon(Long userId, Coupon coupon);
}
