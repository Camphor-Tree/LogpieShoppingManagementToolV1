<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改图片： ${image.imageDescription}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
              <form role="form" style="padding:20px" action="<c:url value="/image/edit" />" method="POST">
               	<input type="hidden" name="ImageId" value="${image.imageId}">
                <div class="form-group">
                  <label for="img_url">图片地址：</label>
                  <input class="form-control" type="url" id="img_url" name="ImageUrl" placeholder="阿里云OSS 照片url地址" value="${image.imageUrl}" required autofocus>
                </div>
                <div class="form-group">
                  <label for="img_description">图片名称：</label>
                  <input class="form-control" type="text" id="img_description" name="ImageDescription" placeholder="图片描述(key)" value="${image.imageDescription}" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>