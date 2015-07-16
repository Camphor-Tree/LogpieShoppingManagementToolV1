package com.logpie.shopping.settings;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Setting;
import com.logpie.shopping.management.storage.SettingDAO;

public class SettingManager
{
    public final static String sSystemSettingNameSpace = "com.logpie.shopping.management.system.setting";

    public class SystemSettingKeys
    {
        // 默认代理
        public final static String SYSTEM_DEFAULT_PROXY = "default.proxy";
        // 默认分红百分比
        public final static String SYSTEM_DEFAULT_PROXY_PROFIT_PERCENTAGE = "default.proxy.profit.percentage";
    }

    private static SettingManager sInstance;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> mSettingCacheMap;
    private SettingDAO mSettingDAO;

    private SettingManager()
    {
        initSetting();
    }

    public synchronized static SettingManager getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new SettingManager();
        }
        return sInstance;
    }

    private void initSetting()
    {
        mSettingDAO = new SettingDAO(Admin.buildSystemSuperAdmin());
        mSettingCacheMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
        final List<Setting> settingList = mSettingDAO.getAllSettings();
        if (settingList != null)
        {
            for (final Setting setting : settingList)
            {
                final String nameSpace = setting.getSettingNameSpace();
                if (!mSettingCacheMap.containsKey(nameSpace))
                {
                    mSettingCacheMap.put(nameSpace, new ConcurrentHashMap<String, String>());
                }
                final ConcurrentHashMap<String, String> nameSpaceSettings = mSettingCacheMap
                        .get(nameSpace);
                nameSpaceSettings.put(setting.getSettingKey(), setting.getSettingValue());
            }
        }
    }

    public synchronized boolean setSystemSetting(final String key, final String value)
    {
        final List<Setting> settingList = mSettingDAO
                .getSettingByNameSpaceAndKey(sSystemSettingNameSpace, key);
        boolean result = false;
        if (settingList == null || settingList.size() == 0)
        {
            // no setting has been set up. insert a new setting
            result = mSettingDAO.addSetting(new Setting(sSystemSettingNameSpace, key, value));
        }
        else if (settingList.size() == 1)
        {
            // If setting already exist, update the value
            final Setting setting = settingList.get(0);
            setting.setSettingValue(value);
            result = mSettingDAO.updateSetting(setting);
        }
        else
        {
            throw new RuntimeException("Dulicate Settings");
        }

        if (result)
        {
            ConcurrentHashMap<String, String> systemSettingCache = mSettingCacheMap
                    .get(sSystemSettingNameSpace);
            if (systemSettingCache == null)
            {
                systemSettingCache = new ConcurrentHashMap<String, String>();
                mSettingCacheMap.put(key, systemSettingCache);
            }
            systemSettingCache.put(key, value);
        }
        return result;
    }

    public String getSystemSetting(final String key)
    {
        final ConcurrentHashMap<String, String> systemSettingCache = mSettingCacheMap
                .get(sSystemSettingNameSpace);
        if (systemSettingCache == null)
        {
            return null;
        }
        return systemSettingCache.get(key);
    }

}
