<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<tag:logpie_common_template>
    <jsp:body>
	<div class="container-fluid col-md-3 col-sm-3">
      <div id="calculate_div">
        <div class="form-group ">
        <label for="original_price"><b>原 价:</b></label><input id="original_price" class="form-control" type="text" placeholder="可输入多个价格 用空格分开" required>
        </div>
        <div class="form-group">
        <label for="original_price"><b>折 扣:</b></label>
        <input id="discount" class="form-control" type="text" placeholder="商品指定折扣 默认没有">
        </div>
        <div class="form-group">
        <label for="original_price"><b>利润率:</b></label>
        <input id="revenue_rate" class="form-control" type="text" placeholder="指定利润率">
        </div>
        <input id="has_tax" type="checkbox" checked>含9.6%的税 <button type="button" class="btn-primary btn btn-sm" style="margin-left:20px" onclick="refreshFastTable();">刷新快捷表</button>
        <br><br/>
        <button type="button" class="btn-success btn btn-block" onclick="calculate();">计算</button>
        <div class="alert-info">定价默认规则：原价10刀以下利润率0.25， 原价10到30刀（包括10刀）利润率0.2， 30刀以上（含30刀）利润率0.15</div>
        <br/>
		  <ul id="calculate_result">
		  </ul>
      <hr/>
      <div id="show_div" style="margin-top:20px;">
        <table class="table table-condensed" id="price_table" cellpadding="1" cellspacing="1">
        </table>
      </div>
    </div>
    </div>
    <script type="text/javascript">
      function calculatePrices(setRevenueRate){
		//clean html
	    document.getElementById("price_table").innerHTML="";
		//build header
		var aaa = document.createElement("tr");
		var cost_header = document.createElement("td");
		cost_header.innerHTML = "商品成本";
		aaa.appendChild(cost_header);
		var price_after_tax = document.createElement("td");
		price_after_tax.innerHTML = "税后售价";
		aaa.appendChild(price_after_tax);
		var price_no_tax = document.createElement("td");
		price_no_tax.innerHTML = "无税售价";
		aaa.appendChild(price_no_tax);
		document.getElementById("price_table").appendChild(aaa);
        for(var i=1;i<=100;i+=1){
          var revenue_rate = 0;
          if(setRevenueRate===-1)
          {
        	  revenue_rate = getRevenueRate(i);
          }
          else
          {
        	  revenue_rate = setRevenueRate;
          }
          
          var row = document.createElement("tr");
          var cell_1 = document.createElement("td");
          cell_1.innerHTML = i + "美金";
          row.appendChild(cell_1);

          var cell_2 = document.createElement("td");
          var finalprice = (i*revenue_rate+i*1.096)*${CurrencyRate};
          cell_2.innerHTML = finalprice.toFixed(0) + "元";
          row.appendChild(cell_2);

          var cell_3 = document.createElement("td");
          var finalprice = (i*(1+revenue_rate))*${CurrencyRate};
          cell_3.innerHTML = finalprice.toFixed(0) + "元";
          row.appendChild(cell_3);

          document.getElementById("price_table").appendChild(row);
        }
      }
      
       function refreshFastTable()
       {
    	   var revenue_rate_string = document.getElementById("revenue_rate").value;
           if(revenue_rate_string==='')
           {
        	   calculatePrices(-1);
           }
           else
           {
        	   var revenue_rate = parseFloat(revenue_rate_string);
        	   calculatePrices(revenue_rate);
           }
          
       }

	   function getRevenueRate(original_price)
	   {
           if(original_price>=0 && original_price<10)
           {
        	   return 0.25;
           }else if(original_price>=10 && original_price<30)
           {
        	   return 0.2;
           }else
           {
        	   return 0.15;
           }
	   }


      function calculate(){
        var original_price_string = document.getElementById("original_price").value;
        var original_prices = new Array();
        original_prices = original_price_string.split(' ');
        var discount = document.getElementById("discount").value;
        var has_tax = document.getElementById("has_tax").checked;
        var revenue_rate_string = document.getElementById("revenue_rate").value;
        var revenue_rate = 0;
        if(revenue_rate_string==='')
        {
            revenue_rate = getRevenueRate(original_price);
        }
        else
        {
		    revenue_rate = parseFloat(revenue_rate_string);
        }
        
        for(index in original_prices)
        {
        	var original_price = parseInt(original_prices[index], 10);
	        var final_price;
	        if(discount!=0)
	        {
	          if(has_tax==true)
	          {
	            final_price = (original_price*discount*revenue_rate+original_price*discount*1.096)*${CurrencyRate};
	          }else
	          {
	            final_price = original_price*discount*(1+revenue_rate)*${CurrencyRate};
	          }
	        }else
	        {
	          if(has_tax==true)
	          {
	            final_price = (original_price*revenue_rate+original_price*1.096)*${CurrencyRate};
	          }else
	          {
	            final_price = original_price*(1+revenue_rate)*${CurrencyRate};
	          }
	        }
	        var element = document.createElement("LI");
			
			if(discount!=0)
			{
	        element.innerHTML = "$" + original_price + " 利润率"+ revenue_rate+"且折扣" + discount + "的价格：￥" + final_price.toFixed(0);
			}else
			{
			element.innerHTML = "$" + original_price + " 利润率"+ revenue_rate+"的价格：￥" + final_price.toFixed(0);
			}
			var list = document.getElementById("calculate_result");
			list.insertBefore(element, list.childNodes[0]);
        }
      }
      refreshFastTable();
    </script>
    </jsp:body>
</tag:logpie_common_template>