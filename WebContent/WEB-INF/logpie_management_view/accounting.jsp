<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>欢迎来到Logpie 财务报表</h3>
        </div>
        <script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['corechart']}]}"></script>
       <div id="piechart" style="width: 900px; height: 500px;"></div>
       <script type="text/javascript">
       google.setOnLoadCallback(drawChart);
       function drawChart() {

         var data = google.visualization.arrayToDataTable([
             ['产品类别', '订单量'],
             <c:forEach items="${pieDataList}" var="entry">
              [ '${entry.key}', ${entry.value} ],
             </c:forEach>
         ]);

         var options = {
           title: '订单所属类别分布',
           pieSliceText: 'label',
         };

         var chart = new google.visualization.PieChart(document.getElementById('piechart'));

         chart.draw(data, options);
       }
       </script>
    </jsp:body>
</tag:logpie_common_template>