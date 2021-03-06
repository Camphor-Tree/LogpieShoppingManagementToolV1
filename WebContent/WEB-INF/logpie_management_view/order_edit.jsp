<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
    	<c:if test="${order !=null}">
        <div class="row"  style="padding:20px">
            <h3>修改订单  订单Id:${order.orderId} 订单时间:${order.orderDate}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/order/edit" />" method="POST" >
                <input name="OrderId" value="${order.orderId}" type="hidden">
                <input name="OrderDate" value="${order.orderDate}" type="hidden">
                <div class="row">
	                <div class="form-group col-sm-4">
	                  <label for="order_buyer">订单购买者</label>
	                  <input class="form-control" type="text" id="order_buyer" name="OrderBuyerName" value="${order.orderBuyerName}" required>
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="order_client">关联用户档案(可空缺)</label>
	                  <select class="form-control" form="order_creation_form" name="OrderClientId">
	                        <option value=""> </option>
							<c:forEach items="${clientList}" var="client">
								  <c:if test="${order.orderClient.clientId == client.clientId}">
							       		<option value="${client.clientId}" selected>No.${client.clientId} ${client.clientRealName}/${client.clientWechatName}/${client.clientWeiboName}</option>
	        					   </c:if>
							       <c:if test="${order.orderClient.clientId != client.clientId}">
							       		 <option value="${client.clientId}">No.${client.clientId} ${client.clientRealName}/${client.clientWechatName}/${client.clientWeiboName} </option>
	        					   </c:if>
							</c:forEach>
					  </select>
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="order_proxy">订单代理人</label>
	                  <select class="form-control" form="order_creation_form" name="OrderProxyId" <c:if test="${admin.isSuperAdmin==false}">readonly</c:if>>
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
                </div>
                <div class="dropdown" style="margin-bottom:20px">
                  <label for="order_product">购买商品</label>
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
                  <input class="form-control" type="number" step="1" id="order_product_count" name="OrderProductCount" value="${order.orderProductCount}" required>
                  </div>
                  <div class="form-group col-sm-6">
                  <label for="order_weight">订单重量（克）</label>
                  <input class="form-control" type="number" step="0.01" min="0" id="order_weight" name="OrderWeight" value="${order.orderWeight}" required <c:if test="${admin.isSuperAdmin==false}">readonly</c:if>>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="order_actual_cost">实际购买成本(美元)</label>
                      <input class="form-control" type="number" step="0.01" id="order_actual_cost" name="OrderActualCost" value="${order.orderActualCost}" required <c:if test="${admin.isSuperAdmin==false}">readonly</c:if>>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_currency_rate">订单当日汇率</label>
                    <input class="form-control" type="number" step="0.01" id="order_currency_rate" name="OrderCurrencyRate" value="${order.orderCurrencyRate}" required <c:if test="${admin.isSuperAdmin==false}">readonly</c:if>>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-3">
                    <label for="estimated_shipping_fee">预计邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="estimated_shipping_fee" name="OrderEstimatedShippingFee" value="${order.orderEstimatedShippingFee}" required <c:if test="${admin.isSuperAdmin==false}">readonly</c:if>>
                  </div>
                  <div class="form-group col-sm-3">
                    <label for="actural_shipping_fee">国际邮费￥(可空缺)</label>
                    <input class="form-control" type="number" step="0.01" id="actural_shipping_fee" name="OrderActualShippingFee" value="${order.orderActualShippingFee}" required <c:if test="${admin.isSuperAdmin==false}">readonly</c:if>>
                  </div>
                  <div class="form-group col-sm-3">
                    <label for="domestic_shipping_fee">国内邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="domestic_shipping_fee" name="OrderDomesticShippingFee" value="${order.orderDomesticShippingFee}" required>
                  </div>
                  <div class="form-group col-sm-3">
                    <label for="customer_paid_domestic_shipping_fee">国内已付邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="customer_paid_domestic_shipping_fee"  name="OrderCustomerPaidDomesticShippingFee" value="${order.orderCustomerPaidDomesticShippingFee}" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-4">
                      <label for="selling_price">最终售价(人民币)</label>
                      <input class="form-control" type="number" step="0.01" id="selling_price" name="OrderSellingPrice" value="${order.orderSellingPrice}" required>
                  </div>
                  <div class="form-group col-sm-3">
                      <label for="copy_selling_price"> </label>
                      <button type="button" class="btn form-control btn-info" id="copy_selling_price" style="margin-top:5px">设置买家付款为售价+国内已付运费</button>
                  </div>
                  <div class="form-group col-sm-5">
                    <label for="order_buyer_paid_money">买家付款(人民币)</label>
                    <input class="form-control" type="number" step="0.01" id="order_buyer_paid_money" name="OrderCustomerPaidMoney" value="${order.orderCustomerPaidMoney}" required>
                  </div>
                </div>
                <!-- only super admin can modify the package -->
                <c:if test="${admin.isSuperAdmin==false}">
                <input type="hidden" name="OrderPackageId" value="${order.orderPackage.packageId}">
                </c:if>
                <c:if test="${admin.isSuperAdmin==true}">
                <div class="form-group">
                  <label for="order_package">所属包裹(可空缺)</label>
                  <select class="form-control" form="order_creation_form" name="OrderPackageId">
                        <option value=""> </option>
						<c:forEach items="${packageList}" var="logpiePackage">
							  <c:if test="${order.orderPackage.packageId == logpiePackage.packageId}">
						       		<option value="${logpiePackage.packageId}" selected>${logpiePackage.packageId}&nbsp; &nbsp; &nbsp;${fn:substring(logpiePackage.packageDate,0,10)}&nbsp; &nbsp; &nbsp;${logpiePackage.packageProxyName}&nbsp; &nbsp; &nbsp;${logpiePackage.packageReceiver} &nbsp; &nbsp; &nbsp;${logpiePackage.packageTrackingNumber}</option>
        					   </c:if>
						       <c:if test="${order.orderPackage.packageId != logpiePackage.packageId}">
						       		 <option value="${logpiePackage.packageId}">${logpiePackage.packageId}&nbsp; &nbsp; &nbsp;${fn:substring(logpiePackage.packageDate,0,10)}&nbsp; &nbsp; &nbsp;${logpiePackage.packageProxyName} &nbsp; &nbsp; &nbsp;${logpiePackage.packageReceiver} &nbsp; &nbsp; &nbsp;${logpiePackage.packageTrackingNumber} </option>
        					   </c:if>
						</c:forEach>
				  </select>
                </div>
                </c:if>
                <!-- only super admin can modify how much money company already received -->
                <c:if test="${admin.isSuperAdmin==true}">
                <div class="row">
                <div class="form-group col-sm-9">
                  <label for="order_company_received_money">公司已收汇款(人民币)</label>
                  <input class="form-control" type="number" step="0.01" id="order_company_received_money" name="OrderCompanyReceivedMoney" value="${order.orderCompanyReceivedMoney}" required>
                </div>
                <div class="form-group col-sm-3">
                      <label for="set_company_received_money"> </label>
                      <button type="button" class="btn form-control btn-info" id="set_company_received_money" style="margin-top:5px">将公司已收汇款设成 买家付款-国内运费</button>
                </div>
                </div>
                </c:if>
                <c:if test="${admin.isSuperAdmin==false}">
	                  <input class="form-control" type="hidden" id="order_company_received_money" name="OrderCompanyReceivedMoney" value="${order.orderCompanyReceivedMoney}" required>
                </c:if>
                <div class="form-group">
                  <label for="order_note">备注 (客户来源，规格颜色，美国跟踪号，国内运费，转寄地址，定金支付情况)</label>
                  <input class="form-control" type="text" id="order_note" name="OrderNote" value="${order.orderNote}">
                </div>
                <div class="row">
	                <!-- only super admin can modify whether the profit is paid -->
	                <c:if test="${admin.isSuperAdmin==true}">
	                <div class="col-sm-6">
	                  <label><input type="checkbox" id="profits_is_paid" name="OrderIsProfitPaid" value="True" <c:if test="${order.orderIsProfitPaid==true}">checked</c:if>/> 利润是否已和代理结算</label>
	                </div>
	                </c:if>
	                <c:if test="${admin.isSuperAdmin==false}">
						<input type="hidden" id="profits_is_paid" name="OrderIsProfitPaid" value="${order.orderIsProfitPaid}"/>
	                </c:if>
	                
	                <div class="col-sm-6">
	                  <label><input type="checkbox" id="sent_to_user" name="OrderSentToUser" value="True" <c:if test="${order.orderSentToUser==true}">checked</c:if>/> 已向用户发货</label>
	                </div>
                </div>
                <input type="hidden" name="RedirectUrl" value="${RedirectUrl}">
                <c:if test="${Anchor != null}">
                <input type="hidden" name="Anchor" value="${Anchor}">
                </c:if>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
                    
        </c:if>
        <c:if test="${order ==null}">
        	<br/><br/>
  	        <div class="alert alert-danger" role="alert">
                    <strong>抱歉!查无此订单 请检查你url中的订单id是否有效</strong>
            </div>
        </c:if>

    </jsp:body>

</tag:logpie_common_template>
<script type="text/javascript">
    $('#copy_selling_price').on('click', function (e) {
    	$('#order_buyer_paid_money').val(+$('#selling_price').val()+ +$('#customer_paid_domestic_shipping_fee').val());
   });
    $('#set_company_received_money').on('click', function (e) {
    	$('#order_company_received_money').val($('#order_buyer_paid_money').val()-$('#domestic_shipping_fee').val());
   });
    
    
</script>