-- 转发机构统计表【微博内容统计】
if not exists (select * from sys.objects where name ='Office_Organization_Statis')
create table Office_Organization_Statis(
	-- 转发机构id
	orgid int not null,
	-- 转发机构转发微博的tid
	tid varchar(36) not null,
	type int not null,
	-- 原微博的tid
	sourceid varchar(36) not null, 
	name nvarchar(100) not null,
	platform int not null,
	uid varchar(36) not null,
	primary key(orgid,tid)
);

if exists (select * from sys.objects where name ='sys_put_Office_Organization_Statis')
drop procedure sys_put_Office_Organization_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Office_Organization_Statis
	@orgid  int,
	@tid varchar(36),
	@sourceid varchar(36),
	@name nvarchar(100),
	@platform int,
	@uid varchar(36),
	@type int
AS
BEGIN
	if not exists (select orgid from Office_Organization_Statis where orgid=@orgid and tid=@tid)
	begin
		insert into Office_Organization_Statis 
		(orgid,tid,sourceid,name,platform,uid,type) 
		values (@orgid,@tid,@sourceid,@name,@platform,@uid,@type)
	end
END;
