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
    <link href="resources/css/offcanvas.css" rel="stylesheet">
    <link href="resources/css/footer.css" rel="stylesheet">
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
            <li class="active"><a href="<c:url value="/home" />">主页</a></li>
			<li><a href="<c:url value="/order_management" />">订单管理</a></li>
			<li><a href="<c:url value="/package_management" />">包裹管理</a></li>
			<li><a href="<c:url value="/accounting" />">财务报表</a></li>
			<li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">更多<span class="caret"></span></a>
	          <ul class="dropdown-menu" role="menu">
	            <li><a href="<c:url value="/category_management" />">分类管理</a></li>
	            <li><a href="<c:url value="/brand_management" />">品牌管理</a></li>
	            <li><a href="<c:url value="/image_management" />">图片管理</a></li>
	            <li><a href="<c:url value="/product_management" />">商品管理</a></li>
	            <li class="divider"></li>
	            <li><a href="<c:url value="/account_management" />">账号管理</a></li>
	            <li class="divider"></li>
	            <li><a href="<c:url value="/logout" />">退出登录</a></li>
	          </ul>
       	    </li>
          </ul>
        </div>
      </div>
    </nav>
    <div class="container">
        <div class="bs-example">
            <jsp:doBody/>
        </div>
    </div>
    <footer class="logpiefooter">
        <p>&copy; <b>www.logpie.com 2015</b></p>
    </footer>
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
    <script src="resources/js/offcanvas.js"></script>
  </body>
</html>