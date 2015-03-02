<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改包裹 包裹Id:${logpiePackage.packageId} 包裹创建时间:${logpiePackage.packageDate}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/package/edit" />" method="POST" >
                <input name="PackageId" value="${logpiePackage.packageId}" type="hidden">
                <input name="PackageDate" value="${logpiePackage.packageDate}" type="hidden">
                <div class="form-group">
                  <label for="package_proxy">物流运营商：</label>
                  <input class="form-control" type="text" id="package_proxy" name="PackageProxyName" value="${logpiePackage.packageProxyName}" required>
                </div>
                <div class="form-group">
                  <label for="package_proxy">包裹追踪号：</label>
                  <input class="form-control" type="text" id="package_proxy" name="PackageTrackingNumber" value="${logpiePackage.packageTrackingNumber}" required>
                </div>
                <div class="form-group">
                  <label for="package_receiver">收件人：</label>
                  <input class="form-control" type="text" id="package_receiver" name="PackageReceiver" value="${logpiePackage.packageReceiver}" required>
                </div>
                <div class="form-group">
                  <label for="package_destination">收件地址：</label>
                  <input class="form-control" type="text" id="package_destination" name="PackageDestination" value="${logpiePackage.packageDestination}" required>
                </div>
                <div class="form-group">
                  <label for="package_weight">包裹重量（g）：</label>
                  <input class="form-control" type="number" id="package_weight" name="PackageWeight" value="${logpiePackage.packageWeight}" required>
                </div>
                <div class="form-group">
                  <label for="package_shipping_fee">邮寄费用：</label>
                  <input class="form-control" type="number" id="package_shipping_fee" name="PackgeShippingFee" value="${logpiePackage.packgeShippingFee}" required>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                    <label for="package_custom_fee">额外海关费用：</label>
                    <input class="form-control" type="number" id="package_custom_fee" name="PackageAdditionalCustomTaxFee" value="0" value="${logpiePackage.packageAdditionalCustomTaxFee}" required>
                  </div>
                  <div class="form-group col-sm-6">
                    <label for="package_insurance_fee">额外保险费用：</label>
                    <input class="form-control"  type="number" id="package_insurance_fee" name="PackageAdditionalInsuranceFee" value="0" value="${logpiePackage.packageAdditionalInsuranceFee}" required>
                  </div>
                </div>
                <div class="form-group">
                  <label for="package_notes">备注(可空缺)：</label>
                  <input class="form-control" type="text" id="package_notes" name="PackageNote" value="${logpiePackage.packageNote}" >
                </div>
                <div class="checkbox">
                  <label><input type="checkbox" id="package_is_shipped" name="PackageIsShipped" value="True" <c:if test="${logpiePackage.packageIsShipped==true}">checked</c:if>/>是否寄出</label>
                </div>
                <div class="checkbox">
                  <label><input type="checkbox" id="package_is_delivered" name="PackageIsDelivered" value="True" <c:if test="${logpiePackage.packageIsDelivered==true}">checked</c:if>/>是否签收</label>
                </div>

                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
    </jsp:body>
</tag:logpie_common_template>