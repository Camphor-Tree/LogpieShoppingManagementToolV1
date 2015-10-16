<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 订单管理</h3>
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
	            <button type="button" class="btn btn-success" data-toggle="modal" data-target=".bs-example-modal-lg" style="font-size:16px;">新建基础数据</button>
			</div>
			<c:if test="${adminList != null}">
			<div class="dropdown col-md-1" style="margin-left:20px">
	  			<button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true" style="margin-left:10px;font-size:16px;">按代理筛选<span class="caret"></span></button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				  	<c:forEach items="${adminList}" var="admin">
				  		<li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="/order_management?${filterLogic.removeFilterQueryUrl}&admin=${admin.adminId}" />">${admin.adminName}</a></li>
					</c:forEach>
				  </ul>
			</div>
			</c:if>
			<div class="dropdown col-md-1" style="margin-left:10px">
	  			<button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-expanded="true" style="font-size:16px;">按购买者筛选<span class="caret"></span></button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2">
				  	<c:forEach items="${orderBuyersList}" var="buyer">
				  		<li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="/order_management?${filterLogic.removeFilterQueryUrl}&buyer=${buyer}" />">${buyer}</a></li>
					</c:forEach>
				  </ul>
			</div>
			<div class="dropdown col-md-1" style="margin-left:17px">
	  			<button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenu3" data-toggle="dropdown" aria-expanded="true" style="font-size:16px;">按包裹筛选<span class="caret"></span></button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu3">
				  	<c:forEach items="${packageList}" var="orderPackage">
				  		<li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="/order_management?${filterLogic.removeFilterQueryUrl}&packageId=${orderPackage.packageId}" />">${orderPackage.packageId}-${orderPackage.packageProxyName}-${fn:substring(orderPackage.packageDate,5,10)}</a></li>
					</c:forEach>
				  </ul>
			</div>
			
			<div class="dropdown col-md-2" style="margin-left:20px;">
				<select class="form-control" form="brand_creation_form" name="OrderBy" onchange="location = this.options[this.selectedIndex].value;" style="height:36px;font-size:16px">
				    <option value="<c:url value="/order_management?${filterLogic.orderByOrderIdUrl}" />" <c:if test="${orderBy=='orderId'}">selected</c:if>>按订单号排序</option>
					<option value="<c:url value="/order_management?${filterLogic.orderByPackageQueryUrl}" />" <c:if test="${orderBy=='package'}">selected</c:if>>按包裹排序</option>
					<option value="<c:url value="/order_management?${filterLogic.orderByBuyerNameUrl}" />" <c:if test="${orderBy=='buyerName'}">selected</c:if>>按购买者排序</option>
				</select>
            </div>
			<div class="col-md-1" style="margin-left:10px">
				<c:if test="${showAll==false}">
	            	  	 <a type="button" class="btn btn-warning" style="font-size:16px;" href="<c:url value="/order_management?${filterLogic.showAllQueryUrl}" />">显示所有订单</a>
	            </c:if>
	            <c:if test="${showAll==true}">
	            	  	 <a type="button" class="btn btn-warning" style="font-size:16px;" href="<c:url value="/order_management?${filterLogic.hideSettleDownOrderQueryUrl}" />">显示未结算订单</a>
	            </c:if>
			</div>
			<c:if test="${admin.isSuperAdmin == false}">
				<a type="button" class="btn btn-danger" style="margin-left:30px;font-size:16px;" href="<c:url value="/order/settledown?adminId=${admin.adminId}" />">快捷结算</a>
			</c:if>
			<c:if test="${adminList != null}">
			<div class="dropdown col-md-1" style="margin-left:30px">
	  			<button class="btn btn-danger dropdown-toggle" type="button" id="dropdownMenu4" data-toggle="dropdown" aria-expanded="true" style="font-size:16px;">快捷结算<span class="caret"></span></button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu4">
				  	<c:forEach items="${adminList}" var="admin">
				  		<li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="/order/settledown?adminId=${admin.adminId}" />">${admin.adminName}</a></li>
					</c:forEach>
				  </ul>
			</div>
			</c:if>
        </div>
        <p>当前显示模式：<b><c:if test="${showAll==true}">显示所有订单</c:if><c:if test="${showAll==false}">只显示未结算订单</c:if></b></p>
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
        <td class="anchor"><a name="a${order.orderId}"><span style="padding-top: 65px; margin-top: -65px;">${order.orderId}</span></a></td>
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
      <div id="bottom_anchor"></div>
      <div class="row">
     	<c:if test="${admin.isSuperAdmin==true}">
      	<div class="text-success">当前订单列表 Logpie 总估计利润: ${profitCalculator.estimatedProfitsForAllOrders}</div>
      	<div class="text-success">当前订单列表 Logpie 已寄出包裹准确总利润: ${profitCalculator.actualProfitsForAllOrders}</div>
      	<div class="text-danger">当前订单列表 Logpie北美总部 总估计利润: ${profitCalculator.netEstimatedProfitsForAllOrders}</div>
      	<div class="text-danger">当前订单列表 Logpie北美总部 已寄出包裹准确总利润: ${profitCalculator.netActualProfitsForAllOrders}</div>
      	<!-- 只有在当前的订单都是未结算订单时 显示在路上的钱才有意义 因为结算的订单 公司已收钱是全部的钱 未除去代理的工资 -->
      	<c:if test="${showAll==false}">
      	<div class="text-warning">当前订单列表 Logpie北美总部 总共在路上的钱: ${profitCalculator.totalMoneyInFly}</div>
      	</c:if>
      	</c:if>
		<c:if test="${admin.isSuperAdmin==false}">
      	<div class="text-primary">当前订单列表 代理:${admin.adminName} 总估计利润: ${profitCalculator.proxyEstimatedProfitsForAllOrders}</div>
      	<div class="text-primary">当前订单列表 代理:${admin.adminName} 已寄出包裹准确总利润: ${profitCalculator.proxyActualProfitsForAllOrders}</div>
      	</c:if>
      	<div class="text-info">当前订单列表 总营业额: ${profitCalculator.totalTurnOver}</div>
      </div>
       <div class="row alert-info">
	      <h4>系统如何判定订单是否已完全结算？ 必须满足以下5个条件</h4>
	      <ol>
	        <li> 如果 用户付款数 等于 卖价+国内用户已付邮费（可能为0， 可能不等于国内运费)</li>
	        <li> 如果 公司收款 等于 用户已付钱数减去国内运费 (一般是代理垫付的) </li>
	        <li> 如果 利润已结算给代理</li>
	        <li> 如果 卖价不为0 那么用户付款数不能为0</li>
	        <li> 如果 已向用户发货为true</li>
	       </ol>
       </div>
      <div>${metric}</div>

        
    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#section-order">订单</a></li>
            <c:if test="${admin.isSuperAdmin==true}">
          	  <li><a data-toggle="tab" href="#section-package">包裹</a></li>
            </c:if>
            <li><a data-toggle="tab" href="#section-client">用户档案</a></li>
            <li><a data-toggle="tab" href="#section-product">产品</a></li>
            <li><a data-toggle="tab" href="#section-category">类别</a></li>
            <li><a data-toggle="tab" href="#section-image">图片</a></li>
            <li><a data-toggle="tab" href="#section-brand">品牌</a></li>
            <!--<li><a data-toggle="tab" href="#section-admin">管理员</a></li>-->
          </ul>
          <div class="tab-content">
          <div id="section-order" class="tab-pane fade in active" style="padding:20px">
              <h3>新建一个订单</h3>
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/order/create" />" method="POST" >
                <div class="row">
	                <div class="form-group col-sm-4">
	                  <label for="order_buyer">订单购买者</label>
	                  <input class="form-control" id="order_buyer" name="OrderBuyerName" required autofocus>
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="order_package">关联用户档案(可空缺)</label>
	                  <select class="form-control" form="order_creation_form" name="OrderClientId">
	                        <option value=""> </option>
							<c:forEach items="${allClientList}" var="client">
							    <option value="${client.clientId}">No.${client.clientId} ${client.clientRealName}/${client.clientWechatName}/${client.clientWeiboName}</option>
							</c:forEach>
					  </select>
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="order_proxy">订单代理人</label>
	                  <select class="form-control" form="order_creation_form" name="OrderProxyId" required>
	                  		  <c:if test="${adminList!=null}">
							    <c:forEach items="${adminList}" var="admin">
							       <c:if test="${admin.adminId == defaultProxy}">
							       		<option value="${admin.adminId}" selected>${admin.adminName}</option>
	        					   </c:if>
	        					   <c:if test="${admin.adminId != defaultProxy}">
							       		<option value="${admin.adminId}">${admin.adminName}</option>
	        					   </c:if>
							    </c:forEach>
							  </c:if>
							  <c:if test="${adminList==null}">
							      <option value="${admin.adminId}">${admin.adminName}</option>
							  </c:if>
					  </select>
	                </div>
                </div>
                <div class="dropdown" style="margin-bottom:20px">
                  <label for="order_product">购买商品</label>
                  <select class="form-control" form="order_creation_form" name="OrderProductId">
						<c:forEach items="${productList}" var="product">
						    <option value="${product.productId}">${product.productName}</option>
						</c:forEach>
				  </select>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                  <label for="order_product_count">购买数量</label>
                  <input class="form-control" type="number" step="1" min="1" max="1000" id="order_product_count" name="OrderProductCount" value="1" required>
                  </div>
                  <div class="form-group col-sm-6">
                  <label for="order_weight">订单重量（克）</label>
                  <input class="form-control" type="number" step="0.01" min="0" id="order_weight" name="OrderWeight" value="0" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="order_actual_cost">实际购买成本(美元$)</label>
                      <input class="form-control" type="number" step="0.01" id="order_actual_cost" name="OrderActualCost" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_currency_rate">订单当日汇率</label>
                    <input class="form-control" type="number" step="0.01" id="order_currency_rate" name="OrderCurrencyRate" value="${CurrencyRate}" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-3">
                    <label for="estimated_shipping_fee">预计邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="estimated_shipping_fee" name="OrderEstimatedShippingFee" required>
                  </div>
                  <div class="form-group col-sm-3">
                    <label for="actural_shipping_fee">国际邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="actural_shipping_fee" name="OrderActualShippingFee">
                  </div>
                  <div class="form-group col-sm-3">
                    <label for="domestic_shipping_fee">国内邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="domestic_shipping_fee" value="0" name="OrderDomesticShippingFee" required>
                  </div>
                  <div class="form-group col-sm-3">
                    <label for="customer_paid_domestic_shipping_fee">国内已付邮费￥</label>
                    <input class="form-control" type="number" step="0.01" id="customer_paid_domestic_shipping_fee" value="0" name="OrderCustomerPaidDomesticShippingFee" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="selling_price">最终售价￥</label>
                      <input class="form-control" type="number" step="0.01"  id="selling_price" name="OrderSellingPrice" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_buyer_paid_money">买家付款￥</label>
                    <input class="form-control" type="number" step="0.01"  id="order_buyer_paid_money" name="OrderCustomerPaidMoney" value="0" required>
                  </div>
                </div>
                <c:if test="${admin.isSuperAdmin==true}">
	                <div class="form-group">
	                  <label for="order_package">所属包裹(可空缺)</label>
	                  <select class="form-control" form="order_creation_form" name="OrderPackageId">
	                        <option value=""> </option>
							<c:forEach items="${allPackageList}" var="logpiePackage">
							    <option value="${logpiePackage.packageId}">${logpiePackage.packageId}&nbsp; &nbsp; &nbsp;${fn:substring(logpiePackage.packageDate,0,10)}&nbsp; &nbsp; &nbsp;${logpiePackage.packageProxyName}&nbsp; &nbsp; &nbsp;${logpiePackage.packageReceiver} &nbsp; &nbsp; &nbsp;${logpiePackage.packageTrackingNumber}</option>
							</c:forEach>
					  </select>
	                </div>
                </c:if>
                <!-- only super admin can modify how much money company already received -->
                <c:if test="${admin.isSuperAdmin==true}">
	                <div class="form-group">
	                  <label for="order_company_received_money">公司已收汇款￥</label>
	                  <input class="form-control" type="number" step="0.01" id="order_company_received_money" name="OrderCompanyReceivedMoney" value="0" required>
	                </div>
                </c:if>
                <c:if test="${admin.isSuperAdmin==false}">
	                  <input class="form-control" type="hidden" id="order_company_received_money" name="OrderCompanyReceivedMoney" value="0" required>
                </c:if>
                <div class="form-group">
                  <label for="order_note">备注 (客户来源，规格颜色，美国跟踪号，国内运费，转寄地址，定金支付情况)</label>
                  <input class="form-control" type="text" id="order_note" name="OrderNote">
                </div>
                <div class="row">
	                <!-- only super admin can modify whether the profit is paid -->
	                <c:if test="${admin.isSuperAdmin==true}">
		                <div class="col-sm-6">
		                  <label><input type="checkbox" id="profits_is_paid" name="OrderIsProfitPaid" value="True"/>利润是否已和代理结算</label>
		                </div>
	                </c:if>
	               	<div class="col-sm-6">
	                  <label><input type="checkbox" id="sent_to_user" name="OrderSentToUser" value="True"/> 已向用户发货</label>
	                </div>
	            </div>
                <c:if test="${admin.isSuperAdmin==false}">
					<label><input type="hidden" id="profits_is_paid" name="OrderIsProfitPaid" value="False"/>  利润是否已和代理结算</label>
                </c:if>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <!-- only super admin can create package -->
            <c:if test="${admin.isSuperAdmin==true}">
            <div id="section-package" class="tab-pane fade" style="padding:20px">
              <h3>新建一个包裹</h3>
              <form role="form" style="padding:20px" id="package_creation_form"  action="<c:url value="/package/create" />" method="POST">
                <div class="form-group">
                  <label for="package_proxy">包裹代理机构：</label>
                   <input class="form-control" type="text" id="package_proxy" name="PackageProxyName" required>
                </div>
                <div class="form-group">
                  <label for="package_receiver">收件人：</label>
                  <input class="form-control" type="text" id="package_receiver" name="PackageReceiver" required>
                </div>
                <div class="form-group">
                  <label for="package_destination">收件地址：</label>
                  <input class="form-control" type="text" id="package_destination" name="PackageDestination" required>
                </div>
                <div class="form-group">
                  <label for="tracking_number">包裹单号：</label>
                  <input class="form-control" type="text" id="tracking_number" name="PackageTrackingNumber" required>
                </div>
                <div class="form-group">
                  <label for="package_weight">包裹重量（克）：</label>
                  <input class="form-control" type="number" id="package_weight" name="PackageWeight" required>
                </div>
                <div class="form-group">
                  <label for="package_shipping_fee">邮寄费用（人民币）：</label>
                  <input class="form-control" type="number" id="package_shipping_fee" name="PackgeShippingFee" required>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                    <label for="package_custom_fee">额外海关费用（人民币）：</label>
                    <input class="form-control" type="number" id="package_custom_fee" name="PackageAdditionalCustomTaxFee" value="0" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="package_insurance_fee">额外保险费用（人民币）：</label>
                    <input class="form-control"  type="number" id="package_insurance_fee" name="PackageAdditionalInsuranceFee" value="0" required>
                  </div>
                </div>
                <div class="form-group">
                  <label for="package_notes">备注(可空缺)：</label>
                  <input class="form-control" type="text" id="package_notes" name="PackageNote">
                </div>
                <div class="checkbox">
                  <label><input type="checkbox" checked="checked" id="package_is_shipped" name="PackageIsShipped" value="True"/>是否寄出</label>
                </div>
                <div class="checkbox">
                  <label><input type="checkbox" id="package_is_delivered" name="PackageIsDelivered" value="True"/>是否签收</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            </c:if>
            <div id="section-client" class="tab-pane fade" style="padding:20px">
              <h3>新建一个用户档案</h3>
              <form role="form" style="padding:20px" action="<c:url value="/client/create" />" method="post">
                <div class="row">
	                <div class="form-group col-sm-4">
	                  <label for="client_real_name">用户名字</label>
	                  <input class="form-control" id="client_real_name" name="ClientRealName" autofocus>
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="client_wechat_name">用户微信名</label>
	                  <input class="form-control" id="client_wechat_name" name="ClientWechatName">
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="client_wechat_number">用户微信号</label>
	                  <input class="form-control" id="client_wechat_number" name="ClientWechatNumber">
	                </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                  <label for="client_weibo_name">用户微博名</label>
                  <input class="form-control" id="client_weibo_name" name="ClientWeiboName">
                  </div>
                  <div class="form-group col-sm-6">
                  <label for="client_taobao_name">用户淘宝名</label>
                  <input class="form-control" id="client_taobao_name" name="ClientTaobaoName">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-12">
                  <label for="client_address">用户地址</label>
                  <input class="form-control" id="client_address" name="ClientAddress">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                  <label for="client_phone">用户手机号</label>
                  <input class="form-control" id="client_weibo_name" name="ClientPhone">
                  </div>
                  <div class="form-group col-sm-6">
                  <label for="client_postal_code">用户邮编</label>
                  <input class="form-control" id="client_postal_code" name="ClientPostalCode">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-12">
                  <label for="client_note">用户备注</label>
                  <input class="form-control" id="client_note" name="ClientNote">
                  </div>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <div id="section-category" class="tab-pane fade" style="padding:20px">
              <h3>新建一个类别</h3>
              <form role="form" style="padding:20px" action="<c:url value="/category/create" />" method="post">
                <div class="form-group">
                  <label for="category">类别名称：</label>
                  <input class="form-control" id="category" name="CategoryName" placeholder="输入新的类别名称" required autofocus>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <div id="section-image" class="tab-pane fade" style="padding:20px">
              <h3>上传一张图片</h3>
              <form role="form" style="padding:20px" action="<c:url value="/image/create" />" method="POST">
                <div class="form-group">
                  <label for="img_url">图片地址：</label>
                  <input class="form-control" type="url" id="img_url" name="ImageUrl" placeholder="阿里云OSS 照片url地址" required autofocus>
                </div>
                <div class="form-group">
                  <label for="img_description">图片名称：</label>
                  <input class="form-control" type="text" id="img_description" name="ImageDescription" placeholder="图片描述(key)" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <div id="section-brand" class="tab-pane fade" style="padding:20px">
              <h3>新建一个品牌</h3>
              <form role="form" style="padding:20px" action="<c:url value="/brand/create" />" method="POST" id="brand_creation_form">
                <div class="form-group">
                  <label for="brand_en">品牌英文名称：</label>
                  <input class="form-control" type="text" id="brand_en" name="BrandEnglishName" required autofocus>
                </div>
                <div class="form-group">
                  <label for="brand_cn">品牌中文名称：</label>
                  <input class="form-control" type="text" id="brand_cn" name="BrandChineseName" required>
                </div>
                <div class="dropdown" style="margin-bottom:20px">
                  <label for="brand_category">品牌所属类别：</label>
						<select class="form-control" form="brand_creation_form" name="BrandCategoryId">
						   <c:forEach items="${categoryList}" var="category">
						    <option value="${category.categoryId}">${category.categoryName}</option>
						    </c:forEach>
						</select>
                </div>
                <div class="row">
                  <div class="dropdown col-sm-5" style="margin-bottom:10px">
                    <label for="brand_image">品牌图片：</label>
						<select class="form-control" form="brand_creation_form" name="BrandImageId" required>
						   <c:forEach items="${imageList}" var="brandImage">
						    <option value="${brandImage.imageId}">${brandImage.imageDescription}</option>
						    </c:forEach>
						</select>
                  </div>
                  <div class="dropdown col-sm-7" style="margin-bottom:10px">
                    <label for="brand_image">相关尺寸图片：</label>
						<select class="form-control" form="brand_creation_form" name="BrandSizeChartImageId" required>
						   <c:forEach items="${imageList}" var="sizeChartImage">
						    <option value="${sizeChartImage.imageId}">${sizeChartImage.imageDescription}</option>
						   </c:forEach>
						</select>
                  </div>
                </div>
                <div class="checkbox" style="padding-left:20px">
                  <label><input type="checkbox" checked="checked" id="brand_is_activated" name="BrandIsActivated" value="True"/>是否激活</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <div id="section-product" class="tab-pane fade" style="padding:20px">
              <h3>新建一个产品</h3>
              <form role="form" style="padding:20px" id="product_creation_form" action="<c:url value="/product/create" />" method="POST">
                <div class="form-group">
                  <label for="product-name">产品名称：</label>
                  <input class="form-control" type="text" id="product-name" name="ProductName" required>
                </div>
                <div class="form-group">
                  <label for="product-description">产品描述：</label>
                  <input class="form-control" type="text" id="product-description" name="ProductDescription" value="无" required>
                </div>
                <div class="form-group">
                  <label for="product-link">产品链接：</label>
                  <input class="form-control" type="url" id="product-link" name="ProductLink" value="http://www.logpie.com" required>
                </div>
                <div class="form-group">
                  <label for="product-weight">产品重量（克）：</label>
                  <input class="form-control" type="number" id="product-weight" name="ProductWeight" required>
                </div>
                <div class="row">
                  <div class="dropdown col-sm-5" style="margin-bottom:10px">
                    <label for="product_brand">所属品牌：</label>
						<select class="form-control" form="product_creation_form" name="ProductBrandId">
						   <c:forEach items="${brandList}" var="brand">
						    <option value="${brand.brandId}">${brand.brandEnglishName}</option>
						    </c:forEach>
						</select>
                  </div>
                  <div class="dropdown col-sm-5" style="margin-bottom:10px">
                    <label for="product_image">产品图片：</label>
						<select class="form-control" form="product_creation_form" name="ProductImageId">
						   <c:forEach items="${imageList}" var="productImage">
						    <option value="${productImage.imageId}">${productImage.imageDescription}</option>
						    </c:forEach>
						</select>
                  </div>
                </div>
                <div class="checkbox" style="padding-left:20px">
                  <label><input type="checkbox" checked="checked" id="product_is_activated" name="ProductIsActivated" value="True"/>是否显示在页面</label>
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