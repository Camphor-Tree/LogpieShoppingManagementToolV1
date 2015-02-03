// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public interface LogpieDataQuery<T>
{
    public RowMapper<T> getQueryResultMapper();

    public Set<String> getQueryConditions();

    // table name => table alias (used in multiple foreign keys connections)
    public Map<String, String> getQueryTables();
}
