<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 结算历史</h3>
        </div>
      <h4>结算历史记录:</h4>
      <table class="table table-striped text-center" style="table-layout:fixed; font-size:15px;">
        <tr class="info">
           <th class="col-xs-1 col-md-1 text-center">结算时间</th>
           <th class="col-xs-1 col-md-1 text-center">结算代理</th>
           <th class="col-xs-6 col-md-6 text-center">结算信息</th>
        </tr>
        <tbody>
        <c:forEach items="${SettleDownRecords}" var="record">
        <tr>
        <td>${record.settleDownRecordDate}</td>
        <td>${record.settleDownRecordAdmin.adminName}</td>
        <td>${record.settleDownRecordInfoReadable}</td>
        </tr>
        </c:forEach>
        </tbody>
        </table>
        <div>历史总计：  代理已付公司:${totalProxyPaidCompanyMoney}
             公司总利润:${totalCompanyProfit}
            代理总利润:${totalProxyProfit}
        </div>
    </jsp:body>
</tag:logpie_common_template>