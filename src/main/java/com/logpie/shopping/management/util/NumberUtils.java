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

    public static boolean floatEquals(final float numberA, final float numberB)
    {
        if (numberA - numberB <= 0.01f && numberA - numberB >= -0.01f)
        {
            return true;
        }
        return false;
    }
    
    public static boolean floatLessThan(final float numberA, final float numberB)
    {
    	if (numberA - numberB <= 0.01f)
    	{
    		return true;
    	}
    	return false;
    }

}
