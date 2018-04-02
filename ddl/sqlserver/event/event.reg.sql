-- 注册时间分布统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Event_Reg_Statis')
create table Event_Reg_Statis(
	--事件id
	eventid int not null,
	--半年内的
	halfYears int not null,
	--1-半年的
	oneYears int not null,
	--1-2年的
	twoYears int not null,
	--2年以上的
	moreYears int not null,
	--组合关键字
	primary key(eventid,halfYears,oneYears,twoYears,moreYears)
);

if exists (select * from sys.objects where name ='sys_put_Event_Reg_Statis')
drop procedure sys_put_Event_Reg_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Event_Reg_Statis
	@eventid    int,
	@halfYears  int,
	@oneYears   int,
	@twoYears   int,
	@moreYears  int
AS
BEGIN
	if not exists (select eventid from Event_Reg_Statis where eventid=@eventid)
	begin
		insert into Event_Reg_Statis (eventid,halfYears,oneYears,twoYears,moreYears) values (@eventid,@halfYears,@oneYears,@twoYears,@moreYears)
	end
	else
	begin
		if(@halfYears = 1)
		begin
			set @halfYears = @halfYears + 1
		end
		else if(@oneYears = 1)
		begin
			set @oneYears = @oneYears + 1
		end
		else if(@twoYears = 1)
		begin
			set @twoYears = @twoYears + 1
		end
		else
		begin
			set @moreYears = @moreYears + 1
		end
		update Event_Reg_Statis set halfYears=@halfYears,oneYears=@oneYears,twoYears=@twoYears,moreYears=@moreYears where eventid=@eventid
	end	
END;
