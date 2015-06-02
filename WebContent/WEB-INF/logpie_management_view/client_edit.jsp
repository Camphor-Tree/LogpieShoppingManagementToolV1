<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row"  style="padding:20px">
            <h3>修改用户档案: ${client.clientShowName}</h3>
        </div>
          <div class="tab-pane fade in active">
             <form role="form" style="padding:20px" action="<c:url value="/client/edit" />" method="post">
                <input name="ClientId" value="${client.clientId}" type="hidden">
                <div class="row">
	                <div class="form-group col-sm-4">
	                  <label for="client_real_name">用户名字</label>
	                  <input type="text" class="form-control" id="client_real_name" name="ClientRealName" value="${client.clientRealName}">
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="client_wechat_name">用户微信名</label>
	                  <input class="form-control" id="client_wechat_name" name="ClientWechatName" value="${client.clientWechatName}">
	                </div>
	                <div class="form-group col-sm-4">
	                  <label for="client_wechat_number">用户微信号</label>
	                  <input class="form-control" id="client_wechat_number" name="ClientWechatNumber" value="${client.clientWechatNumber}">
	                </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                  <label for="client_weibo_name">用户微博名</label>
                  <input class="form-control" id="client_weibo_name" name="ClientWeiboName" value="${client.clientWeiboName}">
                  </div>
                  <div class="form-group col-sm-6">
                  <label for="client_taobao_name">用户淘宝名</label>
                  <input class="form-control" id="client_taobao_name" name="ClientTaobaoName" value="${client.clientTaobaoName}">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-12">
                  <label for="client_address">用户地址</label>
                  <input class="form-control" id="client_address" name="ClientAddress" value="${client.clientAddress}">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-6">
                  <label for="client_phone">用户手机号</label>
                  <input class="form-control" id="client_weibo_name" name="ClientPhone" value="${client.clientPhone}">
                  </div>
                  <div class="form-group col-sm-6">
                  <label for="client_postal_code">用户邮编</label>
                  <input class="form-control" id="client_postal_code" name="ClientPostalCode" value="${client.clientPostalCode}">
                  </div>
                </div>
                <div class="row">
                  <div class="form-group col-sm-12">
                  <label for="client_note">用户备注</label>
                  <input class="form-control" id="client_note" name="ClientNote" value="${client.clientNote}">
                  </div>
                </div>
                <button type="submit" class="btn btn-primary btn-block">确定</button>
              </form>
          </div>
    </jsp:body>
</tag:logpie_common_template>