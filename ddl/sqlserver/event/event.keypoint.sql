-- 关键观点统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_KeyPoint_Statis')
create table Event_KeyPoint_Statis(
	-- 事件id
	eventid int not null,
	-- 微博发布的时间
	created bigint not null,
	--微博id
	tid bigint not null,
	--平台类型
	platform int not null,
	--该条微博转发数
	reposts int not null,
	--组合关键字
	primary key(eventid,tid,platform)
);

if exists (select * from sys.objects where name ='sys_put_Event_KeyPoint_Statis')
drop procedure sys_put_Event_KeyPoint_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_KeyPoint_Statis
	@eventid int,
	@tid bigint,
	@platform int,
	@reposts int,
	@created bigint
AS
BEGIN
	
	if not exists (select eventid from Event_KeyPoint_Statis where eventid=@eventid and tid=@tid and platform=@platform)
	begin
		insert into Event_KeyPoint_Statis (eventId,tid,platform,reposts,created)
		values(@eventid,@tid,@platform,@reposts,@created)
	end
	else 
	begin
		update Event_KeyPoint_Statis set reposts=@reposts where eventid=@eventid and tid=@tid and platform=@platform
	end
	
END;
