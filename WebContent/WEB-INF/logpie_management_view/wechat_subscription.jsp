<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 微信订阅号管理</h3>
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
	        <div class="col-md-1">
	            <button type="button" class="btn btn-success" data-toggle="modal" data-target=".bs-example-modal-lg" style="font-size:16px;">新建回复规则</button>
			</div>
        </div>
      <div><h4>自动回复规则</h4></div>
      <table class="table table-striped text-center table-bordered" style="table-layout:fixed;vertical-align:middle;" >
        <thead>
        <tr class="info" style="font-size:15px;">
	        <th class="col-xs-2 col-md-2 text-center">编号</th>
	        <th class="col-xs-2 col-md-2 text-center">类型</th>
	        <th class="col-xs-4 col-md-4 text-center">关键字</th>
	        <th class="col-xs-6 col-md-6 text-center">回复消息</th>
	        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${orderList}" var="order">
        <tr class='clickable-row' data-href='./order?id=${order.orderId}' style="font-size:16px" height="36" >
        <td class="anchor" style="color:#428bca"><a name="a${order.orderId}"><span style="padding-top: 65px; margin-top: -65px;"></span></a>${order.orderId}</td>
        <td>${fn:substring(order.orderDate,5,10)}</td>
        <td <c:if test="${order.orderSentToUser == true}">style="background-color:#FFCCCC"</c:if>><c:if test="${order.orderClient == null}">${order.orderBuyerName}</c:if> <c:if test="${order.orderClient != null}"><a href="<c:url value="/client_management#a${order.orderClient.clientId}"/>">${order.orderBuyerName}</a></c:if></td>
        <td <c:if test="${order.orderPackage.packageIsDelivered == true}">style="background-color:#DFF0D8"</c:if>>${order.orderProduct.productName}</td>
        <td>${order.orderProductCount}</td>
        <td style="background-color:#FFCC99">${order.orderSellingPrice}</td>
        <td>${order.orderActualCost}</td>
        <td>${order.orderWeight}</td>
        <td>${order.orderProxy.adminName}</td>
        <!--<td>${order.orderProxyProfitPercentage}</td>-->
        <!--<td>${order.orderCurrencyRate}</td>-->
        <!-- <td>${order.orderEstimatedShippingFee}</td>-->
        <td>${order.orderActualShippingFee}</td>
        <td>${order.orderDomesticShippingFee}</td>
        <td>${order.orderCustomerPaidDomesticShippingFee}</td>
        <td>${order.orderFinalActualCost}</td>
        <td style="background-color:#FFCCCC">${order.orderCustomerPaidMoney}</td>
        <td>${order.orderFinalProfit}</td>
        <td>${order.orderCompanyReceivedMoney}</td>
        <td><c:if test="${order.orderIsProfitPaid == true}">是</c:if><c:if test="${order.orderIsProfitPaid == false}">否</c:if></td>
        <!--<td>${order.orderNote}</td>-->
        <td><a type="button" class="btn-small btn-info" href=<c:url value="/order/edit?id=${order.orderId}&ru=${CurrentUrl}&anchor=a${order.orderId}" />>修改</a></td>
        </tr>
        <tr style="font-size:13px;">
          <td colspan="4" class="text-left" style="color:#999999"><c:if test="${order.orderPackage == null}">暂无包裹信息</c:if><c:if test="${order.orderPackage != null}"><a href="<c:url value="/package?id=${order.orderPackage.packageId}"/>">包裹${order.orderPackage.packageId} ${order.orderPackage.packageReceiver} ${order.orderPackage.packageProxyName} ${fn:substring(order.orderPackage.packageDate,5,10)} ${order.orderPackage.packageTrackingNumber}</a></c:if></td>
          <td colspan="14" class="text-left" style="color:#999999">备注: ${order.orderNote}</td>
        </tr>
        </c:forEach>
        </tbody>
      </table>

    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#section-simple-text－auto-reply">简单文本回复</a></li>
            <li><a data-toggle="tab" href="#section-img－text－auto-reply">图文回复</a></li>
            <li><a data-toggle="tab" href="#section-auto-reply-type">自动回复类型</a></li>
          </ul>
          <div class="tab-content">
          <div id="section-simple-text－auto-reply" class="tab-pane fade in active" style="padding:20px">
              <h3>新建一个简单文本回复规则</h3>
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/order/create" />" method="POST" >
                <div class="row">
	                <div class="form-group col-sm-8">
	                  <label for="order_buyer">用户关键字</label>
	                  <input class="form-control" id="order_buyer" name="OrderBuyerName" required autofocus>
	                </div>
	                <div class="dropdown col-sm-4" style="margin-bottom:10px">
                    <label for="product_brand">回复类型：</label>
						<select class="form-control" form="product_creation_form" name="AutoReplyTypeId">
						   <c:forEach items="${autoReplyType}" var="brand">
						    <option value="${brand.brandId}">${brand.brandEnglishName}</option>
						    </c:forEach>
						</select>
                  </div>
				</div>
                <div class="row">
                  <div class="form-group col-sm-12">
                      <label for="selling_price">回复文本</label>
                      <input class="form-control" type="number" step="0.01"  id="selling_price" name="OrderSellingPrice" required>
                  </div>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    </jsp:body>
</tag:logpie_common_template>