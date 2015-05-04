// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public class Coupon implements RowMapper<Coupon>, LogpieModel
{
    public static final String DB_KEY_COUPON_ID = "CouponId";
    public static final String DB_KEY_COUPON_CODE = "CouponCode";

    private String mCouponId;
    private String mCouponCode;

    /**
     * @param couponId
     * @param couponCode
     */
    public Coupon(String couponId, String couponCode)
    {
        super();
        mCouponId = couponId;
        mCouponCode = couponCode;
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Coupon.DB_KEY_COUPON_CODE, mCouponCode);
        return modelMap;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_COUPON_ID;
    }

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof Coupon)
        {
            final Coupon compareToCoupon = (Coupon) object;
            if (compareToCoupon.mCouponId.equals(mCouponId)
                    && compareToCoupon.mCouponCode.equals(mCouponCode))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return getCouponByResultSet(rs, rowNum);
    }

    public static final Coupon getCouponByResultSet(ResultSet rs, int rowNum) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String couponId = String.valueOf(rs.getInt(DB_KEY_COUPON_ID));
        final String couponCode = rs.getString(DB_KEY_COUPON_CODE);
        return new Coupon(couponId, couponCode);
    }

    /**
     * @return the couponId
     */
    public String getCouponId()
    {
        return mCouponId;
    }

    /**
     * @param couponId
     *            the couponId to set
     */
    public void setCouponId(String couponId)
    {
        mCouponId = couponId;
    }

    /**
     * @return the couponCode
     */
    public String getCouponCode()
    {
        return mCouponCode;
    }

    /**
     * @param couponCode
     *            the couponCode to set
     */
    public void setCouponCode(String couponCode)
    {
        mCouponCode = couponCode;
    }

}
