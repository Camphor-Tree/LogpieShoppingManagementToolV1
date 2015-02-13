// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * This factory is used to produce DataSource instance
 * 
 * @author zhoyilei
 *
 */
public class LogpieDataSourceFactory
{
    public static SimpleDriverDataSource getDataSource()
    {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        // dataSource.setUrl("jdbc:mysql://localhost:8889/Logpie");
        // remote server use port 3306. Amazon Linux
        dataSource.setUrl(
                "jdbc:mysql://localhost:3306/Logpie?useUnicode=yes&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    public static JdbcTemplate getJdbcTemplate()
    {
        return new JdbcTemplate(getDataSource());
    }
}
