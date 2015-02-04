// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.Map;
import java.util.Set;

/**
 * @author zhoyilei
 *
 */
public interface LogpieDataUpdate<T>
{
    public String getUpdateTable();

    /**
     * Key-Value pair will transformed to SQL syntax (UPDATE
     * <table>
     * SET key1=value1,key2=value2 WHERE <condition>)
     * 
     * @return
     */
    public Map<String, Object> getUpdateValues();

    public Set<String> getUpdateConditions();
}
