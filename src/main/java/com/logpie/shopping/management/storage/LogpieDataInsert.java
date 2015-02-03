// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.Map;

/**
 * @author zhoyilei
 *
 */
public interface LogpieDataInsert<T>
{
    public String getInsertTable();

    /**
     * Key-Value pair will transformed to SQL syntax (INSERT INTO table_name
     * (key1,key2,key3,...) VALUES (value1,value2,value3,...);)
     * 
     * @return
     */
    public Map<String, Object> getInsertValues();
}
