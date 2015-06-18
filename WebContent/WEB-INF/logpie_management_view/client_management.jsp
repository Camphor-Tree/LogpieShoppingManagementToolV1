<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 用户档案管理</h3>
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
      <table id="sortableTable" class="table table-striped text-center tablesorter" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <thead>
        <tr class="info">
        <th class="col-xs-1 col-md-1 text-center">用户Id</th>
        <th class="col-xs-2 col-md-2 text-center">用户真实姓名</th>
        <th class="col-xs-2 col-md-2 text-center">用户微信名</th>
        <th class="col-xs-2 col-md-2 text-center">用户微信号</th>
        <th class="col-xs-2 col-md-2 text-center">用户微博名</th>
        <th class="col-xs-2 col-md-2 text-center">用户淘宝名</th>
        <th class="col-xs-7 col-md-7 text-center">用户地址</th>
        <th class="col-xs-2 col-md-2 text-center">用户邮编</th>
        <th class="col-xs-3 col-md-3 text-center">用户手机</th>
        <th class="col-xs-2 col-md-2 text-center">用户备注</th>
        <th class="col-xs-2 col-md-2 text-center">用户加入时间</th>
        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${clientList}" var="client">
        <tr>
         <td class="anchor"><a name="a${client.clientId}"><span style="padding-top: 60px; margin-top: -60px;">${client.clientId}</span></a></td>
        <td>${client.clientRealName}</td>
        <td>${client.clientWechatName}</td>
        <td>${client.clientWechatNumber}</td>
        <td>${client.clientWeiboName}</td>
        <td>${client.clientTaobaoName}</td>
        <td>${client.clientAddress}</td>
        <td>${client.clientPostalCode}</td>
        <td>${client.clientPhone}</td>
        <td>${client.clientNote}</td>
        <td>${fn:substring(client.clientJoinTime,0,10)}</td>
        <td><a type="button" class="btn btn-warning" href="./client/edit?id=${client.clientId}">修改</a></td>
        </tr>
        </c:forEach>
        </tbody>
        </table>

    </jsp:body>
</tag:logpie_common_template>
<script src="<c:url value="/resources/js/tablesorter.min.js" />"></script>
<script type="text/javascript">
        $("#sortableTable").tablesorter();
</script>

