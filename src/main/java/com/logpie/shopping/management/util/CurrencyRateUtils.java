// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.util;

import org.json.JSONObject;

/**
 * @author zhoyilei
 *
 */
public class CurrencyRateUtils
{
    private static final String sFreeCurrentConverterApiUrl = "http://www.freecurrencyconverterapi.com/api/v3/convert?q=%s_%s&compact=y";
    private static final String sChineseYuan = "CNY";
    private static final String sUSDollar = "USD";
    private static final String sValue = "val";

    public static Float getCurrencyRate(final String fromCurrency, final String toCurrency)
    {
        final String queryUrl = String
                .format(sFreeCurrentConverterApiUrl, fromCurrency, toCurrency);
        final String response = SimpleAPIConnection.doGetQuery(queryUrl);
        if (response != null)
        {
            try
            {
                final JSONObject responseJSON = new JSONObject(response);
                final JSONObject rateJSON = responseJSON.getJSONObject(fromCurrency + "_"
                        + toCurrency);
                final Double rate = rateJSON.getDouble(sValue);
                return NumberUtils.keepTwoDigitsDecimalForFloat(rate.floatValue());
            } catch (Exception e)
            {

            }
        }
        return 1.0f;
    }

    public static Float getUScurrencyRate()
    {
        return getCurrencyRate(sUSDollar, sChineseYuan);
    }
}
