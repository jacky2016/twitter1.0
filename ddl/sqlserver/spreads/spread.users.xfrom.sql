-------------------------
-- 来源分析
-------------------------

if not exists (select * from sys.objects where name ='Spread_Users_From_Statis')
create table Spread_Users_From_Statis(
	sid int not null,
	type int not null,
	xfrom int not null,
	value int not null default(0),
	primary key(sid,type,xfrom)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_From_Statis')
drop procedure sys_inc_Spread_Users_From_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_From_Statis
	@sid int,
	@type int,
	@xfrom int
AS
BEGIN
	if(not exists (select * from Spread_Users_From_Statis where sid=@sid and xfrom =@xfrom and type=@type))
	begin
		insert into Spread_Users_From_Statis(sid,xfrom,value,type)
		values(@sid,@xfrom,1,@type)
	end
	else 
	begin
	update Spread_Users_From_Statis set value=1+value	where sid=@sid and xfrom =@xfrom and type=@type
	end 
END;