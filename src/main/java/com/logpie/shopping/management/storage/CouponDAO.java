// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Coupon;

/**
 * @author zhoyilei
 *
 */
public class CouponDAO extends LogpieBaseDAO<Coupon>
{

    /**
     * @param admin
     */
    public CouponDAO(Admin admin)
    {
        super(admin);
    }

    private static final Logger LOG = Logger.getLogger(CouponDAO.class);
    public static final String sCouponTableName = "Coupons";

    /**
     * For recording a used coupon into the database
     * 
     * @param coupon
     * @return true if adding coupon successfully. false if adding coupon fails
     */
    public boolean addCoupon(final Coupon coupon)
    {
        final LogpieDataInsert<Coupon> addCouponInsert = new AddCouponInsert(coupon);
        return super.insertData(addCouponInsert);
    }

    public boolean isCouponAlreadyUsed(final Coupon coupon)
    {
        final CheckCouponExistQuery checkCouponExistQuery = new CheckCouponExistQuery(coupon);
        final List<Coupon> queryResult = super.queryResult(checkCouponExistQuery);
        if (queryResult == null || queryResult.isEmpty())
        {
            return false;
        }
        return true;
    }

    private class CheckCouponExistQuery implements LogpieDataQuery<Coupon>
    {
        private Coupon mCoupon;

        CheckCouponExistQuery(Coupon coupon)
        {
            mCoupon = coupon;
        }

        @Override
        public RowMapper<Coupon> getQueryResultMapper()
        {
            return mCoupon;
        }

        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> queryConditions = new HashSet<String>();
            queryConditions.add(Coupon.DB_KEY_COUPON_CODE + "=\"" + mCoupon.getCouponCode() + "\"");
            return queryConditions;
        }

        @Override
        public String getMainQueryTable()
        {
            return CouponDAO.sCouponTableName;
        }

        @Override
        public Map<String, String> getJoinTables()
        {
            return null;
        }

        @Override
        public Set<String> getLeftJoinCondition()
        {
            return null;
        }

        @Override
        public Set<String> getOrderBy()
        {
            return null;
        }
    }

    private class AddCouponInsert implements LogpieDataInsert<Coupon>
    {
        private Coupon mCoupon;

        AddCouponInsert(final Coupon coupon)
        {
            mCoupon = coupon;
        }

        @Override
        public String getInsertTable()
        {
            return sCouponTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mCoupon.getModelMap();
        }
    }

}
