-- 传播分析相关的脚本

------------------------------------------------------------------------------------------------------
-- 微博转发来源统计表
if not exists (select * from sys.objects where name ='Weibo_Repost_Source_An')
create table Weibo_Repost_Source_An(
	id int identity(1,1) not null primary key,
	-- 要监测的微博ID，该ID是元数据库中创建监测项时生成的
	weiboid int not null,
	-- 发布类型：转发/评论  
	[type] int not null, 
	-- 微博发布来源
	[source] int not null,
	-- 转发或者评论的次数？ 
	[value] int not null 
);

-- 更新转发来源
if exists (select * from sys.objects where name ='sys_put_Weibo_Repost_Source_An')
drop procedure sys_put_Weibo_Repost_Source_An;

create PROCEDURE sys_put_Weibo_Repost_Source_An 
	@weiboid int,
	@type int,
	@source int
AS
BEGIN
	update weibo_repost_source_an set value=value+1 
	where weiboid = @weiboid and [type] = @type and [source] = @source
END;
------------------------------------------------------------------------------------------------------

-- 微博转发观点统计表
if not exists (select * from sys.objects where name ='Weibo_Repost_Opinion_An')
create table Weibo_Repost_Opinion_An(
	id int identity(1,1) not null primary key,
	-- 监测的微博系统id
	weiboid int NOT NULL, 
	-- 转发时带文字的数量
	opinion int NOT NULL, 
	-- 转发时不带文字的数量
	unopinion int NOT NULL 
);

-- 更新转发观点统计表
if exists (select * from sys.objects where name ='sys_put_Weibo_Repost_Opinion_An')
drop procedure sys_put_Weibo_Repost_Opinion_An;

create PROCEDURE sys_put_Weibo_Repost_Opinion_An
	@weiboid int,
	@op int,
	@unop int
AS
BEGIN
	if(not exists(select weiboid from Weibo_Repost_Opinion_An where @weiboid=weiboid))
	begin
		insert into Weibo_Repost_Opinion_An(weiboid,opinion,unopinion)
		values(@weiboid,@op,@unop)
	end
	else
	begin
		update Weibo_Repost_Opinion_An set opinion=opinion+@op,
		unopinion=unopinion+@unop where @weiboid=weiboid 
	end
END;

------------------------------------------------------------------------------------------------------
-- 微博转发区域统计表
if not exists (select * from sys.objects where name ='Weibo_Repost_Local_An')
create table Weibo_Repost_Local_An(
	id int identity(1,1) not null primary key,
	-- 监测的微博，系统id
	weiboid int NOT NULL,
	--发布类型 
	[type] int NOT NULL,
	--转发或者评论的作者所在的城市
	city int NOT NULL,
	--作者所在的区域
	location nvarchar(150) NULL,
	-- 这个城市一共有多少发布的 
	[value] int NOT NULL 
);

-- 更新转发地域统计表
if exists (select * from sys.objects where name ='sys_put_Weibo_Repost_Local_An')
drop procedure sys_put_Weibo_Repost_Local_An;

CREATE PROCEDURE sys_put_Weibo_Repost_Local_An
	@weiboid int,
	@type int,
	@city int,
	@location nvarchar(50)
AS
BEGIN
	if(not exists(select weiboid from Weibo_Repost_Local_An where @weiboid=weiboid and @type=[type]
	and city=@city and location=@location))
	begin
		insert into Weibo_Repost_Local_An(weiboid,[type],city,location,value)
		values(@weiboid,@type,@city,@location,1)
	end
	else
	begin
		update Weibo_Repost_Local_An set value = value+1 where @weiboid=weiboid and @type=[type]
	and city=@city and location=@location
	end
