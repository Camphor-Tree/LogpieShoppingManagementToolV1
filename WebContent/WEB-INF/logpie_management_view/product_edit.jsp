<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改商品： ${product.productName}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
			 <form role="form" style="padding:20px" id="product_modify_form" action="<c:url value="/product/edit" />" method="POST">
                <input type="hidden" name="ProductId" value="${product.productId}">
                <input type="hidden" name="ProductPostDate" value="${product.productPostDate}">
                
                <div class="form-group">
                  <label for="product-name">产品名称：</label>
                  <input class="form-control" type="text" id="product-name" name="ProductName"  value="${product.productName}" required>
                </div>
                <div class="form-group">
                  <label for="product-description">产品描述：</label>
                  <input class="form-control" type="text" id="product-description" name="ProductDescription" value="${product.productDescription}" required>
                </div>
                <div class="form-group">
                  <label for="product-link">产品链接：</label>
                  <input class="form-control" type="url" id="product-link" name="ProductLink" value="${product.productLink}" required>
                </div>
                <div class="form-group">
                  <label for="product-weight">产品重量（克）：</label>
                  <input class="form-control" type="number" id="product-weight" name="ProductWeight" value="${product.productWeight}" required>
                </div>
                <div class="row">
                  <div class="dropdown col-sm-5" style="margin-bottom:10px">
                    <label for="product_brand">所属品牌：</label>
						<select class="form-control" form="product_modify_form" name="ProductBrandId">
						   <c:forEach items="${brandList}" var="brand">
							   <c:if test="${product.productBrand.brandId == brand.brandId}">
						       		<option value="${brand.brandId}" selected>${brand.brandEnglishName}</option>
	       					   </c:if>
						       <c:if test="${product.productBrand.brandId != brand.brandId}">
						       		 <option value="${brand.brandId}">${brand.brandEnglishName}}</option>
	       					   </c:if>
       					   </c:forEach>
						</select>
                  </div>
                  <div class="dropdown col-sm-5" style="margin-bottom:10px">
                    <label for="product_image">产品图片：</label>
						<select class="form-control" form="product_modify_form" name="ProductImageId">
						   <c:forEach items="${imageList}" var="productImage">
						    <option value="${productImage.imageId}">${productImage.imageDescription}</option>
						    </c:forEach>
						    <c:forEach items="${imageList}" var="image">
							   <c:if test="${product.productImage.imageId == image.imageId}">
						       		<option value="${image.imageId}" selected>${image.imageDescription}</option>
	       					   </c:if>
						       <c:if test="${product.productBrand.brandId != brand.brandId}">
						       		 <option value="${image.imageId}">${image.imageDescription}</option>
	       					   </c:if>
       					   </c:forEach>
						</select>
                  </div>
                </div>
                <div class="checkbox" style="padding-left:20px">
                  <label><input type="checkbox" checked="checked" id="product_is_activated" name="ProductIsActivated" value="True"/>是否显示在页面</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>