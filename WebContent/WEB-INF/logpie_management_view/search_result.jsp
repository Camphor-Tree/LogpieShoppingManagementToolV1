<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
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
    	<div class="row">
            <h3>站内搜索：<span class="text-danger">${SearchString}</span> 共有${SearchResultsCount}个结果:</h3>
            <h5>本次搜索共用时：${SearchPerformance}毫秒</h5>
        </div>
        <hr></hr>
        <h4>共 ${OrdersCount} 个 <span class="text-info"><b>订单</b></span> 满足搜索条件</h4>
		<tag:order_list></tag:order_list>
        <hr></hr>
        <h4>共 ${PackagesCount} 个 <span class="text-info"><b>包裹</b></span>  满足搜索条件</h4>
              <table class="table table-striped text-center" style="table-layout:fixed; font-size:18px;">
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
        <c:if test="${admin.isSuperAdmin==true}">
        	<th class="col-xs-2 col-md-2 text-center">修改</th>
        </c:if>
        </tr>
        <tbody>
        <c:forEach items="${packageList}" var="logpiePackage">
        <tr class='clickable-row' data-href='./package?id=${logpiePackage.packageId}'>
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
        <c:if test="${admin.isSuperAdmin==true}">
        <td><a type="button" class="btn btn-warning" href="./package/edit?id=${logpiePackage.packageId}">修改</a></td>
        </c:if>
        </tr>
        </c:forEach>
        </tbody>
        </table>
        <h5>点击包裹可以进入包裹详情。</h5>
        
        <hr></hr>
        <h4>共 ${ClientsCount} 个 <span class="text-info"><b>用户档案</b></span> 满足搜索条件</h4>
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
        <td>${client.clientId}</td>
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