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
    // We currently use currencylayer.com
    private static final String sFreeCurrentConverterApiUrl = "http://apilayer.net/api/live?access_key=46645c90759497801e3c821b14859cc1&source=%s&currencies=%s&format=1";
    private static final String sChineseYuan = "CNY";
    private static final String sUSDollar = "USD";

    private static Float sCurrencyRate = 6.2f;

    public synchronized void refreshCurrencyRate(final String fromCurrency, final String toCurrency)
    {
        LOG.debug("Refreshing currency Rate");
        final String queryUrl = String.format(sFreeCurrentConverterApiUrl, fromCurrency,
                toCurrency);
        final String response = SimpleAPIConnection.doGetQuery(queryUrl);
        if (response != null)
        {
            try
            {
                /**
                 * { "success":true,
                 * "terms":"https:\/\/currencylayer.com\/terms",
                 * "privacy":"https:\/\/currencylayer.com\/privacy",
                 * "timestamp":1437113654, "source":"USD", "quotes":{
                 * "USDCNY":6.20785 } }
                 */
                final JSONObject responseJSON = new JSONObject(response);
                final JSONObject rateJSON = responseJSON.getJSONObject("quotes");
                final Double rate = rateJSON.getDouble(fromCurrency + toCurrency);
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

    // Execute every 2 hour
    @Scheduled(fixedRate = 1000 * 3600 * 2)
    public synchronized void refreshUScurrencyRate()
    {
        refreshCurrencyRate(sUSDollar, sChineseYuan);
    }
}
