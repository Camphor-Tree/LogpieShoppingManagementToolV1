// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.Map;

import com.logpie.shopping.management.model.Category;

/**
 * @author zhoyilei
 *
 */
public class CategoryDAO extends LogpieBaseDAO<Category>
{
    private static final String sCategoryTableName = "Categories";

    public boolean addCategory(final Category category)
    {
        final LogpieDataInsert<Category> addCategoryInsert = new AddCategoryInsert(category);
        return super.insertData(addCategoryInsert);
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
            final String categoryName = mCategory.getCategoryName();
            final Map<String, Object> insertValues = new HashMap<String, Object>();
            insertValues.put(Category.DB_KEY_CATEGORY_NAME, categoryName);
            return insertValues;
        }

    }
}
