// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public class Client implements RowMapper<Client>, LogpieModel
{
    public static final String DB_KEY_CLIENT_ID = "ClientId";
    public static final String DB_KEY_CLIENT_REAL_NAME = "ClientRealName";
    public static final String DB_KEY_CLIENT_WECHAT_NAME = "ClientWechatName";
    public static final String DB_KEY_CLIENT_WEIBO_NAME = "ClientWeiboName";
    public static final String DB_KEY_CLIENT_TAOBAO_NAME = "ClientTaobaoName";
    public static final String DB_KEY_CLIENT_ADDRESS = "ClientAddress";
    public static final String DB_KEY_CLIENT_POSTAL_CODE = "ClientPostalCode";
    public static final String DB_KEY_CLIENT_PHONE = "ClientPhone";
    public static final String DB_KEY_CLIENT_NOTE = "ClientNote";
    public static final String DB_KEY_CLIENT_JOIN_TIME = "ClientJoinTime";

    private String mClientId;
    private String mClientRealName;
    private String mClientWechatName;
    private String mClientWeiboName;
    private String mClientTaobaoName;
    private String mClientAddress;
    private String mClientPostalCode;
    private String mClientPhone;
    private String mClientNote;
    private String mClientJoinTime;

    @Override
    public Map<String, Object> getModelMap()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPrimaryKey()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean compareTo(Object object)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Client mapRow(ResultSet arg0, int arg1) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the clientId
     */
    public String getClientId()
    {
        return mClientId;
    }

    /**
     * @param clientId
     *            the clientId to set
     */
    public void setClientId(String clientId)
    {
        mClientId = clientId;
    }

    /**
     * @return the clientRealName
     */
    public String getClientRealName()
    {
        return mClientRealName;
    }

    /**
     * @param clientRealName
     *            the clientRealName to set
     */
    public void setClientRealName(String clientRealName)
    {
        mClientRealName = clientRealName;
    }

    /**
     * @return the clientWechatName
     */
    public String getClientWechatName()
    {
        return mClientWechatName;
    }

    /**
     * @param clientWechatName
     *            the clientWechatName to set
     */
    public void setClientWechatName(String clientWechatName)
    {
        mClientWechatName = clientWechatName;
    }

    /**
     * @return the clientWeiboName
     */
    public String getClientWeiboName()
    {
        return mClientWeiboName;
    }

    /**
     * @param clientWeiboName
     *            the clientWeiboName to set
     */
    public void setClientWeiboName(String clientWeiboName)
    {
        mClientWeiboName = clientWeiboName;
    }

    /**
     * @return the clientTaobaoName
     */
    public String getClientTaobaoName()
    {
        return mClientTaobaoName;
    }

    /**
     * @param clientTaobaoName
     *            the clientTaobaoName to set
     */
    public void setClientTaobaoName(String clientTaobaoName)
    {
        mClientTaobaoName = clientTaobaoName;
    }

    /**
     * @return the clientAddress
     */
    public String getClientAddress()
    {
        return mClientAddress;
    }

    /**
     * @param clientAddress
     *            the clientAddress to set
     */
    public void setClientAddress(String clientAddress)
    {
        mClientAddress = clientAddress;
    }

    /**
     * @return the clientPostalCode
     */
    public String getClientPostalCode()
    {
        return mClientPostalCode;
    }

    /**
     * @param clientPostalCode
     *            the clientPostalCode to set
     */
    public void setClientPostalCode(String clientPostalCode)
    {
        mClientPostalCode = clientPostalCode;
    }

    /**
     * @return the clientPhone
     */
    public String getClientPhone()
    {
        return mClientPhone;
    }

    /**
     * @param clientPhone
     *            the clientPhone to set
     */
    public void setClientPhone(String clientPhone)
    {
        mClientPhone = clientPhone;
    }

    /**
     * @return the clientJoinTime
     */
    public String getClientJoinTime()
    {
        return mClientJoinTime;
    }

    /**
     * @param clientJoinTime
     *            the clientJoinTime to set
     */
    public void setClientJoinTime(String clientJoinTime)
    {
        mClientJoinTime = clientJoinTime;
    }

    /**
     * @return the clientNote
     */
    public String getClientNote()
    {
        return mClientNote;
    }

    /**
     * @param clientNote
     *            the clientNote to set
     */
    public void setClientNote(String clientNote)
    {
        mClientNote = clientNote;
    }

}
