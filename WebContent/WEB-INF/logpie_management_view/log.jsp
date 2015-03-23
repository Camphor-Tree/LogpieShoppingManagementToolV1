<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>Logpie系统改动日志</h3>
        </div>
   		
      <table class="table table-striped text-center" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <tr class="info">
        <th class="col-xs-1 col-md-1 text-center">日志Id</th>
        <th class="col-xs-2 col-md-2 text-center">日志改动管理员</th>
        <th class="col-xs-2 col-md-2 text-center">日志生成时间</th>
        <th class="col-xs-4 col-md-4 text-center">日志SQL</th>
        <th class="col-xs-4 col-md-4 text-center">日志注解</th>
        </tr>
        <tbody>
        <c:forEach items="${dbLogList}" var="dbLog">
        <tr>
        <td>${dbLog.dbLogId}</td>
        <td>${dbLog.dbLogAdmin.adminName}</td>
        <td>${dbLog.dbLogTime}</td>
        <td>${dbLog.dbLogSQL}</td>
        <td>${dbLog.dbLogComment}</td>
        </tr>
        </c:forEach>
        </tbody>
        </table>
    </jsp:body>
</tag:logpie_common_template>