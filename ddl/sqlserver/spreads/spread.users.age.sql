-------------------------
-- 年龄统计信息
-------------------------
if not exists (select * from sys.objects where name ='Spread_Users_Age_Statis')
create table Spread_Users_Age_Statis(
	sid int not null,
	age int not null,
	[value] int not null default(0),
	primary key(sid,age)
);

if exists (select * from sys.objects where name ='sys_inc_Spread_Users_Age_Statis')
drop procedure sys_inc_Spread_Users_Age_Statis;
CREATE PROCEDURE sys_inc_Spread_Users_Age_Statis
	@sid int,
	@age int
AS
BEGIN
if(not exists (select * from Spread_Users_Age_Statis where sid = @sid and age=@age))
begin
	insert into Spread_Users_Age_Statis (sid,age,value)
	values(@sid,@age,1)
end
else
begin
	update Spread_Users_Age_Statis set 
	value=value+1
	where @sid=sid and @age=age
end
END;