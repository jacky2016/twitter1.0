-- 事件趋势统计表
if not exists (select * from sys.objects where name ='Event_Trend_Statis')
create table Event_Trend_Statis(
	--事件id
	eventid int not null,
	--发布时间，这里按小时保存
	[timezone] datetime not null,
	--这个时刻发布的帖子ID
	tid varchar(36) not null,
	--组合关键字
	primary key(eventid,[timezone],tid)
);

if exists (select * from sys.objects where name ='sys_put_Event_Trend_Statis')
drop procedure sys_put_Event_Trend_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Trend_Statis
	@eventId int,
	@timezone datetime,
	@tid varchar(36)
AS
BEGIN

	if not exists (select eventid from Event_Trend_Statis where eventid=@eventid and tid=@tid and timezone=@timezone)
	begin
		insert into Event_Trend_Statis (eventid,timezone,tid)
		values(@eventid,@timezone,@tid)
	end
END;

-- 查询逻辑
