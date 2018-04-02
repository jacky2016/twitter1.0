-------------------------
-- 微博转发区域统计表
-------------------------
if not exists (select * from sys.objects where name ='Spread_Users_City_Statis')
create table Spread_Users_City_Statis(
	-- 监测的微博，系统id
	sid int not null,
	--发布类型 
	[type] int not null,
	-- 区县ID
	province int not null,
	--转发或者评论的作者所在的城市
	city int not null,
	-- 这个城市一共有多少发布的 
	[value] int not null default(0),
	primary key(sid,type,city,province) 
);

-- 更新转发地域统计表
if exists (select * from sys.objects where name ='sys_inc_Spread_Users_City_Statis')
drop procedure sys_inc_Spread_Users_City_Statis;

CREATE PROCEDURE sys_inc_Spread_Users_City_Statis
	@sid int,
	@type int,
	@city int,
	@province int
AS
BEGIN
	if(not exists(select sid from Spread_Users_City_Statis where @city=city and sid=@sid and province=@province))
	begin
		insert into Spread_Users_City_Statis(sid,[type],city,value,province)
		values(@sid,@type,@city,1,@province)
	end
	else
	begin
		update Spread_Users_City_Statis set value = value+1 where @city=city and sid=@sid and @province = province
	end
END;