<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
    	<c:if test="${order !=null}">
        <div class="row">
            <h3>订单信息</h3>
        </div>
  	    <div class="alert alert-success" role="alert">
           <strong>订单Id: </strong><c:out value="${order.orderId}"/><br />
           <strong>订单日期: </strong><c:out value="${order.orderDate}"/><br />
		   <strong>订单产品号: </strong><c:out value="${order.orderProduct.productId}"/><br />
		   <strong>订单产品名称: </strong><c:out value="${order.orderProduct.productName}"/><br />
		   <strong>订单产品数量: </strong><c:out value="${order.orderProductCount}"/><br />
		   <strong>订单重量: </strong><c:out value="${order.orderWeight}"/><br />
		   <strong>订单买家: </strong><c:out value="${order.orderBuyerName}"/><br />
		   <strong>订单代理人: </strong><c:out value="${order.orderProxy.adminName}"/><br />
		   <strong>订单代理分红百分比: </strong><c:out value="${order.orderProxyProfitPercentage}"/><br />
		   <strong>订单实际购买成本(美元): </strong><c:out value="${order.orderActualCost}"/><br />
		   <strong>订单当日汇率: </strong><c:out value="${order.orderCurrencyRate}"/><br />
		   <strong>订单所属包裹号: </strong><c:out value="${order.orderPackage.packageId}"/><br />
		   <strong>订单估计邮费(人民币): </strong><c:out value="${order.orderEstimatedShippingFee}"/><br />
		   <strong>订单国际邮费(人民币): </strong><c:out value="${order.orderActualShippingFee}"/><br />
		   <strong>订单国内邮费(人民币): </strong><c:out value="${order.orderDomesticShippingFee}"/><br />
		   <strong>订单客户已付国内邮费(人民币): </strong><c:out value="${order.orderCustomerPaidDomesticShippingFee}"/><br />
		   <strong>订单售价(人民币): </strong><c:out value="${order.orderSellingPrice}"/><br />
		   <strong>订单客户已付钱数(人民币): </strong><c:out value="${order.orderCustomerPaidMoney}"/><br />
		   <strong>订单最终利润(人民币): </strong><c:out value="${order.orderFinalProfit}"/><br />
		   <strong>订单公司已收汇款(人民币)：</strong><c:out value="${order.orderCompanyReceivedMoney}"/><br />
		   <strong>订单代理利润已结算: </strong><c:out value="${order.orderIsProfitPaid}"/><br />
		   <strong>订单已向用户发货: </strong><c:out value="${order.orderSentToUser}"/><br />
		   <strong>订单备注: </strong><c:out value="${order.orderNote}"/><br />
        </div>
        
        </c:if>
        <c:if test="${order ==null}">
  	        <div class="alert alert-danger" role="alert">
                    <strong>抱歉!查无此订单 请检查你url中的订单id是否有效</strong>
            </div>
        </c:if>

    </jsp:body>
</tag:logpie_common_template>