<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改类别: ${category.categoryName}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/category/edit" />" method="POST" >
                <input name="CategoryId" value="${category.categoryId}" type="hidden">
                <div class="form-group">
                  <label for="category_name">分类名称</label>
                  <input class="form-control" type="text" id="category_name" name="CategoryName" value="${category.categoryName}" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>