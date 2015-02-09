<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
    	<c:if test="${logpiePackage !=null}">
        <div class="row">
            <h3>包裹信息</h3>
        </div>
  	    <div class="alert alert-success" role="alert">
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