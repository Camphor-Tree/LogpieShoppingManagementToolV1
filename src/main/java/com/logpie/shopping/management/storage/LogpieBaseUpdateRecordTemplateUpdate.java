// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.logpie.shopping.management.model.LogpieModel;

/**
 * @author zhoyilei
 *
 */
public class LogpieBaseUpdateRecordTemplateUpdate<T> implements LogpieDataUpdate<T>
{
    private String mTableName;
    private LogpieModel mModel;
    private String mIdKey;
    private String mIdValue;

    public LogpieBaseUpdateRecordTemplateUpdate(final LogpieModel model, final String tableName,
            final String idKey, final String idValue)
    {
        mModel = model;
        mTableName = tableName;
        mIdKey = idKey;
        mIdValue = idValue;
    }

    @Override
    public String getUpdateTable()
    {
        return mTableName;
    }

    @Override
    public Map<String, Object> getUpdateValues()
    {

        LogpieModel model = (LogpieModel) mModel;
        return model.getModelMap();
    }

    @Override
    public Set<String> getUpdateConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions.add(String.format("%s=%s", mIdKey, mIdValue));
        return conditions;
    }

}
