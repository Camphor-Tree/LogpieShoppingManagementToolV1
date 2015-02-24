// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.accounting.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author zhoyilei
 *
 */
public class GoogleChartHelper
{
    public static List<KeyValue> getPieDataListFromMap(Map<String, Integer> dataMap)
    {
        final ArrayList<KeyValue> pieDataList = new ArrayList<KeyValue>();
        for (final Entry<String, Integer> dataEntry : dataMap.entrySet())
        {
            pieDataList.add(new KeyValue(dataEntry.getKey(), String.valueOf(dataEntry.getValue())));
        }
        return pieDataList;
    }

    public static class KeyValue
    {
        String key;
        String value;

        public KeyValue(String key, String value)
        {
            super();
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }

}
