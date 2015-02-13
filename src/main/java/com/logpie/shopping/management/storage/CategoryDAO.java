// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class CategoryDAO extends LogpieBaseDAO<Category>
{
    private static final Logger LOG = Logger.getLogger(CategoryDAO.class);
    public static final String sCategoryTableName = "Categories";

    /**
     * For adding a new category into the database
     * 
     * @param category
     * @return true if adding category successfully. false if adding category
     *         fails
     */
    public boolean addCategory(final Category category)
    {
        final LogpieDataInsert<Category> addCategoryInsert = new AddCategoryInsert(category);
        return super.insertData(addCategoryInsert);
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<Category> getAllCategory()
    {
        GetAllCategoryQuery getAllCategoryQuery = new GetAllCategoryQuery();
        return super.queryResult(getAllCategoryQuery);
    }

    /**
     * For querying specific Category by CategoryId
     * 
     * @param categoryId
     * @return Category corresponding to the CategoryId
     */
    public Category getCategoryById(final String categoryId)
    {
        GetCategoryByIdQuery getCategoryByIdQuery = new GetCategoryByIdQuery(categoryId);
        List<Category> categoryList = super.queryResult(getCategoryByIdQuery);
        if (CollectionUtils.isEmpty(categoryList) || categoryList.size() > 1)
        {
            LOG.error("The category cannot be found by this id:" + categoryId);
            return null;
        }
        return categoryList.get(0);
    }

    /**
     * Update the category profile
     * 
     * @param category
     * @return
     */
    public boolean updateCategoryProfile(final Category category)
    {
        final UpdateCategoryUpdate updateCategoryUpdate = new UpdateCategoryUpdate(category,
                sCategoryTableName, category.getCategoryId());
        return super.updateData(updateCategoryUpdate);
    }

    private class AddCategoryInsert implements LogpieDataInsert<Category>
    {
        private Category mCategory;

        AddCategoryInsert(final Category category)
        {
            mCategory = category;
        }

        @Override
        public String getInsertTable()
        {
            return sCategoryTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mCategory.getModelMap();
        }
    }

    private class GetAllCategoryQuery extends LogpieBaseQueryAllTemplateQuery<Category>
    {
        GetAllCategoryQuery()
        {
            super(new Category(), CategoryDAO.sCategoryTableName);
        }
    }

    private class GetCategoryByIdQuery extends
            LogpieBaseQuerySingleRecordByIdTemplateQuery<Category>
    {
        GetCategoryByIdQuery(final String categoryId)
        {
            super(new Category(), CategoryDAO.sCategoryTableName, Category.DB_KEY_CATEGORY_ID,
                    categoryId);
        }
    }

    private class UpdateCategoryUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Category>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateCategoryUpdate(LogpieModel model, String tableName, String categoryId)
        {
            super(model, tableName, Category.DB_KEY_CATEGORY_ID, categoryId);
        }
    }
}
