<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>欢迎来到 订单管理</h3>
        </div>
        <c:if test="${action_message !=null}">
  	        <div class="alert alert-success" role="alert">
                    <strong>${action_message}</strong>
            </div>
		</c:if>
        <div class="row">
            <p><a class="btn btn-xs btn-success" href="./category/create">Create</a></p>
        <table class="table table-striped table-bordered table-hover">
        <tr>
        <th>订单序号</th>
        <th>订单日期</th>
        <th>商品名称</th>
        <th>商品数量</th>
        <th>商品重量</th>
        <th>订单购买者</th>
        <th>订单代理者</th>
        <th>订单代理分红百分比</th>
        <th>订单购买最终价(美元)</th>
        <th>订单当日汇率</th>
        <th>订单包裹序号</th>
        <th>订单估计运费(人民币)</th>
        <th>订单实际运费(人民币)</th>
        <th>订单售价</th>
        <th>实收账款</th>
        <th>订单最终利润</th>
        <th>订单利润已结算</th>
        <th>订单备注</th>
        </tr>
        <c:forEach items="${orderList}" var="order">
        <tr>
        <td>${order.orderId}</td>
        <td>${order.orderDate}</td>
        <td>${order.orderProduct.productName}</td>
        <td>${order.orderProductCount}</td>
        <td>${order.orderProduct.productWeight}</td>
        <td>${order.orderBuyerName}</td>
        <td>${order.orderProxy.adminName}</td>
        <td>${order.orderProxyProfitPercentage}</td>
        <td>${order.orderActualCost}</td>
        <td>${order.orderCurrencyRate}</td>
        <td><a href="./package?id=${order.orderPackage.packageId}">${order.orderPackage.packageId}</a></td>
        <td>${order.orderEstimatedShippingFee}</td>
        <td>${order.orderActualShippingFee}</td>
        <td>${order.orderSellingPrice}</td>
        <td>${order.orderCustomerPaidMoney}</td>
        <td>${order.orderFinalProfit}</td>
        <td>${order.orderIsProfitPaid}</td>
        <td>${order.orderNote}</td>
        </tr>
        </c:forEach>
        
        
        <tbody>
        </tbody>
        </table>
        </div><!-- /row -->
    </jsp:body>
</tag:logpie_common_template>

