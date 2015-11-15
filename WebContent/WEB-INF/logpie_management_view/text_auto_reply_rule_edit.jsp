<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改简单文本回复： ${textAutoReplyRule.textAutoReplyRuleKeyword}</h3>
        </div>
          <div id="section-order" class="tab-pane fade in active">
              <form role="form" style="padding:20px" id="order_creation_form" action="<c:url value="/text_auto_reply_rule/edit" />" method="POST" >
                <input type="hidden" name="TextAutoReplyRuleId" value="${textAutoReplyRule.textAutoReplyRuleId}">
                <div class="row">
	                <div class="form-group col-sm-12">
	                  <label for="auto_reply_rule">自动回复关键字规则</label>
	                  <input class="form-control" id="auto_reply_rule" name="TextAutoReplyRuleKeyword" value="${textAutoReplyRule.textAutoReplyRuleKeyword}" required autofocus>
	                </div>
				</div>
                <div class="row">
                  <div class="form-group col-sm-12">
                      <label for="auto_reply_string">自动回复文本</label>
                      <input class="form-control" id="auto_reply_string" name="TextAutoReplyRuleReplyString" value="${textAutoReplyRule.textAutoReplyRuleReplyString}" required>
                  </div>
                </div>
                <div class="checkbox" style="padding-left:20px">
                  	 <input type="checkbox" id="product_is_activated" name="TextAutoReplyRuleActivated" value="True" <c:if test="${textAutoReplyRule.textAutoReplyRuleActivated==true}">checked</c:if>/>激活该规则</label>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
         </div>
    </jsp:body>
</tag:logpie_common_template>