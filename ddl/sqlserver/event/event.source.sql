-- 来源分布统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_Source_Statis')
create table Event_Source_Statis(
	--事件id
	eventid int not null,
	--来源id
	sourceid int not null,
	--来源数量
	[value] int not null,
	--组合关键字
	primary key(eventid,sourceid,value)
);

if exists (select * from sys.objects where name ='sys_put_Event_Source_Statis')
drop procedure sys_put_Event_Source_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Source_Statis
	@eventid int,
	@sourceid int,
	@value int
AS
declare
@total   int,
@temp    int
BEGIN
	if not exists (select eventid from Event_Source_Statis where eventid=@eventid)
	begin
		insert into Event_Source_Statis (eventid,sourceid,value) values (@eventid,@sourceid,@value)
	end
	else
	begin
		if exists(select sourceid from Event_Source_Statis where eventid=@eventid and sourceid=@sourceid)
		begin
			select @temp=value from Event_Source_Statis where eventid=@eventid and sourceid=@sourceid
			set @total = @value + @temp
			update Event_Source_Statis set value=@total where eventid=@eventid and sourceid=@sourceid
		end
		else
		begin
			insert into Event_Source_Statis (eventid,sourceid,value) values (@eventid,@sourceid,@value)		
		end
	end	
END;
