<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script src="<c:url value="/resources/js/orderQuickEdit_beta_1.0.0.js" />"></script>
<table class="table table-striped text-center table-bordered" style="table-layout:fixed;vertical-align:middle;" >
  <thead>
  <tr class="info" style="font-size:15px;">
   <th class="col-xs-2 col-md-2 text-center">订单号</th>
   <th class="col-xs-2 col-md-2 text-center">订单日期</th>
   <th class="col-xs-4 col-md-4 text-center">购买者</th>
   <th class="col-xs-6 col-md-6 text-center">商品名称</th>
   <th class="col-xs-1 col-md-1 text-center">数量</th>
   <th class="col-xs-2 col-md-2 text-center">售价￥</th>
   <th class="col-xs-2 col-md-2 text-center">购买成本$</th>
   <th class="col-xs-2 col-md-2 text-center">重量</th>
   <th class="col-xs-2 col-md-2 text-center">代理者</th>
   <th class="col-xs-2 col-md-2 text-center">国际邮费￥</th>
   <th class="col-xs-2 col-md-2 text-center">国内邮费￥</th>
   <th class="col-xs-2 col-md-2 text-center">已付国内邮费￥</th>
   <th class="col-xs-2 col-md-2 text-center">总成本￥</th>
   <th class="col-xs-2 col-md-2 text-center">实收账款￥</th>
   <th class="col-xs-2 col-md-2 text-center">最终利润￥</th>
   <th class="col-xs-2 col-md-2 text-center">公司入账￥</th>
   <th class="col-xs-2 col-md-2 text-center">利润已结算</th>
   <th class="col-xs-2 col-md-2 text-center">修改</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${orderList}" var="order">
  <tr class='clickable-row' data-href='./order?id=${order.orderId}' style="font-size:16px" height="36" >
  <td class="anchor" style="color:#428bca"><a name="a${order.orderId}"><span style="padding-top: 65px; margin-top: -65px;"></span></a>${order.orderId}</td>
  <td>${fn:substring(order.orderDate,5,10)}</td>
  <td id="OrderBuyerName_${order.orderId}" <c:if test="${order.orderSentToUser == true}">style="background-color:#FFCCCC"</c:if>><c:if test="${order.orderClient == null}">${order.orderBuyerName}</c:if> <c:if test="${order.orderClient != null}"><a href="<c:url value="/client_management#a${order.orderClient.clientId}"/>">${order.orderBuyerName}</a></c:if></td>
  <td id="OrderProductName_${order.orderId}" <c:if test="${order.orderPackage.packageIsDelivered == true}">style="background-color:#DFF0D8"</c:if>>${order.orderProduct.productName}</td>
  <td id="OrderProductCount_${order.orderId}">${order.orderProductCount}</td>
  <td id="OrderSellingPrice_${order.orderId}" style="background-color:#FFCC99">${order.orderSellingPrice}</td>
  <td id="OrderActualCost_${order.orderId}">${order.orderActualCost}</td>
  <td id="OrderWeight_${order.orderId}" >${order.orderWeight}</td>
  <td>${order.orderProxy.adminName}</td>
  <!--<td>${order.orderProxyProfitPercentage}</td>-->
  <!--<td>${order.orderCurrencyRate}</td>-->
  <!-- <td>${order.orderEstimatedShippingFee}</td>-->
  <td id="OrderActualShippingFee_${order.orderId}">${order.orderActualShippingFee}</td>
  <td id="OrderDomesticShippingFee_${order.orderId}">${order.orderDomesticShippingFee}</td>
  <td id="OrderCustomerPaidDomesticShippingFee_${order.orderId}">${order.orderCustomerPaidDomesticShippingFee}</td>
  <td id="OrderFinalActualCost_${order.orderId}">${order.orderFinalActualCost}</td>
  <td id="OrderCustomerPaidMoney_${order.orderId}" style="background-color:#FFCCCC">${order.orderCustomerPaidMoney}</td>
  <td id="OrderFinalProfit_${order.orderId}">${order.orderFinalProfit}</td>
  <td id="OrderCompanyReceivedMoney_${order.orderId}">${order.orderCompanyReceivedMoney}</td>
  <td id="OrderIsProfitPaid_${order.orderId}"><c:if test="${order.orderIsProfitPaid == true}">是</c:if><c:if test="${order.orderIsProfitPaid == false}">否</c:if></td>
  <td><a type="button" class="btn-small btn-info" href=<c:url value="/order/edit?id=${order.orderId}&ru=${CurrentUrl}&anchor=a${order.orderId}" />>修改</a></td>
  </tr>
  <tr style="font-size:13px;">
    <td colspan="4" class="text-left" id="OrderPackage_${order.orderId}" style="color:#999999">
    <c:if test="${order.orderPackage == null}">
    <input id="quick_set_package_input_${order.orderId}" type="number" class="col-xs-2" style="padding-left:0px;padding-right:0px" placeholder="输入包裹号"></input><button id="quick_set_package_submit_${order.orderId}" type="submit" class="col-xs-1 btn-small has-spinner" style="padding-left:0px;padding-right:0px;color:green;border: none;background:#e9e9e9;" onclick="quickSetPackage(${order.orderId},'<c:url value="/order/quick_edit/set_package" />','<c:url value="/order/query" />');"><span class="glyphicon glyphicon-ok spinner" aria-hidden="true"></span></button>
    <div class="col-xs-8" style="padding: 2px; margin-left:20px; vertical-align: middle;" id="quick_set_package_result_${order.orderId}"></div>
    </c:if>
    <c:if test="${order.orderPackage != null}">
    <a href="<c:url value="/package?id=${order.orderPackage.packageId}"/>">包裹${order.orderPackage.packageId} ${order.orderPackage.packageReceiver} ${order.orderPackage.packageProxyName} ${fn:substring(order.orderPackage.packageDate,5,10)} ${order.orderPackage.packageTrackingNumber}</a>
    </c:if>
    </td>
    <td id="OrderNote_${order.orderId}" colspan="9" class="text-left" style="color:#999999">备注: ${order.orderNote}</td>
    <td colspan="3" class="text-left"><input id="quick_receive_money_input_${order.orderId}" type="number" class="col-xs-8" style="padding-left:0px;padding-right:0px" placeholder="用户已付国内邮费"></input><button id="quick_receive_money_submit_${order.orderId}" type="submit" class="btn-small btn-success has-spinner col-xs-4" style="background-color:#bcf89c;border-color:#bcf89c;color:#5c5c60;padding-left:0px;padding-right:0px" onclick="quickReceiveMoney(${order.orderId},'<c:url value="/order/quick_edit/receive_money" />','<c:url value="/order/query" />');">收款<span class="spinner"></span></button></td>
    <td colspan="2" class="text-left" id="quick_edit_result_${order.orderId}"></td>
  </tr>
  </c:forEach>
  </tbody>
</table>