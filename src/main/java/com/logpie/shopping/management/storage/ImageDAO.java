// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class ImageDAO extends LogpieBaseDAO<Image>
{
    private static final Logger LOG = Logger.getLogger(ImageDAO.class);
    public static final String sImageTableName = "Images";

    /**
     * For adding a new image into the database
     * 
     * @param image
     * @return true if adding image successfully. false if adding image fails
     */
    public boolean addImage(final Image image)
    {
        final LogpieDataInsert<Image> addImageInsert = new AddImageInsert(image);
        return super.insertData(addImageInsert);
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<Image> getAllImage()
    {
        GetAllImageQuery getAllImageQuery = new GetAllImageQuery();
        return super.queryResult(getAllImageQuery);
    }

    /**
     * For querying specific image by ImageId
     * 
     * @param imageId
     * @return Image corresponding to the ImageId
     */
    public Image getImageById(final String imageId)
    {
        GetImageByIdQuery getImageByIdQuery = new GetImageByIdQuery(imageId);
        List<Image> ImageList = super.queryResult(getImageByIdQuery);
        if (CollectionUtils.isEmpty(ImageList) || ImageList.size() > 1)
        {
            LOG.error("The Image cannot be found by this id:" + imageId);
            return null;
        }
        return ImageList.get(0);
    }

    /**
     * Update the image profile
     * 
     * @param image
     * @return
     */
    public boolean updateImageProfile(final Image image)
    {
        final UpdateImageUpdate updateImageUpdate = new UpdateImageUpdate(image, sImageTableName,
                image.getImageId());
        return super.updateData(updateImageUpdate);
    }

    private class AddImageInsert implements LogpieDataInsert<Image>
    {
        private Image mImage;

        AddImageInsert(final Image image)
        {
            mImage = image;
        }

        @Override
        public String getInsertTable()
        {
            return sImageTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mImage.getModelMap();
        }
    }

    private class GetAllImageQuery extends LogpieBaseQueryAllTemplateQuery<Image>
    {
        GetAllImageQuery()
        {
            super(new Image(), ImageDAO.sImageTableName);
        }
    }

    private class GetImageByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Image>
    {
        GetImageByIdQuery(final String imageId)
        {
            super(new Image(), ImageDAO.sImageTableName, Image.DB_KEY_IMAGE_ID, imageId);
        }
    }

    private class UpdateImageUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Image>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateImageUpdate(LogpieModel model, String tableName, final String imageId)
        {
            super(model, tableName, Image.DB_KEY_IMAGE_ID, imageId);
        }
    }

}
