<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<tag:logpie_common_template>
    <jsp:body>
	<div class="container-fluid col-md-3 col-sm-3">
      <div id="calculate_div">
        <b>原 价： </b><input id="original_price" type="text">
        <br/><br/>
        <b>折 扣： </b><input id="discount" type="text">
        <br/><br/>
	    <b>利润率：</b><input id="revenue_rate" type="text" value="0.15">
        <br/><br/>
        <input id="has_tax" type="checkbox" checked>含9.5%的税 <button type="button" class="btn-primary btn btn-sm" style="margin-left:20px" onclick="refreshFastTable();">刷新快捷表</button>
        <br/><br/>
        <button type="button" class="btn-success btn btn-block" onclick="calculate();">计算</button>
        <br/>
		  <ul id="calculate_result">
		  </ul>
      <hr/>
      <div id="show_div" style="margin-top:20px;">
        <table class="table table-condensed" id="price_table" cellpadding="1" cellspacing="1">
		   <tr>
            <td>商品成本</td>
            <td>税后售价</td>
            <td>无税售价</td>
          </tr>
        </table>
      </div>
    </div>
    </div>
    <script type="text/javascript">
      function getPrice(revenue_rate){
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
		
        for(var i=5;i<=100;i+=5){
          var row = document.createElement("tr");
          var cell_1 = document.createElement("td");
          cell_1.innerHTML = i + "美金";
          row.appendChild(cell_1);

          var cell_2 = document.createElement("td");
          var finalprice = (i*revenue_rate+i*1.095)*6.27;
          cell_2.innerHTML = finalprice.toFixed(0) + "元";
          row.appendChild(cell_2);

          var cell_3 = document.createElement("td");
          var finalprice = (i*(1+revenue_rate))*6.27;
          cell_3.innerHTML = finalprice.toFixed(0) + "元";
          row.appendChild(cell_3);

          document.getElementById("price_table").appendChild(row);
        }
      }
        function refreshFastTable()
        {
            var revenue_rate = parseFloat(document.getElementById("revenue_rate").value);
            getPrice(revenue_rate);
        }
		
	   function initFastTable(){
		getPrice(0.2);
	   }


      function calculate(){
        var original_price = document.getElementById("original_price").value;
        var discount = document.getElementById("discount").value;
        var has_tax = document.getElementById("has_tax").checked;
		var revenue_rate = parseFloat(document.getElementById("revenue_rate").value);
        
        var final_price;
        if(discount!=0)
        {
          if(has_tax==true)
          {
            final_price = (original_price*discount*revenue_rate+original_price*discount*1.095)*6.27;
          }else
          {
            final_price = original_price*discount*(1+revenue_rate)*6.27;
          }
        }else
        {
          if(has_tax==true)
          {
            final_price = (original_price*revenue_rate+original_price*1.095)*6.27;
          }else
          {
            final_price = original_price*(1+revenue_rate)*6.27;
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
      refreshFastTable();
    </script>
    </jsp:body>
</tag:logpie_common_template>