// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author xujiahang This class is the model class for Image table
 */
public class Image implements RowMapper<Image>, LogpieModel
{
    public static final String DB_KEY_IMAGE_ID = "ImageId";
    public static final String DB_KEY_IMAGE_URL = "ImageUrl";
    public static final String DB_KEY_IMAGE_DESCRIPTION = "ImageDescription";

    private String mImageId;
    private String mImageUrl;
    private String mImageDescription;

    // For RowMapper
    public Image()
    {

    }

    // ImageId should be generated automatically by database
    public Image(final String imageUrl, final String imageDescription)
    {
        this(null, imageUrl, imageDescription);
    }

    public Image(final String imageId, final String imageUrl, final String imageDescription)
    {
        this.mImageId = imageId;
        this.mImageUrl = imageUrl;
        this.mImageDescription = imageDescription;
    }

    @Override
    public Image mapRow(final ResultSet rs, final int rowNum) throws SQLException
    {
        return getImageByResultSet(rs, rowNum, null);
    }

    public static Image getImageByResultSet(final ResultSet rs, final int rowNum,
            final String imageTableAlias) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }

        String keyImageId;
        String keyImageUrl;
        String keyImageDescription;
        if (imageTableAlias != null)
        {
            keyImageId = imageTableAlias + "." + DB_KEY_IMAGE_ID;
            keyImageUrl = imageTableAlias + "." + DB_KEY_IMAGE_URL;
            keyImageDescription = imageTableAlias + "." + DB_KEY_IMAGE_DESCRIPTION;
        }
        else
        {
            keyImageId = DB_KEY_IMAGE_ID;
            keyImageUrl = DB_KEY_IMAGE_URL;
            keyImageDescription = DB_KEY_IMAGE_DESCRIPTION;
        }
        final String imageId = rs.getString(keyImageId);
        final String imageUrl = rs.getString(keyImageUrl);
        final String imageDescription = rs.getString(keyImageDescription);
        return new Image(imageId, imageUrl, imageDescription);
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Image.DB_KEY_IMAGE_DESCRIPTION, mImageDescription);
        modelMap.put(Image.DB_KEY_IMAGE_URL, mImageUrl);
        return modelMap;
    }

    public static Image readNewImageFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String imageUrl = request.getParameter("ImageUrl");
        final String imageDescription = request.getParameter("ImageDescription");
        return new Image(imageUrl, imageDescription);
    }

    /**
     * @return ImageId
     */
    public String getImageId()
    {
        return mImageId;
    }

    /**
     * @return ImageUrl
     */
    public String getImageUrl()
    {
        return mImageUrl;
    }

    /**
     * @return ImageDescription
     */
    public String getImageDescription()
    {
        return mImageDescription;
    }

}
