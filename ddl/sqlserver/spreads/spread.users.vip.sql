-------------------------
--认证比例
-------------------------

if not exists (select * from sys.objects where name ='Spread_Users_Vip_Statis')
create table Spread_Users_Vip_Statis(
	sid int not null,
	type int not null,
	vips int not null default(0),
	novips int not null default(0),
	primary key(sid,type)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Vip_Statis')
drop procedure sys_inc_Spread_Users_Vip_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_Vip_Statis
	@sid int,
	@type int,
	@vips int,
	@novips int
AS
BEGIN
	if(not exists (select * from Spread_Users_Vip_Statis where sid=@sid and @type = type))
	begin
		insert into Spread_Users_Vip_Statis(sid,vips,novips,type)
		values(@sid,@vips,@novips,@type)
	end
	else 
	begin
	update Spread_Users_Vip_Statis set vips=vips+@vips,novips=novips+@novips	where sid=@sid and type=@type
	end 
END;