<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tag:logpie_common_template>
    <jsp:body>
    <div class="container">
        <div class="row">
            <h3>${AccountingType}</h3>
        </div>
       <script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1.1','packages':['corechart']}]}"></script>
       <div class="row">
       <div id="LineChart1" style="height: 600px;" class="col-md-12"></div>
       </div>
       <div class="row">
       <div id="LineChart2" style="height: 600px;" class="col-md-12"></div>
       </div>
       <script type="text/javascript">
       google.setOnLoadCallback(drawChart);
       function drawChart() {
         var data1 = google.visualization.arrayToDataTable([
             <c:if test="${chartType == 'OrderNumbers'}">
                   ['${LineChart2.chartKeyLabel}', '${LineChart2.chartValueLabel}'],
                   <c:forEach items="${LineChartDataList2}" var="entry">
                       [ '${entry.key}', ${entry.value} ],
                   </c:forEach>
             </c:if>
             <c:if test="${chartType == 'OrderProfits'}">    
	             <c:if test="${admin.isSuperAdmin == false}">
	                 ['${LineChart1.chartKeyLabel}','${LineChart1.chartValueLabel}'],
		             <c:forEach items="${LineChartDataList1}" var="entry">
		              [ '${entry.key}', ${entry.value} ],
		             </c:forEach>
	             </c:if>
	             <c:if test="${admin.isSuperAdmin == true}">
	                 ['${LineChart1.chartKeyLabel}','${LineChart1.chartValueLabel}','${LineChart1.chartValueLabel2}','${LineChart1.chartValueLabel3}'],
		             <c:forEach items="${LineChartDataList1}" var="entry">
		              [ '${entry.key}', ${entry.value1},${entry.value2},${entry.value3} ],
	                 </c:forEach>
	             </c:if>
             </c:if>
         ]);
         data1.addColumn({type: 'number', role: 'annotation'});
         var options1 = {
           title: '${LineChart1.chartLabel}',
           titleTextStyle: {fontSize: 22},
           curveType: 'line',
           annotations: { stemColor: 'white', textStyle: { fontSize: 16 } },
           pointSize: 7,
         };

         var chart = new google.visualization.LineChart(document.getElementById('LineChart1'));
         chart.draw(data1, options1);
         <c:if test="${LineChart2 != null}">
         var data2 = google.visualization.arrayToDataTable([
			<c:if test="${chartType == 'OrderNumbers'}">
			      ['${LineChart2.chartKeyLabel}', '${LineChart2.chartValueLabel}'],
			      <c:forEach items="${LineChartDataList2}" var="entry">
			       [ '${entry.key}', ${entry.value} ],
			      </c:forEach>
		   </c:if>
		   <c:if test="${chartType == 'OrderProfits'}">                 
	           <c:if test="${admin.isSuperAdmin == false}">
	                 ['${LineChart2.chartKeyLabel}', '${LineChart2.chartValueLabel}'],
		             <c:forEach items="${LineChartDataList2}" var="entry">
		              [ '${entry.key}', ${entry.value} ],
		             </c:forEach>
	           </c:if>
	           <c:if test="${admin.isSuperAdmin == true}">
	                  ['${LineChart2.chartKeyLabel}', '${LineChart2.chartValueLabel}','${LineChart2.chartValueLabel2}','${LineChart2.chartValueLabel3}'],
		             <c:forEach items="${LineChartDataList2}" var="entry">
		              [ '${entry.key}', ${entry.value1},${entry.value2},${entry.value3}],
	                 </c:forEach>
		        </c:if>
	        </c:if>
         ]);
         var options2 = {
                 title: '${LineChart2.chartLabel}',
                 titleTextStyle: {fontSize: 22},
                 curveType: 'line',
                 annotations: { stemColor: 'white', textStyle: { fontSize: 16 } },
        		 pointSize: 7,
          };
         
         var chart2 = new google.visualization.LineChart(document.getElementById('LineChart2'));
         chart2.draw(data2, options2);
     	 </c:if>
       }
       </script>
    </div>
    </jsp:body>
</tag:logpie_common_template>