package com.logpie.shopping.management.business.logic;

import com.logpie.shopping.management.model.Client;
import com.logpie.shopping.management.storage.ClientDAO;

public final class ClientMappingLogic
{
    public static Client tryMapNameToClientProfile(final String name)
    {
        final ClientDAO clientDAO = new ClientDAO(null);
        // Try map wechat name first
        Client client = clientDAO.getClientByWechatName(name);
        if (client == null)
        {
            // Then try to map weibo name
            client = clientDAO.getClientByWeiboName(name);
        }
        return client;

    }
}
