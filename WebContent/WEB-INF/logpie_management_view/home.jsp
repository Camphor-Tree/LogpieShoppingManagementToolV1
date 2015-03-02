<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>

<tag:logpie_common_template>
    <jsp:body>
        <h3>你好, ${AdminName} 欢迎来到Logpie内部管理工具</h3>
        <h4>更新日志：</h4>
        <h5>2015-03-01 版本:Alpha 1.10</h5>
        <ol>
        <li>增加了包裹管理界面，支持查询所有包裹信息。</li>
        <li>增加了包裹修改界面，支持修改包裹信息。</li>
        </ol>
    </jsp:body>
</tag:logpie_common_template>

