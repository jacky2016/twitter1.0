-- 关键用户统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_KeyUse_Statis')
create table Event_KeyUse_Statis(
	--事件id
	eventid int not null,
	--用户标示
	ucode varchar(36) not null,
	--用户粉丝的数量
	followers int not null,
	--组合关键字
	primary key(eventid,ucode)
);

if exists (select * from sys.objects where name ='sys_put_Event_KeyUse_Statis')
drop procedure sys_put_Event_KeyUse_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_KeyUse_Statis
	@eventid int,
	@ucode varchar(36),
	@followers int
AS
BEGIN
		if exists (select ucode from Event_KeyUse_Statis where eventid=@eventid and ucode=@ucode)
		begin
			update Event_KeyUse_Statis set followers=@followers where eventid=@eventid and ucode=@ucode
		end
		else
		begin
			insert into Event_KeyUse_Statis (eventid,ucode,followers) values (@eventid,@ucode,@followers)
		end
END;
