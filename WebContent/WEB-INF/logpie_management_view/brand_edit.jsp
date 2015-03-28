<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改品牌： ${brand.brandEnglishName}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
			<form role="form" style="padding:20px" action="<c:url value="/brand/edit" />" method="POST" id="brand_creation_form">
                <input type="hidden" name="BrandId" value="${brand.brandId}">
                <div class="form-group">
                  <label for="brand_en">品牌英文名称</label>
                  <input class="form-control" type="text" id="brand_en" name="BrandEnglishName" value="${brand.brandEnglishName}" required autofocus>
                </div>
                <div class="form-group">
                  <label for="brand_cn">品牌中文名称</label>
                  <input class="form-control" type="text" id="brand_cn" name="BrandChineseName" value="${brand.brandChineseName}" required>
                </div>
                <div class="dropdown" style="margin-bottom:20px">
                  <label for="brand_category">品牌所属类别</label>
                     <select class="form-control" form="brand_creation_form" name="BrandCategoryId">
                    	 <c:forEach items="${categoryList}" var="category">
						  <c:if test="${brand.brandCategory.categoryId == category.categoryId}">
					       		<option value="${category.categoryId}" selected>${category.categoryName}</option>
       					   </c:if>
					       <c:if test="${brand.brandCategory.categoryId != category.categoryId}">
					       		 <option value="${category.categoryId}">${category.categoryName}</option>
       					   </c:if>
       					 </c:forEach>
       			    </select>
                </div>
                <div class="row">
                  <div class="dropdown col-sm-5" style="margin-bottom:10px">
                    <label for="brand_image">品牌图片</label>
						<select class="form-control" form="brand_creation_form" name="BrandImageId">
						    <c:forEach items="${imageList}" var="brandImage">
							  <c:if test="${brand.brandImage.imageId == brandImage.imageId}">
						       		<option value="${brandImage.imageId}" selected>${brandImage.imageDescription}</option>
        					   </c:if>
						       <c:if test="${brand.brandImage.imageId != category.categoryId}">
						       		 <option value="${brandImage.imageId}">${brandImage.imageDescription}</option>
        					   </c:if>
						    </c:forEach>
						</select>
                  </div>
                  <div class="dropdown col-sm-7" style="margin-bottom:10px">
                    <label for="brand_image">相关尺寸图片</label>
						<select class="form-control" form="brand_creation_form" name="BrandSizeChartImageId" required>
						    <c:forEach items="${imageList}" var="brandSizeChartImage">
							  <c:if test="${brand.brandSizeChartImage.imageId == brandSizeChartImage.imageId}">
						       		<option value="${brandSizeChartImage.imageId}" selected>${brandSizeChartImage.imageDescription}</option>
        					   </c:if>
						       <c:if test="${brand.brandSizeChartImage.imageId != brandSizeChartImage.imageId}">
						       		 <option value="${brandSizeChartImage.imageId}">${brandSizeChartImage.imageDescription}</option>
        					   </c:if>
						    </c:forEach>
						</select>
                  </div>
                </div>
                <div class="checkbox" style="padding-left:20px">
                  <label><input type="checkbox" checked="checked" id="brand_is_activated" name="BrandIsActivated" value="True"/>是否激活</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>