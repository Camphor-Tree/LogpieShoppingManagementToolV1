<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://logpieimage.oss-cn-qingdao.aliyuncs.com/weblogo.png">
    <link rel="icon" href="http://logpieimage.oss-cn-qingdao.aliyuncs.com/weblogo.png">
    <title>Logpie</title>
    <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value="/resources/css/offcanvas.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/footer.css" />" rel="stylesheet">
    <style type="text/css">
h2{
    margin: 0;     
    color: #666;
    padding-top: 90px;
    font-size: 52px;
    font-family: "trebuchet ms", sans-serif;    
}
.item{
    background: #CFCFCF;    
    text-align: center;
    height: 300px !important;
}
.carousel{
    margin-top: 20px;
}
.bs-example{
	margin: 20px;
}
    </style>
  </head>
  <body>
   <nav class="navbar navbar-fixed-top navbar-inverse" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<c:url value="/home" />">Logpie米国小买手</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
			<li class="<c:if test="${ActiveTab=='order_management'}">active</c:if>"><a href="<c:url value="/order_management?orderBy=orderId" />">订单管理</a></li>
			<li class="<c:if test="${ActiveTab=='package_management'}">active</c:if>"><a href="<c:url value="/package_management" />">包裹管理</a></li>
			<li class="<c:if test="${ActiveTab=='accounting'}">active</c:if>"><a href="<c:url value="/accounting" />">财务报表</a></li>
			<li class="<c:if test="${ActiveTab=='calculator'}">active</c:if>"><a href="<c:url value="/calculator" />">定价计算器</a></li>
			<li class="<c:if test="${ActiveTab=='client_management'}">active</c:if>"><a href="<c:url value="/client_management" />">客户档案管理</a></li>
			<li class="<c:if test="${ActiveTab=='log'}">active</c:if>"><a href="<c:url value="/log" />">系统改动日志</a></li>
			<li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">更多<span class="caret"></span></a>
	          <ul class="dropdown-menu" role="menu">
	            <li><a href="<c:url value="/category_management" />">分类管理</a></li>
	            <li><a href="<c:url value="/brand_management" />">品牌管理</a></li>
	            <li><a href="<c:url value="/image_management" />">图片管理</a></li>
	            <li><a href="<c:url value="/product_management" />">商品管理</a></li>
	            <li><a href="<c:url value="/coupon_management" />">优惠券管理</a></li>
	            <li class="divider"></li>
	            <li><a href="<c:url value="/settle_down_history" />">结算历史记录</a></li>
	            <li><a href="<c:url value="/system_backup" />">系统备份</a></li>
	            <li><a href="<c:url value="/settings" />">设置</a></li>
	            <li class="divider"></li>
	            <li><a href="<c:url value="/admin_profile" />">账号管理</a></li>
	            <li class="divider"></li>
	            <li><a href="<c:url value="/logout" />">退出登录</a></li>
	          </ul>
       	    </li>
          </ul>
          <form class="navbar-form" role="search" method="get" id="search-form" name="search-form" action="<c:url value="/search" />">
						        <div class="input-group">
						            <input type="text" class="form-control" placeholder="搜索订单、包裹、用户档案..." id="query" name="key">
							            <div class="input-group-btn">
						            <button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-search"></span></button>
						            </div>
						        </div>
		  </form>
        </div>
      </div>
    </nav>
    <div class="container" style="width:98%;">
        <div>
            <jsp:doBody/>
        </div>
    </div>
    <footer class="logpiefooter">
        <p>&copy; <b>logpie.com 2014-<%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %></b></p>
    </footer>
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
    <script src="<c:url value="/resources/js/offcanvas.js" />"></script>
    <script type="text/javascript">
    //for table row is clickable
    jQuery(document).ready(function($) {
        $(".clickable-row").click(function() {
            window.document.location = $(this).data("href");
        });
    });
	</script>
  </body>
</html>