END;
------------------------------------------------------------------------------------------------------
-- 微博转发关键词热度分析
-- 此表数据可以同步也可以线下异步计算获得
if not exists (select * from sys.objects where name ='Weibo_Repost_Hot_An')
create table Weibo_Repost_Hot_An(
	id int identity(1,1) not null primary key,
	-- 监测微博系统id
	weiboid int NOT NULL,
	--发布类型 
	[type] int NOT NULL,
	--关键字
	keyword nvarchar(150) NOT NULL,
	--该关键词出现的次数
	times int NOT NULL,
	--最后一次更新时间
	updated datetime NOT NULL
);

------------------------------------------------------------------------------------------------------
-- 粉丝人气统计表
if not exists (select * from sys.objects where name ='Weibo_Repost_Fans_Pop_An')
create table Weibo_Repost_Fans_Pop_An(
	id int identity(1,1) not null primary key,
	weiboid int NOT NULL,
	-- 发布类型
	[type] int NOT NULL,
	-- 0-100粉丝有多少 
	v1 int NOT NULL, 
	-- 100-1000粉丝的数量
	v2 int NOT NULL, 
	-- 1000-10000粉丝的数量
	v3 int NOT NULL, 
	--1万-10万粉丝的数量
	v4 int NOT NULL,
	--10万-100万粉丝的数量 
	v5 int NOT NULL,
	--百万以上粉丝的有多少
	v6 int NOT NULL
);

-- 更新粉丝人气统计表
if exists (select * from sys.objects where name ='sys_put_Weibo_Repost_Fans_Pop_An')
drop procedure sys_put_Weibo_Repost_Fans_Pop_An;

create PROCEDURE sys_put_Weibo_Repost_Fans_Pop_An
	@weiboid int,
	@type int,
	@v1 int,
	@v2 int,
	@v3 int,
	@v4 int,
	@v5 int,
	@v6 int
AS
BEGIN
	if(not exists(select weiboid from Weibo_Repost_Fans_Pop_An where @weiboid=weiboid and [type] =@type))
	begin
		insert into Weibo_Repost_Fans_Pop_An(weiboid,[type],v1,v2,v3,v4,v5,v6)
		values(@weiboid,@type,@v1,@v2,@v3,@v4,@v5,@v6)
	end
	else
	begin
		update Weibo_Repost_Fans_Pop_An set v1=v1+@v1,
		v2=v2+@v2,v3=v3+@v3,v4=v4+@v4,v5=v5+@v5
		where @weiboid=weiboid and [type] = @type
	end
END;
------------------------------------------------------------------------------------------------------
-- 转发用户统计表
if not exists (select * from sys.objects where name ='Weibo_Repost_Count_An')
create table Weibo_Repost_Count_An(
	id int identity(1,1) not null primary key,
	-- 微博id
	weiboid int NOT NULL, 
	[type] int NOT NULL,
	--转发此微博有多少vip
	vip int NOT NULL, 
	--转发此微博的有多少不是vip
	novip int NOT NULL,
	--转发的女人有多少
	female int NOT NULL,
	--转发的男人有多少
	male int NOT NULL,
	--转发的不知性别的有多少
	unsex int NOT NULL
);

-- 更新转发用户统计表
if exists (select * from sys.objects where name ='sys_put_Weibo_Repost_Count_An')
drop procedure sys_put_Weibo_Repost_Count_An;

create PROCEDURE [dbo].[sys_put_Weibo_Repost_Count_An] 
	@weiboid int,
	@type int,
	@vip int,
	@novip int,
	@female int,
	@male int,
	@unsex int
AS
BEGIN
	update Weibo_Repost_Count_An set 
	vip=vip+@vip,novip=novip+@novip,
	female=female+@female,male=@male+male,
	unsex = unsex+@unsex
	where weiboid = @weiboid and [type] = @type
END;

------------------------------------------------------------------------------------------------------
if not exists (select * from sys.objects where name ='Weibo_Ages_An')
--年龄分布统计表
create table Weibo_Ages_An(
	id int identity(1,1) not null primary key,
	-- 微博id
	weiboid int NOT NULL, 
	-- 微博类型
	[type] int NOT NULL,
	-- 保存年龄分组信息
	[value] varchar(150) null
)


