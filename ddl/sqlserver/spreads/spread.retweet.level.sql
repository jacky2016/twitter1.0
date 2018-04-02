-------------------------
-- 转发层级
-------------------------
if not exists (select * from sys.objects where name ='Spread_Retweet_Level_Statis')
create table Spread_Retweet_Level_Statis(
	-- spread id
	sid int not null,
	-- 这条微博转发了sid
	tid varchar(36) not null,
	-- 转发数，这里转发数是0
	retweets int not null default(0),
	-- tid是谁创建的，这个就是关键用户
	[ucode] nvarchar(36) not null,
	-- 这个作者有多少粉丝
	followers int not null default(0),
	-- 这个作者是不是vip
	vip bit not null default(0),
	-- 当前层数
	[level] int not null,
	--每个监测对象每一层只能有一个一样的帖子
	primary key(sid,[tid],[level])
);


if exists (select * from sys.objects where name ='sys_inc_Spread_Retweet_Level_Statis')
drop procedure sys_inc_Spread_Retweet_Level_Statis;

CREATE PROCEDURE sys_inc_Spread_Retweet_Level_Statis
	@sid int,
	@tid varchar(36),
	@retweets int,
	@ucode nvarchar(36),
	@followers int,
	@vip bit,
	@level int
AS
BEGIN
	if(not exists(select sid from Spread_Retweet_Level_Statis where sid=@sid and @tid=tid and level=@level))
	begin
		insert into Spread_Retweet_Level_Statis(sid,tid,retweets,ucode,followers,vip,level)values
		(@sid,@tid,@retweets,@ucode,@followers,@vip,@level)
	end
	else
	begin
		update Spread_Retweet_Level_Statis set retweets =@retweets,followers=@followers where sid=@sid and @tid=tid and level=@level
	end
END;