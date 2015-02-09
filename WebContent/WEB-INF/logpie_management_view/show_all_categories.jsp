<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>所有类别:</h3>
        </div>
		<c:forEach  items="${categoryList}" var="category">
  	    <div class="alert alert-success" role="alert">
                    <strong><c:out value="${category.categoryName}"/></strong>
        </div>
        </c:forEach>
    </jsp:body>
</tag:logpie_common_template>