<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 品牌管理</h3>
        </div>
        <c:if test="${action_message_success !=null}">
  	        <div class="alert alert-success" role="alert">
                    <strong>${action_message_success}</strong>
            </div>
		</c:if>
		<c:if test="${action_message_fail !=null}">
  	        <div class="alert alert-danger" role="alert">
                    <strong>${action_message_fail}</strong>
            </div>
		</c:if>
      <table class="table table-striped text-center" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <tr class="info">
        <th class="col-xs-1 col-md-1 text-center">品牌Id</th>
        <th class="col-xs-2 col-md-2 text-center">品牌英文名</th>
        <th class="col-xs-7 col-md-2 text-center">品牌中文名</th>
        <th class="col-xs-2 col-md-2 text-center">品牌图片</th>
        <th class="col-xs-2 col-md-2 text-center">品牌尺寸图片</th>
        <th class="col-xs-2 col-md-2 text-center">品牌类别</th>
        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        <tbody>
        <c:forEach items="${brandList}" var="brand">
        <tr>
        <td>${brand.brandId}</td>
        <td>${brand.brandEnglishName}</td>
        <td>${brand.brandChineseName}</td>
        <td><a href="${brand.brandImage.imageUrl}">${brand.brandImage.imageUrl}</a></td>
        <td><a href="${brand.brandSizeChartImage.imageUrl}">${brand.brandSizeChartImage.imageUrl}</a></td>
        <td>${brand.brandCategory.categoryName}</td>
        <td><a type="button" class="btn btn-warning" href="./brand/edit?id=${brand.brandId}">修改</a></td>
        
        </tr>
        </c:forEach>
        </tbody>
        </table>
    </jsp:body>
</tag:logpie_common_template>