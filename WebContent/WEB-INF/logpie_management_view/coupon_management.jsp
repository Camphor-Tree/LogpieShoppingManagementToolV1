<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>欢迎来到Logpie 优惠券管理</h3>
        </div>
        <br/>
        <c:if test="${admin.isSuperAdmin == true }">
        <div class="row">
	        <div class="col-md-6">
	        <form action="<c:url value="/coupon/generate" />" method="GET">
	        <input type="number" name="productPrice" step="1" class="col-md-6" style="height:36px;width:240px" placeholder="输入商品价格" required autofocus>
	        <button type="submit" class="btn btn-success col-md-6" style="font-size:13pt;height:36px;width:240px">生成新优惠券码</button>
	        </form>
	        </div>
        </div>
        <c:if test="${CouponCode != null}">
        <div class="row" style="font-size:13pt;">
            <br>
        	<div class="alert alert-success col-md-4">
         	新生成优惠券码: ${CouponCode}
         	</div>
        </div>
		</c:if>
        </c:if>
        <hr></hr>
        <div class="row">
	        <div class="col-md-6">
	        <form action="<c:url value="/coupon/validate" />" method="GET">
	        <input type="text" name="couponCode" step="1" class="col-md-6" style="height:36px;width:240px" placeholder="输入优惠券码" required autofocus>
	        <button type="submit" class="btn btn-info col-md-6" style="font-size:13pt;height:36px;width:240px">验证优惠券码</button>
	        </form>
	        </div>
        </div>
        <c:if test="${ValidateCouponCode != null}">
        <div class="row" style="font-size:13pt;">
            <br>
        	<div class="alert alert-success col-md-4">
         	优惠券码: ${ValidateCouponCode} 优惠券价值: ${ValidateCouponValue}
         	</div>
        </div>
		</c:if>
        <hr></hr>
        <div class="row">
	        <div class="col-md-6">
	        <form action="<c:url value="/coupon/use" />" method="POST">
	        <input type="text" name="couponCode" step="1" class="col-md-6" style="height:36px;width:240px" placeholder="输入优惠券码" required autofocus>
	        <button type="submit" class="btn btn-danger col-md-6" style="font-size:13pt;height:36px;width:240px">使用优惠券码</button>
	        </form>
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
    </jsp:body>
</tag:logpie_common_template>