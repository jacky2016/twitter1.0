-- 地域统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_Local_Statis')
create table Event_Local_Statis(
	--事件id
	eventid int not null,
	--地域
	city int not null,
	--地域的数量
	[value] int not null,
	--组合关键字
	primary key(eventid,city,value)
);

if exists (select * from sys.objects where name ='sys_put_Event_Local_Statis')
drop procedure sys_put_Event_Local_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Local_Statis
	@eventid int,
	@city int,
	@value int
AS
BEGIN
	if not exists (select eventid from Event_Local_Statis where eventid=@eventid)
	begin
		insert into Event_Local_Statis (eventid,city,value) values (@eventid,@city,@value)
	end
	else
	begin
		if exists (select city from Event_Local_Statis where eventid=@eventid and city=@city)
		begin
			set @value = @value + 1
			update Event_Local_Statis set value=@value where eventid=@eventid and city=@city
		end
		else
		begin
			insert into Event_Local_Statis (eventid,city,value) values (@eventid,@city,@value)
		end
	end	
END;
