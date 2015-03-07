<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改个人资料</h3>
        </div>
        <div>姓名: ${admin.adminName}</div>
          <div id="section-admin-profile" class="tab-pane fade in active">
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/admin/edit" />" method="POST" >
                <input name="AdminId" value="${admin.adminId}" type="hidden">
                <input name="AdminName" value="${admin.adminName}" type="hidden">
                <div class="form-group">
                  <label for="admin_email">Email:(登录名)</label>
                  <input class="form-control" type="email" id="admin_email" name="AdminEmail" value="${admin.adminEmail}" required>
                </div>
                <div class="form-group">
                  <label for="admin_qq">QQ:</label>
                  <input class="form-control" type="text" id="admin_qq" name="AdminQQ" value="${admin.adminQQ}" required>
                </div>
                <div class="form-group">
                  <label for="admin_wechat">微信:</label>
                  <input class="form-control" type="text" id="admin_wechat" name="AdminWechat" value="${admin.adminWechat}" required>
                </div>
                <div class="form-group">
                  <label for="admin_phone">手机号:</label>
                  <input class="form-control" type="text" id="admin_phone" name="AdminPhone" value="${admin.adminPhone}" required>
                </div>
                <div class="form-group">
                  <label for="admin_id">身份证件号:</label>
                  <input class="form-control" type="text" id="admin_id" name="AdminIdentityNumber" value="${admin.adminIdentityNumber}" required>
                </div>
                <div class="form-group">
                  <label for="admin_password">管理员密码</label>
                  <input class="form-control" type="password" id="admin_password" name="AdminPassword" value="${admin.adminPassword}" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确认修改</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>