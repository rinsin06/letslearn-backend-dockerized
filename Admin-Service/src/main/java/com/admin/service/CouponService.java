package com.admin.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Coupon;
import com.admin.entity.CouponUsage;
import com.admin.repository.CouponRepository;
import com.admin.repository.CouponUsageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUsageRepository couponUsageRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public void saveCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    public Coupon getCouponByCode(String couponCode) {
        return couponRepository.findByCode(couponCode);
    }

    public boolean isCouponAlreadyUsedByUser(Long userId, Coupon coupon) {
        return couponUsageRepository.existsByUserIdAndCoupon(userId, coupon);
    }

    public void applyCouponToUser(Long userId, Coupon coupon) {
        if (isCouponAlreadyUsedByUser(userId, coupon)) {
//            throw new CouponAlreadyUsedException("This coupon has already been used by the user.");
        }
        CouponUsage couponUsage = new CouponUsage();
        couponUsage.setUserId(userId);
        couponUsage.setCoupon(coupon);
        couponUsageRepository.save(couponUsage);
    }
    
    public void removeCouponToUser(Long userId, Coupon coupon) {
        if (isCouponAlreadyUsedByUser(userId, coupon)) {
//            throw new CouponAlreadyUsedException("This coupon has already been used by the user.");
        }
        CouponUsage couponUsage = new CouponUsage();
        couponUsage.setUserId(userId);
        couponUsage.setCoupon(coupon);
        couponUsageRepository.save(couponUsage);
    }

}

