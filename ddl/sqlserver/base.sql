-- 用户基本信息表，由两个地方可以创建
-- 后台OA系统，在创建客户时需要创为客户最少创建一个管理员的用户和管理员的角色
-- 前台创建，客户管理员可以创建多个用户使用微博系统
create table Base_Users(
	id int identity(1,1) primary key,
	-- 用户所在的客户编号，该字段参照Base_Customs表
	CustomID int not null,
	-- 用户名称，用来登录的
	UserName nvarchar(70) not null,
	-- 用户昵称，用来显示的
	NickName nvarchar(70) not null,
	Email nvarchar(150),
	Tele varchar(20),
	-- 是否是管理员
	IsAdmin bit default(0),
	-- 用户创建时间
	CreateTime datetime not null,
	-- 该帐号是否可用
	IsEnabled bit not null,
	-- 这里保存的是用户密码MD5的CODE，不可逆
	passwd nvarchar(200) not null,
	-- 标识这个用户的审核人，如果该人需要审核，则这里保存此人需要被谁审核
	-- 如果该人不需要审核，此字段为空
	checkid int null
);

-- 绑定的帐号信息
create table Base_Account(
	id int identity(1,1) primary key,
	-- 用户唯一标识
	ucode varchar(36) not null,
	-- 用户平台唯一标识
	uid varchar(36) not null,
	-- 帐号所属客户编号
	customid int not null,
	-- 帐号创建者，这个帐号是由谁绑定到系统里的
	creator int not null,
	-- 帐号的昵称，平台相关的昵称
	[name] nvarchar(70) not null,
	-- 帐号所属的平台
	platform int not null
);

-- 绑定帐号的授权信息
create table Base_Account_Auths(
	id int identity(1,1) primary key,
	-- 帐号ID对应Base_Account里面的ID
	accountid int not null,
	-- 帐号的平台唯一标识，冗余字段
	uid varchar(36) not null,
	-- 平台信息
	platform int not null,
	-- 帐号授权票据
	token varchar(50) not null,
	-- 这个授权是针对哪个应用程序授权的
	appid int not null,
	-- 还有这么多秒这个授权就过期了
	expires_in int not null,
	-- 这个授权是何时创建的，何时授权的
	authTime datetime not null
);

-- 我的帐号信息，是绑定帐号的子集
create table My_Account(
	id int identity(1,1) primary key,
	-- 谁的帐号？保存用户标识，我的帐号标识
	userid int not null,
	-- 对应Base_Account里面的ID，绑定帐号的ID
	accountid int not null,
	-- 当前这个帐号是否是关闭状态
	closed bit not null,
	-- 当前这个帐号的位置，排序使用
	[position] int not null,
	-- 当前这个帐号是否是展开的
	expand bit not null
);

-- My_Sending 保存发送微博内容的列表
create table My_Sending(
	-- 自增长唯一ID 
	id int identity(1,1) primary key,
	-- 发布的内容(原创内容，转发内容，评论内容)
	text nvarchar(300) not null,
	-- 当前内容提交的时间
	submit datetime not null,
	-- 发布时间，该字段记录当前微博是定时发布还是定时发布
	-- 如果是定时发布，则这里记录的是计划发布时间，
	-- 如果是立即发布，则这里记录的是0，表示立刻就要发布 
	sent bigint not null default(0),
	-- 当前内容包含的图片信息
	images varchar(150) null,
	-- 当前这条内容是谁创建的
	userid int not null,
	-- 当前这条内容的类型，1-原创，2-转发，3-评论
	[type] int not null,
	-- 如果是转发或者评论，则这里记录的是要评论和要转发的微博ID
	-- 否则这里为空
	sourceid varchar(36) null,
	-- 标志是否有相同的操作;是否评论或者转发为false，是否同时评论或者转发为true
	-- 如果当前内容是原创，则此字段无意义
	-- 如果当前内容是转发，则此字段为1说明同时需要评论，如果为0（默认为0）则不需要同时评论。
	-- 如果当前内容是评论，则此字段为1说明同时需要转发，如果为0（默认为0）则不需要同时转发。
	flag bit not null default(0),
	-- 审核状态：0不需要审核，1待审核，2审核通过，3审核失败
	approved int not null default(0),
	-- 审核人字段，由于现在业务逻辑更改为审核是面向人不是面向内容，所以此字段作废，冗余保留
	auditor int null
);


