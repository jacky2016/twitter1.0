-- 热门关键词统计表【事件用户分析】
if not exists (select * from sys.objects where name ='Spread_HotWord_Statis')
create table Spread_HotWord_Statis(
	--事件id
	mid int not null,
	--关键词
	keyword nvarchar(50) not null,
	--权重
	[weight] float not null,
	-- 词性
	keytype varchar(20) not null,
	created datetime not null default(getdate()),
	--组合关键字
	primary key(mid,keyword,keytype)
);

if exists (select * from sys.objects where name ='sys_put_Spread_HotWord_Statis')
drop procedure sys_put_Spread_HotWord_Statis;
-- 入库逻辑
CREATE PROCEDURE sys_put_Spread_HotWord_Statis
	@mid int,
	@keyword   nvarchar(50),
	@weight float,
	@keytype varchar(20)
AS
BEGIN
	if not exists (select mid from Spread_HotWord_Statis where mid=@mid and @keyword=keyword and keytype = @keytype)
	begin
		insert into Spread_HotWord_Statis (mid,keyword,keytype,weight) values (@mid,@keyword,@keytype,@weight)
	end
	else
	begin
			update Spread_HotWord_Statis set weight=@weight where mid=@mid and @keyword=keyword and keytype = @keytype	
	end	
END;
