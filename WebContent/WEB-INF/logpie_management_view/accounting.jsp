<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>欢迎来到Logpie 财务报表</h3>
        </div>
        </br>
        <div class="row">
	        <div class="col-md-4">
	        <form action="<c:url value="/accounting/piechart" />" method="GET">
	        <div class="form-group">
	           <label style="font-size:14pt">指定月份</label>
	           <input id="datePicker" name="year_month" type="month" min="2015-02" value="${Today}" style="font-size:14pt"/>
	        </div>
	        <button type="submit" name="type" value="OrderInCategory" class="btn btn-primary btn-block" style="width:240px; font-size:13pt">类别订单数量分布图</button>
	        <button type="submit" name="type" value="OrderInBrand" class="btn btn-primary btn-block" style="width:240px; font-size:13pt">品牌订单数量分布图</button>
	        <button type="submit" name="type" value="OrderInAdmin" class="btn btn-primary btn-block" style="width:240px; font-size:13pt">代理订单数量分布图</button>
	        </br>
	        <button type="submit" name="type" value="OrderProfitInCategory" class="btn btn-danger btn-block" style="width:240px; font-size:13pt">类别订单利润分布图</button>
	        <button type="submit" name="type" value="OrderProfitInBrand" class="btn btn-danger btn-block" style="width:240px; font-size:13pt">品牌订单利润分布图</button>
	        <button type="submit" name="type" value="OrderProfitInAdmin" class="btn btn-danger btn-block" style="width:240px; font-size:13pt">代理订单利润分布图</button>
	        </form>
	        </div>
	        <div class="col-md-4">
	        <form action="<c:url value="/accounting/linechart" />" method="GET">
	        <div class="form-group">
	        <label style="font-size:14pt">趋势日折线图/月折线图</label>
	        </div>
	        <button type="submit" name="type" value="OrderNumbers" class="btn btn-success btn-block" style="width:240px; font-size:13pt">订单数量历史趋势</button>
	        <button type="submit" name="type" value="OrderProfits" class="btn btn-success btn-block" style="width:240px; font-size:13pt">订单利润历史趋势</button>
	        </form>
	        </div>
        </div>
    </jsp:body>
</tag:logpie_common_template>