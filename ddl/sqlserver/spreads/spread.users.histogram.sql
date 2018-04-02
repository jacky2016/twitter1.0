-------------------------
-- 用户转发/评论统计柱状图表
-------------------------
if not exists (select * from sys.objects where name ='Spread_Users_Histogram_Statis')
create table Spread_Users_Histogram_Statis(
	--spread id
	sid int not null,
	-- 转发/评论
	[type] int not null,
	-- 这个用户评论或者转发了这个微博	
	v1 int default(0),
	v2 int default(0),
	v3 int default(0),
	v4 int default(0),
	v5 int default(0),
	primary key(sid,type)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Histogram_Statis')
drop procedure sys_inc_Spread_Users_Histogram_Statis;

CREATE PROCEDURE sys_inc_Spread_Users_Histogram_Statis
	@sid int,
	@type int,
	@v1 int,
	@v2 int,
	@v3 int,
	@v4 int,
	@v5 int
AS
BEGIN
	 if(not exists (select * from Spread_Users_Histogram_Statis where @sid=sid and type=@type))
begin
	insert into Spread_Users_Histogram_Statis (sid,type,v1,v2,v3,v4,v5)
	values(@sid,@type,@v1,@v2,@v3,@v4,@v5)
end
else
begin
	update Spread_Users_Histogram_Statis set 
	v1 = v1+@v1,
	v2 = v2+@v2,
	v3 = v3+@v3,
	v4 = v4+@v4,
	v5 = v5+@v5
	where @sid=sid and type=@type
end
END;