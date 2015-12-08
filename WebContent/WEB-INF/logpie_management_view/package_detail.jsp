<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
    	<c:if test="${logpiePackage !=null}">
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
    	<div class="row">
            <h3>${logpiePackage.packageId}号包裹详细信息 包裹日期：${logpiePackage.packageDate}</h3>
        </div>
		<tag:order_list></tag:order_list>
        <div class="row">
        <span>当前包裹总重：</span><span>${packageTotalWeight} 克</span> <c:if test="${admin.isSuperAdmin==true}"><a class="btn btn-info" href="./package/quickCalculateShippingFeeDistribution?id=${logpiePackage.packageId}" style="margin-left:20px">快捷计算分摊运费</a></c:if>
        <c:if test="${admin.isSuperAdmin == true}">
        <a type="button" class="btn btn-info" href=<c:url value="/package/edit?id=${logpiePackage.packageId}" /> style="margin-left:20px">修改包裹</a>
        <a type="button" class="btn btn-info" href=<c:url value="/package/delivered?id=${logpiePackage.packageId}" /> style="margin-left:20px">包裹已到</a>
        </c:if>
        </div>
        <hr/>
  	    <div class="alert" role="alert">
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
        </c:if>
        <c:if test="${logpiePackage ==null}">
  	        <div class="alert alert-danger" role="alert">
                    <strong>抱歉!查无此包裹 请检查你url中的包裹id是否有效</strong>
            </div>
        </c:if>

    </jsp:body>
</tag:logpie_common_template>