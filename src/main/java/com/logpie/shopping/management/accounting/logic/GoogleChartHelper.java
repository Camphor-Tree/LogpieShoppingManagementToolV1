// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.accounting.logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author zhoyilei
 *
 */
public class GoogleChartHelper
{
    public static List<KeyValue> getPieDataListFromMap(final Map<String, ?> dataMap)
    {
        final ArrayList<KeyValue> pieDataList = new ArrayList<KeyValue>();
        for (final Entry<String, ?> dataEntry : dataMap.entrySet())
        {
            if (dataEntry.getValue() instanceof Double)
            {
                // Format the double to show two digit decimal
                pieDataList.add(new KeyValue(dataEntry.getKey(),
                        String.format("%.2f", dataEntry.getValue())));
            }
            else
            {
                pieDataList.add(
                        new KeyValue(dataEntry.getKey(), String.valueOf(dataEntry.getValue())));
            }
        }
        return pieDataList;
    }

    public static List<KeyValue> getLineChartDataListFromStringIntegerMap(
            final Map<String, Integer> dataMap)
    {
        final MonthDayValueComparator bvc = new MonthDayValueComparator(dataMap);
        final TreeMap<String, Integer> sortedDataMap = new TreeMap<String, Integer>(bvc);
        if (dataMap != null)
        {
            sortedDataMap.putAll(dataMap);
        }
        final ArrayList<KeyValue> pieDataList = new ArrayList<KeyValue>();
        for (final Entry<String, Integer> dataEntry : sortedDataMap.entrySet())
        {
            pieDataList.add(new KeyValue(dataEntry.getKey(), String.valueOf(dataEntry.getValue())));
        }
        return pieDataList;
    }

    public static List<KeyValue> getLineChartDataListFromStringDoubleMap(
            final Map<String, Double> dataMap, final boolean isMonthlyFormat)
    {
        Comparator<String> comparator;
        if (isMonthlyFormat)
        {
            comparator = new YearMonthValueComparator(dataMap);
        }
        else
        {
            comparator = new MonthDayValueComparator(dataMap);
        }
        final TreeMap<String, Double> sortedDataMap = new TreeMap<String, Double>(comparator);
        if (dataMap != null)
        {
            sortedDataMap.putAll(dataMap);
        }
        final ArrayList<KeyValue> pieDataList = new ArrayList<KeyValue>();
        for (final Entry<String, Double> dataEntry : sortedDataMap.entrySet())
        {
            pieDataList.add(
                    new KeyValue(dataEntry.getKey(), String.format("%.2f", dataEntry.getValue())));
        }
        return pieDataList;
    }

    public static List<KeyValues> getLineChartDataListFromStringDoubleMaps(
            final boolean isMonthlyFormat, final Map<String, Double>... dataMap)
    {
        Comparator<String> comparator;
        if (isMonthlyFormat)
        {
            comparator = new YearMonthValueComparator(dataMap[0]);
        }
        else
        {
            comparator = new MonthDayValueComparator(dataMap[0]);
        }

        final ArrayList<KeyValues> lineDataList = new ArrayList<KeyValues>();

        final TreeMap<String, Double> sortedDataMap = new TreeMap<String, Double>(comparator);
        if (dataMap != null)
        {
            sortedDataMap.putAll(dataMap[0]);
        }

        for (final Entry<String, Double> dataEntry : sortedDataMap.entrySet())
        {
            final Double[] values = new Double[dataMap.length];
            final String key = dataEntry.getKey();
            for (int i = 0; i < dataMap.length; i++)
            {
                values[i] = dataMap[i].get(key);
            }
            lineDataList.add(new KeyValues(dataEntry.getKey(), values));
        }

        return lineDataList;
    }

    /**
     * Help sort the map by key (based on time).
     * 
     * @author zhoyilei
     *
     */
    public static class YearMonthValueComparator implements Comparator<String>
    {

        Map<String, ?> base;

        public YearMonthValueComparator(final Map<String, ?> base)
        {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        @Override
        public int compare(String a, String b)
        {
            final DateFormat formatter = new SimpleDateFormat("yyyy-MM");
            try
            {
                Date dateA = formatter.parse(a);
                Date dateB = formatter.parse(b);
                if (dateA.before(dateB))
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            } catch (ParseException e)
            {
                // returning 0 would merge keys
                return -1;
            }
        }
    }

    /**
     * Help sort the map by key (based on time).
     * 
     * @author zhoyilei
     *
     */
    public static class MonthDayValueComparator implements Comparator<String>
    {

        Map<String, ?> base;

        public MonthDayValueComparator(Map<String, ?> base)
        {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        @Override
        public int compare(String a, String b)
        {
            final DateFormat formatter = new SimpleDateFormat("MM-dd");
            try
            {
                Date dateA = formatter.parse(a);
                Date dateB = formatter.parse(b);
                if (dateA.before(dateB))
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            } catch (ParseException e)
            {
                // returning 0 would merge keys
                return -1;
            }
        }
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

    public static class KeyValues
    {
        String key;
        String value1;
        String value2;
        String value3;

        public KeyValues(String key, String value1, String value2, String value3)
        {
            super();
            this.key = key;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        public KeyValues(String key, Double[] values)
        {
            super();
            this.key = key;
            this.value1 = String.valueOf(values[0]);
            this.value2 = String.valueOf(values[1]);
            this.value3 = String.valueOf(values[2]);
        }

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue1()
        {
            return this.value1;
        }

        public void setValue1(String value)
        {
            this.value1 = value;
        }

        public String getValue2()
        {
            return this.value2;
        }

        public void setValue2(String value)
        {
            this.value2 = value;
        }

        public String getValue3()
        {
            return this.value3;
        }

        public void setValue3(String value)
        {
            this.value3 = value;
        }
    }

}
