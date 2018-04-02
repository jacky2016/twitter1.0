-------------------------
-- 用户标签分析
-------------------------

if not exists (select * from sys.objects where name ='Spread_Users_Tags_Statis')
create table Spread_Users_Tags_Statis(
	sid int not null,
	type int not null,
	tag nvarchar(150),
	value int,
	primary key(sid,tag,type)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Tags_Statis')
drop procedure sys_inc_Spread_Users_Tags_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_Tags_Statis
	@sid int,
	@type int,
	@tag nvarchar(150)
AS
BEGIN
	if(not exists (select * from Spread_Users_Tags_Statis where sid=@sid and @tag=tag and type = @type))
	begin
		insert into Spread_Users_Tags_Statis(sid,tag,value,type)
		values(@sid,@tag,1,@type)
	end
	else 
	begin
	update Spread_Users_Tags_Statis set value=1+value
	where sid=@sid and tag=@tag and type = @type
	end 
END;