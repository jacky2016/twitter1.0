create procedure sys_put_#tableName#
	@uid varchar(36),
	@platform int,
	@tid varchar(36),
	@type int,
	@text nvarchar(300),
	@created bigint,
	@xfrom int,
	@ucode varchar(36),
	@reposts int,
	@comments int,
	@source varchar(36),
	@images varchar(1500),
	@url varchar(150),
	@layer int,
	@homeid varchar(36),
	@sourceid varchar(36),
	@out int output 
AS 
BEGIN 
	set @out = 0 
	if not exists (select tid from #tableName# where tid = @tid and platform =@platform) 
		begin 
			insert into #tableName#
			([uid],[platform],tid,[type],[text],created,xfrom,ucode,reposts,comments,[source],images,url,layer,homeid,sourceid)
			values
			(@uid,@platform,@tid,@type,@text,@created,@xfrom,@ucode,@reposts,@comments,@source,@images,@url,@layer,@homeid,@sourceid) 
			set @out = 1 
		end 
	else 
		begin 
			update #tableName# set [text]=@text,xfrom=@xfrom,reposts=@reposts,layer=@layer,sourceid=@sourceid,
			comments=@comments,images=@images,[source]=@source where tid = @tid and platform =@platform
			set @out = 2 
		end 
end