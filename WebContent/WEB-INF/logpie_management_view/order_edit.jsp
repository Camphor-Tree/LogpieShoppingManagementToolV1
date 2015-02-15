<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改订单  订单Id:${order.orderId} 订单时间:${order.orderDate}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/order/edit" />" method="POST" >
                <input name="OrderId" value="${order.orderId}" type="hidden">
                <input name="OrderDate" value="${order.orderDate}" type="hidden">
                <div class="form-group">
                  <label for="order_buyer">订单购买者：</label>
                  <input class="form-control" type="text" id="order_buyer" name="OrderBuyerName" value="${order.orderBuyerName}" required>
                </div>
                <div class="form-group">
                  <label for="order_proxy">订单代理人：</label>
                  <select class="form-control" form="order_creation_form" name="OrderProxyId">
						   <c:forEach items="${adminList}" var="admin">
						       <c:if test="${order.orderProxy.adminId == admin.adminId}">
						       		<option value="${admin.adminId}" selected>${admin.adminName}</option>
        					   </c:if>
						       <c:if test="${order.orderProxy.adminId!= admin.adminId}">
						       		<option value="${admin.adminId}">${admin.adminName}</option>
        					   </c:if>
						    </c:forEach>
				  </select>
                </div>
                <div class="dropdown" style="margin-bottom:20px">
                  <label for="order_product">购买商品：</label>
                  <select class="form-control" form="order_creation_form" name="OrderProductId">
						<c:forEach items="${productList}" var="product">
							   <c:if test="${order.orderProduct.productId == product.productId}">
						       		<option value="${product.productId}" selected>${product.productName}</option>
        					   </c:if>
						       <c:if test="${order.orderProduct.productId != product.productId}">
						       		 <option value="${product.productId}">${product.productName}</option>
        					   </c:if>
						</c:forEach>
				  </select>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                  <label for="order_product_count">购买数量</label>
                  <input class="form-control" type="number" id="order_product_count" name="OrderProductCount" value="${order.orderProductCount}" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_proxy_profit_percentage">代理分红百分比</label>
                    <input class="form-control" type="number" id="order_proxy_profit_percentage" name="OrderProxyProfitPercentage" value="${order.orderProxyProfitPercentage}" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="order_actual_cost">实际购买成本(美元)(可空缺)：</label>
                      <input class="form-control" type="number" id="order_actual_cost" name="OrderActualCost" value="${order.orderActualCost}">
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_currency_rate">订单当日汇率：</label>
                    <input class="form-control" type="number" id="order_currency_rate" name="OrderCurrencyRate" value="${order.orderCurrencyRate}" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                    <label for="estimated_shipping_fee">预计邮费：</label>
                    <input class="form-control" type="number" id="estimated_shipping_fee" name="OrderEstimatedShippingFee" value="${order.orderEstimatedShippingFee}" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="actural_shipping_fee">实际邮费(人民币)(可空缺)：</label>
                    <input class="form-control" type="number" id="actural_shipping_fee" name="OrderActualShippingFee" value="${order.orderActualShippingFee}">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="selling_price">最终售价(人民币)：</label>
                      <input class="form-control" type="number" id="selling_price" name="OrderSellingPrice" value="${order.orderSellingPrice}" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_buyer_paid_money">买家实际付款(人民币)(可空缺)</label>
                    <input class="form-control" type="number" id="order_buyer_paid_money" name="OrderCustomerPaidMoney" value="${order.orderCustomerPaidMoney}">
                  </div>
                </div>
                
                <div class="form-group">
                  <label for="order_package">所属包裹(可空缺)：</label>
                  <select class="form-control" form="order_creation_form" name="OrderPackageId">
                        <option value=""> </option>
						<c:forEach items="${packageList}" var="logpiePackage">
							  <c:if test="${order.orderPackage.packageId == logpiePackage.packageId}">
						       		<option value="${logpiePackage.packageId}" selected>id:${logpiePackage.packageId} date:${logpiePackage.packageDate}</option>
        					   </c:if>
						       <c:if test="${order.orderPackage.packageId != logpiePackage.packageId}">
						       		 <option value="${logpiePackage.packageId}">id:${logpiePackage.packageId} date:${logpiePackage.packageDate}</option>
        					   </c:if>
						    
						</c:forEach>
				  </select>
                </div>
                <div class="form-group">
                  <label for="order_company_received_money">公司已收汇款(可空缺)：</label>
                  <input class="form-control" type="number" id="order_company_received_money" name="OrderCompanyReceivedMoney" value="${order.orderCompanyReceivedMoney}">
                </div>
                <div class="form-group">
                  <label for="order_note">备注(可空缺)：</label>
                  <input class="form-control" type="text" id="order_note" name="OrderNote" value="${order.orderNote}">
                </div>
                <div class="checkbox" style="padding-left:20px">
                  <label><input type="checkbox" id="profits_is_paid" name="OrderIsProfitPaid" value="True" <c:if test="${order.orderIsProfitPaid==true}">checked</c:if>/>利润是否已和代理结算</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>