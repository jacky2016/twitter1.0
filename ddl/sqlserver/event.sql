-- 数据环境事件相关

------------------------------------------------------------------------------------------------------
-- 事件产生的微博来源统计表
if not exists (select * from sys.objects where name ='Event_Source_An')
create table Event_Source_An(
	--事件id
	eventid int not null,
	--来源id
	sourceid bigint not null,
	--该来源在这个事件上有多少
	value int not null,
	--组合关键字
	primary key(eventid,sourceid)
);

if exists (select * from sys.objects where name ='sys_put_Event_Source_An')
drop procedure sys_put_Event_Source_An;

CREATE PROCEDURE sys_put_Event_Source_An
	@eventId int,
	@sourceId int
AS
BEGIN
if(not exists (select * from Event_Source_An where eventid = @eventId and @sourceId = sourceId))
begin
	insert into event_source_an (eventid,sourceid,value)
	values(@eventid,@sourceId,1)
end
else
begin
	update event_source_an set value=value+1 where eventid = @eventId and @sourceId = sourceId
end
END;
------------------------------------------------------------------------------------------------------

-- 事件关键字统计表
if not exists (select * from sys.objects where name = 'Event_WordCloud')
create table Event_WordCloud(
	id int identity(1,1) not null primary key,
	--事件id
	eventId int not null,
	--事件中的关键字，这个关键字是产生的
	keyword nvarchar(50) not null,
	--该关键字在事件中出现的次数，聚合值
	[count] int not null,
	--该事件关键字最后一次统计的时间
	lastupdated datetime not null
);

------------------------------------------------------------------------------------------------------

-- 注册统计表，该表统计这个事件里面的用户注册的时间分布情况
if not exists (select * from sys.objects where name ='Event_Reg_An')
create table Event_Reg_An(
	id int identity(1,1) not null,
	--事件id
	eventid int not null,
	--注册时间在半年以内的
	v1 int not null default(0),
	--半年到一年的
	v2 int not null default(0),
	--一年到2年的
	v3 int not null default(0),
	--两年以上的
	v4 int not null default(0)
);

if exists (select * from sys.objects where name ='sys_put_Event_Reg_An')
drop procedure sys_put_Event_Reg_An;

CREATE PROCEDURE sys_put_Event_Reg_An
	@eventId int,
	@v1 int,
	@v2 int,
	@v3 int,
	@v4 int
AS
BEGIN
if(not exists (select * from Event_Reg_An where eventid = @eventId))
begin
	insert into Event_Reg_An (eventid,v1,v2,v3,v4)
	values(@eventid,@v1,@v2,@v3,@v4)
end
else
begin
	update Event_Reg_An set 
	v1=v1+@v1,v2=v2+@v2,
	v3=v3+@v3,v4=v4+@v4
	where eventid = @eventId
end
END;

------------------------------------------------------------------------------------------------------

-- 地域分布统计表
if not exists (select * from sys.objects where name ='Event_Local_An')
create table Event_Local_An(
	id int identity(1,1) not null primary key,
	--事件id
	eventId int not null,
	--城市id 
	city int not null,
	--如果有区域则记录区域信息
	location nvarchar(50) null,
	[value] int not null default(0)
);

if exists (select * from sys.objects where name ='sys_put_Event_Local_An')
drop procedure sys_put_Event_Local_An;

CREATE PROCEDURE [dbo].[sys_put_Event_Local_An] 
	@eventId int,
	@city int,
	@location int
AS
BEGIN
if(not exists (select * from Event_Local_An where eventid = @eventId and @city=city and @location=location))
begin
	insert into Event_Local_An (eventid,city,location,value)
	values(@eventId,@city,@location,1)
end
else
begin
	update Event_Local_An set 
	value=value+1
	where eventid = @eventId and @city=city and @location=location
end
END;

------------------------------------------------------------------------------------------------------

-- 关键用户统计表
if not exists (select * from sys.objects where name ='Event_KeyUser_An')
create table Event_KeyUser_An(
	id int identity(1,1) not null primary key,
	--事件id
	eventId int not null,
	--用户ucode，如果能得到uid则这里记录的是uid否则记录的是名称或者domain
	ucode varchar(36) not null, 
	--该用户的粉丝数
	followers int not null default(0),
	-- 该用户发布的微博数
	weibos int not null default(0),
	--该用户关注的用户数
	friends int not null default(0)
);

if exists (select * from sys.objects where name ='sys_put_Event_KeyUser_An')
drop procedure sys_put_Event_KeyUser_An;

CREATE PROCEDURE sys_put_Event_KeyUser_An 
	@eventId int,
	@ucode varchar(36),
	@followers int,
	@weibos int,
	@friends int
AS
BEGIN

declare @count int

select @count = count(*) from event_keyuser_an where eventId = @eventId

