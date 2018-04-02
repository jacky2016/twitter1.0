-------------------------
-- 传播分析趋势统计表
-------------------------
if not exists (select * from sys.objects where name ='Spread_Trend_Statis')
create table Spread_Trend_Statis(
	--spread id
	sid int not null,
	--发布时间，这里按小时保存
	[timezone] datetime not null,
	-- 转发/评论
	[type] int not null,
	--这个时刻发了多少
	[value] int not null default(0),
	--组合关键字
	primary key(sid,[timezone],[type])
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Trend_Statis')
drop procedure sys_inc_Spread_Trend_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_inc_Spread_Trend_Statis
	@sid int,
	@timezone datetime,
	@type int
AS
BEGIN
if(not exists (select sid from Spread_Trend_Statis where sid=@sid and [type] = @type and timezone=@timezone ))
begin
	insert into Spread_Trend_Statis(sid,timezone,type,value)values(
	@sid,@timezone,@type,1)
end
else
begin
	update Spread_Trend_Statis set value=value+1
	where sid=@sid and [type] = @type and timezone=@timezone 
end
END;