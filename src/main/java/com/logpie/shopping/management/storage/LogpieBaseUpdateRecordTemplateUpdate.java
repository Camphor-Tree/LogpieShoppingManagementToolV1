// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

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

    public LogpieBaseUpdateRecordTemplateUpdate(final LogpieModel model, final String tableName)
    {
        mModel = model;
        mTableName = tableName;
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
        return null;
    }

}