if(@count<50)
	begin
		insert into event_keyuser_an (eventId,ucode,followers,weibos,friends)
		values(@eventId,@ucode,@followers,@weibos,@friends)
	end
	else
	begin
		declare @min int
		declare @id int

		select top 1 @min=followers,@id=id from event_keyuser_an where @eventId=eventId order by followers desc

		if(@followers>@min)
		begin
			delete from event_keyuser_an where id=@id
			
			insert into event_keyuser_an (eventId,ucode,followers,weibos,friends)
		values(@eventId,@ucode,@followers,@weibos,@friends)
		end
				
	end

END;
------------------------------------------------------------------------------------------------------

-- 关键观点统计表
if not exists (select * from sys.objects where name ='Event_KeyPoint_Top100')
create table Event_KeyPoint_Top100(
	id int identity(1,1) not null primary key,
	-- 事件id
	eventId int not null, 
	-- 被转发最多的帖子id
	tid varchar(36) not null,
	-- 被转发了多少次
	reposts int not null default(0) 
);

if exists (select * from sys.objects where name ='sys_put_Event_KeyPoint_Top100')
drop procedure sys_put_Event_KeyPoint_Top100;

CREATE PROCEDURE [dbo].[sys_put_Event_KeyPoint_Top100]
	@eventid int,
	@tid varchar(36),
	@reposts int
AS
BEGIN
	declare @count int
	select @count=COUNT(id) from Event_KeyPoint_Top100 where eventId =@eventid
	
	if(@count<50)
	begin
		insert into Event_KeyPoint_Top100 (eventId,tid,reposts)
		values(@eventid,@tid,@reposts)
	end
	else
	begin
		-- 找到最小的repost
		declare @min int
		declare @id int
		select top 1 @min = reposts,@id=id from Event_KeyPoint_Top100 where @eventid =eventId order by reposts desc
		if(@reposts>@min)
			begin
				delete from Event_KeyPoint_Top100 where id =@id
				insert into Event_KeyPoint_Top100 (eventId,tid,reposts)
				values(@eventid,@tid,@reposts)
			end
	end
END;
------------------------------------------------------------------------------------------------------

-- 基本信息统计
if not exists (select * from sys.objects where name ='Event_Count_An')
create table Event_Count_An(
	id int identity(1,1) not null primary key,
	-- 事件id
	eventId int not null, 
	-- 此事件下有多少微博
	posts int not null default(0), 
	-- 此事件下有多少转发的微博
	reposts int not null default(0), 
	-- 此事件下有多少女的发微博
	females int not null default(0),
	-- 此事件下有多少男的发微博 
	males int not null default(0), 
	-- 此事件下有多少不男不女的发微博
	unsex int not null default(0),
	-- 此事件下有多少vip用户发微博 
	vip int not null default(0) 
);

if exists (select * from sys.objects where name ='sys_put_Event_Count_An')
drop procedure sys_put_Event_Count_An;

CREATE PROCEDURE sys_put_Event_Count_An
	@eventid bigint,
	@ispost int,
	@isrepost int,
	@isfemale int,
	@ismale int,
	@isunsex int,
	@isvip int
AS
BEGIN
	if not exists (select eventid from Event_Count_An where eventId = @eventid)
		begin
			insert into Event_Count_An(eventid,posts,reposts,females,males,unsex,vip)
			values (@eventid,0,0,0,0,0,0)
		end
	
	if(@ispost >= 1)
		begin
			update Event_Count_An set posts=posts+@ispost where eventId = @eventid
		end
	if(@isrepost >=1)
		begin
			update Event_Count_An set reposts=reposts+@isrepost where eventId = @eventid
		end
	if(@isfemale >=1)
		begin
			update Event_Count_An set females=females+@isfemale where eventId = @eventid
		end
	if(@ismale >=1)
		begin
			update Event_Count_An set males=males+@ismale where eventId = @eventid
		end
	if(@isunsex >=1)
		begin
			update Event_Count_An set unsex=unsex+@isunsex where eventId = @eventid
		end
	if(@isvip >=1)
		begin
			update Event_Count_An set vip=vip+@isvip where eventId = @eventid
		end
	
END;
------------------------------------------------------------------------------------------------------

-- 事件中的大号统计，所谓大号是指粉丝数量多的号
-- 该表是为了尽可能准确同步扩散情况
if not exists (select * from sys.objects where name ='Event_Big_User')
create table Event_Big_User(
	id int identity(1,1) not null primary key,
	-- 用户唯一标识
	ucode varchar(36) not null,
	-- 事件id 
	eventId int not null, 
	-- 这个用户有多少粉丝
	followers int not null, 
	-- 微博的id
	tid varchar(36) not null 
);

------------------------------------------------------------------------------------------------------



