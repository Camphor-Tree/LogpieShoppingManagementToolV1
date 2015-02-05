<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>创建新的分类</h3>
        </div>
        <form action="/LogpieShopping/category/create" method="post">
        <input type="text" id="newCategoryName" name="CategoryName" class="form-control" placeholder="输入新的分类名称" required autofocus>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登 陆</button>
        </form>
    </jsp:body>
</tag:logpie_common_template>

