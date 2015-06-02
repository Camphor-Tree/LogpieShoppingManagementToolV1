package com.logpie.shopping.management.init;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.LogpieDataSourceFactory;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * This class is used to finish Logpie system initialization
 * 
 * @author zhoyilei
 *
 */
public class LogpieInitialization
{
    Logger LOG = Logger.getLogger(LogpieInitialization.class);

    public void init()
    {
        try
        {
            initDatabase();
            initSuperAdminAccount();
        } catch (Exception e)
        {
            LOG.error("error happens when init the database", e);
        }
    }

    private void initDatabase()
    {
        // Currently, need to manually run the command below to create a
        // database and enable utf-8 encoding
        // "CREATE DATABASE `Logpie` CHARACTER SET utf8 COLLATE utf8_general_ci;"
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
                .execute("create table if not exists Products(ProductId serial primary key, ProductName text not null, ProductDescription text, ProductLink text, ProductWeight int unsigned, ProductIsActivated boolean not null default true, ProductPostDate timestamp not null default CURRENT_TIMESTAMP, ProductImageId bigint unsigned, ProductBrandId bigint unsigned, foreign key (ProductBrandId) references Brands(BrandId) on update cascade on delete cascade, foreign key (ProductImageId) references Images(ImageId) on update cascade on delete cascade)");
        // jdbcTemplate
        // .execute("create table if not exists ProductSpec(ProductSpecId serial primary key, ProductId bigint unsigned not null, Size text, Color text, Weight int not null, EstimatedShippingFee float not null, OriginalCost float not null, OriginalPrice float not null, IsOnsale boolean not null default false, OnsaleCost float, OnsalePrice float, Availability boolean not null default true, ImageId bigint unsigned not null, foreign key (ProductId) references Products(ProductId) on update cascade on delete cascade, foreign key (ImageId) references Images(ImageId) on update cascade on delete cascade)");
        // jdbcTemplate
        // .execute("create table if not exists Reviews(ReviewId serial primary key, ReviewContent text not null, ReviewDate timestamp not null default current_timestamp, ReviewerName text, ProductId bigint unsigned not null, foreign key (ProductId) references Products(ProductId) on update cascade on delete cascade)");
        jdbcTemplate
                .execute("create table if not exists Packages(PackageId serial primary key, PackageProxyName text not null, PackageTrackingNumber text not null, PackageReceiver text not null, PackageDestination text not null, PackageDate timestamp not null, PackageWeight int not null, PackageShippingFee int not null, PackageAdditionalCustomTaxFee float, PackageAdditionalInsuranceFee float, PackageIsShipped boolean not null default false, PackageIsDelivered boolean not null default false, PackageNote text)");
        jdbcTemplate
                .execute("create table if not exists Admins(AdminId serial primary key, AdminName text not null, AdminPassword text not null, AdminEmail text not null, AdminQQ text, AdminWechat text, AdminPhone text not null, AdminIdentityNumber text)");
        jdbcTemplate
                .execute("create table if not exists Orders(OrderId serial primary key, OrderDate timestamp not null default current_timestamp, OrderProductId bigint unsigned not null, OrderProductCount int not null, OrderWeight float default 0, OrderBuyerName text not null, OrderProxyId bigint unsigned, OrderProxyProfitPercentage float not null, OrderActualCost float, OrderCurrencyRate float not null, OrderPackageId bigint unsigned, OrderEstimatedShippingFee float not null, OrderActualShippingFee float, OrderSellingPrice float not null, OrderCustomerPaidMoney float,OrderDomesticShippingFee float default 0, OrderCustomerPaidDomesticShippingFee float default 0, OrderCompanyReceivedMoney float, OrderIsProfitPaid boolean not null default false, OrderSentToUser boolean default false, OrderNote text, foreign key (OrderPackageId) references Packages(PackageId) on update cascade on delete cascade, foreign key (OrderProxyId) references Admins(AdminId) on update cascade on delete cascade, foreign key (OrderProductId) references Products(ProductId) on update cascade on delete cascade)");
        jdbcTemplate
                .execute("create table if not exists DbLog(DbLogId serial primary key, DbLogAdminId bigint unsigned, DbLogTime timestamp not null default current_timestamp, DBLogSQL text not null, DBLogComment text,foreign key (DbLogAdminId) references Admins(AdminId))");
        jdbcTemplate
                .execute("create table if not exists Coupons(CouponId serial primary key, CouponCode text not null)");
        jdbcTemplate
                .execute("create table if not exists SettleDownRecords(SettleDownRecordId serial primary key, SettleDownRecordAdmin bigint unsigned, SettleDownRecordDate timestamp not null default current_timestamp, SettleDownRecordInfo text, foreign key (SettleDownRecordAdmin) references Admins(AdminId) on update cascade on delete cascade)");
        jdbcTemplate
                .execute("create table if not exists Clients(ClientId serial primary key, ClientRealName text, ClientWechatName text, ClientWechatNumber text,ClientWeiboName text,ClientTaobaoName text,ClientAddress text,ClientPostalCode text,ClientPhone text,ClientNote text,ClientJoinTime timestamp not null default current_timestamp)");
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

    private void initSuperAdminAccount()
    {
        final AdminDAO adminDAO = new AdminDAO(null);
        final List<Admin> adminList = adminDAO.getAllAdmins();
        // If there is no account, then create a super admin
        if (CollectionUtils.isEmpty(adminList))
        {
            final Admin superAdmin = new Admin("许嘉航", "admin@logpie.com", "313541388",
                    "logpiezxqy", "2065197113", "123456", "1", "haohaoxuexi");
            final Admin adminSuzhou = new Admin("乔梦颖", "278489810@qq.com", "278489810",
                    "ElenaQ222", "18626158611", "123456", "1", "qiaomengying");
            final Admin adminShanghai = new Admin("杨秋菊", "391810590@qq.com", "391810590",
                    "Xwy030705yqj", "18916555359", "123456", "1", "yangqiuju");
            final Admin adminZhongying = new Admin("仲瑛", "8904143@qq.com", "8904143", "8904143",
                    "123456", "123456", "1", "zhongying");
            final Admin adminSihua = new Admin("司华", "sihua@qq.com", "123456", "123456", "123456",
                    "123456", "1", "sihua");
            final boolean initSuperAdminSuccess = adminDAO.addAdmin(superAdmin);
            final boolean initSuzhouAdminSuccess = adminDAO.addAdmin(adminSuzhou);
            final boolean initShanghaiAdminSuccess = adminDAO.addAdmin(adminShanghai);
            final boolean initAdminZhongyingSuccess = adminDAO.addAdmin(adminZhongying);
            final boolean initAdminSihuaSuccess = adminDAO.addAdmin(adminSihua);
            if (initSuperAdminSuccess && initSuzhouAdminSuccess && initShanghaiAdminSuccess
                    && initAdminZhongyingSuccess && initAdminSihuaSuccess)
            {
                LOG.debug("init admins success");
            }
        }

    }
}
