package com.logpie.shopping.management.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class Initialization {

	public void init() {
		initDatabase();
	}

	private void initDatabase() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:8889/Logpie");
		dataSource.setUsername("root");
		dataSource.setPassword("root");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		System.out.println("Start to creating tables...");
		jdbcTemplate.execute("drop table if exists AuthorizationOwner");
		jdbcTemplate.execute("drop table if exists Authorization");
		jdbcTemplate.execute("drop table if exists OrderRecord");
		jdbcTemplate.execute("drop table if exists ExchangeRate");
		jdbcTemplate.execute("drop table if exists Orders");
		jdbcTemplate.execute("drop table if exists Admins");
		jdbcTemplate.execute("drop table if exists Packages");
		jdbcTemplate.execute("drop table if exists Reviews");
		jdbcTemplate.execute("drop table if exists ProductSpec");
		jdbcTemplate.execute("drop table if exists Products");
		jdbcTemplate.execute("drop table if exists Brands");
		jdbcTemplate.execute("drop table if exists Images");
		jdbcTemplate.execute("drop table if exists Categories");
		jdbcTemplate
				.execute("create table Images(ImageId serial primary key, ImageUrl text not null, ImageDescription text)");
		jdbcTemplate
				.execute("create table Categories(CategoryId serial primary key, CategoryName text not null, IsActivated boolean not null default true)");
		jdbcTemplate
				.execute("create table Brands(BrandId serial primary key, BrandEnglishName text not null, BrandChineseName text, IsActivated boolean not null default true, BrandImageId bigint unsigned not null, BrandSizeChartId bigint unsigned, CategoryId bigint unsigned not null, foreign key (BrandImageId) references Images(ImageId) on update cascade on delete cascade, foreign key (BrandSizeChartId) references Images(ImageId) on update cascade on delete cascade, foreign key (CategoryId) references Categories(CategoryId) on update cascade on delete cascade)");
		jdbcTemplate
				.execute("create table Products(ProductId serial primary key, ProductName text not null, ProductDescription text, ShippingCategory char(1) not null, ProductRating decimal(2,1), IsActivated boolean not null default true, PostDate datetime not null, BrandId bigint unsigned not null, foreign key (BrandId) references Brands(BrandId) on update cascade on delete cascade)");
		jdbcTemplate
				.execute("create table ProductSpec(ProductSpecId serial primary key, ProductId bigint unsigned not null, Size text, Color text, Weight int not null, EstimatedShippingFee float not null, OriginalCost float not null, OriginalPrice float not null, IsOnsale boolean not null default false, OnsaleCost float, OnsalePrice float, Availability boolean not null default true, ImageId bigint unsigned not null, foreign key (ProductId) references Products(ProductId) on update cascade on delete cascade, foreign key (ImageId) references Images(ImageId) on update cascade on delete cascade)");
		jdbcTemplate
				.execute("create table Reviews(ReviewId serial primary key, ReviewContent text not null, ReviewDate timestamp not null default current_timestamp, ReviewerName text, ProductId bigint unsigned not null, foreign key (ProductId) references Products(ProductId) on update cascade on delete cascade)");
		jdbcTemplate
				.execute("create table Packages(PackageId serial primary key, TrackingNumber text not null, PackageReceiver text not null, PackageDestination text not null, PackageDate datetime not null, PackageWeight int not null, ShippingCategory char(1) not null, ShippingFee float not null, CustomTaxFee float, InsuranceFee float, IsShipped boolean not null default false, IsArrived boolean not null default false, PackageNote text)");
		jdbcTemplate
				.execute("create table Admins(AdminId serial primary key, AdminName text not null, AdminPassword text not null, AdminEmail text not null, AdminQQ int, AdminWechat text, AdminPhone int not null, AdminIdentityNumber int)");
		jdbcTemplate
				.execute("create table Orders(OrderId serial primary key, OrderDate timestamp not null default current_timestamp, OrderPrice float not null, IsDirectShipping boolean not null default false, DirectShippingFee float, UserName text not null, UserAddress text not null, AdminId bigint unsigned not null, IsPending boolean not null default true, IsPaid boolean not null default false, IsProcessing boolean not null default false, IsCompleted boolean not null default false, IsCanceled boolean not null default false, OrderNote text, foreign key (AdminId) references Admins(AdminId) on update cascade on delete cascade)");
		jdbcTemplate
				.execute("create table ExchangeRate(Date timestamp primary key default current_timestamp, Rate float not null)");
		jdbcTemplate
				.execute("create table OrderRecord(RecordId serial primary key, OrderId bigint unsigned not null, ProductSpecId bigint unsigned not null, FinalBuyCost float, FinalSellPrice float not null, FinalShippingFee float, FinalRevenue float, ReceiptImageId bigint unsigned, PackageId bigint unsigned, foreign key (OrderId) references Orders(OrderId) on update cascade on delete cascade, foreign key (ProductSpecId) references ProductSpec(ProductSpecId) on update cascade on delete cascade, foreign key (ReceiptImageId) references Images(ImageId) on update cascade on delete cascade, foreign key (PackageId) references Packages(PackageId) on update cascade on delete cascade)");
		jdbcTemplate
				.execute("create table Authorization(AuthorizationId serial primary key, AuthorizationName text not null, AuthorizationTable text not null, AuthorizationRight text not null, SubDescription text)");
		jdbcTemplate
				.execute("create table AuthorizationOwner(AuthorizationOwnerId serial primary key, AdminId bigint unsigned not null, AuthorizationId bigint unsigned not null, foreign key (AdminId) references Admins(AdminId) on update cascade on delete cascade, foreign key (AuthorizationId) references Authorization(AuthorizationId) on update cascade on delete cascade)");
		System.out.println("Finish creating tables...");

	}
}
