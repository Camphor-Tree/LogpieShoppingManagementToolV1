package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.model.Setting;

public class SettingDAO extends LogpieBaseDAO<Setting>
{
    public static final String sSettingTableName = "Settings";

    public SettingDAO(final Admin admin)
    {
        super(admin);
    }

    public boolean addSetting(final Setting setting)
    {
        final LogpieDataInsert<Setting> adddSettingInsert = new AddSettingInsert(setting);
        return super.insertData(adddSettingInsert);
    }

    public List<Setting> getAllSettingsByNameSpace(final String nameSpace)
    {
        final LogpieDataQuery<Setting> getSettingsByNameSpaceQuery = new GetSettingsByNameSpaceQuery(
                nameSpace);
        return super.queryResult(getSettingsByNameSpaceQuery);
    }

    public List<Setting> getSettingByNameSpaceAndKey(final String nameSpace, final String key)
    {
        final LogpieDataQuery<Setting> getSettingsByNameSpaceAndKeyQuery = new GetSettingsByNameSpaceAndKeyQuery(
                nameSpace, key);
        return super.queryResult(getSettingsByNameSpaceAndKeyQuery);
    }

    public List<Setting> getAllSettings()
    {
        final LogpieDataQuery<Setting> getAllSettingsQuery = new GetAllSettingsQuery();
        return super.queryResult(getAllSettingsQuery);
    }

    /**
     * Update the setting
     * 
     * @param setting
     * @return
     */
    public boolean updateSetting(final Setting setting)
    {
        final UpdateSettingUpdate updateSettingUpdate = new UpdateSettingUpdate(setting,
                sSettingTableName, setting.getSettingId());
        return super.updateData(updateSettingUpdate,
                "更新了系统设置 " + setting.getSettingNameSpace() + ":" + setting.getSettingKey());
    }

    private class GetSettingsByNameSpaceQuery extends LogpieBaseQueryAllTemplateQuery<Setting>
    {
        final String mNameSpace;

        GetSettingsByNameSpaceQuery(final String nameSpace)
        {
            super(new Setting(), SettingDAO.sSettingTableName);
            mNameSpace = nameSpace;
        }

        // no query condition for getting all query.
        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> queryConditions = new HashSet<String>();
            queryConditions
                    .add(String.format("%s=\"%s\"", Setting.DB_KEY_SETTING_NAMESPACE, mNameSpace));
            return queryConditions;
        }
    }

    private class GetSettingsByNameSpaceAndKeyQuery extends GetSettingsByNameSpaceQuery
    {
        final String mKey;

        GetSettingsByNameSpaceAndKeyQuery(final String nameSpace, final String key)
        {
            super(nameSpace);
            mKey = key;
        }

        // no query condition for getting all query.
        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> queryConditions = super.getQueryConditions();
            queryConditions.add(String.format("%s=\"%s\"", Setting.DB_KEY_SETTING_KEY, mKey));
            return queryConditions;
        }

    }

    private class GetAllSettingsQuery extends LogpieBaseQueryAllTemplateQuery<Setting>
    {
        GetAllSettingsQuery()
        {
            super(new Setting(), SettingDAO.sSettingTableName);
        }
    }

    private class UpdateSettingUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Setting>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateSettingUpdate(LogpieModel model, String tableName, String settingId)
        {
            super(model, tableName, Setting.DB_KEY_SETTING_ID, settingId);
        }
    }

    private class AddSettingInsert implements LogpieDataInsert<Setting>
    {
        private Setting mSetting;

        AddSettingInsert(final Setting setting)
        {
            mSetting = setting;
        }

        @Override
        public String getInsertTable()
        {
            return sSettingTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mSetting.getModelMap();
        }
    }
}
