// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.util.Map;

/**
 * @author zhoyilei
 *
 */
public interface LogpieModel
{
    public Map<String, Object> getModelMap();

    public String getPrimaryKey();

    public boolean compareTo(Object object);
}
