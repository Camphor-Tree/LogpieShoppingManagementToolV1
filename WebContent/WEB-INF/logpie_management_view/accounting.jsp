<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
    <div class="container">
        <div class="row">
            <h3>欢迎来到Logpie 财务报表</h3>
        </div>
        </br>
        
        <form action="<c:url value="/accounting/piechart" />" method="GET">
        <div class="form-group">
           <label for="datePicker">指定月份</label>
           <input id="datePicker" name="year_month" type="month" min="2015-02" value="${Today}"/>
        </div>
        <button type="submit" name="type" value="OrderInCategory" class="btn btn-primary btn-block" style="width:240px">订单类别分布图</button>
        <button type="submit" name="type" value="OrderInBrand" class="btn btn-primary btn-block" style="width:240px">订单品牌分布图</button>
        <button type="submit" name="type" value="OrderInAdmin" class="btn btn-primary btn-block" style="width:240px">订单代理分布图</button>
        </form>
    </div>
    </jsp:body>
</tag:logpie_common_template>