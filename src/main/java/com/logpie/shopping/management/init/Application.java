// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.init;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This is just for temporary usage. 
 * TODO: we need remove the application
 * 
 * @author zhoyilei
 *
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }

}