-- 发送者列表，该表保存的是由哪些帐号（发送者）来发布微博
create table Sender(
	-- 自增长唯一ID 
	id int identity(1,1) primary key,
	-- 发送哪条内容 sending id
	sid int not null,
	-- 在哪个平台上发布
	platform int not null,
	-- 用哪个帐号发送
	uid int not null,
	-- 发送状态：1=未发送、2=发送成功、3=发送失败
	status int not null,
	-- 尝试发送的次数
	tries int null default(0)
);

-- 抓取任务列表
create table Base_Crawler_Task_List(
	id int identity(1,1) primary key,
	-- 爬虫任务标识，使用该token来获得搜索结果，每次JOB会产生一个新的
	token varchar(100) not null,
	-- 这个任务创建的时间
	created bigint not null,
	-- 抓取微博的首页URL
	homeurl varchar(150) not null,
	-- 微博ID：1=新浪微博，2=腾讯微博；5=人民微博
	domainid int not null,
	-- 类型：1=关注；2=粉丝；3=微博
	[type] int not null,
	-- 执行的时间，未执行为0，执行为当前的时间
	executed bigint not null default(0),
	-- 取结果的时间，未取结果为0，取结果为当前时间
	acquired bigint not null default(0),
	-- 抓取任务属于哪个客户
	customId int not null,
	-- 监测的对象ID
	monitorId int not null,
	-- 监测类型
	monitorType int not null
);
--本地城市code
create table Base_City(
	id int identity(1,1) primary key,
	--城市code
	Code int not null,
	--城市
	City nvarchar(50) not null,
	--省份
	Province nvarchar(50) not null
);
--舆情收集分类
create table Base_Collection_Group(
	ID int identity(1,1) primary key,
	--分类名称
	GroupName nvarchar(50) not null,
	--客户编号
	CustomID int not null
);
--舆情收集
create table Base_Collections(
	ID int identity(1,1) primary key,
	--分类名称
	GroupID int not null,
	--收集微博的ID
	PostID bigint not null,
	--谁收集的
	Creator int not null,
	--收集时间
	CreateTime datetime not null,
	--处理的状态
	Status int not null,
	--处理人
	Processor  int
);
--舆情收集的微博
create table Base_Collection_Posts(
	id bigint identity(1,1) primary key,
	--uid
	uid varchar(36) not null,
	--ucode
	ucode varchar(36) not null,
	--平台
	platform int not null,
	--微博id
	tid varchar(36) not null,
	--微博类型
	type int not null,
	--微博内容
	text nvarchar(150) not null,
	--创建时间
	created bigint not null,
	--来源
	xfrom int not null,
	--转发数
	reposts int not null,
	--评论数
	comments int not null,
	--原微博的引用
	source varchar(36) null,
	--原微博的id
	sourceid bigint null,
	--相似id
	contentid varchar(50) null,
	--创建时间
	CT datetime not null,
	--微博图片
	images nvarchar(500) null,
	--微博URL
	url varchar(50) null,
	--转发关系
	layer int not null
);
--账号预警
create table Office_Account_Warning(
	ID int identity(1,1) primary key,
	--分类名称
	groupName nvarchar(50) not null,
	--关键词
	keyword nvarchar(500) not null,
	--发送人
	receiver nvarchar(50) not null,
	--发送方式
	type nvarchar(50) not null,
	--客户编号
	customid int not null,
	--创建时间
	created datetime not null
);
--事件预警
create table Office_Event_Warning(
	id int identity(1,1) primary key,
	--预警的微博数
	weibo int not null,
	--发送人
	receiver nvarchar(50) not null,
	--发送方式
	type nvarchar(50) not null,
	--客户编号
	customid int not null,
	--创建时间
	created datetime not null
);
--微博预警
create table Office_Weibo_Warning(
	id int identity(1,1) primary key,
	--微博id
	tid varchar(36) not null,
	--平台类型
	platform int not null,
	--预警的评论数
	comment int not null,
	--预警的转发数
	repost int not null,
	--发送人
	receiver nvarchar(50) not null,
	--发送方式
	type nvarchar(50) not null,
	--客户编号
	customid int not null,
	--创建时间
	created datetime not null
);


-- 创建SQL Server的链接库和同义词
EXEC sp_addlinkedserver 
@server='192.168.2.13',--被访问的服务器别名 
@srvproduct='', 
@provider='SQLOLEDB', 
@datasrc='192.168.2.13' --要访问的服务器 

-- 创建连接服务器的登录
EXEC sp_addlinkedsrvlogin 
'192.168.2.13', --被访问的服务器别名 
'false', 
NULL, 
'sa', --帐号 
'kx*0000' --密码 

-- 创建同义词
CREATE SYNONYM [dbo].[___A] FOR [xk_weibo].[dbo].[Base_Accounts]



