// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Client;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class ClientDAO extends LogpieBaseDAO<Client>
{
    private static final Logger LOG = Logger.getLogger(ClientDAO.class);
    public static final String sClientTableName = "Clients";

    /**
     * @param admin
     */
    public ClientDAO(final Admin admin)
    {
        super(admin);
    }

    /**
     * For adding a new client into the database
     * 
     * @param client
     * @return true if adding client successfully. false if adding client fails
     */
    public boolean addClient(final Client client)
    {
        final LogpieDataInsert<Client> addClientInsert = new AddClientInsert(client);
        return super.insertData(addClientInsert);
    }

    /**
     * For querying specific Client by ClientId
     * 
     * @param clientId
     * @return Client corresponding to the ClientId
     */
    public Client getClientById(final String clientId)
    {
        GetClientByIdQuery getClientByIdQuery = new GetClientByIdQuery(clientId);
        List<Client> clientList = super.queryResult(getClientByIdQuery);
        if (CollectionUtils.isEmpty(clientList) || clientList.size() > 1)
        {
            LOG.error("The client cannot be found by this id:" + clientId);
            return null;
        }
        return clientList.get(0);
    }

    /**
     * Update the client profile
     * 
     * @param client
     * @return
     */
    public boolean updateClientProfile(final Client client)
    {
        final UpdateClientUpdate updateClientUpdate = new UpdateClientUpdate(client,
                sClientTableName, client.getClientId());
        return super.updateData(updateClientUpdate, "更新了用户档案信息");
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<Client> getAllClients()
    {
        final GetAllClientQuery getAllClientQuery = new GetAllClientQuery();
        return super.queryResult(getAllClientQuery);
    }

    /**
     * Search client
     * 
     * @return clients match the search condition
     */
    public List<Client> searchClient(final String searchString)
    {
        SearchClientQuery searchClientQuery = new SearchClientQuery(searchString);
        return super.queryResult(searchClientQuery);
    }

    private class AddClientInsert implements LogpieDataInsert<Client>
    {
        private Client mClient;

        AddClientInsert(final Client client)
        {
            mClient = client;
        }

        @Override
        public String getInsertTable()
        {
            return sClientTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mClient.getModelMap();
        }
    }

    private class GetAllClientQuery extends LogpieBaseQueryAllTemplateQuery<Client>
    {
        GetAllClientQuery()
        {
            super(new Client(), ClientDAO.sClientTableName);
        }
    }

    private class SearchClientQuery extends LogpieBaseQueryAllTemplateQuery<Client>
    {
        private String mSearchString;

        SearchClientQuery(final String searchString)
        {
            super(new Client(), ClientDAO.sClientTableName);
            mSearchString = searchString;
        }

        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> conditions = new HashSet<String>();
            // add search condition
            final StringBuilder searchConditionBuilder = new StringBuilder("(");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_ADDRESS);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_NOTE);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_PHONE);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_POSTAL_CODE);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_REAL_NAME);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_TAOBAO_NAME);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_WECHAT_NAME);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_WECHAT_NUMBER);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(Client.DB_KEY_CLIENT_WEIBO_NAME);
            searchConditionBuilder.append(" like '%" + mSearchString + "%')");
            conditions.add(searchConditionBuilder.toString());
            return conditions;
        }
    }

    private class GetClientByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Client>
    {
        GetClientByIdQuery(final String clientId)
        {
            super(new Client(), ClientDAO.sClientTableName, Client.DB_KEY_CLIENT_ID, clientId);
        }
    }

    private class UpdateClientUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Client>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateClientUpdate(LogpieModel model, String tableName, String clientId)
        {
            super(model, tableName, Client.DB_KEY_CLIENT_ID, clientId);
        }
    }

}
