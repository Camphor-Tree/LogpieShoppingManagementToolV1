<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags/" %>
<tag:logpie_common_template>
    <jsp:body>
        <div class="row">
            <h3>欢迎来到 订单管理</h3>
        </div>
        <div class="row">
            <p><a class="btn btn-xs btn-success" href="create.php">Create</a></p>
        <table class="table table-striped table-bordered table-hover">
        <tr>
        <th>订单Id</th>
        <th>商品名称</th>
        <th>商品数量</th>
        <th>商品重量</th>
        <th>订单购买者</th>
        <th>订单代理者</th>
        <th>订单日期</th>
        <th>订单购买最终价(美元)</th>
        <th>订单当日汇率</th>
        <th>订单包裹信息</th>
        <th>订单估计运费(人民币)</th>
        <th>订单实际运费(人民币)</th>
        <th>订单售价</th>
        <th>订单最终利润</th>
        <th>订单代理分红百分比</th>
        <th>商品尺寸大小备注</th>
        </tr>
        <tbody>
        </tbody>
        </table>
        </div><!-- /row -->
    </jsp:body>
</tag:logpie_common_template>

