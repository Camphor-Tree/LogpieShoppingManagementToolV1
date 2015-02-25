// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.accounting.logic;


/**
 * @author zhoyilei
 *
 */
public class LogpiePieChart
{
    private String mChartLabel;
    private String mChartKeyLabel;
    private String mChartValueLabel;

    /**
     * @param chartLabel
     * @param chartKeyLabel
     * @param chartValueLabel
     */
    public LogpiePieChart(String chartLabel, String chartKeyLabel, String chartValueLabel)
    {
        super();
        mChartLabel = chartLabel;
        mChartKeyLabel = chartKeyLabel;
        mChartValueLabel = chartValueLabel;
    }

    /**
     * @return the chartLabel
     */
    public String getChartLabel()
    {
        return mChartLabel;
    }

    /**
     * @param chartLabel
     *            the chartLabel to set
     */
    public void setChartLabel(String chartLabel)
    {
        mChartLabel = chartLabel;
    }

    /**
     * @return the chartKeyLabel
     */
    public String getChartKeyLabel()
    {
        return mChartKeyLabel;
    }

    /**
     * @param chartKeyLabel
     *            the chartKeyLabel to set
     */
    public void setChartKeyLabel(String chartKeyLabel)
    {
        mChartKeyLabel = chartKeyLabel;
    }

    /**
     * @return the chartValueLabel
     */
    public String getChartValueLabel()
    {
        return mChartValueLabel;
    }

    /**
     * @param chartValueLabel
     *            the chartValueLabel to set
     */
    public void setChartValueLabel(String chartValueLabel)
    {
        mChartValueLabel = chartValueLabel;
    }

}
