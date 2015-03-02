<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 订单管理</h3>
	        <div class="col-md-2">
	            <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg" style="margin-left:30px;font-size:16px;">新建基础数据</button>
			</div>
			<div class="dropdown col-md-2">
	  			<button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true" style="margin-left:30px;font-size:16px;">按代理筛选<span class="caret"></span></button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				  	<c:forEach items="${adminList}" var="admin">
				  		<li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="/order_management?admin=${admin.adminId}" />">${admin.adminName}</a></li>
					</c:forEach>
				  </ul>
			</div>
			<div class="dropdown col-md-2">
	  			<button class="btn btn-info dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-expanded="true" style="margin-left:30px;font-size:16px;">按购买者筛选<span class="caret"></span></button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2">
				  	<c:forEach items="${orderBuyersList}" var="buyer">
				  		<li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="/order_management?buyer=${buyer}" />">${buyer}</a></li>
					</c:forEach>
				  </ul>
			</div>
        </div>
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
   		
      <table class="table table-striped text-center" style="table-layout:fixed;vertical-align:middle; font-size:15px;">
        <tr class="info">
        <th class="col-xs-1 col-md-1 text-center">No</th>
        <th class="col-xs-2 col-md-2 text-center">订单日期</th>
        <th class="col-xs-4 col-md-4 text-center">商品名称</th>
        <th class="col-xs-1 col-md-1 text-center">数量</th>
        <!-- <th>商品重量</th> -->
        <th class="col-xs-2 col-md-2 text-center">购买者</th>
        <th class="col-xs-2 col-md-2 text-center">代理者</th>
        <!--<th>订单代理分红百分比</th>-->
        <th class="col-xs-2 col-md-2 text-center">购买终价$</th>
        <!--<th>订单当日汇率</th>-->
        <th class="col-xs-1 col-md-1 text-center">包裹</th>
        <!--<th>订单估计运费(人民币)</th>-->
        <th class="col-xs-2 col-md-2 text-center">实际运费￥</th>
        <th class="col-xs-2 col-md-2 text-center">订单售价￥</th>
        <th class="col-xs-2 col-md-2 text-center">实收账款￥</th>
        <th class="col-xs-2 col-md-2 text-center">最终利润￥</th>
        <th class="col-xs-2 col-md-2 text-center">公司入账￥</th>
        <th class="col-xs-2 col-md-2 text-center">利润是否结算</th>
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
        <!-- <td>${order.orderProduct.productWeight}</td>-->
        <td>${order.orderBuyerName}</td>
        <td>${order.orderProxy.adminName}</td>
        <!--<td>${order.orderProxyProfitPercentage}</td>-->
        <td>${order.orderActualCost}</td>
        <!--<td>${order.orderCurrencyRate}</td>-->
        <td><a href="./package?id=${order.orderPackage.packageId}">${order.orderPackage.packageId}</a></td>
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
        
    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#section-order">订单</a></li>
            <li><a data-toggle="tab" href="#section-package">包裹</a></li>
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
                <div class="form-group">
                  <label for="order_buyer">订单购买者：</label>
                  <input class="form-control" id="order_buyer" name="OrderBuyerName" required autofocus>
                </div>
                <div class="form-group">
                  <label for="order_proxy">订单代理人：</label>
                  <select class="form-control" form="order_creation_form" name="OrderProxyId" required>
						   <c:forEach items="${adminList}" var="admin">
						    <option value="${admin.adminId}">${admin.adminName}</option>
						    </c:forEach>
				  </select>
                </div>
                <div class="dropdown" style="margin-bottom:20px">
                  <label for="order_product">购买商品：</label>
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
                    <label for="order_proxy_profit_percentage">代理分红百分比</label>
                    <input class="form-control" type="number" step="0.01" id="order_proxy_profit_percentage" name="OrderProxyProfitPercentage" value="0.4" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="order_actual_cost">实际购买成本(美元)(可空缺)：</label>
                      <input class="form-control" type="number" step="0.01" id="order_actual_cost" name="OrderActualCost">
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_currency_rate">订单当日汇率：</label>
                    <input class="form-control" type="number" step="0.01" id="order_currency_rate" name="OrderCurrencyRate" value="6.23" required>
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                    <label for="estimated_shipping_fee">预计邮费：</label>
                    <input class="form-control" type="number" step="0.01" id="estimated_shipping_fee" name="OrderEstimatedShippingFee" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="actural_shipping_fee">实际邮费(人民币)(可空缺)：</label>
                    <input class="form-control" type="number" step="0.01" id="actural_shipping_fee" name="OrderActualShippingFee">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                      <label for="selling_price">最终售价(人民币)：</label>
                      <input class="form-control" type="number" step="0.01"  id="selling_price" name="OrderSellingPrice" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="order_buyer_paid_money">买家付款(人民币)</label>
                    <input class="form-control" type="number" step="0.01"  id="order_buyer_paid_money" name="OrderCustomerPaidMoney" value="0" required>
                  </div>
                </div>
                
                <div class="form-group">
                  <label for="order_package">所属包裹(可空缺)：</label>
                  <select class="form-control" form="order_creation_form" name="OrderPackageId">
                        <option value=""> </option>
						<c:forEach items="${packageList}" var="logpiePackage">
						    <option value="${logpiePackage.packageId}">id:${logpiePackage.packageId} date:${logpiePackage.packageDate}</option>
						</c:forEach>
				  </select>
                </div>
                <div class="form-group">
                  <label for="order_company_received_money">公司已收汇款：</label>
                  <input class="form-control" type="number" step="0.01" id="order_company_received_money" name="OrderCompanyReceivedMoney" value="0" required>
                </div>
                <div class="form-group">
                  <label for="order_note">备注(可空缺)：</label>
                  <input class="form-control" type="text" id="order_note" name="OrderNote">
                </div>
                <div class="checkbox" style="padding-left:20px">
                  <label><input type="checkbox" id="profits_is_paid" name="OrderIsProfitPaid" value="True"/>利润是否已和代理结算</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
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
                  <label for="package_weight">包裹重量（g）：</label>
                  <input class="form-control" type="number" id="package_weight" name="PackageWeight" required>
                </div>
                <div class="form-group">
                  <label for="package_shipping_fee">邮寄费用：</label>
                  <input class="form-control" type="number" id="package_shipping_fee" name="PackgeShippingFee" required>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                    <label for="package_custom_fee">额外海关费用：</label>
                    <input class="form-control" type="number" id="package_custom_fee" name="PackageAdditionalCustomTaxFee" value="0" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="package_insurance_fee">额外保险费用：</label>
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
						<select class="form-control" form="brand_creation_form" name="BrandImageId">
						   <c:forEach items="${imageList}" var="brandImage">
						    <option value="${brandImage.imageId}">${brandImage.imageDescription}</option>
						    </c:forEach>
						</select>
                  </div>
                  <div class="dropdown col-sm-7" style="margin-bottom:10px">
                    <label for="brand_image">相关尺寸图片(可无)：</label>
						<select class="form-control" form="brand_creation_form" name="BrandSizeChartImageId">
						    <option value=""></option>
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
                  <input class="form-control" type="text" id="product-description" name="ProductDescription" required>
                </div>
                <div class="form-group">
                  <label for="product-link">产品链接：</label>
                  <input class="form-control" type="url" id="product-link" name="ProductLink" required>
                </div>
                <div class="form-group">
                  <label for="product-weight">产品重量：</label>
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