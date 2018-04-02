-- 性别比例统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_Grender_Statis')
create table Event_Grender_Statis(
	--事件id
	eventid int not null,
	--男性数量
	males int not null,
	--女性数量
	females int not null,
	--未知
	unsex int not null,
	--组合关键字
	primary key(eventid,males,females)
);

if exists (select * from sys.objects where name = 'sys_put_Event_Grender_Statis')
drop procedure sys_put_Event_Grender_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Grender_Statis
	@eventId int,
	@males int,
	@females int,
	@unsex   int
AS
BEGIN
	if not exists (select eventid from Event_Grender_Statis where eventid = @eventid)
	begin
		insert into Event_Grender_Statis (eventid,males,females,unsex)
		values (@eventid,@males,@females,@unsex)
	end
	else
	begin
		if(@males = 1)
		begin	
			set @males = @males+1
		end	
		else if(@females = 1)	
		begin
			set @females = @females+1
		end	
		else
		begin	
			set @unsex = @unsex+1
		end	
		update Event_Grender_Statis set males=@males,females=@females,unsex=@unsex where eventid=@eventid
	end
END;
