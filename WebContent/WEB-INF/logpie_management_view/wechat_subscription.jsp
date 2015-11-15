<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row" style="margin-bottom:10px">
        	<h3>欢迎来到 微信订阅号管理</h3>
        	<h4>微信订阅号: <b>Logpie米国小买手</b></h4>
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
	        <div class="col-md-1">
	            <button type="button" class="btn btn-success" data-toggle="modal" data-target=".bs-example-modal-lg" style="font-size:16px;">新建回复规则</button>
			</div>
        </div>
      <div><h4>简单文本自动回复规则</h4></div>
      <table class="table table-striped text-center table-bordered" style="table-layout:fixed;vertical-align:middle;" >
        <thead>
        <tr class="info" style="font-size:15px;">
	        <th class="col-xs-1 col-md-1 text-center">编号</th>
	        <th class="col-xs-4 col-md-4 text-center">关键字</th>
	        <th class="col-xs-6 col-md-6 text-center">回复消息</th>
	        <th class="col-xs-1 col-md-1 text-center">规则激活</th>
	        <th class="col-xs-1 col-md-1 text-center">修改</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${textAutoReplyRuleList}" var="textAutoReplyRule">
        <tr style="font-size:16px" height="36" >
        <td class="anchor" style="color:#428bca"><a name="a${textAutoReplyRule.textAutoReplyRuleId}"><span style="padding-top: 65px; margin-top: -65px;"></span></a>${textAutoReplyRule.textAutoReplyRuleId}</td>
        <td>${textAutoReplyRule.textAutoReplyRuleKeyword}</td>
        <td>${textAutoReplyRule.textAutoReplyRuleReplyString}</td>
        <td>${textAutoReplyRule.textAutoReplyRuleActivated}</td>
        <td><a type="button" class="btn btn-warning" href=<c:url value="/text_auto_reply_rule/edit?id=${textAutoReplyRule.textAutoReplyRuleId}&anchor=a${textAutoReplyRule.textAutoReplyRuleId}" />>修改</a></td>
        </tr>
        </c:forEach>
        </tbody>
      </table>

    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#section-simple-text－auto-reply">简单文本回复</a></li>
            <li><a data-toggle="tab" href="#section-simple-text－auto-reply-test">简单文本回复测试</a></li>
          </ul>
          <div class="tab-content">
          <div id="section-simple-text－auto-reply" class="tab-pane fade in active" style="padding:20px">
              <h3>新建一个简单文本回复规则</h3>
              <form role="form" style="padding:20px" id="simple-text－auto-reply_form" action="<c:url value="/text_auto_reply/create" />" method="POST" >
                <div class="row">
	                <div class="form-group col-sm-12">
	                  <label for="auto_reply_rule">自动回复关键字规则</label>
	                  <input class="form-control" id="auto_reply_rule" name="TextAutoReplyRuleKeyword" value="[]" required autofocus>
	                </div>
				</div>
                <div class="row">
                  <div class="form-group col-sm-12">
                      <label for="auto_reply_string">自动回复文本</label>
                      <input class="form-control" id="auto_reply_string" name="TextAutoReplyRuleReplyString" required>
                  </div>
                </div>
                <div class="checkbox" style="padding-left:20px">
                  	 <input type="checkbox" checked="checked" id="product_is_activated" name="TextAutoReplyRuleActivated" value="True"/>激活该规则</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
              <h4>文档说明</h4>
              <div class="alert-success">
              <h5 class="text-info"><b>关键字规则</b></h5>
              <p>用户向订阅号发送的指令必须以关键字开头。 所以设定回复规则时必须设定关键字。关键字用英文中括号"[]"表示</p>
              <p>关键字规则可以设置参数。目前支持有2种类型的参数: 1.普通参数: 2. 并列参数 </p>
              <p>1. 普通参数: 参数用美元符号$$包裹。普通参数可有多个。例如: [注册] $realName$ $phoneNumber$ $weiboName$ 参数数量最多10个
              <p>2. 并列参数: 在普通参数后加星号 如:[订单查询] $id$＊</p>
              <p>注意如果您使用并列参数，则有且只能有一个并列参数。  额外的参数，哪怕时普通参数也不合法。使用并列参数后,用户可一次发多个并列参数 用空格分开。例如："订单查询 40 50"。系统会同时回复多个结果。</p>
              <h5 class="text-info"><b>自动回复文本</b></h5>
              <p>当满足关键字规则时，你想要发送给用户的文本。 自动回复文本中可以使用在关键字中设定的参数. 参数可以使用多次 如: "你查询的订单号$id$ 详情如下:http://t.logpie.com/order?id=$id$"</p>
              <h5 class="text-info"><b>例子</b></h5>
              <p>自动回复关键字规则: [订单查询] $id$*  自动回复文本：你查询的订单号$id$ 详情如下:http://t.logpie.com/order?id=$id$</p>
              <p>发送本文: 订单查询 20 30</p>
              <p>收到回复: 你查询的订单号20 详情如下:http://t.logpie.com/order?id=20 你查询的订单号30 详情如下:http://t.logpie.com/order?id=30</p>
              </div>
            </div>
            <div id="section-simple-text－auto-reply-test" class="tab-pane fade" style="padding:20px">
              <h3>测试简单文本回复规则</h3>
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/text_auto_reply/test" />" method="POST" >
                <div class="row">
	                <div class="form-group col-sm-12">
	                  <label for="auto_reply_rule">发送文本</label>
	                  <input class="form-control" id="auto_reply_rule" name="TestAutoReplyRequest" value="" required autofocus>
	                </div>
				</div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    </jsp:body>
</tag:logpie_common_template>