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
   		<button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg" style="margin:10px">新建基础数据</button>

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
        <th>修改</th>
        </tr>
        <c:forEach items="${orderList}" var="order">
        <tr>
        <td><a href="./order?id=${order.orderId}">${order.orderId}</a></td>
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
        <td><a type="button" class="btn btn-warning" href="./order/edit?id=${order.orderId}">修改</a></td>
        </tr>
        </c:forEach>
        <tbody>
        </tbody>
        </table>
        
    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#section-category">类别</a></li>
            <li><a data-toggle="tab" href="#section-image">图片</a></li>
            <li><a data-toggle="tab" href="#section-brand">品牌</a></li>
            <li><a data-toggle="tab" href="#section-product">产品</a></li>
            <li class="dropdown"><a data-toggle="dropdown" class="dropdown-toggle" href="#">Dropdown <b class="caret"></b></a>
            <ul class="dropdown-menu">
                <li><a data-toggle="tab" href="#dropdown1">Dropdown1</a></li>
                <li><a data-toggle="tab" href="#dropdown2">Dropdown2</a></li>
            </ul>
            </li>
          </ul>
          <div class="tab-content">
            <div id="section-category" class="tab-pane fade in active" style="padding:20px">
              <h3>新建一个类别</h3>
              <form role="form" style="padding:20px" action="./category/create" method="post">
                <div class="form-group">
                  <label for="category">类别名称：</label>
                  <input class="form-control" id="category" name="CategoryName" placeholder="输入新的类别名称" required autofocus>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <div id="section-image" class="tab-pane fade" style="padding:20px">
              <h3>上传一张图片</h3>
              <form role="form" style="padding:20px" action="./image/create" method="POST">
                <div class="form-group">
                  <label for="img_url">图片地址：</label>
                  <input class="form-control" id="img_url" name="ImageUrl" placeholder="阿里云OSS 照片url地址">
                </div>
                <div class="form-group">
                  <label for="img_description">图片名称：</label>
                  <input class="form-control" id="img_description" name="ImageDescription" placeholder="图片描述(key)">
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
            <div id="section-brand" class="tab-pane fade" style="padding:20px">
              <h3>新建一个品牌</h3>
              <form role="form" style="padding:20px" action="./brand/create" method="POST" id="brand_creation_form">
                <div class="form-group">
                  <label for="brand_en">品牌英文名称：</label>
                  <input class="form-control" id="brand_en" name="BrandEnglishName">
                </div>
                <div class="form-group">
                  <label for="brand_cn">品牌中文名称：</label>
                  <input class="form-control" id="brand_cn" name="BrandChineseName">
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
                    <label for="brand_image">相关尺寸图片(可选)：</label>
						<select class="form-control" form="brand_creation_form" name="BrandSizeChartImageId">
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
              <form role="form" style="padding:20px" id="product_creation_form" action="./product/create" method="POST">
                <div class="form-group">
                  <label for="product-name">产品名称：</label>
                  <input class="form-control" id="product-name" name="ProductName">
                </div>
                <div class="form-group">
                  <label for="product-description">产品描述：</label>
                  <input class="form-control" id="product-description" name="ProductDescription">
                </div>
                <div class="form-group">
                  <label for="product-link">产品链接：</label>
                  <input class="form-control" id="product-link" name="ProductLink">
                </div>
                <div class="form-group">
                  <label for="product-weight">产品重量：</label>
                  <input class="form-control" id="product-weight" name="ProductWeight">
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
            <div id="dropdown1" class="tab-pane fade">
              <h3>Dropdown 1</h3>
              <p>WInteger convallis, nulla in sollicitudin placerat, ligula enim auctor lectus, in mollis diam dolor at lorem. Sed bibendum nibh sit amet dictum feugiat. Vivamus arcu sem, cursus a feugiat ut, iaculis at erat. Donec vehicula at ligula vitae venenatis. Sed nunc nulla, vehicula non porttitor in, pharetra et dolor. Fusce nec velit velit. Pellentesque consectetur eros.</p>
            </div>
            <div id="dropdown2" class="tab-pane fade">
              <h3>Dropdown 2</h3>
              <p>Donec vel placerat quam, ut euismod risus. Sed a mi suscipit, elementum sem a, hendrerit velit. Donec at erat magna. Sed dignissim orci nec eleifend egestas. Donec eget mi consequat massa vestibulum laoreet. Mauris et ultrices nulla, malesuada volutpat ante. Fusce ut orci lorem. Donec molestie libero in tempus imperdiet. Cum sociis natoque penatibus et magnis.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    </jsp:body>
</tag:logpie_common_template>

