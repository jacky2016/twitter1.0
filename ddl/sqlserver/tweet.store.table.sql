create table #tableName#(
	id bigint identity(1,1) not null,
	uid varchar(36) null,
	ucode varchar(36) not null,
	platform int not null,	
	tid varchar(36) not null,
	[type] int not null,
	text nvarchar(300) not null,
	created bigint not null,
	xfrom int not null,
	reposts int not null default 0,	
	comments int not null default 0,
	-- 引用ID	
	[source] varchar(36) null,
	-- 源微博ID
	sourceid varchar(36) null,
	CT datetime not null default getdate(),
	images nvarchar(1500) null,	
	url varchar(150) null,
	layer int not null,
	homeid varchar(36),
	primary key(tid,platform)
)