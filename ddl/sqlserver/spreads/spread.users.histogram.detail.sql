-------------------------
-- 用户转发/评论统计柱状图表 带详细信息
-------------------------
if not exists (select * from sys.objects where name ='Spreads_Histogram_Detail_Statis')
create table Spreads_Histogram_Detail_Statis(
	--spread id
	sid int not null,
	-- 转发/评论
	[type] int not null,
	-- 这个用户评论或者转发了这个微博	
	uid varchar(36) not null,
	-- 这个用户有多少粉丝数
	followers int not null default(0),
	primary key(sid,type,uid)
);

if exists (select * from sys.objects where name ='sys_inc_Spreads_Histogram_Detail_Statis')
drop procedure sys_inc_Spreads_Histogram_Detail_Statis;

CREATE PROCEDURE sys_inc_Spreads_Histogram_Detail_Statis
	@sid int,
	@type int,
	@uid varchar(36),
	@followers int
AS
BEGIN
	 if(not exists (select * from Spreads_Histogram_Detail_Statis where @sid=sid and type=@type and uid=@uid))
begin
	insert into Spreads_Histogram_Detail_Statis (sid,type,uid,followers)
	values(@sid,@type,@uid,@followers)
end
else
begin
	update Spreads_Histogram_Detail_Statis set followers = @followers
	where @sid=sid and type=@type and uid=@uid
end
END;