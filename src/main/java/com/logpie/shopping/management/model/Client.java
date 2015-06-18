// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

/**
 * @author zhoyilei
 *
 */
public class Client implements RowMapper<Client>, LogpieModel
{
    public static final String DB_KEY_CLIENT_ID = "ClientId";
    public static final String DB_KEY_CLIENT_REAL_NAME = "ClientRealName";
    public static final String DB_KEY_CLIENT_WECHAT_NAME = "ClientWechatName";
    public static final String DB_KEY_CLIENT_WECHAT_NUMBER = "ClientWechatNumber";
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
    private String mClientWechatNumber;
    private String mClientWeiboName;
    private String mClientTaobaoName;
    private String mClientAddress;
    private String mClientPostalCode;
    private String mClientPhone;
    private String mClientNote;
    private String mClientJoinTime;

    public Client()
    {
    }

    /**
     * @param clientId
     * @param clientRealName
     * @param clientWechatName
     * @param clientWechatNumber
     * @param clientWeiboName
     * @param clientTaobaoName
     * @param clientAddress
     * @param clientPostalCode
     * @param clientPhone
     * @param clientNote
     * @param clientJoinTime
     */
    public Client(String clientId, String clientRealName, String clientWechatName,
            String clientWechatNumber, String clientWeiboName, String clientTaobaoName,
            String clientAddress, String clientPostalCode, String clientPhone, String clientNote,
            String clientJoinTime)
    {
        super();
        mClientId = clientId;
        mClientRealName = clientRealName;
        mClientWechatName = clientWechatName;
        mClientWechatNumber = clientWechatNumber;
        mClientWeiboName = clientWeiboName;
        mClientTaobaoName = clientTaobaoName;
        mClientAddress = clientAddress;
        mClientPostalCode = clientPostalCode;
        mClientPhone = clientPhone;
        mClientNote = clientNote;
        mClientJoinTime = clientJoinTime;
    }

