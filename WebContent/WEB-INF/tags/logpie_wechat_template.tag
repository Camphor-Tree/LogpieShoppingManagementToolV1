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
    <title>Logpie微信公众号</title>
    <link href="//res.wx.qq.com/open/libs/weui/1.0.2/weui.min.css" rel="stylesheet">
    <script src="//lib.sinaapp.com/js/jquery/2.0/jquery.min.js"></script>
    <script src="<c:url value="/resources/js/offcanvas.js" />"></script>
  </head>
  <body>
    <div class="container" style="width:100%; ">
        <div>
            <jsp:doBody/>
        </div>
    </div>
   <div class="weui-footer">
       <p class="weui-footer__text">Copyright &copy; 2014-<%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> logpie.com</p>
   </div>
  </body>
</html>