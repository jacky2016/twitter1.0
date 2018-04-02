if exists (select * from sys.objects where name ='t_users')
drop table t_users;

go
create table t_users(
	-- 内部唯一标识，新浪的是name
	[name] varchar(30) not null primary key,
	-- openid
	openid nvarchar(36) null,
	city_code int null,
	country_code int null,
	-- 地区代码
	province_code int null,
	-- 听众数 粉丝数
	fansnum int default(0),
	-- 收藏数
	favnum int default(0),
	head varchar(150) null,
	-- 个人主页
	homepage varchar(150),
	-- 收听人数 关注数
	idolnum int default(0),
	-- 个人说明
	introduction nvarchar(150) null,
	isvip bit,
	location nvarchar(50),
	-- 互听好友数
	mutual_fans_num int default(0),
	-- 注册时间
	regtime bigint,
	sex int,
	tag nvarchar(500),
	tweetnum int default(0),
	-- 认证信息,
	verifyinfo nvarchar(50),
	-- 经验值
	exp varchar(10),
	-- 微博等级
	level int,
	r_level int default(0),
	r_lastupdated bigint
);

go
if exists (select * from sys.objects where name ='sys_put_t_user')
drop proc sys_put_t_user;

go
-- 更新方法
create procedure [sys_put_t_user]
	@name varchar(30),
	@openid nvarchar(36),
	@city_code int,
	@country_code int,
	@province_code int,
	@fansnum int,
	@favnum int,
	@head varchar(150),
	@homepage varchar(150),
	@idolnum int,
	@introduction nvarchar(150),
	@isvip bit,
	@location nvarchar(50),
	@mutual_fans_num int,
	@regtime bigint,
	@sex int,
	@tag nvarchar(500),
	@tweetnum int,
	@verifyinfo nvarchar(50),
	@exp varchar(10),
	@level int,
	@r_level int,
	@r_lastupdated bigint,
	@isXunku bit,
	@out int output
AS
BEGIN
	set @out = 0
    if(not exists (select name from t_users where name=@name))
    begin
		insert into t_users([name],openid,city_code,country_code,
			province_code,fansnum,favnum,head,homepage,idolnum,
			introduction,isvip,location,mutual_fans_num,regtime,
			sex,tag,tweetnum,verifyinfo,exp,level,r_level,r_lastupdated)
		values(@name,@openid,@city_code,@country_code,
			@province_code,@fansnum,@favnum,@head,@homepage,@idolnum,
			@introduction,@isvip,@location,@mutual_fans_num,@regtime,
			@sex,@tag,@tweetnum,@verifyinfo,@exp,@level,@r_level,@r_lastupdated)
		set @out = 1
    end
    else
    begin
     	-- 如果是讯库来的用户，则不需要更新
		if(@isXunku=0)
		begin
			update t_users set city_code=@city_code,country_code=@country_code,
			province_code=@province_code,fansnum=@fansnum,favnum=@favnum,
			head=@head,homepage=@homepage,idolnum=@idolnum,
			introduction=@introduction,isvip=@isvip,location=@location,
			mutual_fans_num=@mutual_fans_num,regtime=@regtime,
			sex=@sex,tag=@tag,tweetnum=@tweetnum,verifyinfo=@verifyinfo,
			exp=@exp,level=@level,r_level=@r_level,r_lastupdated=@r_lastupdated
			where [name]=@name
			set @out = 2
		end
    end
END

go


if exists (select * from sys.objects where name ='t_followers')
drop table t_followers;

go

-- 粉丝关系表
create table t_followers(
	[name] varchar(36) not null,
	follower_name varchar(36) not null,
	primary key([name],[follower_name])
);

go

if exists (select * from sys.objects where name ='t_followings')
drop table t_followings;

go

-- 关注表
create table t_followings(
	[name] varchar(36) not null,
	following_name varchar(36) not null,
	primary key([name],following_name)
);
