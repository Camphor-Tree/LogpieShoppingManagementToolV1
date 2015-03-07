<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 商品管理</h3>
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
        <th class="col-xs-1 col-md-1 text-center">No.</th>
        <th class="col-xs-2 col-md-2 text-center">商品名称</th>
        <th class="col-xs-2 col-md-2 text-center">商品品牌</th>
        <th class="col-xs-2 col-md-4 text-center">商品描述</th>
        <th class="col-xs-2 col-md-2 text-center">商品链接</th>
        <th class="col-xs-2 col-md-5 text-center">商品图片</th>
        <th class="col-xs-2 col-md-2 text-center">商品重量</th>
        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        <tbody>
        <c:forEach items="${productList}" var="product">
        <tr>
        <td>${product.productId}</td>
        <td>${product.productName}</td>
        <td>${product.productBrand.brandEnglishName}</td>
        <td>${product.productDescription}</td>
        <td><a href="${product.productLink}">${product.productLink}</a></td>
        <td><a href="${product.productImage.imageUrl}">${product.productImage.imageUrl}</a></td>
        <td>${product.productWeight}</td>
        <td><a type="button" class="btn btn-warning" href="./product/edit?id=${product.productId}">修改</a></td>
        </tr>
        </c:forEach>
        </tbody>
        </table>
    </jsp:body>
</tag:logpie_common_template>