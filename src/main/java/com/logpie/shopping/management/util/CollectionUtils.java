// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.util;

import java.util.Collection;

/**
 * @author zhoyilei
 *
 */

public final class CollectionUtils
{
    private CollectionUtils()
    {

    }

    public static boolean isEmpty(final Collection<?> c)
    {
        return c == null || c.isEmpty();
    }
}
