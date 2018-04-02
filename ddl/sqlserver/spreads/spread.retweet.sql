-------------------------
-- 关键用户，已传播按粉丝数统计
-------------------------
if not exists (select * from sys.objects where name ='Spread_Retweet_Fans_Statis')
create table Spread_Retweet_Fans_Statis(
	-- spread id
	sid int not null,
	-- 这条微博转发了sid
	tid varchar(36) not null,
	-- tid是什么时间创建的
	created datetime not null,
	-- tid又被转发了多少次，二次转发次数
	-- 转发数 
	retweets int not null default(0),
	-- tid是谁创建的
	[name] nvarchar(50) not null,
	-- 作者是哪个区域的
	location varchar(150),
	-- 这个作者有多少粉丝
	followers int not null default(0),
	-- 这个作者是不是vip
	vip bit not null default(0),
	--组合关键字
	primary key(sid,[tid])
);


if exists (select * from sys.objects where name ='sys_inc_Spread_Retweet_Fans_Statis')
drop procedure sys_inc_Spread_Retweet_Fans_Statis;

CREATE PROCEDURE sys_inc_Spread_Retweet_Fans_Statis
	@sid int,
	@tid varchar(36),
	@created datetime,
	@retweets int,
	@name nvarchar(36),
	@location varchar(100),
	@followers int,
	@vip bit
AS
BEGIN
	if(not exists(select sid from Spread_Retweet_Fans_Statis where sid=@sid and @tid=tid))
	begin
		insert into Spread_Retweet_Fans_Statis(sid,tid,created,retweets,name,location,followers,vip)values
		(@sid,@tid,@created,@retweets,@name,@location,@followers,@vip)
	end
	else
	begin
		update Spread_Retweet_Fans_Statis set retweets =@retweets,followers=@followers where sid=@sid and @tid=tid
	end
END;

---------------------------------------------------------------------------------------------------------------------
-------------------------
-- 关键用户，已传播按转发数统计
-------------------------
if not exists (select * from sys.objects where name ='Spread_Retweet_Nums_Statis')
create table Spread_Retweet_Nums_Statis(
	-- spread id
	sid int not null,
	-- 这条微博转发了sid
	tid varchar(36) not null,
	-- tid是什么时间创建的
	created datetime not null,
	-- tid又被转发了多少次，二次转发次数
	-- 转发数 
	retweets int not null default(0),
	-- tid是谁创建的
	[name] nvarchar(50) not null,
	-- 作者是哪个区域的
	location varchar(150),
	-- 这个作者有多少粉丝
	followers int not null default(0),
	-- 这个作者是不是vip
	vip bit not null default(0),
	--组合关键字
	primary key(sid,[tid])
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Retweet_Nums_Statis')
drop procedure sys_inc_Spread_Retweet_Nums_Statis;

CREATE PROCEDURE sys_inc_Spread_Retweet_Nums_Statis
	@sid int,
	@tid varchar(36),
	@created datetime,
	@retweets int,
	@name nvarchar(36),
	@location varchar(100),
	@followers int,
	@vip bit
AS
BEGIN
	if(not exists(select sid from Spread_Retweet_Nums_Statis where sid=@sid and @tid=tid))
	begin
		insert into Spread_Retweet_Nums_Statis(sid,tid,created,retweets,name,location,followers,vip)values
		(@sid,@tid,@created,@retweets,@name,@location,@followers,@vip)
	end
	else
	begin
		update Spread_Retweet_Nums_Statis set retweets =@retweets,followers=@followers where sid=@sid and @tid=tid
	end
END;

---------------------------------------------------------------------------------------------------------------------
-------------------------
-- 关键用户，未传播
-------------------------
if not exists (select * from sys.objects where name ='Spread_Retweet_Unspread_Statis')
create table Spread_Retweet_Unspread_Statis(
	-- spread id
	sid int not null,
	-- 这条微博转发了sid
	tid varchar(36) not null,
	-- tid是什么时间创建的
	created datetime not null,
	-- tid又被转发了多少次，二次转发次数
	-- 转发数，这里转发数是0
	retweets int not null default(0),
	-- tid是谁创建的
	[name] nvarchar(50) not null,
	-- 作者是哪个区域的
	location varchar(150),
	-- 这个作者有多少粉丝
	followers int not null default(0),
	-- 这个作者是不是vip
	vip bit not null default(0),
	--组合关键字
	primary key(sid,[tid])
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Retweet_Unspread_Statis')
drop procedure sys_inc_Spread_Retweet_Unspread_Statis;

CREATE PROCEDURE sys_inc_Spread_Retweet_Unspread_Statis
	@sid int,
	@tid varchar(36),
	@created datetime,
	@retweets int,
	@name nvarchar(36),
	@location varchar(100),
	@followers int,
	@vip bit
AS
BEGIN
	if(not exists(select sid from Spread_Retweet_Unspread_Statis where sid=@sid and @tid=tid))
	begin
		insert into Spread_Retweet_Unspread_Statis(sid,tid,created,retweets,name,location,followers,vip)values
		(@sid,@tid,@created,@retweets,@name,@location,@followers,@vip)
	end
	else
	begin
		update Spread_Retweet_Unspread_Statis set retweets =@retweets,followers=@followers where sid=@sid and @tid=tid
	end
END;

-------------------------
-- 转发态度
-------------------------
