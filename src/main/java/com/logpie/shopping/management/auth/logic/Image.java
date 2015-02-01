package com.logpie.shopping.management.auth.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author xujiahang This class is the model class for Image table
 */
public class Image implements RowMapper<Image> {

	public static final String DB_KEY_IMAGE_ID = "ImageId";
	public static final String DB_KEY_IMAGE_URL = "ImageUrl";
	public static final String DB_KEY_IMAGE_DESCRIPTION = "ImageDescription";

	private final String mImageId;
	private final String mImageUrl;
	private final String mImageDescription;

	public Image(final String imageId, final String imageUrl,
			final String imageDescription) {
		this.mImageId = imageId;
		this.mImageUrl = imageUrl;
		this.mImageDescription = imageDescription;
	}

	// ImageId should be generated automatically by database
	public Image(final String imageUrl, final String imageDescription) {
		this(null, imageUrl, imageDescription);
	}

	/**
	 * @return ImageId
	 */
	public String getImageId() {
		return mImageId;
	}

	/**
	 * @return ImageUrl
	 */
	public String getImageUrl() {
		return mImageUrl;
	}

	/**
	 * @return ImageDescription
	 */
	public String getImageDescription() {
		return mImageDescription;
	}

	/*
	 * Map the database row into Image object
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs == null || rowNum == 0) {
			return null;
		}
		final String imageId = rs.getString(DB_KEY_IMAGE_ID);
		final String imageUrl = rs.getString(DB_KEY_IMAGE_URL);
		final String imageDescription = rs.getString(DB_KEY_IMAGE_DESCRIPTION);

		return new Image(imageId, imageUrl, imageDescription);
	}

}
