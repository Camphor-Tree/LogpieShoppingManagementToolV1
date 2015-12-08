function getErrorMsg(jqXHR)
{
	if (jqXHR.status === 0) {
        msg = '网络出错，请检查网络';
    } else if (jqXHR.status == 404) {
        msg = '找不到该服务';
    } else if (jqXHR.status == 500) {
        msg = '服务器内部错误';
    } else if (exception === 'parsererror') {
        msg = 'json解析错误';
    } else if (exception === 'timeout') {
        msg = '页面请求超时';
    } else if (exception === 'abort') {
        msg = '请求被中止';
    } else {
        msg = '未知错误' + jqXHR.responseText;
    }
	return msg;
}
    
function refreshOrderRow(orderId, changedInfo, requestUrl)
{
	$.ajax({
        url : requestUrl+'?id='+orderId,
        error: function(jqXHR, textStatus, errorThrown) {
        	msg=getErrorMsg(jqXHR);
        	$('#quick_edit_result_'+orderId).html("<div class='text-danger'>修改"+changedInfo+"成功，但无法刷新当前订单信息,原因："+msg+"</div>");
        	 $('#quick_receive_money_submit_'+orderId).toggleClass('active');
        	},
        success : function(resultJSON) {
        	if(resultJSON.result=='success')
        	{
        		var orderJSON = resultJSON.order;
        		$('#quick_edit_result_'+orderId).html("<div class='text-success'>修改"+changedInfo+"成功</div>");
        		$('#OrderBuyerName_'+orderId).html(orderJSON.OrderBuyerName);
        		$('#OrderProductName_'+orderId).html(orderJSON.OrderProductName);
        		$('#OrderProductCount_'+orderId).html(orderJSON.OrderProductCount);
        		$('#OrderSellingPrice_'+orderId).html(orderJSON.OrderSellingPrice);
        		$('#OrderActualCost_'+orderId).html(orderJSON.OrderActualCost);
        		$('#OrderWeight_'+orderId).html(orderJSON.OrderWeight);
        		$('#OrderActualShippingFee_'+orderId).html(orderJSON.OrderActualShippingFee);
        		$('#OrderDomesticShippingFee_'+orderId).html(orderJSON.OrderDomesticShippingFee);
        		$('#OrderCustomerPaidDomesticShippingFee_'+orderId).html(orderJSON.OrderCustomerPaidDomesticShippingFee);
        		$('#OrderFinalActualCost_'+orderId).html(orderJSON.OrderFinalActualCost);
        		$('#OrderCustomerPaidMoney_'+orderId).html(orderJSON.OrderCustomerPaidMoney);
        		$('#OrderFinalProfit_'+orderId).html(orderJSON.OrderFinalProfit);
        		$('#OrderCompanyReceivedMoney_'+orderId).html(orderJSON.OrderCompanyReceivedMoney);
        		$('#OrderIsProfitPaid_'+orderId).html(orderJSON.OrderIsProfitPaid?"是":"否");
        		if(orderJSON.hasOwnProperty('OrderPackage')){
            		var pLink = $("<a />", {
            		    href : "/package?id="+orderJSON.OrderPackage.PackageId,
            		    text : orderJSON.OrderPackage.PackageDescription
            		});
            		$('#OrderPackage_'+orderId).html(pLink);
        		}
        		else
        		{
        			$('#OrderPackage_'+orderId).html("<input id=\"quick_set_package_input_"+orderId+"\" type=\"number\" class=\"col-xs-2\" style=\"padding-left:0px;padding-right:0px\" placeholder=\"输入包裹号\"></input><button id=\"quick_set_package_submit_"+orderId+"\" type=\"submit\" class=\"col-xs-1 btn-small has-spinner\" style=\"padding-left:0px;padding-right:0px;color:green;border: none;background:#e9e9e9;\" onclick=\"quickSetPackage("+orderId+")\"><span class=\"glyphicon glyphicon-ok spinner\" aria-hidden=\"true\"></span></button>");
        		}
        		$('#OrderNote_'+orderId).html("备注: "+orderJSON.OrderNote);
        	}
        	else
        	{
        	    $('#quick_edit_result_'+orderId).html("<div class='text-danger'>修改"+changedInfo+"成功，但无法刷新当前订单信息，原因：服务器返回错误结果"+resultJSON.reason+"</div>");
        	}
        	$('#quick_receive_money_submit_'+orderId).toggleClass('active');
        }
    });
}
function quickReceiveMoney(orderId, requestUrl, queryOrderUrl) {
	$('#quick_receive_money_submit_'+orderId).toggleClass('active');
	var params = {
        id: orderId,
        domestic_shipping_fee: $('#quick_receive_money_input_'+orderId).val(),
    };
    $.ajax({
        url : requestUrl + '?'+ $.param(params),
        error: function(jqXHR, textStatus, errorThrown) {
        	msg=getErrorMsg(jqXHR);
            $('#quick_edit_result_'+orderId).html("<div class='text-danger'>修改失败,原因:"+msg+"</div>");
            $('#quick_receive_money_submit_'+orderId).toggleClass('active');
        	},
        success : function(resultJSON) {
        	if(resultJSON.result=='success')
        	{
        		refreshOrderRow(orderId,'收款信息',queryOrderUrl);
        	}
        	else
        	{
        	    $('#quick_edit_result_'+orderId).html("<div class='text-danger'>修改失败,原因:"+resultJSON.reason+"</div>");
        	    $('#quick_receive_money_submit_'+orderId).toggleClass('active');
        	}
        }
    });
}

function quickSetPackage(orderId,requestUrl, queryOrderUrl) {
	$('#quick_set_package_submit_'+orderId).toggleClass('active');
	var params = {
	        id: orderId,
	        package_id: $('#quick_set_package_input_'+orderId).val(),
	    };
	packageId = $('#quick_set_package_input_'+orderId).val();
    $.ajax({
        url : requestUrl + '?'+ $.param(params),
        error: function(jqXHR, textStatus, errorThrown) {
        	msg=getErrorMsg(jqXHR);
            $('#quick_edit_result_'+orderId).html("<div class='text-danger'>修改失败,原因:"+msg+"</div>");
            $('#quick_set_package_submit_'+orderId).toggleClass('active');
        	},
        success : function(resultJSON) {
        	if(resultJSON.result=='success')
        	{
        		refreshOrderRow(orderId,'包裹',queryOrderUrl);
        	}
        	else
        	{
        	    $('#quick_edit_result_'+orderId).html("<div class='text-danger'>修改失败,原因:"+resultJSON.reason+"</div>");
        	    $('#quick_set_package_submit_'+orderId).toggleClass('active');
        	}
        }
    });
}