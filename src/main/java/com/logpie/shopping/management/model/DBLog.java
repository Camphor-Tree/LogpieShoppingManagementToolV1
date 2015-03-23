// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei DBLog is used to record all the db operations by admins.
 */
public class DBLog implements RowMapper<DBLog>, LogpieModel
{
    public static final String DB_KEY_DBLOG_ID = "DbLogId";
    public static final String DB_KEY_DBLOG_ADMIN_ID = "DbLogAdminId";
    public static final String DB_KEY_DBLOG_TIME = "DbLogTime";
    public static final String DB_KEY_DBLOG_SQL = "DbLogSQL";
    public static final String DB_KEY_DBLOG_COMMENT = "DbLogComment";

    private String mDbLogId;
    private Admin mDbLogAdmin;
    private String mDbLogTime;
    private String mDbLogSQL;
    private String mDbLogComment;

    public DBLog()
    {

    }

    /**
     * @param dbLogId
     * @param dbLogAdmin
     * @param dbLogTime
     * @param dbLogSQL
     * @param dbLogComment
     */
    public DBLog(String dbLogId, Admin dbLogAdmin, String dbLogTime, String dbLogSQL,
            String dbLogComment)
    {
        super();
        mDbLogId = dbLogId;
        mDbLogAdmin = dbLogAdmin;
        mDbLogTime = dbLogTime;
        mDbLogSQL = dbLogSQL;
        mDbLogComment = dbLogComment;
    }

    /**
     * @param dbLogAdmin
     * @param dbLogSQL
     * @param dbLogComment
     */
    public DBLog(Admin dbLogAdmin, String dbLogSQL, String dbLogComment)
    {
        super();
        mDbLogAdmin = dbLogAdmin;
        mDbLogSQL = dbLogSQL;
        mDbLogComment = dbLogComment;
    }

    /**
     * @param dbLogAdmin
     * @param dbLogTime
     * @param dbLogSQL
     * @param dbLogComment
     */
    public DBLog(Admin dbLogAdmin, String dbLogTime, String dbLogSQL, String dbLogComment)
    {
        super();
        mDbLogAdmin = dbLogAdmin;
        mDbLogTime = dbLogTime;
        mDbLogSQL = dbLogSQL;
        mDbLogComment = dbLogComment;
    }

    /**
     * @return the dbLogId
     */
    public String getDbLogId()
    {
        return mDbLogId;
    }

    /**
     * @param dbLogId
     *            the dbLogId to set
     */
    public void setDbLogId(String dbLogId)
    {
        mDbLogId = dbLogId;
    }

    /**
     * @return the dbLogAdmin
     */
    public Admin getDbLogAdmin()
    {
        return mDbLogAdmin;
    }

    /**
     * @param dbLogAdmin
     *            the dbLogAdmin to set
     */
    public void setDbLogAdmin(Admin dbLogAdmin)
    {
        mDbLogAdmin = dbLogAdmin;
    }

    /**
     * @return the dbLogTime
     */
    public String getDbLogTime()
    {
        return mDbLogTime;
    }

    /**
     * @param dbLogTime
     *            the dbLogTime to set
     */
    public void setDbLogTime(String dbLogTime)
    {
        mDbLogTime = dbLogTime;
    }

    /**
     * @return the dbLogSQL
     */
    public String getDbLogSQL()
    {
        return mDbLogSQL;
    }

    /**
     * @param dbLogSQL
     *            the dbLogSQL to set
     */
    public void setDbLogSQL(String dbLogSQL)
    {
        mDbLogSQL = dbLogSQL;
    }

    /**
     * @return the dbLogComment
     */
    public String getDbLogComment()
    {
        return mDbLogComment;
    }

    /**
     * @param dbLogComment
     *            the dbLogComment to set
     */
    public void setDbLogComment(String dbLogComment)
    {
        mDbLogComment = dbLogComment;
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(DBLog.DB_KEY_DBLOG_ADMIN_ID, mDbLogAdmin.getAdminId());
        if (mDbLogTime != null)
        {
            modelMap.put(DBLog.DB_KEY_DBLOG_TIME, mDbLogTime);
        }
        modelMap.put(DBLog.DB_KEY_DBLOG_SQL, mDbLogSQL);
        modelMap.put(DBLog.DB_KEY_DBLOG_COMMENT, mDbLogComment);
        return modelMap;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_DBLOG_ID;
    }

    @Override
    public DBLog mapRow(ResultSet rs, int row) throws SQLException
    {
        return getDBLogByResultSet(rs, row);
    }

    public static DBLog getDBLogByResultSet(final ResultSet rs, final int row) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String dbLogId = rs.getString(DB_KEY_DBLOG_ID);
        final Admin dbLogAdmin = Admin.getAdminByResultSet(rs, row);
        final Date dbLogTime = rs.getTimestamp(DB_KEY_DBLOG_TIME);
        final String dbLogTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(dbLogTime);
        final String dbLogSQL = rs.getString(DB_KEY_DBLOG_SQL);
        final String dbLogComment = rs.getString(DB_KEY_DBLOG_COMMENT);
        return new DBLog(dbLogId, dbLogAdmin, dbLogTimeString, dbLogSQL, dbLogComment);
    }

    @Override
    public boolean compareTo(Object object)
    {
        // not needed
        return false;
    }
}
