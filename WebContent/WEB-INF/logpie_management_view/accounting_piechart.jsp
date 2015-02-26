<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>${AccountingType}</h3>
        </div>
       <script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['corechart']}]}"></script>
       <div class="row">
           <div id="piechart1" style="height: 600px;" class="col-md-12"></div>
       </div>
       <c:if test="${PieChart2 != null}">
           <div class="row">
           <div id="piechart2" style="height: 600px;" class="col-md-12"></div>
           </div>
       </c:if>


       <script type="text/javascript">
       google.setOnLoadCallback(drawChart);
       function drawChart() {
         var data1 = google.visualization.arrayToDataTable([
             ['${PieChart1.chartKeyLabel}', '${PieChart1.chartValueLabel}'],
             <c:forEach items="${PieChartDataList1}" var="entry">
              [ '${entry.key}', ${entry.value} ],
             </c:forEach>
         ]);
         var options1 = {
           title: '${PieChart1.chartLabel}',
           titleTextStyle: {fontSize: 22},
           pieSliceText: 'label',
           
         };

         var chart = new google.visualization.PieChart(document.getElementById('piechart1'));
         chart.draw(data1, options1);
         <c:if test="${PieChart2 != null}">
         var data2 = google.visualization.arrayToDataTable([
              ['${PieChart2.chartKeyLabel}', '${PieChart2.chartValueLabel}'],
              <c:forEach items="${PieChartDataList2}" var="entry">
               [ '${entry.key}', ${entry.value} ],
              </c:forEach>
         ]);
         var options2 = {
           title: '${PieChart2.chartLabel}',
           titleTextStyle: {fontSize: 22},
           pieSliceText: 'label',
         };
         var chart2 = new google.visualization.PieChart(document.getElementById('piechart2'));
         chart2.draw(data2, options2);
     	 </c:if>
       }
       </script>
    </jsp:body>
</tag:logpie_common_template>