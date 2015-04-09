<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 系统备份管理</h3>
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
   	  <a class="btn btn-info" href="./database_backup" style="margin-bottom:10px;">手动备份当前数据库</a>
      <h4>备份历史记录:</h4>
      <table class="table table-striped text-center" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <tr class="info">
        <th class="col-xs-6 col-md-6 text-center">备份记录</th>
        </tr>
        <tbody>
        <c:forEach items="${BackupHistory}" var="history">
        <tr>
        <td>${history}</td>
        </tr>
        </c:forEach>
        </tbody>
        </table>
    </jsp:body>
</tag:logpie_common_template>