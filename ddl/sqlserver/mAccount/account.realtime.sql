-- 帐号监测 实时监测

if not exists (select * from sys.objects where name ='MAccount_Fans_Statis')
create table MAccount_Fans_Statis(
	-- 监测帐号ID
	mid int not null,
	-- 时间区域
	timezone date,
	-- 当天粉丝数
	fans int default(0),
	primary key(mid,timezone)	
);

if not exists (select * from sys.objects where name ='MAccount_Trend_Statis')
create table MAccount_Trend_Statis(
	-- 监测帐号ID
	mid int not null,
	-- 时间区域
	timezone date,
	weibos int default(0),
	retweets int default(0),
	primary key(mid,timezone)	
);

if exists (select * from sys.objects where name ='sys_inc_MAccount_Fans_Statis')
drop procedure sys_inc_MAccount_Fans_Statis;

CREATE PROCEDURE sys_inc_MAccount_Fans_Statis
	@mid int,
	@timezone date,
	@fans int
AS
BEGIN
	if(not exists(select mid from MAccount_Fans_Statis where mid=@mid and timezone=@timezone))
	begin
		insert into MAccount_Fans_Statis(mid,timezone,fans)values
		(@mid,@timezone,@fans)
	end
	else
	begin
		update MAccount_Fans_Statis set fans=@fans where mid=@mid and timezone=@timezone
	end
END;


if exists (select * from sys.objects where name ='sys_inc_MAccount_Trend_Statis')
drop procedure sys_inc_MAccount_Trend_Statis;

CREATE PROCEDURE sys_inc_MAccount_Trend_Statis
	@mid int,
	@timezone date,
	@weibos int,
	@retweets int
AS
BEGIN
	if(not exists(select mid from MAccount_Trend_Statis where mid=@mid and timezone=@timezone))
	begin
		insert into MAccount_Trend_Statis(mid,timezone,weibos,retweets)values
		(@mid,@timezone,@weibos,@retweets)
	end
	else
	begin
		update MAccount_Trend_Statis set weibos=weibos+@weibos,retweets=retweets+@retweets
		 where mid=@mid and timezone=@timezone
	end
END;