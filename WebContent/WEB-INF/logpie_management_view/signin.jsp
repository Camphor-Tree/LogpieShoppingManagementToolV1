<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://logpieimage.oss-cn-qingdao.aliyuncs.com/weblogo.png">
    <title>欢迎登陆Logpie后台管理</title>
    <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/css/signin.css" rel="stylesheet" >
  </head>

  <body>

    <div class="container">
      <form class="form-signin" action="/LogpieShopping/signin" method="post">
        <h2 class="form-signin-heading">登陆logpie管理系统</h2>
        <label for="inputEmail" class="sr-only">管理员email</label>
        <input type="email" id="inputEmail" name="email" class="form-control" placeholder="管理员 email" required autofocus>
        <label for="inputPassword" class="sr-only">管理员 密码</label>
        <input type="password" id="inputPassword" name="password" class="form-control" value="fakePassword" placeholder="管理员 密码" required>
		<!--  Currently we don't support remember me
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> 信任这台设备
          </label>
        </div>
        -->
        <button class="btn btn-lg btn-primary btn-block" type="submit">登 陆</button>
      </form>
    </div> <!-- /container -->
  </body>
</html>