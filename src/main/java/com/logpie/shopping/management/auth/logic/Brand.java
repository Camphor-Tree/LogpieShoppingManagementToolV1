package com.logpie.shopping.management.auth.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Brand implements RowMapper<Brand> {

	public static final String DB_KEY_BRAND_ID = "";
	public static final String DB_KEY_BRAND_NAME_EN = "";
	public static final String DB_KEY_BRAND_NAME_CN = "";
	public static final String DB_KEY_BRAND_URL = "";
	public static final String DB_KEY_BRAND_SIZE_CHART_URL = "";
	public static final String DB_KEY_BRAND_CATEGORY_ID = "";
	public static final String DB_KEY_BRAND_IS_ACTIVATED = "";

	private final String mBrandId;
	private final String mBrandUrl;
	private final String mBrandNameEN;
	private final String mBrandNameCN;
	private final String mBrandSizeChartUrl;
	private final String mBrandCategoryId;
	private final boolean mBrandIsActivated;

	public Brand(String brandId, String brandUrl, String brandNameEN,
			String brandNameCN, String brandSizeChartUrl,
			String brandCategoryId, boolean isActivated) {
		this.mBrandId = brandId;
		this.mBrandUrl = brandUrl;
		this.mBrandNameEN = brandNameEN;
		this.mBrandNameCN = brandNameCN;
		this.mBrandSizeChartUrl = brandSizeChartUrl;
		this.mBrandCategoryId = brandCategoryId;
		this.mBrandIsActivated = isActivated;
	}

	public String getBrandId() {
		return mBrandId;
	}

	public String getBrandUrl() {
		return mBrandUrl;
	}

	public String getBrandNameEN() {
		return mBrandNameEN;
	}

	public String getBrandNameCN() {
		return mBrandNameCN;
	}

	public String getBrandSizeChartUrl() {
		return mBrandSizeChartUrl;
	}

	public String getBrandCategoryId() {
		return mBrandCategoryId;
	}

	public boolean isActivated() {
		return mBrandIsActivated;
	}

	@Override
	public Brand mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs == null || rowNum == 0) {
			return null;
		}
		final String brandId = rs.getString(DB_KEY_BRAND_ID);
		final String brandUrl = rs.getString(DB_KEY_BRAND_URL);
		final String brandNameEN = rs.getString(DB_KEY_BRAND_NAME_EN);
		final String brandNameCN = rs.getString(DB_KEY_BRAND_NAME_CN);
		final String brandSizeChartUrl = rs
				.getString(DB_KEY_BRAND_SIZE_CHART_URL);
		final String brandCategoryId = rs.getString(DB_KEY_BRAND_CATEGORY_ID);
		final boolean brandIsActivated = rs
				.getBoolean(DB_KEY_BRAND_IS_ACTIVATED);

		return new Brand(brandId, brandUrl, brandNameEN, brandNameCN,
				brandSizeChartUrl, brandCategoryId, brandIsActivated);
	}

}
