-- 转发用户/评论用户粉丝数
if not exists (select * from sys.objects where name ='Spread_Users_Fans_Statis')
create table Spread_Users_Fans_Statis(
	--spread id
	sid int not null,
	type int not null,
	-- 0-100个粉丝的个数
	c100 int not null default(0),
	-- 100-1000粉丝
	c1000 int not null default(0),
	-- 1000-1W的粉丝
	c1w int not null default(0),
	-- 1w-10w的粉丝
	c10w int not null default(0),
	-- 超过10w粉丝的
	c100w int not null default(0)
	primary key(sid,type)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Fans_Statis')
drop procedure sys_inc_Spread_Users_Fans_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_Fans_Statis
	@sid int,
	@type int,
	@c100 int,
	@c1000 int,
	@c1w int,
	@c10w int,
	@c100w int
AS
BEGIN
	if(not exists (select sid from Spread_Users_Fans_Statis where sid=@sid and type=@type))
	begin
		insert into Spread_Users_Fans_Statis(sid,type,c100,c1000,c1w,c10w,c100w)
		values(@sid,@type,0,0,0,0,0)
	end
		update Spread_Users_Fans_Statis set c100 = c100+@c100,
		c1000 = c1000+@c1000,c1w=c1w+@c1w,c10w=c10w+@c10w,
		c100w=c100w+@c100w where sid=@sid and type=@type
END;
