-- 网评员统计表【考核管理】
if not exists (select * from sys.objects where name ='Office_naviecunt_Statis')
create table Office_naviecunt_Statis(
	--客户id
	customid  int not null,
	--网评员名称
	navieid  int not null,
	--微博id
	tid nvarchar(36) not null,
	--平台类型
	platform int not null,
	--uid
	uid nvarchar(36) not null,
	--机构名称
	[type] int not null,
	--微博发布时间
	created  bigint not null,
	--组合关键字
	primary key(navieid,tid,platform,uid,type)
);

if exists (select * from sys.objects where name ='sys_put_Office_naviecunt_Statis')
drop procedure sys_put_Office_naviecunt_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Office_naviecunt_Statis
	@customid  int,
	@navieid   int,
	@tid nvarchar(36),
	@platform int,
	@uid nvarchar(36),
	@type int,
	@created  bigint
AS
BEGIN
	if not exists (select * from Office_naviecunt_Statis where navieid=@navieid and tid=@tid and platform=@platform and uid=@uid and type=@type)
	begin
		insert into Office_naviecunt_Statis (customid,navieid,tid,platform,uid,[type],created) values (@customid,@navieid,@tid,@platform,@uid,@type,@created)
	end
END;
