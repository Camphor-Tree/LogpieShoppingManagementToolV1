package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class Setting implements RowMapper<Setting>, LogpieModel
{
    private static final Logger LOG = Logger.getLogger(Setting.class);

    public static final String DB_KEY_SETTING_ID = "SettingId";
    public static final String DB_KEY_SETTING_NAMESPACE = "SettingNameSpace";
    public static final String DB_KEY_SETTING_KEY = "SettingKey";
    public static final String DB_KEY_SETTING_VALUE = "SettingValue";

    private String mSettingId;
    private String mSettingNameSpace;
    private String mSettingKey;
    private String mSettingValue;

    public Setting()
    {
    }

    public Setting(final String settingId, final String settingNameSpace, final String settingKey,
            final String settingValue)
    {
        mSettingId = settingId;
        mSettingNameSpace = settingNameSpace;
        mSettingKey = settingKey;
        mSettingValue = settingValue;
    }

    public Setting(final String settingNameSpace, final String settingKey,
            final String settingValue)
    {
        mSettingNameSpace = settingNameSpace;
        mSettingKey = settingKey;
        mSettingValue = settingValue;
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Setting.DB_KEY_SETTING_NAMESPACE, this.mSettingNameSpace);
        modelMap.put(Setting.DB_KEY_SETTING_KEY, this.mSettingKey);
        modelMap.put(Setting.DB_KEY_SETTING_VALUE, this.mSettingValue);
        return modelMap;
    }

    @Override
    public String getPrimaryKey()
    {
        return Setting.DB_KEY_SETTING_ID;
    }

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof Setting)
        {
            final Setting compareToSetting = (Setting) object;
            if (compareToSetting.mSettingId.equals(mSettingId)
                    && compareToSetting.mSettingNameSpace.equals(mSettingNameSpace)
                    && compareToSetting.mSettingKey.equals(mSettingKey)
                    && compareToSetting.mSettingValue.equals(mSettingValue))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Setting mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return getSettingByResultSet(rs, rowNum);
    }

    public static final Setting getSettingByResultSet(ResultSet rs, int rowNum) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String settingId = String.valueOf(rs.getInt(Setting.DB_KEY_SETTING_ID));
        final String settingNameSpace = rs.getString(Setting.DB_KEY_SETTING_NAMESPACE);
        final String settingKey = rs.getString(Setting.DB_KEY_SETTING_KEY);
        final String settingValue = rs.getString(Setting.DB_KEY_SETTING_VALUE);
        return new Setting(settingId, settingNameSpace, settingKey, settingValue);
    }

    public String getSettingId()
    {
        return mSettingId;
    }

    public void setSettingId(String mSettingId)
    {
        this.mSettingId = mSettingId;
    }

    public String getSettingNameSpace()
    {
        return mSettingNameSpace;
    }

    public void setSettingNameSpace(String mSettingNameSpace)
    {
        this.mSettingNameSpace = mSettingNameSpace;
    }

    public String getSettingKey()
    {
        return mSettingKey;
    }

    public void setSettingKey(String mSettingKey)
    {
        this.mSettingKey = mSettingKey;
    }

    public String getSettingValue()
    {
        return mSettingValue;
    }

    public void setSettingValue(String mSettingValue)
    {
        this.mSettingValue = mSettingValue;
    }

}
