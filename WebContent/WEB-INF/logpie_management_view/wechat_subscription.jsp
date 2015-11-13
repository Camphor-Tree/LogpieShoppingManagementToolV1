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
      <div><h4>自动回复规则</h4></div>
      <table class="table table-striped text-center table-bordered" style="table-layout:fixed;vertical-align:middle;" >
        <thead>
        <tr class="info" style="font-size:15px;">
	        <th class="col-xs-2 col-md-2 text-center">编号</th>
	        <th class="col-xs-4 col-md-4 text-center">关键字</th>
	        <th class="col-xs-6 col-md-6 text-center">回复消息</th>
	        <th class="col-xs-2 col-md-2 text-center">修改</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${autoReplyRuleList}" var="autoReplyRule">
        <tr style="font-size:16px" height="36" >
        <td class="anchor" style="color:#428bca"><a name="a${autoReplyRule.settingId}"><span style="padding-top: 65px; margin-top: -65px;"></span></a>${autoReplyRule.settingId}</td>
        <td>${autoReplyRule.settingKey}</td>
        <td>${autoReplyRule.settingValue}</td>
        <td><a type="button" class="btn-small btn-info" href=<c:url value="/auto_reply/edit?id=${order.orderId}&ru=${CurrentUrl}&anchor=a${order.orderId}" />>修改</a></td>
        </tr>
        </c:forEach>
        </tbody>
      </table>

    <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#section-simple-text－auto-reply">简单文本回复</a></li>
            <li><a data-toggle="tab" href="#section-img－text－auto-reply">图文回复</a></li>
          </ul>
          <div class="tab-content">
          <div id="section-simple-text－auto-reply" class="tab-pane fade in active" style="padding:20px">
              <h3>新建一个简单文本回复规则</h3>
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/text_auto_reply/create" />" method="POST" >
                <div class="row">
	                <div class="form-group col-sm-12">
	                  <label for="auto_reply_rule">自动回复关键字规则</label>
	                  <input class="form-control" id="auto_reply_rule" name="TextAutoReplyRule" value="[]" required autofocus>
	                </div>
				</div>
                <div class="row">
                  <div class="form-group col-sm-12">
                      <label for="auto_reply_string">自动回复文本</label>
                      <input class="form-control" id="auto_reply_string" name="TextAutoReplyReplyString" required>
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