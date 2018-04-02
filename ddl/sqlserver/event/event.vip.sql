-- 认证比例统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_Vip_Statis')
create table Event_Vip_Statis(
	--事件id
	eventid int not null,
	--认证用户数量
	vip int not null,
	--普通用户数量
	unvip int not null,
	--组合关键字
	primary key(eventid,vip,unvip)
);

if exists (select * from sys.objects where name ='sys_put_Event_Vip_Statis')
drop procedure sys_put_Event_Vip_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Vip_Statis
	@eventid int,
	@vip int,
	@unvip int
AS
BEGIN
	if not exists (select eventid from Event_Vip_Statis where eventid=@eventid)
	begin
		insert into Event_Vip_Statis (eventid,vip,unvip) values (@eventid,@vip,@unvip)
	end
	else
	begin
		if(@vip = 1)
		begin
			set @vip = @vip+1
		end
		if(@unvip = 1)
		begin
			set @unvip = @unvip+1
		end
		update Event_Vip_Statis set vip=@vip,unvip=@unvip where eventid=@eventid
	end	
END;
