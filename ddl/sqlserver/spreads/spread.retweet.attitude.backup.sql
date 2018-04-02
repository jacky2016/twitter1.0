-------------------------
-- 转发态度
-------------------------
if not exists (select * from sys.objects where name ='Spread_Retweet_Attitude_Statis')
create table Spread_Retweet_Attitude_Statis(
	sid int not null,
	tid varchar(36),
	followers int not null default(0),
	-- 转发观点
	attitude nvarchar(300),
	primary key(sid,tid)	
);


if exists (select * from sys.objects where name ='sys_inc_Spread_Retweet_Attitude_Statis')
drop procedure sys_inc_Spread_Retweet_Attitude_Statis;

CREATE PROCEDURE sys_inc_Spread_Retweet_Attitude_Statis
	@sid int,
	@tid varchar(36),
	@followers int,
	@attitude nvarchar(300)
AS
BEGIN
	if(not exists(select sid from Spread_Retweet_Attitude_Statis where sid=@sid and tid=@tid))
	begin
		insert into Spread_Retweet_Attitude_Statis(sid,tid,followers,attitude)values
		(@sid,@tid,@followers,@attitude)
	end
	else
	begin
		update Spread_Retweet_Attitude_Statis set followers=@followers where sid=@sid and tid=@tid
	end
END;