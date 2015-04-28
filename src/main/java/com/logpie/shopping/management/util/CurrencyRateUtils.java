// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.util;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zhoyilei
 *
 */
public class CurrencyRateUtils
{
    private static final Logger LOG = Logger.getLogger(CurrencyRateUtils.class);
    private static final String sFreeCurrentConverterApiUrl = "http://www.freecurrencyconverterapi.com/api/v3/convert?q=%s_%s&compact=y";
    private static final String sChineseYuan = "CNY";
    private static final String sUSDollar = "USD";
    private static final String sValue = "val";

    private static Float sCurrencyRate = 6.2f;

    public synchronized void refreshCurrencyRate(final String fromCurrency, final String toCurrency)
    {
        LOG.debug("Refreshing currency Rate");
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
                sCurrencyRate = NumberUtils.keepTwoDigitsDecimalForFloat(rate.floatValue());
            } catch (Exception e)
            {
                LOG.error("Error happened when trying to refresh to currency rate", e);
            }
        }
    }

    public static Float getUScurrencyRate()
    {
        return sCurrencyRate;
    }

    // Execute every 1 hour
    @Scheduled(fixedRate = 1000 * 3600)
    public synchronized void refreshUScurrencyRate()
    {
        refreshCurrencyRate(sUSDollar, sChineseYuan);
    }
}
