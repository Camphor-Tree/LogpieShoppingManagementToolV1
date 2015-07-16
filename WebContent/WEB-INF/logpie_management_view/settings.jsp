<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
     <c:if test="${admin.isSuperAdmin==true}">
        <div class="row"  style="padding:10px">
            <h3>系统设置</h3>
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
          <div id="section-order" class="tab-pane fade in active">
			<form role="form" action="<c:url value="/settings/set" />" method="POST" id="system_settings_form">
                <div class="form-group col-sm-4">
	                  <label for="order_proxy">系统默认代理 （新建订单时会默认选中该代理）</label>
	                  <select class="form-control" form="system_settings_form" name="SystemDefaultProxy">
							   <c:forEach items="${adminList}" var="admin">
							       <c:if test="${admin.adminId == SystemDefaultProxy}">
							       		<option value="${admin.adminId}" selected>${admin.adminName}</option>
	        					   </c:if>
							       <c:if test="${admin.adminId != SystemDefaultProxy}">
							       		<option value="${admin.adminId}">${admin.adminName}</option>
	        					   </c:if>
							    </c:forEach>
					  </select>
	            </div>
                <div class="form-group col-sm-4">
                  <label for="system_default_proxy_profit_percentage">系统默认代理分红百分比</label>
                  <input id="system_default_proxy_profit_percentage" class="form-control" type="text" name="SystemDefaultProxyProfitPercentage" value="${SystemDefaultProxyProfitPercentage}" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block col-sm-4">确定</button>
              </form>
            </div>
    </c:if>
    <c:if test="${admin.isSuperAdmin!=true}">
       暂未开放该功能
    </c:if>
    </jsp:body>
</tag:logpie_common_template>