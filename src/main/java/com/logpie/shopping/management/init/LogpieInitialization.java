package com.logpie.shopping.management.init;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.logpie.shopping.management.storage.LogpieDataSourceFactory;

public class LogpieInitialization
{
    Logger LOG = Logger.getLogger(LogpieInitialization.class);

    public void init()
    {
        try
        {
            initDatabase();
        } catch (Exception e)
        {
            LOG.error("error happens when init the database", e);
        }
    }

    private void initDatabase()
    {
        SimpleDriverDataSource dataSource = LogpieDataSourceFactory.getDataSource();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        LOG.debug("Start to creating tables...");

        jdbcTemplate
                .execute("create table if not exists Images(ImageId serial primary key, ImageUrl text not null, ImageDescription text)");
        jdbcTemplate
                .execute("create table if not exists Categories(CategoryId serial primary key, CategoryName text not null)");
        jdbcTemplate
                .execute("create table if not exists Brands(BrandId serial primary key, BrandEnglishName text not null, BrandChineseName text, BrandIsActivated boolean not null default true, BrandImageId bigint unsigned, BrandSizeChartId bigint unsigned, BrandCategoryId bigint unsigned not null, foreign key (BrandImageId) references Images(ImageId) on update cascade on delete cascade, foreign key (BrandSizeChartId) references Images(ImageId) on update cascade on delete cascade, foreign key (BrandCategoryId) references Categories(CategoryId) on update cascade on delete cascade)");
        jdbcTemplate
                .execute("create table if not exists Products(ProductId serial primary key, ProductName text not null, ProductDescription text, ProductLink text, ProductWeight int unsigned, ProductIsActivated boolean not null default true, ProductPostDate timestamp not null default CURRENT_TIMESTAMP, ProductImageId bigint unsigned, ProductBrandId bigint unsigned, foreign key (BrandId) references Brands(BrandId) on update cascade on delete cascade, foreign key (ProductImageId) references Images(ImageId) on update cascade on delete cascade)");
        // jdbcTemplate
        // .execute("create table if not exists ProductSpec(ProductSpecId serial primary key, ProductId bigint unsigned not null, Size text, Color text, Weight int not null, EstimatedShippingFee float not null, OriginalCost float not null, OriginalPrice float not null, IsOnsale boolean not null default false, OnsaleCost float, OnsalePrice float, Availability boolean not null default true, ImageId bigint unsigned not null, foreign key (ProductId) references Products(ProductId) on update cascade on delete cascade, foreign key (ImageId) references Images(ImageId) on update cascade on delete cascade)");
        // jdbcTemplate
        // .execute("create table if not exists Reviews(ReviewId serial primary key, ReviewContent text not null, ReviewDate timestamp not null default current_timestamp, ReviewerName text, ProductId bigint unsigned not null, foreign key (ProductId) references Products(ProductId) on update cascade on delete cascade)");
        jdbcTemplate
                .execute("create table if not exists Packages(PackageId serial primary key, PackageProxyName text not null, PackageTrackingNumber text not null, PackageReceiver text not null, PackageDestination text not null, PackageDate timestamp not null, PackageWeight int not null, PackageShippingFee int not null, PackageAdditionalCustomTaxFee float, PackageAdditionalInsuranceFee float, PackageIsShipped boolean not null default false, PackageIsDelivered boolean not null default false, PackageNote text)");
        jdbcTemplate
                .execute("create table if not exists Admins(AdminId serial primary key, AdminName text not null, AdminPassword text not null, AdminEmail text not null, AdminQQ int, AdminWechat text, AdminPhone int not null, AdminIdentityNumber int)");
        jdbcTemplate
                .execute("create table if not exists Orders(OrderId serial primary key, OrderDate timestamp not null default current_timestamp, OrderProductId bigint unsigned not null, OrderProductCount int not null, OrderBuyerName text not null, OrderProxyId bigint unsigned, OrderProxyProfitPercentage float default 0.4 not null, OrderCurrencyRate float default 6.23 not null, OrderPackageId bigint unsigned, OrderEstimatedShippingFee bigint not null, OrderActualShippingFee bigint, OrderSellingPrice bigint not null, OrderCustomerPaidMoney bigint not null, OrderFinalProfit bigint, OrderIsProfitPaid boolean not null default false, OrderNote text, foreign key (OrderProxy) references Admins(OrderAdminId) on update cascade on delete cascade)");
        // jdbcTemplate
        // .execute("create table if not exists ExchangeRate(Date timestamp primary key default current_timestamp, Rate float not null)");
        // jdbcTemplate
        // .execute("create table if not exists OrderRecord(RecordId serial primary key, OrderId bigint unsigned not null, ProductSpecId bigint unsigned not null, FinalBuyCost float, FinalSellPrice float not null, FinalShippingFee float, FinalRevenue float, ReceiptImageId bigint unsigned, PackageId bigint unsigned, foreign key (OrderId) references Orders(OrderId) on update cascade on delete cascade, foreign key (ProductSpecId) references ProductSpec(ProductSpecId) on update cascade on delete cascade, foreign key (ReceiptImageId) references Images(ImageId) on update cascade on delete cascade, foreign key (PackageId) references Packages(PackageId) on update cascade on delete cascade)");
        // jdbcTemplate
        // .execute("create table if not exists Authorization(AuthorizationId serial primary key, AuthorizationName text not null, AuthorizationTable text not null, AuthorizationRight text not null, SubDescription text)");
        // jdbcTemplate
        // .execute("create table if not exists AuthorizationOwner(AuthorizationOwnerId serial primary key, AdminId bigint unsigned not null, AuthorizationId bigint unsigned not null, foreign key (AdminId) references Admins(AdminId) on update cascade on delete cascade, foreign key (AuthorizationId) references Authorization(AuthorizationId) on update cascade on delete cascade)");
        LOG.debug("Finish creating tables...");

    }
}
