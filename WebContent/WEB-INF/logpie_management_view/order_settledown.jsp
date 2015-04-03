<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
         <c:if test="${admin != null}">
        <div class="row" style="margin-bottom:10px">
        	<br>
        	<h3>欢迎来到 订单快捷结算 当前结算代理为:${admin.adminName}</h3>
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
        </div>  		
      <table class="table table-striped text-center table-bordered" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <tr class="info">
        	<th class="col-xs-1 col-md-1 text-center"></th>
	        <th class="col-xs-1 col-md-1 text-center">No</th>
	        <th class="col-xs-2 col-md-2 text-center">订单日期</th>
	        <th class="col-xs-2 col-md-2 text-center">购买者</th>
	        <th class="col-xs-4 col-md-4 text-center">商品名称</th>
	        <th class="col-xs-1 col-md-1 text-center">数量</th>
	        <th class="col-xs-2 col-md-2 text-center">购买成本$</th>
	        <th class="col-xs-2 col-md-2 text-center">重量</th>
	        <th class="col-xs-2 col-md-2 text-center">国际邮费￥</th>
	        <th class="col-xs-2 col-md-2 text-center">总成本￥</th>
	        <th class="col-xs-2 col-md-2 text-center">售价￥</th>
	        <th class="col-xs-2 col-md-2 text-center">实收账款￥</th>
	        <th class="col-xs-2 col-md-2 text-center">最终利润￥</th>
	        <th class="col-xs-2 col-md-2 text-center">公司入账￥</th>
	        <th class="col-xs-1 col-md-1 text-center">利结</th>
	        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        <form role="form" id="order_settledown_form" action="<c:url value="/order/settledown" />" method="POST" >
        <input type="hidden" name="adminId" value="${admin.adminId}"/>
        <tbody>
	        <c:forEach items="${orderList}" var="order">
	        <tr>
	        <td><input type="checkbox" name="SettleDownOrders" value="${order.orderId}"/></td>
	        <td>${order.orderId}</td>
	        <td>${fn:substring(order.orderDate,5,10)}</td>
	        <td>${order.orderBuyerName}</td>
	        <td <c:if test="${order.orderPackage.packageIsDelivered == true}">style="background-color:#dff0d8"</c:if>>${order.orderProduct.productName}</td>
	        <td>${order.orderProductCount}</td>
	        <td>${order.orderActualCost}</td>
	        <td>${order.orderWeight}</td>
	        <!--<td>${order.orderProxyProfitPercentage}</td>-->
	        <!--<td>${order.orderCurrencyRate}</td>-->
	        <!-- <td>${order.orderEstimatedShippingFee}</td>-->
	        <td>${order.orderActualShippingFee}</td>
	        <td>${order.orderFinalActualCost}</td>
	        <td style="background-color:#FFCC99">${order.orderSellingPrice}</td>
	        <td style="background-color:#FFCCCC">${order.orderCustomerPaidMoney}</td>
	        <td>${order.orderFinalProfit}</td>
	        <td>${order.orderCompanyReceivedMoney}</td>
	        <td><c:if test="${order.orderIsProfitPaid == true}">是</c:if><c:if test="${order.orderIsProfitPaid == false}">否</c:if></td>
	        <!--<td>${order.orderNote}</td>-->
	        <td><a type="button" class="btn btn-info" href=<c:url value="/order/edit?id=${order.orderId}&ru=${CurrentUrl}" />>修改</a></td>
	        </tr>
	        <tr>
	          <td colspan="4" class="text-left"><c:if test="${order.orderPackage == null}">暂无包裹信息</c:if><c:if test="${order.orderPackage != null}"><a href="./package?id=${order.orderPackage.packageId}">包裹${order.orderPackage.packageId} ${order.orderPackage.packageProxyName} ${fn:substring(order.orderPackage.packageDate,5,10)} ${order.orderPackage.packageTrackingNumber}</a></c:if></td>
	          <td colspan="12" class="text-left">备注: ${order.orderNote}</td>
	        </tr>
	        </c:forEach>
        </tbody>
      </table>
       <button type="submit" class="btn btn-primary btn-block">快捷清算</button>
       <p>点击快捷清算会将选中的订单的公司收入设成 买家付款（实收账款），并将利润已结算设成 “是”</p>
      </form>
      </c:if>
      <c:if test="${admin == null}">
         	</br></br>
  	        <div class="alert alert-danger" role="alert">
                    <strong>抱歉!查无此代理 请检查你url中的订单id是否有效</strong>
            </div>
      </c:if>
    </jsp:body>
</tag:logpie_common_template>