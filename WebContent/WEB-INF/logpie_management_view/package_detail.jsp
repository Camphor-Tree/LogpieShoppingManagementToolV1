<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
    	<c:if test="${logpiePackage !=null}">
        <div class="row">
            <h3>包裹信息</h3>
        </div>
  	    <div class="alert alert-success" role="alert">
           <strong>包裹Id: </strong><c:out value="${logpiePackage.packageId}"/><br />
           <strong>包裹运营商: </strong><c:out value="${logpiePackage.packageProxyName}"/><br />
		   <strong>包裹跟踪单号: </strong><c:out value="${logpiePackage.packageTrackingNumber}"/><br />
		   <strong>包裹收件人: </strong><c:out value="${logpiePackage.packageReceiver}"/><br />
		   <strong>包裹目的地: </strong><c:out value="${logpiePackage.packageDestination}"/><br />
		   <strong>包裹寄送日期: </strong><c:out value="${logpiePackage.packageDate}"/><br />
		   <strong>包裹重量: </strong><c:out value="${logpiePackage.packageWeight}"/><br />
		   <strong>包裹运费: </strong><c:out value="${logpiePackage.packgeShippingFee}"/><br />
		   <strong>包裹额外关税: </strong><c:out value="${logpiePackage.packageAdditionalCustomTaxFee}"/><br />
		   <strong>包裹额外保险: </strong><c:out value="${logpiePackage.packageAdditionalInsuranceFee}"/><br />
		   <strong>包裹已寄出: </strong><c:out value="${logpiePackage.packageIsShipped}"/><br />
		   <strong>包裹已到达: </strong><c:out value="${logpiePackage.packageIsDelivered}"/><br />
		   <strong>包裹备注: </strong><c:out value="${logpiePackage.packageNote}"/><br />
        </div>
        <h3>以下订单属于该包裹：</h3>
        <table class="table table-striped text-center" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
	        <tr class="info">
	        <th class="col-xs-1 col-md-1 text-center">No</th>
	        <th class="col-xs-2 col-md-2 text-center">订单日期</th>
	        <th class="col-xs-4 col-md-4 text-center">商品名称</th>
	        <th class="col-xs-1 col-md-1 text-center">数量</th>
	        <th class="col-xs-2 col-md-2 text-center">重量</th>
	        <th class="col-xs-2 col-md-2 text-center">购买者</th>
	        <th class="col-xs-2 col-md-2 text-center">代理者</th>
	        <!--<th>订单代理分红百分比</th>-->
	        <th class="col-xs-2 col-md-2 text-center">购买成本$</th>
	        <!--<th>订单当日汇率</th>-->
	        <th class="col-xs-1 col-md-1 text-center">包裹</th>
	        <!--<th>订单估计运费(人民币)</th>-->
	        <th class="col-xs-2 col-md-2 text-center">实际运费￥</th>
	        <th class="col-xs-2 col-md-2 text-center">订单售价￥</th>
	        <th class="col-xs-2 col-md-2 text-center">实收账款￥</th>
	        <th class="col-xs-2 col-md-2 text-center">最终利润￥</th>
	        <th class="col-xs-2 col-md-2 text-center">公司入账￥</th>
	        <th class="col-xs-1 col-md-1 text-center">利结</th>
	        <!--<th>订单备注</th>-->
	        <th class="col-xs-2 col-md-2 text-center">修改</th>
	        </tr>
	        <tbody>
	        <c:forEach items="${orderList}" var="order">
	        <tr >
	        <td><a href="./order?id=${order.orderId}">${order.orderId}</a></td>
	        <td>${fn:substring(order.orderDate,5,10)}</td>
	        <td>${order.orderProduct.productName}</td>
	        <td>${order.orderProductCount}</td>
	        <td>${order.orderWeight}</td>
	        <td>${order.orderBuyerName}</td>
	        <td>${order.orderProxy.adminName}</td>
	        <!--<td>${order.orderProxyProfitPercentage}</td>-->
	        <td>${order.orderActualCost}</td>
	        <!--<td>${order.orderCurrencyRate}</td>-->
	        <td <c:if test="${order.orderPackage.packageIsDelivered == true}">style="background-color:#dff0d8"</c:if>><a href="./package?id=${order.orderPackage.packageId}">${order.orderPackage.packageId}</a></td>
	        <!-- <td>${order.orderEstimatedShippingFee}</td>-->
	        <td>${order.orderActualShippingFee}</td>
	        <td>${order.orderSellingPrice}</td>
	        <td>${order.orderCustomerPaidMoney}</td>
	        <td>${order.orderFinalProfit}</td>
	        <td>${order.orderCompanyReceivedMoney}</td>
	        <td><c:if test="${order.orderIsProfitPaid == true}">是</c:if><c:if test="${order.orderIsProfitPaid == false}">否</c:if></td>
	        <!--<td>${order.orderNote}</td>-->
	        <td><a type="button" class="btn btn-warning" href="./order/edit?id=${order.orderId}">修改</a></td>
	        </tr>
	        </c:forEach>
	        </tbody>
        </table>
        <div class="row">
        <span>当前包裹总重：</span><span>${packageTotalWeight}</span> <c:if test="${admin.isSuperAdmin==true}"><a class="btn btn-info" href="./package/quickCalculateShippingFeeDistribution?id=${logpiePackage.packageId}">快捷计算分摊运费</a></c:if>
        </div>
        </c:if>
        <c:if test="${logpiePackage ==null}">
  	        <div class="alert alert-danger" role="alert">
                    <strong>抱歉!查无此包裹 请检查你url中的包裹id是否有效</strong>
            </div>
        </c:if>

    </jsp:body>
</tag:logpie_common_template>