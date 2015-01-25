// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

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

    public Set<String> getQueryTables();
}
