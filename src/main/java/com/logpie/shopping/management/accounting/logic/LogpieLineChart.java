// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.accounting.logic;

/**
 * @author zhoyilei
 *
 */
public class LogpieLineChart
{
    private String mChartLabel;
    private String mChartKeyLabel;
    private String mChartValueLabel;
    private String mChartValueLabel2;
    private String mChartValueLabel3;

    /**
     * @param chartLabel
     * @param chartKeyLabel
     * @param chartValueLabel
     */
    public LogpieLineChart(String chartLabel, String chartKeyLabel, String chartValueLabel)
    {
        super();
        mChartLabel = chartLabel;
        mChartKeyLabel = chartKeyLabel;
        mChartValueLabel = chartValueLabel;
    }

    public LogpieLineChart(String chartLabel, String chartKeyLabel, String chartValueLabel,
            String chartValueLabel2, String chartValueLabel3)
    {
        super();
        mChartLabel = chartLabel;
        mChartKeyLabel = chartKeyLabel;
        mChartValueLabel = chartValueLabel;
        mChartValueLabel2 = chartValueLabel2;
        mChartValueLabel3 = chartValueLabel3;
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

    public String getChartValueLabel2()
    {
        return mChartValueLabel2;
    }

    public void setChartValueLabel2(String chartValueLabel2)
    {
        mChartValueLabel2 = chartValueLabel2;
    }

    public String getChartValueLabel3()
    {
        return mChartValueLabel3;
    }

    public void setChartValueLabel3(String chartValueLabel3)
    {
        mChartValueLabel3 = chartValueLabel3;
    }

}
