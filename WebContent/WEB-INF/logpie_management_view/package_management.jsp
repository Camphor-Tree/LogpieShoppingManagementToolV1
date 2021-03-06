<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 包裹管理 （默认只显示未到包裹）</h3>
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
		
	    <div class="col-md-2" style="margin-bottom:10px">
	    	<c:if test="${showAll==false}">
	        <a type="button" class="btn btn-info" style="font-size:16px;" href="<c:url value="/package_management?showAll=true" />">显示所有包裹</a>
	        </c:if>
	        <c:if test="${showAll==true}">
	        <a type="button" class="btn btn-warning" style="font-size:16px;" href="<c:url value="/package_management" />">显示未到包裹</a>
	        </c:if>
		</div>
		
		<div class="col-md-2" style="margin-bottom:10px">
	        <a type="button" class="btn btn-danger" style="font-size:16px;" href="<c:url value="/package_management?showAllDelivered=true" />">显示所有已到包裹</a>
		</div>
	  
	   <c:if test="${admin.isSuperAdmin==true}">
	   
	   <div class="col-md-1 btn" disabled style="margin-bottom:10px;font-size:16px;">
		     快速新建包裹:
	   </div>
	   <c:forEach items="${adminList}" var="admin">
		 	<c:if test="${admin.isSuperAdmin==false}">
			<div class="col-md-1" style="margin-bottom:10px">
		        <a type="button" class="btn btn-success" style="font-size:16px;" href="<c:url value="/package/quick_create?adminId=${admin.adminId}"/>">${admin.adminName}</a>
			</div>
			</c:if>
			<c:if test="${admin.isSuperAdmin==true}">
			<div class="col-md-1" style="margin-bottom:10px">
		        <a type="button" class="btn btn-success" style="font-size:16px;" href="<c:url value="/package/quick_create?adminId=${admin.adminId}"/>">司华</a>
			</div>
			</c:if>
       </c:forEach>
       </c:if>
   		
      <table class="table table-striped text-center" style="table-layout:fixed; font-size:18px;">
        <tr class="info">
        <th class="col-xs-1 col-md-1 text-center">No.</th>
        <th class="col-xs-2 col-md-2 text-center">物流运营商</th>
        <th class="col-xs-4 col-md-4 text-center">包裹跟踪单号</th>
        <th class="col-xs-2 col-md-2 text-center">收件人</th>
        <th class="col-xs-5 col-md-5 text-center">地址</th>
        <th class="col-xs-2 col-md-2 text-center">包裹寄送时间</th>
        <th class="col-xs-2 col-md-2 text-center">包裹重量(克)</th>
        <th class="col-xs-2 col-md-2 text-center">运费￥</th>
        <th class="col-xs-1 col-md-1 text-center">关税</th>
        <th class="col-xs-1 col-md-1 text-center">保险</th>
        <th class="col-xs-1 col-md-1 text-center">寄出</th>
        <th class="col-xs-1 col-md-1 text-center">收到</th>
        <c:if test="${admin.isSuperAdmin==true}">
        	<th class="col-xs-2 col-md-2 text-center">修改</th>
        </c:if>
        </tr>
        <tbody>
        <c:forEach items="${packageList}" var="logpiePackage">
        <tr class='clickable-row' data-href='./package?id=${logpiePackage.packageId}'>
        <td class="anchor" style="color:#428bca"><a name="a${logpiePackage.packageId}" ><span style="padding-top: 65px; margin-top: -65px;"></span></a>${logpiePackage.packageId}</td>
        <td>${logpiePackage.packageProxyName}</td>
        <td>${logpiePackage.packageTrackingNumber}</td>
        <td>${logpiePackage.packageReceiver}</td>
        <td>${logpiePackage.packageDestination}</td>
        <td>${fn:substring(logpiePackage.packageDate,0,10)}</td>
        <td>${logpiePackage.packageWeight}</td>
        <td>${logpiePackage.packgeShippingFee}</td>
        <td>${logpiePackage.packageAdditionalCustomTaxFee}</td>
        <td>${logpiePackage.packageAdditionalInsuranceFee}</td>
        <td><c:if test="${logpiePackage.packageIsShipped == true}">是</c:if><c:if test="${logpiePackage.packageIsShipped == false}">否</c:if></td>
        <td><c:if test="${logpiePackage.packageIsDelivered == true}">是</c:if><c:if test="${logpiePackage.packageIsDelivered == false}">否</c:if></td>
        <c:if test="${admin.isSuperAdmin==true}">
        <td><a type="button" class="btn btn-warning" href="./package/edit?id=${logpiePackage.packageId}">修改</a></td>
        </c:if>
        </tr>
        </c:forEach>
        </tbody>
        </table>
        <h5>点击包裹可以进入包裹详情。</h5>
    </jsp:body>
</tag:logpie_common_template>