    /**
     * @param clientRealName
     * @param clientWechatName
     * @param clientWechatNumber
     * @param clientWeiboName
     * @param clientTaobaoName
     * @param clientAddress
     * @param clientPostalCode
     * @param clientPhone
     * @param clientNote
     * @param clientJoinTime
     */
    public Client(String clientRealName, String clientWechatName, String clientWechatNumber,
            String clientWeiboName, String clientTaobaoName, String clientAddress,
            String clientPostalCode, String clientPhone, String clientNote)
    {
        super();
        mClientRealName = clientRealName;
        mClientWechatName = clientWechatName;
        mClientWechatNumber = clientWechatNumber;
        mClientWeiboName = clientWeiboName;
        mClientTaobaoName = clientTaobaoName;
        mClientAddress = clientAddress;
        mClientPostalCode = clientPostalCode;
        mClientPhone = clientPhone;
        mClientNote = clientNote;
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Client.DB_KEY_CLIENT_REAL_NAME, mClientRealName);
        modelMap.put(Client.DB_KEY_CLIENT_WECHAT_NAME, mClientWechatName);
        modelMap.put(Client.DB_KEY_CLIENT_WECHAT_NUMBER, mClientWechatNumber);
        modelMap.put(Client.DB_KEY_CLIENT_WEIBO_NAME, mClientWeiboName);
        modelMap.put(Client.DB_KEY_CLIENT_TAOBAO_NAME, mClientTaobaoName);
        modelMap.put(Client.DB_KEY_CLIENT_ADDRESS, mClientAddress);
        modelMap.put(Client.DB_KEY_CLIENT_POSTAL_CODE, mClientPostalCode);
        modelMap.put(Client.DB_KEY_CLIENT_PHONE, mClientPhone);
        modelMap.put(Client.DB_KEY_CLIENT_NOTE, mClientNote);
        return modelMap;
    }

    public static Client readNewClientFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        // ClientId and ClientJoinTime is auto generated
        final String clientRealName = request.getParameter("ClientRealName");
        final String clientWechatName = request.getParameter("ClientWechatName");
        final String clientWechatNumber = request.getParameter("ClientWechatNumber");
        final String clientWeiboName = request.getParameter("ClientWeiboName");
        final String clientTaobaoName = request.getParameter("ClientTaobaoName");
        final String clientAddress = request.getParameter("ClientAddress");
        final String clientPostalCode = request.getParameter("ClientPostalCode");
        final String clientPhone = request.getParameter("ClientPhone");
        final String clientNote = request.getParameter("ClientNote");

        return new Client(clientRealName, clientWechatName, clientWechatNumber, clientWeiboName,
                clientTaobaoName, clientAddress, clientPostalCode, clientPhone, clientNote);
    }

    public static Client readModifiedClientFromRequest(final HttpServletRequest request)
    {

        if (request == null)
        {
            return null;
        }
        final String clientId = request.getParameter("ClientId");
        final String clientJoinTime = request.getParameter("ClientJoinTime");

        final String clientRealName = request.getParameter("ClientRealName");
        final String clientWechatName = request.getParameter("ClientWechatName");
        final String clientWechatNumber = request.getParameter("ClientWechatNumber");
        final String clientWeiboName = request.getParameter("ClientWeiboName");
        final String clientTaobaoName = request.getParameter("ClientTaobaoName");
        final String clientAddress = request.getParameter("ClientAddress");
        final String clientPostalCode = request.getParameter("ClientPostalCode");
        final String clientPhone = request.getParameter("ClientPhone");
        final String clientNote = request.getParameter("ClientNote");

        return new Client(clientId, clientRealName, clientWechatName, clientWechatNumber,
                clientWeiboName, clientTaobaoName, clientAddress, clientPostalCode, clientPhone,
                clientNote, clientJoinTime);

    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_CLIENT_ID;
    }

    @Override
    public boolean compareTo(final Object object)
    {
        if (object instanceof Client)
        {
            final Client compareToClient = (Client) object;
            if (compareToClient.mClientId.equals(mClientId)
                    && compareToClient.mClientAddress.equals(mClientAddress)
                    && compareToClient.mClientJoinTime.equals(mClientJoinTime)
                    && compareToClient.mClientNote.equals(mClientNote)
                    && compareToClient.mClientPhone.equals(mClientPhone)
                    && compareToClient.mClientPostalCode.equals(mClientPostalCode)
                    && compareToClient.mClientRealName.equals(mClientRealName)
                    && compareToClient.mClientTaobaoName.equals(mClientTaobaoName)
                    && compareToClient.mClientWechatName.equals(mClientWechatName)
                    && compareToClient.mClientWechatNumber.equals(mClientWechatNumber)
                    && compareToClient.mClientWeiboName.equals(mClientWeiboName))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Client mapRow(ResultSet rs, int row) throws SQLException
    {
        return getClientByResultSet(rs, row);
    }

    public static Client getClientByResultSet(final ResultSet rs, final Integer row)
            throws SQLException
    {
        if (rs == null)
        {
            return null;
        }

        final String clientId = rs.getString(DB_KEY_CLIENT_ID);
        if (clientId == null)
        {
            return null;
        }
        final String clientRealName = String.valueOf(rs.getString(DB_KEY_CLIENT_REAL_NAME));
        final String clientWechatName = String.valueOf(rs.getString(DB_KEY_CLIENT_WECHAT_NAME));
        final String clientWechatNumber = String.valueOf(rs.getString(DB_KEY_CLIENT_WECHAT_NUMBER));
        final String clientWeiboName = String.valueOf(rs.getString(DB_KEY_CLIENT_WEIBO_NAME));
        final String clientTaobaoName = String.valueOf(rs.getString(DB_KEY_CLIENT_TAOBAO_NAME));
        final String clientAddress = String.valueOf(rs.getString(DB_KEY_CLIENT_ADDRESS));
        final String clientPostalCode = String.valueOf(rs.getString(DB_KEY_CLIENT_POSTAL_CODE));
        final String clientPhone = String.valueOf(rs.getString(DB_KEY_CLIENT_PHONE));
        final String clientNote = String.valueOf(rs.getString(DB_KEY_CLIENT_NOTE));
        final Date clientJoinTime = rs.getTimestamp(DB_KEY_CLIENT_JOIN_TIME);
        String clientJoinTimeString = null;
        if (clientJoinTime != null)
        {
            clientJoinTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(clientJoinTime);
        }

        return new Client(clientId, clientRealName, clientWechatName, clientWechatNumber,
                clientWeiboName, clientTaobaoName, clientAddress, clientPostalCode, clientPhone,
                clientNote, clientJoinTimeString);
    }

    /**
     * The api to show the client profile name. Some clients may miss the real
     * name or some This api will return a non-null name. Follow the logic
     * order: WechatName > WeiboName > RealName > TaobaoName
     * 
     * @return
     */
    public String getClientShowName()
    {
        if (!StringUtils.isEmpty(mClientWechatName))
        {
            return mClientWechatName;
        }
        if (!StringUtils.isEmpty(mClientWeiboName))
        {
            return mClientWeiboName;
        }
        if (!StringUtils.isEmpty(mClientRealName))
        {
            return mClientRealName;
        }
        if (!StringUtils.isEmpty(mClientTaobaoName))
        {
            return mClientTaobaoName;
        }

        return "匿名";
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
     * @return the clientWechatNumber
     */
    public String getClientWechatNumber()
    {
        return mClientWechatNumber;
    }

    /**
     * @param clientWechatNumber
     *            the clientWechatNumber to set
     */
    public void setClientWechatNumber(String clientWechatNumber)
    {
        mClientWechatNumber = clientWechatNumber;
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
