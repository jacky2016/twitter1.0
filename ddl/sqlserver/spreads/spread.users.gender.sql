-------------------------
-- 传播分析 性别统计表
-------------------------
if not exists (select * from sys.objects where name ='Spread_Users_Gender_Statis')
create table Spread_Users_Gender_Statis(
	--spread id
	sid int not null,
	type int not null,
	--发布时间，这里按小时保存
	males int not null default(0),
	famales int not null default(0),
	unknows int not null default(0),
	primary key(sid,type)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Gender_Statis')
drop procedure sys_inc_Spread_Users_Gender_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_Gender_Statis
	@sid int,
	@type int,
	@famales int,
	@males int,
	@unknows int
AS
BEGIN
	if(not exists (select * from Spread_Users_Gender_Statis where sid=@sid and type=@type))
	begin
		insert into Spread_Users_Gender_Statis(sid,males,famales,unknows,type)
		values(@sid,@males,@famales,@unknows,@type)
	end
	else 
	begin
	update Spread_Users_Gender_Statis set famales = famales+@famales,
	males = males+@males,unknows=unknows+@unknows
	where sid=@sid and type=@type
	end 
END;