<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 包裹管理</h3>
        </div>
        <c:if test="${action_message !=null}">
  	        <div class="alert alert-success" role="alert">
                    <strong>${action_message}</strong>
            </div>
		</c:if>
   		
      <table class="table table-striped text-center" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <tr class="info">
        <th class="col-xs-1 col-md-1 text-center">No.</th>
        <th class="col-xs-2 col-md-2 text-center">物流运营商</th>
        <th class="col-xs-4 col-md-4 text-center">包裹跟踪单号</th>
        <th class="col-xs-2 col-md-2 text-center">收件人</th>
        <th class="col-xs-5 col-md-5 text-center">地址</th>
        <th class="col-xs-2 col-md-2 text-center">包裹寄送时间</th>
        <th class="col-xs-2 col-md-2 text-center">包裹重量(克)</th>
        <th class="col-xs-2 col-md-2 text-center">运费￥</th>
        <th class="col-xs-1 col-md-1 text-center">关税</th>
        <th class="col-xs-1 col-md-1 text-center">保险</th>
        <th class="col-xs-1 col-md-1 text-center">寄出</th>
        <th class="col-xs-1 col-md-1 text-center">收到</th>
        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        <tbody>
        <c:forEach items="${packageList}" var="logpiePackage">
        <tr>
        <td><a href="./package?id=${logpiePackage.packageId}">${logpiePackage.packageId}</a></td>
        <td>${logpiePackage.packageProxyName}</td>
        <td>${logpiePackage.packageTrackingNumber}</td>
        <td>${logpiePackage.packageReceiver}</td>
        <td>${logpiePackage.packageDestination}</td>
        <td>${fn:substring(logpiePackage.packageDate,0,10)}</td>
        <td>${logpiePackage.packageWeight}</td>
        <td>${logpiePackage.packgeShippingFee}</td>
        <td>${logpiePackage.packageAdditionalCustomTaxFee}</td>
        <td>${logpiePackage.packageAdditionalInsuranceFee}</td>
        <td><c:if test="${logpiePackage.packageIsShipped == true}">是</c:if><c:if test="${logpiePackage.packageIsShipped == false}">否</c:if></td>
        <td><c:if test="${logpiePackage.packageIsDelivered == true}">是</c:if><c:if test="${logpiePackage.packageIsDelivered == false}">否</c:if></td>
        
        <td><a type="button" class="btn btn-warning" href="./package/edit?id=${logpiePackage.packageId}">修改</a></td>
        
        </tr>
        </c:forEach>
        </tbody>
        </table>
    </jsp:body>
</tag:logpie_common_template>