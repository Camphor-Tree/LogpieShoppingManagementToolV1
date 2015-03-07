// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.util;

/**
 * @author zhoyilei
 *
 */
public class NumberUtils
{
    public static Float keepTwoDigitsDecimalForFloat(final Float rowNumer)
    {
        return Float.valueOf(String.format("%.2f", rowNumer));
    }

}
