if exists (select * from sys.objects where name ='s_users')
drop table s_users;

go
create table s_users(
	-- 内部唯一标识，新浪的永远都是int
	uid bigint not null primary key,
	-- 显示名
	screen_name nvarchar(30) null,
	-- 名称（通常和显示名是一样的）
	[name] nvarchar(30) null,
	-- 性别
	gender int null,
	-- 创建时间
	created_at bigint null,
	followers_count int default(0),
	friends_count int default(0),
	statuses_count int default(0),
	profile_image_url varchar(150) null,
	verified bit null,
	-- 城市编号
	city int null,
	-- 用户所在省级ID
	province int null,
	-- 地域信息
	location nvarchar(50) null,
	-- 描述信息
	description nvarchar(70) null,
	-- 帐号刷新信息
	-- 最后一次更新时间 
	[r_lastupdated] bigint null,
	-- 帐号的刷新级别
	[r_level] int default(0),
	-- sina特有的属性
	favourites int default(0),
	-- 用户的微博统一URL地址
	profile_url varchar(150) null,
	-- 个性化域名 
	[domain] varchar(100) null,
	-- 微号
	weihao bigint null,
	-- 博客的url
	[url] varchar(150) null,
	tags nvarchar(500) null,
	avatar_large varchar(150) null
);

go
if exists (select * from sys.objects where name ='sys_put_s_user')
drop proc sys_put_s_user;

go
-- 更新方法
create procedure [sys_put_s_user]
	@uid varchar(36),
	@screen_name nvarchar(30),
	@name nvarchar(30),
	@gender int,
	@created_at bigint,
	@followers_count int,
	@friends_count int,
	@statuses_count int,
	@profile_image_url varchar(150),
	@verified bit,
	@city int,
	@province int,
	@location nvarchar(50),
	@description nvarchar(50),
	@r_lastupdated bigint,
	@r_level int,
	@favourites int,
	@profile_url varchar(150),
	@domain varchar(40),
	@weihao bigint,
	@url varchar(150),
	@tags nvarchar(500),
	@avatar_large varchar(150),
	@isXunku bit,
	@out int output
AS
BEGIN
	set @out = 0
    if(not exists (select uid from s_users where uid=@uid))
    begin
		insert into s_users(
			uid,screen_name,[name],
			gender,created_at,followers_count,friends_count,statuses_count,
			profile_image_url,verified,city,province,location,description,
			r_lastupdated,[r_level],favourites,profile_url,[domain],weihao,
			[url],tags,avatar_large)
		values(
			@uid,@screen_name,@name,
			@gender,@created_at,@followers_count,@friends_count,@statuses_count,
			@profile_image_url,@verified,@city,	@province,@location,	@description,
			@r_lastupdated,@r_level,@favourites,@profile_url,@domain,@weihao,
			@url,	@tags,@avatar_large)
		set @out = 1
    end
    else
    begin
     	-- 如果是讯库来的用户，则不需要更新
		if(@isXunku=0)
		begin
			update s_users set screen_name=@screen_name,[name]=@name,
			gender=@gender,created_at=@created_at,followers_count=@followers_count,
			friends_count=@friends_count,statuses_count=@statuses_count,profile_image_url=@profile_image_url,
			verified=@verified,city=@city,province=@province,location=@location,description=@description,
			r_lastupdated=@r_lastupdated,[r_level]=@r_level,favourites=@favourites,
			profile_url=@profile_url,[domain]=@domain,weihao=@weihao,[url]=@url,
			tags=@tags,avatar_large=@avatar_large
			where uid=@uid
			set @out = 2
		end
    end
END

go


if exists (select * from sys.objects where name ='s_followers')
drop table s_followers;

go

-- 粉丝关系表
create table s_followers(
	uid bigint not null,
	follower_uid bigint not null,
	primary key(uid,follower_uid)
);

go

if exists (select * from sys.objects where name ='s_followings')
drop table s_followings;

go

-- 关注表
create table s_followings(
	uid bigint not null,
	following_uid bigint not null,
	primary key(uid,following_uid)
);
