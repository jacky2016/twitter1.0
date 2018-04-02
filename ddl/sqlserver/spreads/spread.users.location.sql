-------------------------
-- 区域分析
-------------------------

if not exists (select * from sys.objects where name ='Spread_Users_Location_Statis')
create table Spread_Users_Location_Statis(
	sid int not null,
	type int not null,
	location nvarchar(150) not null,
	[value] int not null default(0),
	primary key(sid,location,type)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Location_Statis')
drop procedure sys_inc_Spread_Users_Location_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_Location_Statis
	@sid int,
	@type int,
	@location nvarchar(150)
AS
BEGIN
	if(not exists (select * from Spread_Users_Location_Statis where sid=@sid and location = @location and type = @type))
	begin
		insert into Spread_Users_Location_Statis(sid,location,value,type)
		values(@sid,@location,1,@type)
	end
	else 
	begin
	update Spread_Users_Location_Statis set value=1+value
	where sid=@sid and location=@location and type=@type
	end 
END;