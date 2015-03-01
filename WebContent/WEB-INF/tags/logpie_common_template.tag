<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
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
          <a class="navbar-brand" href="/LogpieShopping/home">Logpie米国小买手</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="/LogpieShopping/home">主页</a></li>
			<li><a href="/LogpieShopping/order_management">订单管理</a></li>
			<li><a href="/LogpieShopping/package_management">包裹管理</a></li>
			<li><a href="/LogpieShopping/accounting">财务报表</a></li>
			<li><a href="/LogpieShopping/logout">退出登录,${AdminName}</a></li>
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