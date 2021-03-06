<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 分类管理</h3>
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
        <th class="col-xs-1 col-md-1 text-center">分类Id</th>
        <th class="col-xs-2 col-md-2 text-center">分类名称</th>
        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        <tbody>
        <c:forEach items="${categoryList}" var="category">
        <tr>
        <td>${category.categoryId}</td>
        <td>${category.categoryName}</td>
        <td><a type="button" class="btn btn-warning" href="./category/edit?id=${category.categoryId}">修改</a></td>
        
        </tr>
        </c:forEach>
        </tbody>
        </table>
    </jsp:body>
</tag:logpie_common_template>