-------------------------
-- 转发态度
-------------------------
if not exists (select * from sys.objects where name ='Spread_Retweet_Attitude_Statis')
create table Spread_Retweet_Attitude_Statis(
	sid int not null,
	attitude_cnt int default(0),
	no_attitude_cnt int default(0),
	primary key(sid)	
);


if exists (select * from sys.objects where name ='sys_inc_Spread_Retweet_Attitude_Statis')
drop procedure sys_inc_Spread_Retweet_Attitude_Statis;

CREATE PROCEDURE sys_inc_Spread_Retweet_Attitude_Statis
	@sid int,
	@cnt int,
	@nocnt int
AS
BEGIN
	if(not exists(select sid from Spread_Retweet_Attitude_Statis where sid=@sid))
	begin
		insert into Spread_Retweet_Attitude_Statis(sid,attitude_cnt,no_attitude_cnt)values
		(@sid,@cnt,@nocnt)
	end
	else
	begin
		update Spread_Retweet_Attitude_Statis set attitude_cnt=attitude_cnt+@cnt,
		no_attitude_cnt=no_attitude_cnt+@nocnt
		where sid=@sid
	end
END;